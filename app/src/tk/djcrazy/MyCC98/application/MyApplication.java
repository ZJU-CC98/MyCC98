package tk.djcrazy.MyCC98.application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import tk.djcrazy.libCC98.data.UserData;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class MyApplication extends Application {

	private static final String TAG = "MyApplication";
	public static final String USER_DATA = "userData";
	public static final String USER_AVATAR = "userAvatar.png";

	private UserData userData;
	private Bitmap userAvatar;

	@Override
	public void onCreate() {
		super.onCreate();
		initUserData();
	}

	/**
	 * 
	 */
	private void initUserData() {
		try {
			FileInputStream clientIn = openFileInput(USER_DATA);
			ObjectInputStream ois = new ObjectInputStream(clientIn);
			userData = (UserData) ois.readObject();
			userAvatar = BitmapFactory.decodeStream(openFileInput(USER_AVATAR));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			userData = new UserData();
			// genNewClient();
			// return;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void storeUserInfo() throws IOException {
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fos = openFileOutput(USER_DATA,
					Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(userData);
			if (userAvatar != null) {
				userAvatar.compress(Bitmap.CompressFormat.PNG, 70,
						openFileOutput(USER_AVATAR, MODE_PRIVATE));
			}
 		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		} finally {
			try {
				if (oos!=null) {
					oos.flush();
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new IOException(e.getMessage());
			}
		}
	}

	// private void genNewClient() {
	// Log.d(TAG, "genNewClient");
	// userData = new UserData();
	// HttpParams params = new BasicHttpParams();
	// HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	// HttpProtocolParams.setContentCharset(params,
	// HTTP.UTF_8);
	// HttpProtocolParams.setUseExpectContinue(params, true);
	// SchemeRegistry schReg = new SchemeRegistry();
	// schReg.register(new Scheme("http", PlainSocketFactory
	// .getSocketFactory(), 80));
	// schReg.register(new Scheme("https",
	// SSLSocketFactory.getSocketFactory(), 443));
	// ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
	// params, schReg);
	// DefaultHttpClient client = new DefaultHttpClient(conMgr, params);
	// client.getParams().setParameter(
	// CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
	// client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);
	// userData.setHttpClient(client);
	//
	// }

	/**
	 * @return the userData
	 */
	public UserData getUserData() {
		return userData;
	}

	/**
	 * @param userData
	 *            the userData to set
	 */
	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	/**
	 * @return the userAvatar
	 */
	public Bitmap getUserAvatar() {
		return userAvatar;
	}

	/**
	 * @param userAvatar
	 *            the userAvatar to set
	 */
	public void setUserAvatar(Bitmap userAvatar) {
		this.userAvatar = userAvatar;
	}
}
