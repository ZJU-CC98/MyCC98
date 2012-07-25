/**
 * 
 */
package tk.djcrazy.libCC98.data;

import android.graphics.Bitmap;

/**
 * @author DJ
 *
 */
public class UserStatueEntity {
	/**
	 * @return the statue
	 */
	public UserStatue getStatue() {
		return statue;
	}
	/**
	 * @param statue the statue to set
	 */
	public void setStatue(UserStatue statue) {
		this.statue = statue;
	}
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
	 * @return the onlineTime
	 */
	public String getOnlineTime() {
		return onlineTime;
	}
	/**
	 * @param onlineTime the onlineTime to set
	 */
	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}	
	
	private UserStatue statue=UserStatue.OFF_LINE;
	private String userName = "";
	private String onlineTime = "";
	private Bitmap userAvartar=null;
	/**
	 * @return the userAvartar
	 */
	public Bitmap getUserAvartar() {
		return userAvartar;
	}
	/**
	 * @param userAvartar the userAvartar to set
	 */
	public void setUserAvartar(Bitmap userAvartar) {
		this.userAvartar = userAvartar;
	}

}
