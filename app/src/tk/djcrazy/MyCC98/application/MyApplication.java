package tk.djcrazy.MyCC98.application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tk.djcrazy.libCC98.data.UserData;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class MyApplication extends Application {

	private static final String TAG = "MyApplication";
	public static final String USERS_INFO = "userData";
	public static final String USER_AVATAR_PREFIX = "userAvatar.png";

 	private UsersInfo usersInfo;
 	private List<Bitmap> userAvatars = new ArrayList<Bitmap>();
 	private static Context context;
 	
	@Override
	public void onCreate() {
		super.onCreate();
		initUsersInfo();
		MyApplication.context = getApplicationContext();
	} 
	
	public static Context getAppContext() {
		return MyApplication.context;
	}

	/**
	 * 
	 */
	private void initUsersInfo() {
		try {
			FileInputStream clientIn = openFileInput(USERS_INFO);
			ObjectInputStream ois = new ObjectInputStream(clientIn);
			usersInfo = (UsersInfo) ois.readObject();
			for (int i = 0; i < usersInfo.users.size(); i++) {
				userAvatars.add(BitmapFactory.decodeStream(openFileInput(USER_AVATAR_PREFIX+i)));
			}
		} catch (FileNotFoundException e1) {
			usersInfo = new UsersInfo();
 		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		Log.d("User info:", usersInfo.toString());
	}

	public void storeUsersInfo() {
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fos = openFileOutput(USERS_INFO,
					Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(usersInfo);
			for (int i = 0; i < userAvatars.size(); i++) {
				userAvatars.get(i).compress(Bitmap.CompressFormat.PNG, 70,
						openFileOutput(USER_AVATAR_PREFIX+i, MODE_PRIVATE));
			}
 		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
 		} finally {
			try {
				if (oos!=null) {
					oos.flush();
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	/**
	 * @return the userData
	 */
	public UserData getCurrentUserData() {
		return usersInfo.getCurrentUserData();
	}
 
	public List<UserData> getAllUserDatas() {
		return usersInfo.users;
	}
	
	public void addNewUser(UserData userData, Bitmap avatar, boolean isCurrentUser) {
		if (!usersInfo.users.contains(userData)) {
			usersInfo.users.add(userData);
			userAvatars.add(avatar);
		} else {
			usersInfo.users.set(usersInfo.users.indexOf(userData), userData);
			userAvatars.set(usersInfo.users.indexOf(userData), avatar);
		}
		if (isCurrentUser) {
			usersInfo.currentUserIndex = usersInfo.users.indexOf(userData);
		}
	}
	
	/**
	 * @return the userAvatar
	 */
	public Bitmap getCurrentUserAvatar() {
		if (usersInfo.users.size()>0) {
			return userAvatars.get(usersInfo.currentUserIndex);
		} else {
			throw new IllegalArgumentException("No user in current");
		}
	}
 	
	public static class UsersInfo implements Serializable{
 		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "UsersInfo [currentUserIndex=" + currentUserIndex
					+ ", users=" + users + "]";
		}
		private static final long serialVersionUID = 1161679319055452529L;
		public int currentUserIndex;
		public ArrayList<UserData> users = new ArrayList<UserData>();
		public UserData getCurrentUserData() {
			if (users.size()>0) {
				return users.get(currentUserIndex);
			} else {
				throw new IllegalArgumentException("No user in current!");
			}
		}
 	}
	
	public UsersInfo getUsersInfo() {
		return usersInfo;
	}
	
	public List<Bitmap> getUserAvatars(){
		return userAvatars;
	}
}
