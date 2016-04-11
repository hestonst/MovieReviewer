package com.thundercats50.moviereviewer.models;

/**
 * The user class contains all the information of the user
 *
 * @author Neil Barooah
 */
public class User {
	
	private String firstname = "";
	private String lastname = "";
	private String major = "";
	private String gender = "";
    private String email = "";

	/**
	 * create empty base user
	 * @param mEmail optional email
	 */
	public User(String mEmail) {
		this(mEmail, "", "", "", "");
	}

    /**
     * full fledged user creation
     * @param mEmail email
     * @param mFirstname firstname
     * @param mLastname lastname
     * @param mMajor major
     * @param mGender gender
     */
    public User(String mEmail, String mFirstname, String mLastname, String mMajor, String mGender) {
        this.email = mEmail;
        if (firstname == null) {
            this.firstname = "";
        } else {
            this.firstname = mFirstname;
        }
        if (lastname == null) {
            this.lastname = "";
        } else {
            this.lastname = mLastname;
        }
        if (major == null) {
            this.major = "Other";
        } else {
            this.major = mMajor;
        }
        if (gender == null) {
            this.gender = "";
        } else {
            this.gender = mGender;
        }
    }

    /**
     * empty constructor
     */
    public User() {
        super();
    }

    /**
     * set this users email
     * @param newEmail the email
     */
    public void setEmail(String newEmail) {
        email = newEmail;
    }

    /**
     * get the users email
     * @return the users email
     */
    public String getEmail() {
        return email;
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
	 * @param mFirstname the firstname
	 */
	public void setFirstname(String mFirstname) {
		this.firstname = mFirstname;
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
	 * @param mLastname
	 */
	public void setLastname(String mLastname) {
		this.lastname = mLastname;
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
     * @param mGender
     */
    public void setGender(String mGender) {
        this.gender = mGender;
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
     * @param mMajor
     */
    public void setMajor(String mMajor) {
        this.major = mMajor;
    }


}