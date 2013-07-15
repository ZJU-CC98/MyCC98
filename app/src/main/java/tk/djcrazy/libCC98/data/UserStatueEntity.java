/**
 *
 */
package tk.djcrazy.libCC98.data;

import android.graphics.Bitmap;

/**
 * @author DJ
 */
public class UserStatueEntity {
    private UserStatue statue = UserStatue.OFF_LINE;
    private String userName = "";
    private String onlineTime = "";
    private Bitmap userAvartar = null;

    public UserStatue getStatue() {
        return statue;
    }

    public void setStatue(UserStatue statue) {
        this.statue = statue;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Bitmap getUserAvartar() {
        return userAvartar;
    }

    public void setUserAvartar(Bitmap userAvartar) {
        this.userAvartar = userAvartar;
    }

}
