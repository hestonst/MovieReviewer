package com.thundercats50.moviereviewer;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.InputMismatchException;

/**
 * TODO: Remove SQL injection potential
 * TODO: Encrypt passwords
 * @author Scott Heston
 * @version 1.1.0
 * Documentation:
 * https://dev.mysql.com/doc/connector-j/en/connector-j-usagenotes-connect-drivermanager.html
 */
public class DBConnector  {

    Connection connection;
    Statement statement;


    public DBConnector() throws ClassNotFoundException, SQLException {
        connect();
    }

    /**
     * Creates connection to mySQL database on connection.
     * @return Connection
     * @throws ClassNotFoundException if Driver lib dependency not found
     * @throws SQLException if authentication issues with DB
     */
    private void connect() throws ClassNotFoundException, SQLException {
        //the implementation should notify the user of these errors if there is no internet access
        //i.e if the database cannot be reached
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // If this line throws an error, make sure gradle is including the java/mySQL connector
            // jar in the class folder. If "bad class file magic", bug with gradle and android:
            // https://github.com/windy1/google-places-api-java/issues/18
            // fix by including lib, not downloading it from Maven
            String url = "jdbc:mysql://sql5.freemysqlhosting.net";
            String user = "sql5104262";
            String pass = "dZsfuN5gwm";
            connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException drivExc) {
            Log.e("DBError", "The database driver has failed.");
            throw new ClassNotFoundException("Could not access database username/password. "
                    + "Check DB Driver.", drivExc);
        } catch (IllegalAccessException iae) {
            throw new ClassNotFoundException("Could not access database username/password. "
                    + "Check DB Driver.", iae);
        }
        catch (InstantiationException ie) {
            throw new ClassNotFoundException("Could not access database username/password. "
                    + "Check DB Driver.", ie);
        }
//        catch (SQLException sqle) {
//            throw new SQLException("The user database cannot be reached. Check your internet.", sqle);
//        }
    }




    /**
     * Method to add user to database. Screens info to prevent duplicates.
     * @param userName
     * @param password
     * @return boolean true if succesfully created
     * @throws SQLException
     */
    public boolean setNewUser(String userName, String password) throws SQLException, InputMismatchException {
        ResultSet resultSet = null;;
        try {
//            if (!userName.matches("[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")
//                || !password.matches("[^a-zA-Z0-9 ]{0,6}") || password.length() < 6) {
//                throw new InputMismatchException("DBC rejected pass or user");
//            }
            statement = connection.createStatement();
            String request = "INSERT INTO sql5104262.UserInfo (Username, Data1) VALUES ('"
                    + userName + "','" + password + "')";

            int didSucceed = statement.executeUpdate(request);

        }
        catch (SQLException e) {
            Log.d("DB Write error", e.getMessage());
            throw e;
        }
        return true;
    }

    /**
     * Method to be run on incorrect login. Updates the username's incorrect login number on the DB.
     * @param user username to be incremented
     */
    public boolean changePass(String user, String pass) {
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
//            String request = "UPDATE sql5104262.UserInfo SET Data1 ="
//                    + pass + " WHERE Username = '" + user + "'";
            //PreparedStatement aStatement = connection.prepareStatement(request);
            //aStatement.executeUpdate();
            //int didSucceed = statement.executeUpdate(request);
            String sql = "SET PASSWORD FOR sql5104262 = PASSWORD('123456')";
            Statement smt = connection.createStatement();
            smt.executeQuery(sql);
            return true;
        } catch (Exception e) {
            Log.d("DB Write error", e.getMessage());
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
    public boolean checkIfUser(String user) throws ClassNotFoundException, SQLException {
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
     */
    public boolean verifyUser(String user, String pass)
            throws ClassNotFoundException, SQLException {
        ResultSet resultSet = getUserData(user);
        if (resultSet.next()) {
            if (resultSet.getString(2).equals(pass)) {
                //there is only 1 entry because there is only one username selected from DB at
                // a time; 2 because resultSet contains user, pass
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
     */
    private ResultSet getUserData(String user)
            throws ClassNotFoundException, SQLException {
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            //keep making new statements as security method to keep buggy code from accessing
            // old data
            String request = "SELECT Username, Data1, LoginAttempts FROM sql5104262.UserInfo WHERE Username="
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
     * Method to be run on incorrect login. Updates the username's incorrect login number on the DB.
     * @param user username to be incremented
     */
    public boolean incrementLoginAttempts(String user) {
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            int newVal = 1 + getLoginAttempts(user);
            String request = "UPDATE sql5104262.UserInfo SET LoginAttempts ="
                    + Integer.toString(newVal) + " WHERE Username = '" + user + "'";
            int didSucceed = statement.executeUpdate(request);
            return true;
        } catch (Exception e) {
            Log.d("DB Write error", e.getMessage());
            return false;
        }
    }

    /**
     * Returns 1000 if user not found.
     * @param user
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public int getLoginAttempts(String user) throws ClassNotFoundException, SQLException {
        int retVal = 1000;
        ResultSet resultSet = null;
        resultSet = getUserData(user);
        if (resultSet.next()) {
            retVal = resultSet.getInt(3);
                //there is only 1 entry because there is only one username selected from DB at
                // a time; 3 because resultSet contains user, pass, attempts
        }
        resultSet.close();
        return retVal;
    }

    /**
     * Must be run to disconnect connection when finished with DB.
     */
    public void disconnect() {
        Log.d("DBC Logout", "entered");
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException sqlEx) {
                Log.d("DBC Logout", sqlEx.getMessage() + sqlEx.getErrorCode() + sqlEx.toString());
            }
            // ignore, means connection was already closed
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException sqlEx) {
                Log.d("DBC Logout", sqlEx.getMessage() + sqlEx.getErrorCode() + sqlEx.toString());
            }
            // ignore, means connection was already closed
        }
    }

}
