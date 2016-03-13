package com.thundercats50.moviereviewer.database;

import android.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;

/**
 * Created by scottheston on 28/02/16.
 */
public class RepositoryConnector extends DBConnector {

    public RepositoryConnector() throws ClassNotFoundException, SQLException {
        super();
    }

    /**
     * method to query DB for ratings matching username
     * @param email to search for ratings
     * @return ResultSet (which can be accessed through a for-while loop)
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ResultSet getUserRatings(String email)
            throws ClassNotFoundException, SQLException {
        ResultSet resultSet = null;
        try {
            if (connection == null) connect();
            statement = connection.createStatement();
            //keep making new statements as security method to keep buggy code from accessing
            // old data
            String request = "SELECT (MovieID,NumericalRating," +
                    "TextReview, PhotoURL) FROM sql5107476.RatingInfo WHERE Email="
                    + "'" + email +"' ORDER BY NumericalRating";
            resultSet = statement.executeQuery(request);
        } catch (SQLException sqle) {
            Log.e("Database SQLException", sqle.getMessage());
            Log.e("Database SQLState", sqle.getSQLState());
            Log.e("Database VendorError", Integer.toString(sqle.getErrorCode()));
        }
        return resultSet;
    }

    /**
     * method to query DB for ratings matching major
     * @param major to search for ratings
     * @return ResultSet (which can be accessed through a for-while loop)
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ResultSet getAllByMajor(String major)
            throws ClassNotFoundException, SQLException {
        ResultSet resultSet = null;
        try {
            if (connection == null) connect();
            statement = connection.createStatement();
            //keep making new statements as security method to keep buggy code from accessing
            // old data
            String request = "SELECT (MovieID,NumericalRating," +
                    "TextReview, PhotoURL) FROM sql5107476.RatingInfo WHERE Email="
                    + "'" + major +"' ORDER BY NumericalRating";
            resultSet = statement.executeQuery(request);
        } catch (SQLException sqle) {
            Log.e("Database SQLException", sqle.getMessage());
            Log.e("Database SQLState", sqle.getSQLState());
            Log.e("Database VendorError", Integer.toString(sqle.getErrorCode()));
        }
        return resultSet;
    }


    /**
     * method to query DB for rating information matching movie's name
     * @param movieID to search for ratings
     * @return ResultSet (which can be accessed through a for-while loop)
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ResultSet getMovieRatings(int movieID)
            throws ClassNotFoundException, SQLException {
        ResultSet resultSet = null;
        try {
            if (connection == null) connect();
            statement = connection.createStatement();
            //keep making new statements as security method to keep buggy code from accessing
            // old data
            String request = "SELECT (NumericalRating, Email," +
                    "TextReview, PhotoURL) FROM sql5107476.RatingInfo WHERE MovieID="
                    + "" + movieID +"";
            resultSet = statement.executeQuery(request);
        } catch (SQLException sqle) {
            Log.e("Database SQLException", sqle.getMessage());
            Log.e("Database SQLState", sqle.getSQLState());
            Log.e("Database VendorError", Integer.toString(sqle.getErrorCode()));
        }
        return resultSet;
    }


    /**
     * Method to add review to database. Screens info to prevent duplicates.
     * @param email email of current user
     * @return boolean true if successfully created
     * @throws SQLException see error message
     */
    public boolean setRating(String email, int movieID, int numericalRating,
                             String textReview, String photoURL)
            throws SQLException, InputMismatchException {
        ResultSet resultSet = null;
        try {
            if (numericalRating < 0 || numericalRating > 100) {
                throw new InputMismatchException("Rating must be from 1-100");
            }
            statement = connection.createStatement();
            String request = "INSERT INTO sql5107476.RatingInfo (MovieID,NumericalRating,"
                    + "Email,TextReview, PhotoURL) VALUES (" + movieID + ","
                    + numericalRating + ",'" + email + "','" + textReview + "','"
                    + photoURL + "')";

            int didSucceed = statement.executeUpdate(request);
        }
        catch (SQLException e) {
            Log.d("DB Write error", e.getMessage());
            throw e;
        }
        return true;
    }



}