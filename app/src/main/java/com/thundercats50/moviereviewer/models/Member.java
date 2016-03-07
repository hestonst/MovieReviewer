package com.thundercats50.moviereviewer.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class that specifies features of members of the app
 *
 * @author Neil Barooah
 */
public abstract class Member {
	
	private String username;
	private String password;
	public static HashMap<Integer, String> reviews = new HashMap();
    public static HashMap<Integer, String> scores = new HashMap();


	/**
     * Constructor for members
     * @param username of member
     * @param password of member
     */
	public Member(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public Member() {
		this.username = null;
		this.password = null;
	}

    /**
	 * Gets the username
	 * @return String username of the User
	 */
	public String getUsername() {
		return username;
	}

    /**
	 * Set username of the user
	 * @param username  
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Get the password
	 * @return String password of the User
     */
	public String getPassword() {
		return password;
	}

    /**
	 * Set the password of the user
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	

    public String toString(){
        return username;
    }
    /**
     * provides information whether a member is an admin
     * @return boolean
     */
	public abstract boolean isAdmin();

    /**
     * checks whether the account is locked
     * @return boolean
     */

	public boolean isLocked() {
		return false;
	}

	public void addRevew(String review, int id) {
		reviews.put(id, review);
	}

    public String getReview(int key) {
        String aString;
        aString = reviews.get(key);
        return aString;
    }
    public void addScore(String score, int id) {
        scores.put(id, score);
    }
    public String getScore(int key) {
        String aString;
        aString = scores.get(key);
        return aString;
    }
}