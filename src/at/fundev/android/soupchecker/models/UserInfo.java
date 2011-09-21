package at.fundev.android.soupchecker.models;

// TODO: Auto-generated Javadoc
/**
 * Simple POJO which contains the info of the user whichs notifications are showen.
 */
public class UserInfo {
	/**
	 * The name of the user.
	 */
	private String userName;
	
	/**
	 * The URL of the soup of the user.
	 */
	private String userSoupUrl;

	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user soup url.
	 *
	 * @param userSoupUrl the new user soup url
	 */
	public void setUserSoupUrl(String userSoupUrl) {
		this.userSoupUrl = userSoupUrl;
	}

	/**
	 * Gets the user soup url.
	 *
	 * @return the user soup url
	 */
	public String getUserSoupUrl() {
		return userSoupUrl;
	}
}
