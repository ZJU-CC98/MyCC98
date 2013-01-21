/**
 * Entrance of program
 * The login activity
 * 
 */

package tk.djcrazy.MyCC98;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.io.IOException;

import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.application.MyApplication;
import tk.djcrazy.MyCC98.dialog.AuthDialog;
import tk.djcrazy.MyCC98.dialog.AuthDialog.MyAuthDialogListener;
import tk.djcrazy.MyCC98.security.Md5;
import tk.djcrazy.MyCC98.task.ProgressRoboAsyncTask;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.libCC98.ICC98Service;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.inject.Inject;

public class LoginActivity extends BaseFragmentActivity implements OnClickListener {
	private static final String TAG = "MyCC98";

	public static final boolean IS_LIFETOY_VERSION = false;

	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD32 = "PASSWORD32";
	public static final String PASSWORD16 = "PASSWORD16";
	public static final String PASSWORD = "PASSWORD";
	public static final String USERINFO = "USERINFO";
	public static final String AUTOLOGIN = "AUTOLOGIN";
	public static final String REMEMBERPWD = "REMEMBERPWD";

	@InjectView(R.id.username)
	private EditText mUsernameEdit;
	@InjectView(R.id.password)
	private EditText mPasswordEdit;
	@InjectView(R.id.login)
	private Button mSignInButton;
	@InjectView(R.id.remember_password)
	private CheckBox rememberPassword;
	@InjectView(R.id.auto_login)
	private CheckBox autoLoginBox;

	private String mUsername = "";
	private String mPassword = "";
	private String mPWD32 = "";
	private String mPWD16 = "";

	private String authUserName = "";
	private String authPassword = "";
	private Boolean authRememberPwd = false;
	private AuthDialog authDialog;

	private static final String AUTHINFO = "AUTHINFO";

	@Inject
	private ICC98Service service;

	@Inject
	private Application application;

	MyAuthDialogListener listener = new MyAuthDialogListener() {
		@Override
		public void onOkClick(String userName, String password,
				Boolean rememberPwd) {
			authUserName = userName;
			authPassword = password;
			authRememberPwd = rememberPwd;
			service.setUseProxy(true);
			service.addProxyAuthorization(userName, password);
			saveAuthInfo();
			setupRememberedLoginInfo();
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		mSignInButton.setOnClickListener(this);
		initAuthInfo();
	}

	private void initAuthInfo() {
		SharedPreferences setting = getSharedPreferences(AUTHINFO, 0);
		authDialog = new AuthDialog(this, listener, setting);
		AlertDialog.Builder authBuilder = new AlertDialog.Builder(this);
		authBuilder.setTitle("是否启用代理？");
		authBuilder.setPositiveButton("启用",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						service.setUseProxy(true);
						((MyApplication) getApplication()).getUserData()
								.setProxyVersion(true);
						authDialog.show();
					}
				});
		authBuilder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						service.setUseProxy(false);
						setupRememberedLoginInfo();
					}
				});
		if (IS_LIFETOY_VERSION) {
			authBuilder.show();
		} else {
			setupRememberedLoginInfo();
		}
	}

	private void saveAuthInfo() {
		Editor editor = getSharedPreferences(AUTHINFO, 0).edit();
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

	private void setupRememberedLoginInfo() {
		SharedPreferences setting = getSharedPreferences(USERINFO, 0);
		if (setting.getBoolean(REMEMBERPWD, false)) {
			mUsernameEdit.setText(setting.getString(USERNAME, ""));
			mPasswordEdit.setText(setting.getString(PASSWORD32, ""));
			mPWD32 = setting.getString(PASSWORD32, "");
			mPWD16 = setting.getString(PASSWORD16, "");
			rememberPassword.setChecked(true);
			if (setting.getBoolean(AUTOLOGIN, false)) {
				autoLoginBox.setChecked(true);
				if (((MyApplication) application).getUserData() != null) {
					forwardToNextActivity();
					return;
				}
			}
		}
		showLoginField();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			doLogin();
			break;
		case R.id.proxy_setting:
			ToastUtils.show(this, "这个按钮目前没用...");
			break;
		}
	}

	private void doLogin() {
		mUsername = mUsernameEdit.getText().toString().trim();
		mPassword = mPasswordEdit.getText().toString().trim();
		if (mUsername.equals("")) {
			ToastUtils.show(this, "用户名不能为空");
		} else if (mPassword.length() <= 0) {
			ToastUtils.show(this, "密码不能为空");
		}
		mPWD16 = mPWD16.equals("") ? Md5.MyMD5(mPassword, Md5.T16) : mPWD16;
		mPWD32 = mPWD32.equals("") ? Md5.MyMD5(mPassword, Md5.T32) : mPWD32;
		new LoginTask(this, mUsername, mPWD32, mPWD16).execute();
	}

	private void onLoginSuccess() {
		Editor editor = getSharedPreferences(USERINFO, 0).edit();
		editor.putBoolean(AUTOLOGIN, autoLoginBox.isChecked());
		if (rememberPassword.isChecked()) {
			editor.putString(USERNAME, mUsername)
					.putString(PASSWORD32, mPWD32)
					.putString(PASSWORD16, mPWD16)
					.putBoolean(REMEMBERPWD, true);
		} else {
			editor.putString(USERNAME, "").putString(PASSWORD32, "")
					.putString(PASSWORD16, "").putBoolean(REMEMBERPWD, false);
		}
		editor.commit();
		forwardToNextActivity();
	}

	private class LoginTask extends ProgressRoboAsyncTask<String> {
		private String userName;
		private String password32;
		private String password16;

		protected LoginTask(Activity context, String userName,
				String password32, String password16) {
			super(context);
			this.userName = userName;
			this.password32 = password32;
			this.password16 = password16;
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
			service.doLogin(userName, password32, password16);
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
