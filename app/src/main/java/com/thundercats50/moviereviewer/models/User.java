package com.thundercats50.moviereviewer.models;

/**
 * The user class contains all the information of the user
 *
 * @author Neil Barooah
 */
public class User extends Member {
	
	private String firstname;
	private String lastname;
	private String major;
	private String gender;
	private String status;
	private boolean locked = false;

	/**
	 *
	 * @param username
	 * @param password
	 */
	public User(String username, String password) {
		super(username, password);
	}


	/**
	 * Get the first name of the user
	 * @return String first name of the user
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * Set the first name of the user
	 * @param firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * Get the last name of the user
	 * @return String first name of the user
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * Set the last name of the user
	 * @param lastname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
    
    /**
     * Get the gender of the user
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Set the gender of the user
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    /**
     * Control the gender display
     * @return String gender
     */
    public String genderCtrl() {
        if ("M".equals(gender) || "m".equals(gender)) {
            return "Male";
        }
        if ("F".equals(gender) || "f".equals(gender)) {
            return "Female";
        }
        return "Unknown";
    }

    /**
     * determines whether the user is locked or not
     * @return boolean locked
     */
    @Override
    public boolean isLocked() {
    	return locked;
    }

    /**
     * locks or unlocks a user account
     * @param boolean whether account should be locked
     */
    public void setLock(boolean lockStatus) {
    	locked = lockStatus;
    }

    /**
     * determines whether the user is an admin
     * @return always false for users
     */
    @Override
    public boolean isAdmin() {
    	return false;
    }

	/**
     * Get major of the user
     * @return String major
     */
    public String getMajor() {
        return major;
    }

    /**
     * Set major of the user
     * @param major
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * Get the status of the user
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

}