/**
 * class for an Admin object
 *
 * @author Neil Barooah
 */
public class Admin extends Member {
	
	/**
	 * constructor of a new admin object
	 * @param username
	 * @param password
	 */
	public Admin(String username, String password) {
	    super(username, password);
	}

	/**
	 * checks if a certain member is an admin
	 * @returns boolean
	 */
	 @Override
	public boolean isAdmin() {
	    return true;
	}
}