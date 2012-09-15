/**
 * Entrance of program
 * The login activity
 * 
 */

package tk.djcrazy.MyCC98;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.application.MyApplication;
import tk.djcrazy.MyCC98.db.BoardInfoDbAdapter;
import tk.djcrazy.MyCC98.dialog.AuthDialog;
import tk.djcrazy.MyCC98.dialog.AuthDialog.MyAuthDialogListener;
import tk.djcrazy.MyCC98.task.ProgressRoboAsyncTask;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.libCC98.ICC98Service;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.inject.Inject;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "MyCC98";

	/**
	 * configure version
	 */
	public static final boolean IS_LIFETOY_VERSION = false;

	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";
	public static final String USERINFO = "USERINFO";
	public static final String AUTOLOGIN = "AUTOLOGIN";
	public static final String REMEMBERPWD = "REMEMBERPWD";
	public static final int LOGIN_FAILED_WITH_SERVER_ERROR = 0;
	public static final int LOGIN_FAILED_WITH_WRONG_USERNAME_OR_PASSWORD = 1;
	public static final int LOGIN_FAILED_WITH_UNKOWN_ERROR = 2;
	public static final int LOGIN_SUCCESS = 3;
	public static final int LOGIN_INFO_ERROR = 4;
	public static final int LOGIN_FAILED_WITH_IO_ERROR = 5;
	public static final int LOGIN_FAILED_WITH_CP_ERROR = 6;

	@InjectView(R.id.username)
	private EditText mUsernameEdit;
	@InjectView(R.id.password)
	private EditText mPasswordEdit;
	@InjectView(R.id.login)
	private Button mSigninButton;
	@InjectView(R.id.remember_password)
	private CheckBox rememberPassword;
	@InjectView(R.id.auto_login)
	private CheckBox autoLoginBox;

	private String mUsername;
	private String mPassword;

	private String authUserName = "";
	private String authPassword = "";
	private Boolean authRememberPwd = false;
	private String arlarmTitle = "";
	private ProgressDialog progressDialog;
	private AuthDialog authDialog;

	private static final String AUTHINFO = "AUTHINFO";
	private static final int LIFETOY_AUTHORIZE_FAILED = 0;
	private static final int LIFETOY_AUTHORIZE_SUCCESS = 1;
	private static final int LIFETOY_AUTHORIZE_FAILED_WITH_EXCEPTION = 2;

	@Inject
	private ICC98Service service;

	private AlertDialog.Builder authBuilder;

	MyAuthDialogListener listener = new MyAuthDialogListener() {
		@Override
		public void onOkClick(String userName, String password,
				Boolean rememberPwd) {
			authUserName = userName;
			authPassword = password;
			authRememberPwd = rememberPwd;
			service.setUseProxy(true);
			new AuthTask(LoginActivity.this, authUserName, authPassword).execute();
		}

		@Override
		public void onCancelClick() {
			service.setUseProxy(false);
			showLoginField();
		}
	};
 
	private void forwardToNextActivity() {
		Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
		startActivity(intent);
 		finish();
	}

	private void showLoginField() {
		final LinearLayout layout = (LinearLayout) findViewById(R.id.login_field);
		ImageView loginImg = (ImageView) findViewById(R.id.login_img);
		animate(layout).setDuration(1000).alpha(1).start();
		animate(loginImg).setDuration(600).translationY(0).start();
	}

	// handle the message
	@SuppressLint("HandlerLeak")
	private Handler authHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LIFETOY_AUTHORIZE_FAILED:
				progressDialog.dismiss();
				Toast.makeText(LoginActivity.this, "lifetoy认证失败",
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			case LIFETOY_AUTHORIZE_SUCCESS:
				progressDialog.dismiss();
				Toast.makeText(LoginActivity.this, "成功通过lifetoy认证",
						Toast.LENGTH_SHORT).show();
				saveAuthInfo();
				showLoginField();
				break;
			case LIFETOY_AUTHORIZE_FAILED_WITH_EXCEPTION:
				progressDialog.dismiss();
				Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT)
						.show();
				finish();
				break;
			default:
				break;
			}
		}
	};

 	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		mSigninButton.setOnClickListener(this);
		checkNetworkInfo();
		setupRememberedLoginInfo();
		initAuthInfo();
	}

	/**
	 * 
	 */
	private void initAuthInfo() {
		SharedPreferences setting = getSharedPreferences(AUTHINFO, 0);
		authDialog = new AuthDialog(this, listener, setting);
		authBuilder = new AlertDialog.Builder(this);
		authBuilder.setTitle(arlarmTitle);
		authBuilder.setPositiveButton("启用",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						service.setUseProxy(true);
						authDialog.show();
					}
				});
		authBuilder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						service.setUseProxy(false);
						showLoginField();
					}
				});
		if (IS_LIFETOY_VERSION) {
			authBuilder.show();
		} else {
			showLoginField();
		}
	}
	
	private void saveAuthInfo() {
		Editor editor = getSharedPreferences(AUTHINFO, 0).edit();
		// save info
		if (authRememberPwd) {
			editor.putString(USERNAME, authUserName)
					.putString(PASSWORD, authPassword)
					.putBoolean(REMEMBERPWD, true);
		} else {
			editor.putString(USERNAME, "").putString(PASSWORD, "")
					.putBoolean(REMEMBERPWD, false);
		}
		editor.commit();
	}

	private void checkNetworkInfo() {
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conMan.getActiveNetworkInfo();
		if (networkInfo == null) {
			arlarmTitle = "您没有连接到任何网络！";
		} else if (networkInfo.getTypeName().equals("WIFI")) {
			arlarmTitle = "您已连接到wifi网络，是否仍然启用lifetoy代理？";
		} else {
			arlarmTitle = "您没有链接到wifi网络，是否启用lifetoy代理？";
		}
	}

	private void setupRememberedLoginInfo() {
		SharedPreferences setting = getSharedPreferences(USERINFO, 0);
		if (setting.getBoolean(REMEMBERPWD, false)) {
			mUsernameEdit.setText(setting.getString(USERNAME, ""));
			mPasswordEdit.setText(setting.getString(PASSWORD, ""));
			rememberPassword.setChecked(true);
			if (setting.getBoolean(AUTOLOGIN, false)) {
				autoLoginBox.setChecked(true);
				doLogin();
			}
		}
	}

	/**
	 * Click event driven, callback function
	 * 
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			doLogin();
			break;
		case R.id.proxy_setting:
			break;
 		}
	}

	private void doLogin() {
		mUsername = mUsernameEdit.getText().toString().trim();
		mPassword  = mPasswordEdit.getText().toString().trim();
		if (mUsername.equals("")) {
			ToastUtils.show(this, "用户名不能为空");
		} else if (mPassword.length() <= 0) {
			ToastUtils.show(this, "密码不能为空");
		}
		new LoginTask(this, mUsername, mPassword).execute();
	}

	private void onLoginSuccess() {
		Editor editor = getSharedPreferences(USERINFO, 0).edit();
		// save info
		if (autoLoginBox.isChecked())
			editor.putBoolean(AUTOLOGIN, true);
		else
			editor.putBoolean(AUTOLOGIN, false);
		if (rememberPassword.isChecked()) {
			editor.putString(USERNAME, mUsername)
					.putString(PASSWORD, mPassword)
					.putBoolean(REMEMBERPWD, true);
		} else {
			editor.putString(USERNAME, "").putString(PASSWORD, "")
					.putBoolean(REMEMBERPWD, false);
		}
		editor.commit();
 		forwardToNextActivity();
	}
	
	private class AuthTask extends ProgressRoboAsyncTask<String> {
		private String userName;
		private String password;

		public AuthTask(Activity context,String userName, String password) {
			super(context);
			this.userName = userName;
			this.password = password;
			dialog.setMessage("正在验证...");
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					AuthTask.this.cancel(true);
				}
			});
		}

		@Override
		public String call() throws Exception {
			service.doProxyAuthorization(userName, password);
			return null;
		}
		@Override
		protected void onSuccess(String t) throws Exception {
			super.onSuccess(t);
			saveAuthInfo();
			showLoginField();
		}
		@Override
		protected void onException(Exception e) throws RuntimeException {
			
		}
	}

	private class LoginTask extends ProgressRoboAsyncTask<String> {
 		private String userName;
		private String password;
 
		protected LoginTask(Activity context, String userName, String password) {
			super(context);
			this.userName = userName;
			this.password = password;
 			dialog.setMessage("正在登录...");
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					LoginTask.this.cancel(true);
				}
			});
		}

 		@Override
		public String call() throws Exception {
			service.doLogin(userName, password);
			return null;
		}

		@Override
		protected void onSuccess(String t) throws Exception {
 			onLoginSuccess();
		}

		@Override
		protected void onException(Exception e) throws RuntimeException {
 			e.printStackTrace();
			if (e.getClass() == IllegalAccessException.class) {
				ToastUtils.show(context, "用户名或密码错误");
			} else if (e.getClass() == IOException.class) {
				ToastUtils.show(context, "网络错误");
			} else {
				ToastUtils.show(context, "未知错误");
			}
		}
	}
}
