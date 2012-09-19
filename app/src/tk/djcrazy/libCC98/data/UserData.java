package tk.djcrazy.libCC98.data;

import java.io.Serializable;

import ch.boye.httpclientandroidlib.client.CookieStore;

public class UserData implements Serializable{
	
 	private static final long serialVersionUID = 4709649440252644448L;
	private String userName;
	private String password;
	
	private boolean lifeToyVersion;
 	private CookieStore cookieStore;

 	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
 	/**
	 * @return the cookieStore
	 */
	public CookieStore getCookieStore() {
		return cookieStore;
	}
	/**
	 * @param cookieStore the cookieStore to set
	 */
	public void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the lifeToyVersion
	 */
	public boolean isLifeToyVersion() {
		return lifeToyVersion;
	}
	/**
	 * @param lifeToyVersion the lifeToyVersion to set
	 */
	public void setLifeToyVersion(boolean lifeToyVersion) {
		this.lifeToyVersion = lifeToyVersion;
	}
}
