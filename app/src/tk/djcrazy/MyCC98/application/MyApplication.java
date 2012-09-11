package tk.djcrazy.MyCC98.application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class MyApplication extends Application {

	private static final String TAG = "MyApplication";
	public static final String HTTP_CLIENT = "httpClient";
	public static final String USER_AVATAR = "userAvatar";
	public static final String USER_NAME_STORE = "userNameStore";
	public static final String USER_NAME = "userName";
	private Bitmap userAvatar;
	private String userName;
	private DefaultHttpClient client;

	/**
	 * @return the client
	 */
	public DefaultHttpClient getClient() {
		return client;
	}
 
	@Override
	public void onCreate() {
  		super.onCreate();
 	}

	/**
	 * 
	 */
	private void initUserData() {
		try {
			FileInputStream clientIn = openFileInput(HTTP_CLIENT);
			ObjectInputStream ois = new ObjectInputStream(clientIn);
			client = (DefaultHttpClient) ois.readObject();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			genNewClient();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		try {
			FileInputStream avatarIn = openFileInput(USER_AVATAR);
			userAvatar = (Bitmap) new ObjectInputStream(avatarIn).readObject();
			userName = getSharedPreferences(USER_NAME_STORE,
					Context.MODE_PRIVATE).getString(USER_NAME, "");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void genNewClient() {
		Log.d(TAG, "genNewClient");
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params,
				HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);
		client = new DefaultHttpClient(conMgr, params);
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);
	}

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
