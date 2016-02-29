package com.thundercats50.moviereviewer.database;

import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;

/**
 * Created by scottheston on 28/02/16.
 */
public class BlackBoardConnector extends DBConnector {

    public BlackBoardConnector() throws ClassNotFoundException, SQLException {
        super();
    }

    /**
     * Method to add user to database. Screens info to prevent duplicates.
     * @param userName
     * @param password
     * @return boolean true if succesfully created
     * @throws SQLException
     */
    public boolean setNewUser(String userName, String password) throws SQLException,
            InputMismatchException {
        ResultSet resultSet = null;
        try {
            if (!isEmailValid(userName)) {
                throw new InputMismatchException("Incorrectly formatted email.");
                //DO NOT CHANGE MESSAGE: USED IN REGISTER-ACTIVITY LOGIC
            }
            if (checkIfUser(userName)) {
                throw new InputMismatchException("User email is already registered.");
                //DO NOT CHANGE MESSAGE: USED IN REGISTER-ACTIVITY LOGIC
            }
            if (!isPasswordValid(password)) {
                throw new InputMismatchException("Password must be alphanumeric and longer than six"
                        + "characters.");
                //DO NOT CHANGE MESSAGE: USED IN REGISTER-ACTIVITY LOGIC
            }
            statement = connection.createStatement();
            String request = "INSERT INTO sql5107476.UserInfo (Username, Data1) VALUES ('"
                    + userName + "','" + password + "')";

            int didSucceed = statement.executeUpdate(request);

        }
        catch (ClassNotFoundException cnfe) {
            Log.d("DB Driver Error", cnfe.getMessage());
            return false;
        }
        catch (SQLException e) {
            Log.d("DB Write error", e.getMessage());
            throw e;
        }
        return true;
    }

    /**
     * Uses RegEx to verify emails
     * @param email
     * @return ifValid
     */
    private boolean isEmailValid(String email) {
        return (email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")
                && email.length() >= 6);
    }

    /**
     * Uses RegEx to verify pass; must be more than 6 chars and alphnumeric
     * @param password
     * @return ifValid
     */
    private boolean isPasswordValid(String password) {
        return (password.matches("[a-zA-Z0-9]{6,30}") && password.length() > 6);
    }


    /**
     * Method to be run on incorrect login. Updates the username's incorrect login number on the DB.
     * @param user username to be incremented
     */
    public boolean changePass(String user, String pass) {
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            String request = "UPDATE sql5107476.UserInfo SET Data1 ='"
                    + pass + "' WHERE Username = '" + user + "'";
            PreparedStatement aStatement = connection.prepareStatement(request);
            aStatement.executeUpdate();
            int didSucceed = statement.executeUpdate(request);

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
     * @throws SQLException
     */
    public boolean verifyUser(String user, String pass)
            throws SQLException {
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
     * @throws SQLException
     */
    private ResultSet getUserData(String user)
            throws SQLException {
        ResultSet resultSet = null;
        try {
            if (connection == null) connect();
            statement = connection.createStatement();
            //keep making new statements as security method to keep buggy code from accessing
            // old data
            String request = "SELECT Username, Data1, LoginAttempts FROM sql5107476.UserInfo WHERE Username="
                    + "'" + user +"'";
            resultSet = statement.executeQuery(request);
        } catch (SQLException sqle) {
            Log.e("Database SQLException", sqle.getMessage());
            Log.e("Database SQLState", sqle.getSQLState());
            Log.e("Database VendorError", Integer.toString(sqle.getErrorCode()));
            throw sqle;
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
            String request = "UPDATE sql5107476.UserInfo SET LoginAttempts ="
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

}
