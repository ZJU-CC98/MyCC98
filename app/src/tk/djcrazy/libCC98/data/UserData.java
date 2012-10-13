package tk.djcrazy.libCC98.data;

import java.io.Serializable;

import ch.boye.httpclientandroidlib.client.CookieStore;

public class UserData implements Serializable{
 	private static final long serialVersionUID = 5656863335203265871L;
	private String userName;
	private String password;
	
	private String proxyUserName;
	private String proxyPassword;
	private boolean proxyVersion;
 	private CookieStore cookieStore;

 	public String getUserName() {
		return userName;
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
 	public String getPassword() {
		return password;
	}
 	public void setPassword(String password) {
		this.password = password;
	}
 	public boolean isProxyVersion() {
		return proxyVersion;
	}
 	public void setProxyVersion(boolean proxyVersion) {
		this.proxyVersion = proxyVersion;
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
