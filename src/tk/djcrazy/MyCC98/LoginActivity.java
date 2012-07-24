/**
 * Enterance of program
 * The login activity
 * 
 */

package tk.djcrazy.MyCC98;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.libCC98.CC98Client;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

public class LoginActivity extends Activity implements OnClickListener {

	// Please always use the strings here, for it is easier to modify in the
	// future
	private static final String TAG = "MyCC98";

	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";
	public static final String USERINFO = "USERINFO";
	public static final String AUTOLOGIN = "AUTOLOGIN";
	public static final String REMEMBERPWD = "REMEMBERPWD";
	public static final String PROXY_IP = "proxy_ip";
	public static final String PROXY_PORT = "proxy_port";
	public static final String USE_PROXY = "use_proxy";
	public static final int LOGIN_FAILED_WITH_SERVER_ERROR = 0;
	public static final int LOGIN_FAILED_WITH_WRONG_USERNAME_OR_PASSWORD = 1;
	public static final int LOGIN_FAILED_WITH_UNKOWN_ERROR = 2;
	public static final int LOGIN_SUCCESS = 3;
	public static final int LOGIN_INFO_ERROR = 4;
	public static final int LOGIN_FAILED_WITH_IO_ERROR = 5;
	public static final int LOGIN_FAILED_WITH_CP_ERROR = 6;

	private EditText mUsernameEdit;

	private EditText mPasswordEdit;

	private String mUsername;

	private String mPassword;

	private Button mSigninButton;

	private Button mProxyButton;

	private CheckBox rememberPassword;

	private CheckBox autoLoginBox;
	private EditText etProxyIP;
	private EditText etProxyPort;
	private CheckBox cbUseProxy;
	private String sProxyIP;
	private int iProxyPort;
	private boolean useProxy;

	Dialog dialog;

	Intent intent = new Intent();

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOGIN_SUCCESS:
				onLoginSuccess();
				break;
			case LOGIN_FAILED_WITH_SERVER_ERROR:
				onLoginFailure("服务器错误！");
				break;
			case LOGIN_FAILED_WITH_UNKOWN_ERROR:
				onLoginFailure("未知错误");
				break;
			case LOGIN_FAILED_WITH_CP_ERROR:
				onLoginFailure("网络错误");
				break;
			case LOGIN_FAILED_WITH_IO_ERROR:
				onLoginFailure("网络错误");
				break;
			case LOGIN_FAILED_WITH_WRONG_USERNAME_OR_PASSWORD:
				onLoginFailure("用户名或密码错误");
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "5EXV7SIGMTTDKYNXTKR4");
	}

	@Override
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	/**
	 * Called when the activity is first created.
	 **/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Settings with full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		findViews();
		SharedPreferences setting = getSharedPreferences(USERINFO, 0);
		if (setting.getBoolean(REMEMBERPWD, false)) {
			mUsernameEdit.setText(setting.getString(USERNAME, ""));
			mPasswordEdit.setText(setting.getString(PASSWORD, ""));
			sProxyIP = setting.getString(PROXY_IP, "");
			iProxyPort = setting.getInt(PROXY_PORT, 141);
			useProxy = setting.getBoolean(USE_PROXY, false);
			rememberPassword.setChecked(true);
			if (setting.getBoolean(AUTOLOGIN, false)) {
				autoLoginBox.setChecked(true);
				doLogin(); 
			}
		}

		intent.setClass(this, HomeActivity.class);
	}

	/**
	 * 
	 */
	private void findViews() {
		mUsernameEdit = (EditText) findViewById(R.id.username);
		mPasswordEdit = (EditText) findViewById(R.id.password);

		mSigninButton = (Button) findViewById(R.id.login);
		mSigninButton.setOnClickListener(this);
		mProxyButton = (Button) findViewById(R.id.proxy_setting);
		mProxyButton.setOnClickListener(this);
		rememberPassword = (CheckBox) findViewById(R.id.remember_password);
		autoLoginBox = (CheckBox) findViewById(R.id.auto_login);
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
			setProxyDialog();
			break;
		default:
			break;
		}
	}

	private void setProxyDialog() {

		etProxyIP = new EditText(this);
		etProxyIP.setText(sProxyIP);
		etProxyIP.setInputType(InputType.TYPE_CLASS_PHONE);
		etProxyIP.setHint("IP:");

		etProxyPort = new EditText(this);
		etProxyPort.setText(String.valueOf(iProxyPort));
		etProxyPort.setInputType(InputType.TYPE_CLASS_PHONE);
		etProxyPort.setHint("端口:");

		cbUseProxy = new CheckBox(this);
		cbUseProxy.setText("使用代理");
		cbUseProxy.setChecked(useProxy);

		LinearLayout llProxyContainer = new LinearLayout(this);
		llProxyContainer.setOrientation(LinearLayout.VERTICAL);
		llProxyContainer.addView(etProxyIP, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		llProxyContainer.addView(etProxyPort, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		llProxyContainer.addView(cbUseProxy, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		new AlertDialog.Builder(this).setTitle("代理设置")
				.setView(llProxyContainer)
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						sProxyIP = etProxyIP.getText().toString();
						iProxyPort = Integer.parseInt(etProxyPort.getText()
								.toString());
						useProxy = cbUseProxy.isChecked();
					}
				}).setNegativeButton(R.string.go_back, null).show();
	}

	/**
     * 
     */
	private void doLogin() {
		mUsername = mUsernameEdit.getText().toString();
		mPassword = mPasswordEdit.getText().toString();
		if (useProxy) {
			CC98Client.setProxy(sProxyIP, iProxyPort);
		} else {
			CC98Client.rmProxy();
		}
		onLoginBegin();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					CC98Client.doLogin(mUsername, mPassword);
					handler.sendEmptyMessage(LOGIN_SUCCESS);
				} catch (ClientProtocolException e) {
					handler.sendEmptyMessage(LOGIN_FAILED_WITH_CP_ERROR);
					e.printStackTrace();
				} catch (IOException e) {
					handler.sendEmptyMessage(LOGIN_FAILED_WITH_IO_ERROR);
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					if (e.getMessage() == CC98Client.ID_PASSWD_ERROR_MSG) {
						handler.sendEmptyMessage(LOGIN_FAILED_WITH_WRONG_USERNAME_OR_PASSWORD);
					} else if (e.getMessage() == CC98Client.SERVER_ERROR) {
						handler.sendEmptyMessage(LOGIN_FAILED_WITH_SERVER_ERROR);
					}
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void onLoginBegin() {
		dialog = ProgressDialog.show(LoginActivity.this, "",
				"Begin Logining...", true);
		dialog.setCancelable(false);
	}

	private void onLoginSuccess() {
		dialog.dismiss();
		CC98Client.setUserName(mUsername);
		CC98Client.setPassword(mPassword);
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
		if (useProxy) {
			editor.putString(PROXY_IP, sProxyIP).putInt(PROXY_PORT, iProxyPort);
		}
		editor.putBoolean(USE_PROXY, useProxy);
		editor.commit();
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		Toast.makeText(this, R.string.msg_login_ok, Toast.LENGTH_SHORT).show();
		startActivity(intent);
		finish();
	}

	private void onLoginFailure(String reason) {
		Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
