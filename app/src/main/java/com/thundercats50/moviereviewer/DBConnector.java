package com.thundercats50.moviereviewer;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
//to access the user/pass stored in db.properties

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * @author Scott Heston
 * @version 0.0.1
 * Documentation:
 * https://dev.mysql.com/doc/connector-j/en/connector-j-usagenotes-connect-drivermanager.html
 */
public class DBConnector {

    Connection connection;
    Statement statement;


    public DBConnector() throws IOException, ClassNotFoundException, SQLException {
        connect();
    }

    /**
     * Creates connection to mySQL database on connection.
     * @return Connection
     * @throws IOException reads user/pass from file db.properties
     * @throws ClassNotFoundException if Driver lib dependency not found
     * @throws SQLException if authentication issues with DB
     */
    private void connect() throws IOException, ClassNotFoundException, SQLException {
        //the implementation should notify the user of these errors if there is no internet access
        //i.e if the database cannot be reached
        try {
            FileInputStream fis = new FileInputStream("app/src/main/java/com/thundercats50/moviereviewer/db.properties");
            Properties props = new Properties();
            props.load(fis);
            Class.forName(props.getProperty("MYSQL_DB_DRIVER_CLASS")).newInstance();
            // If this line throws an error, make sure gradle is including the java/mySQL connector
            // jar in the class folder. If "bad class file magic", bug with gradle and android:
            // https://github.com/windy1/google-places-api-java/issues/18
            // fix by including lib, not downloading it from Maven
            String url = props.getProperty("MYSQL_DB_URL");
            String user = props.getProperty("MYSQL_DB_USERNAME");
            String pass = props.getProperty("MYSQL_DB_PASSWORD");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (IOException ioe) {
            Log.e("IOError", "db.properties not read. Cannot connect to database.");
            throw new IOException("Could not access database username/password. "
                    + "Check db.properties.", ioe);
        } catch (ClassNotFoundException drivExc) {
            Log.e("DBError", "The database driver has failed.");
            throw new ClassNotFoundException("Could not access database username/password. "
                    + "Check db.properties.", drivExc);
        } catch (IllegalAccessException iae) {
            throw new ClassNotFoundException("Could not access database username/password. "
                    + "Check db.properties.", iae);
        }
        catch (InstantiationException ie) {
            throw new ClassNotFoundException("Could not access database username/password. "
                    + "Check db.properties.", ie);
        }
        catch (SQLException sqle) {
            throw new SQLException("The user database cannot be reached. Check your internet.");
        }
    }




    /**
     * Method to add user to database. Screens info to prevent duplicates.
     * @param userName
     * @param password
     * @return boolean true if succesfully created
     * @throws SQLException
     */
    public boolean setNewUser(String userName, String password) throws SQLException {
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            String request = "INSERT INTO sql5104262.UserInfo (Username, Data1) VALUES ('" +
                    userName + "','" + password + "')";
            int didSucceed = statement.executeUpdate(request);
            return true;
        } catch (Exception e) {
            Log.d("BD Write error", e.getMessage());
            return false;
        }
    }

    /**
     * Checks whether user already exists. Only for use when creating a new user,
     * to make sure a user is not overwritten. To verify login, use verifyUser method
     * @param user
     * @return isValid
     * @throws SQLException
     */
    public boolean checkIfUser(String user)
            throws ClassNotFoundException, SQLException, IOException {
        ResultSet resultSet = getUserData(user);
        if (resultSet.next()) {
            resultSet.close();
            return true;
        }
        return false;
    }

    /**
     * Verifies user/pass combinations for login
     * @param user
     * @param pass
     * @return boolean true if valid
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    public boolean verifyUser(String user, String pass)
            throws ClassNotFoundException, SQLException, IOException {
        ResultSet resultSet = getUserData(user);
        if (resultSet.next()) {
            if (resultSet.getString(2).equals(pass)) {
                //there is only 1 row because there is only one username selected from DB at a time
                return true;
            }
            resultSet.close();
        }
        return false;
    }

    /**
     * private method to query DB for user information matching username
     * @param user
     * @return ResultSet
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    private ResultSet getUserData(String user)
            throws ClassNotFoundException, SQLException, IOException {
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            //keep making new statements as security method to keep buggy code from accessing
            // old data
            String request = "SELECT Username, Data1 FROM sql5104262.UserInfo WHERE Username="
                    + "'" + user +"'";
            resultSet = statement.executeQuery(request);
        } catch (SQLException sqle) {
            Log.e("Database SQLException", sqle.getMessage());
            Log.e("Database SQLState", sqle.getSQLState());
            Log.e("Database VendorError", Integer.toString(sqle.getErrorCode()));
        }
        return resultSet;
    }

    /**
     * Must be run to disconnect connection when finished with DB.
     */
    public void disconnect() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException sqlEx) { }
            // ignore, means connection was already closed
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException sqlEx) { }
            // ignore, means connection was already closed
        }
    }

}
