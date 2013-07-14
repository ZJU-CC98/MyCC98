package tk.djcrazy.libCC98.data;

import java.io.Serializable;

import ch.boye.httpclientandroidlib.client.CookieStore;

public class UserData implements Serializable {
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserData [userName=" + userName + ", password32=" + password32
				+ ", password16=" + password16 + ", proxyUserName="
				+ proxyUserName + ", proxyPassword=" + proxyPassword
				+ ", loginType=" + loginType + ", cookieStore=" + cookieStore
				+ "]";
	}

	private static final long serialVersionUID = 5656863335203265871L;
	private String userName;
	private String password32;
	private String password16;

	private String proxyUserName;
	private String proxyPassword;
	private LoginType loginType;

	private CookieStore cookieStore;
	
	
	
	/**
	 * @return the loginType
	 */
	public LoginType getLoginType() {
		return loginType;
	}

	/**
	 * @param loginType the loginType to set
	 */
	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}

	public String getUserName() {
		return userName;
	}
 
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((loginType == null) ? 0 : loginType.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserData other = (UserData) obj;
		if (loginType != other.loginType)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	public String getPassword32() {
		return password32;
	}

	public String getPassword16() {
		return password16;
	}

	public void setPassword32(String password) {
		this.password32 = password;
	}

	public void setPassword16(String password) {
		this.password16 = password;
	}

 	public String getProxyUserName() {
		return proxyUserName;
	}

	public void setProxyUserName(String proxyUserName) {
		this.proxyUserName = proxyUserName;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}
}
