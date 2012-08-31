package tk.djcrazy.MyCC98.application;

import android.app.Application;
import android.graphics.Bitmap;

public class MyApplication extends Application {

	private Bitmap userAvatar;
	private String userName;

	public Bitmap getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(Bitmap userAvatar) {
		 this.userAvatar = userAvatar;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
