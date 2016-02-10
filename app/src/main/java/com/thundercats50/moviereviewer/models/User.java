package com.thundercats50.moviereviewer.models;

/**
 * The user class contains all the information of the user
 *
 * @author Neil Barooah
 */
public class User {
	
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String major;
	private String gender;
	private String status;
	private int userType;

	/**
	 *
	 * @param username
	 * @param password
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * Gets the username
	 * @return String username of the com.thundercats50.moviereviewer.models.User
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
	 * @return String password of the com.thundercats50.moviereviewer.models.User
     */
	public String getPassword() {
		return "Notapassword";
	}

	/**
	 * Set the password of the user
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @param firstname
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
     * Get the user type
     * @return int type of user
     */
    public int getUserType() {
    	return userType;
    }

    /**
     * Set the user type: 1 for admin and 2 otherwise
     * @return int type of user
     */
    public void setUserType(int userType) {
    	this.userType = userType;
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

    public String toString(){
        return username;
    }

}