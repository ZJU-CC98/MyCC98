/**
 * Entrance of program
 * The login activity
 * 
 */

package tk.djcrazy.MyCC98;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.inject.Inject;

import java.io.IOException;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.dialog.AuthDialog;
import tk.djcrazy.MyCC98.dialog.AuthDialog.MyAuthDialogListener;
import tk.djcrazy.MyCC98.security.Md5;
import tk.djcrazy.MyCC98.util.Intents;
import tk.djcrazy.MyCC98.util.ProgressRoboAsyncTask;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.libCC98.CachedCC98Service;
import tk.djcrazy.libCC98.data.LoginType;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

public class LoginActivity extends BaseFragmentActivity implements
		OnClickListener {
	private static final String TAG = "MyCC98";

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

	@InjectExtra(value = Intents.EXTRA_NEED_LOGIN, optional = true)
	private boolean mNeedLogin = false;
	private String mUsername = "";
	private String mPassword = "";
	private String mPWD32 = "";
	private String mPWD16 = "";

	private String authUserName = "";
	private String authPassword = "";
	private LoginType mLoginType = LoginType.NORMAL;
	private AuthDialog authDialog;
	private Boolean authRememberPwd = false;

	private static final String AUTHINFO = "AUTHINFO";

	@Inject
	private CachedCC98Service service;

	MyAuthDialogListener listener = new MyAuthDialogListener() {
		@Override
		public void onOkClick(String userName, String password,
				Boolean rememberPwd, LoginType type) {
			authUserName = userName;
			authPassword = password;
			mLoginType = type;
			authRememberPwd = rememberPwd;
			saveAuthInfo();
			showLoginField();
		}

		@Override
		public void onCancelClick() {
			mLoginType = LoginType.NORMAL;
			showLoginField();
		}
	};

	private void forwardToNextActivity() {
		Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	private void showLoginField() {
		final LinearLayout layout = (LinearLayout) findViewById(R.id.login_field);
		ImageView loginImg = (ImageView) findViewById(R.id.login_img);
		animate(layout).setDuration(800)
				.setInterpolator(new AccelerateDecelerateInterpolator())
				.alpha(1).setStartDelay(1000).start();
		animate(loginImg).setDuration(800).setStartDelay(400)
				.setInterpolator(new AccelerateDecelerateInterpolator())
				.translationY(0).start();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		mSignInButton.setOnClickListener(this);
		if (mNeedLogin) {
			initAuthInfo();
		} else if (service.getusersInfo().users.size() < 1) {
			initAuthInfo();
		} else {
			forwardToNextActivity();
		}
	}

	private void initAuthInfo() {
		SharedPreferences setting = getSharedPreferences(AUTHINFO, 0);
		authDialog = new AuthDialog(this, listener, setting);
		AlertDialog.Builder authBuilder = new AlertDialog.Builder(this);
		authBuilder.setTitle("是否启用代理？");
		authBuilder.setCancelable(false);
		authBuilder.setPositiveButton("启用",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						authDialog.show();
					}
				});
		authBuilder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						showLoginField();
					}
				});
		authBuilder.setCancelable(false);
		if (Config.IS_NORMAL_VERSION) {
            showLoginField();
        } else {
            authBuilder.show();
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

@Override
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
			return;
		} else if (mPassword.length() <= 0) {
			ToastUtils.show(this, "密码不能为空");
			return;
		}
		mPWD16 = mPWD16.equals("") ? Md5.MyMD5(mPassword, Md5.T16) : mPWD16;
		mPWD32 = mPWD32.equals("") ? Md5.MyMD5(mPassword, Md5.T32) : mPWD32;
		new LoginTask(this, mUsername, mPWD32, mPWD16).execute();
	}

	private void onLoginSuccess() {
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
			service.doLogin(userName, password32, password16, authUserName,
					authPassword, mLoginType);
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
