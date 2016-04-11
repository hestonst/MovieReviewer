package com.thundercats50.moviereviewer.database;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * TODO: Extra Credit, Encrypt passwords
 * @author Scott Heston
 * @version 2.0.0
 */
abstract class DBConnector  {

    Connection connection;
    Statement statement;
    private String connectionError;


    DBConnector() throws SQLException {
        connect();
        connectionError = "Connection Error";
    }

    /**
     * Creates connection to mySQL database on connection.
     * @throws SQLException if authentication issues with DB
     */
    void connect() throws SQLException {
        //the implementation should notify the user of these errors if there is no internet access
        //i.e if the database cannot be reached
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // If this line throws an error, make sure gradle is including the java/mySQL connector
            // jar in the class folder. If "bad class file magic", bug with gradle and android:
            // https://github.com/windy1/google-places-api-java/issues/18
            // fix by including lib, not downloading it from Maven
            String url = "jdbc:mysql://sql5.freemysqlhosting.net";
            String user = "sql5107476";
            String pass = "YMVSuA8eWm";
            connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            Log.e("DBError", "The database driver has failed.");
            Log.d("DBC ClassNtFndException",
                    "Could not access database username/password. Check DB Driver.");
        } catch (IllegalAccessException iae) {
            Log.d("ClassNotFoundException", "Could not access database username/password. "
                    + "Check DB Driver.");
        } catch (SQLException SQL_Exception) {
            Log.d(connectionError, "Check internet for MySQL access." + SQL_Exception.getMessage() + SQL_Exception.getSQLState());
            for (Throwable e : SQL_Exception) {
                e.printStackTrace(System.err);
                Log.d(connectionError, "SQLState: " +
                        ((SQLException) e).getSQLState());

                Log.d(connectionError, "Error Code: " +
                        ((SQLException) e).getErrorCode());

                Log.d(connectionError, "Message: " + e.getMessage());

                Throwable t = SQL_Exception.getCause();
                while(t != null) {
                    Log.d(connectionError, "Cause: " + t);
                    t = t.getCause();
                }
            }
            throw SQL_Exception;
        }
        catch (Exception e) {
            throw new SQLException("Unknown connection error: assume no internet.", e);
        }
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
