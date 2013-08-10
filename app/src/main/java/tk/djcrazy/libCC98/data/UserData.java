package tk.djcrazy.libCC98.data;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.boye.httpclientandroidlib.client.CookieStore;
import ch.boye.httpclientandroidlib.cookie.Cookie;
import ch.boye.httpclientandroidlib.impl.cookie.BasicClientCookie;
import ch.boye.httpclientandroidlib.impl.cookie.BasicClientCookie2;

public class UserData implements Serializable {
	private static final long serialVersionUID = 5656863335203265871L;
	private String userName;
	private String password32;
	private String password16;

	private String proxyUserName;
	private String proxyPassword;

    private String proxyHost;
	private LoginType loginType;


    private List<BasicClientCookie> cookies = new ArrayList<BasicClientCookie>();

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public List<BasicClientCookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<BasicClientCookie> cookies) {
        this.cookies = cookies;
    }

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
