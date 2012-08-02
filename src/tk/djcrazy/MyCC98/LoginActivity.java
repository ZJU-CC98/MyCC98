/**
 * Enterance of program
 * The login activity
 * 
 */

package tk.djcrazy.MyCC98;

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

import tk.djcrazy.MyCC98.animation.DropDownAnimation;
import tk.djcrazy.MyCC98.db.BoardInfoDbAdapter;
import tk.djcrazy.MyCC98.dialog.AuthDialog;
import tk.djcrazy.MyCC98.dialog.AuthDialog.MyAuthDialogListener;
import tk.djcrazy.MyCC98.util.DisplayUtil;
import tk.djcrazy.libCC98.CC98ClientImpl;
import tk.djcrazy.libCC98.ICC98Service;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.inject.Inject;

public class LoginActivity extends BaseActivity implements OnClickListener {
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

	private Dialog dialog;

	private Intent intent;

	public static final String UPDATE_LINK_LIFETOY = "http://10.110.19.123:80/update/lifetoy.html";
	public static final String UPDATE_LINK_NORMAL = "http://10.110.19.123:80/update/index.html";
	/**
	 * configure version
	 */
	public static final boolean IS_LIFETOY_VERSION = false;
	private String authUserName = "";
	private String authPassword = "";
	private Boolean authRememberPwd = false;
	private String arlarmTitle = "";
	private ProgressDialog progressDialog;
	private AuthDialog authDialog;
	private BoardInfoDbAdapter adapter;
	private ProgressDialog pBar;
	private static final String AUTHINFO = "AUTHINFO";
	public static final int CURRENTDBVERSION = 9;
	private static final String VERSION = "VERSION";
	private static final String BOARDDBVERSION = "BOARDDBVERSION";
	private int prefVersion = 0;

	private static final String DOWNLOAD_LINK = "downloadLink";
	private static final String FILE_SIZE = "fileSize";
	private static final String INCREASE_SIZE = "increaseSize";

	private static final int UPDATE_SERVER_IS_UNREACHABLE = 2;
	private static final int NO_UPDATE = 1;
	private static final int AVAIABLE_UPDATE = 0;

	private static final int DOWNLOAD_FAILURE = 0;
	private static final int DOWNLOAD_SUCCESS = 1;
	private static final int INIT_FILESIZE = 2;
	private static final int UPDATE_PROGRESS = 3;

	private static final int LIFETOY_AUTHORIZE_FAILED = 0;
	private static final int LIFETOY_AUTHORIZE_SUCCESS = 1;
	private static final int LIFETOY_AUTHORIZE_FAILED_WITH_EXCEPTION = 2;
	private static final int INIT_BOARD_DB_SUCCESS = 3;
	private static final int INIT_BOARD_DB_FAILED = 4;

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
			Log.d("auth", userName);
			Log.d("auth", password);
			Log.d("auth", rememberPwd + "");
			authThread.start();
			progressDialog = ProgressDialog.show(LoginActivity.this, "",
					"正在进行认证...");
		}

		@Override
		public void onCancelClick() {
			Log.d(TAG, "cancel");
			showLoginField();
		}
	};

	private Thread authThread = new Thread() {
		@Override
		public void run() {

			try {
				if (service.doProxyAuthorization(authUserName, authPassword)) {
					authHandler.sendEmptyMessage(LIFETOY_AUTHORIZE_SUCCESS);
				} else {
					authHandler.sendEmptyMessage(LIFETOY_AUTHORIZE_FAILED);
				}

			} catch (ClientProtocolException e) {
				authHandler
						.sendEmptyMessage(LIFETOY_AUTHORIZE_FAILED_WITH_EXCEPTION);
				e.printStackTrace();
			} catch (IOException e) {
				authHandler
						.sendEmptyMessage(LIFETOY_AUTHORIZE_FAILED_WITH_EXCEPTION);
				e.printStackTrace();
			} catch (URISyntaxException e) {
				authHandler
						.sendEmptyMessage(LIFETOY_AUTHORIZE_FAILED_WITH_EXCEPTION);
				e.printStackTrace();
			}
		}
	};

	private Thread initDBThread = new Thread() {
		@Override
		public void run() {
			adapter = new BoardInfoDbAdapter(LoginActivity.this,
					CURRENTDBVERSION);
			try {
				adapter.createDataBase();
				authHandler.sendEmptyMessage(INIT_BOARD_DB_SUCCESS);
			} catch (IOException e) {
				authHandler.sendEmptyMessage(INIT_BOARD_DB_FAILED);
				e.printStackTrace();
			}
		}
	};

	private Thread checkUpateThread = new Thread() {
		@Override
		public void run() {
			String content = "";
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(
						(IS_LIFETOY_VERSION ? UPDATE_LINK_LIFETOY
								: UPDATE_LINK_NORMAL));
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);
				client.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, 1000);
				HttpResponse response = client.execute(get);
				content = EntityUtils.toString(response.getEntity());
				int newVersion = Integer.parseInt(content.split(" ")[0]);
				String downLink = content.split(" ")[1];
				downLink = downLink.replaceAll("(?=.apk).*?", "");
				if (newVersion == getVerCode(LoginActivity.this)) {
					checkUpdateHandler.sendEmptyMessage(NO_UPDATE);
				} else if (newVersion > getVerCode(LoginActivity.this)) {
					Message msg = new Message();
					msg.what = AVAIABLE_UPDATE;
					Bundle bundle = new Bundle();
					bundle.putString(DOWNLOAD_LINK, downLink);
					msg.setData(bundle);
					checkUpdateHandler.sendMessage(msg);
				}
			} catch (ClientProtocolException e) {
				Log.d(TAG, e.getMessage());
				checkUpdateHandler
						.sendEmptyMessage(UPDATE_SERVER_IS_UNREACHABLE);
				e.printStackTrace();
			} catch (IOException e) {
				checkUpdateHandler
						.sendEmptyMessage(UPDATE_SERVER_IS_UNREACHABLE);
				e.printStackTrace();
			} catch (Exception e) {
				checkUpdateHandler
						.sendEmptyMessage(UPDATE_SERVER_IS_UNREACHABLE);
				e.printStackTrace();
			}
		}
	};

	private Handler checkUpdateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NO_UPDATE:
			case UPDATE_SERVER_IS_UNREACHABLE:
				Log.d(TAG, "aaa");
				if (IS_LIFETOY_VERSION) {
					authBuilder.create().show();
				} else {

					showLoginField();
				}
				break;
			case AVAIABLE_UPDATE:
				final String dlink = msg.getData().getString(DOWNLOAD_LINK);
				AlertDialog.Builder updateBuilder = new AlertDialog.Builder(
						LoginActivity.this);

				updateBuilder.setTitle("发现更新");
				updateBuilder.setMessage("MyCC98客户端现在有新版本，是否立即更新到最新版本？");
				updateBuilder.setPositiveButton("更新",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								pBar = new ProgressDialog(LoginActivity.this);
								pBar.setTitle("正在下载");
								pBar.setMessage("请稍候...");
								pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								downFileAnother(dlink);
							}
						});
				updateBuilder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								forwardToNextActivity();

							}

						});
				updateBuilder.create().show();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 
	 */
	private void forwardToNextActivity() {
		startActivity(intent);
		overridePendingTransition(R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);
		finish();
	}

	private void downFileAnother(final String url) {

		pBar.show();
		new Thread() {
			public void run() {
				try {
					int fileSize = -1;
					int downedSize = 0;

					File file = new File(
							Environment.getExternalStorageDirectory(),
							"MyCC98_latest.apk");
					RandomAccessFile saveFile = new RandomAccessFile(file, "rw");
					URL fileUrl = new URL(url);
					HttpURLConnection connection = (HttpURLConnection) fileUrl
							.openConnection();
					fileSize = connection.getContentLength();
					Log.d(TAG, "fileSize:" + fileSize);
					Message msg = new Message();
					msg.what = INIT_FILESIZE;
					Bundle bundle = new Bundle();
					bundle.putInt(FILE_SIZE, fileSize);
					msg.setData(bundle);
					updateHandler.sendMessage(msg);
					DataInputStream fileStream = new DataInputStream(
							new BufferedInputStream(connection.getInputStream()));
					byte[] fileByte;
					int onecelen;
					while (fileSize != downedSize) {
						if (fileSize - downedSize > 262144) {
							fileByte = new byte[262144];
							onecelen = 262144;
						} else {
							fileByte = new byte[(fileSize - downedSize)];
							onecelen = fileSize - downedSize;
						}

						onecelen = fileStream.read(fileByte, 0, onecelen);
						saveFile.write(fileByte, 0, onecelen);
						downedSize += onecelen;
						bundle.putInt(INCREASE_SIZE, onecelen);
						Message tmpmsg = new Message();
						tmpmsg.what = UPDATE_PROGRESS;
						tmpmsg.setData(bundle);
						updateHandler.sendMessage(tmpmsg);
						Log.d(TAG, downedSize + "");
					}
					saveFile.close();
					updateHandler.sendEmptyMessage(DOWNLOAD_SUCCESS);
				} catch (ClientProtocolException e) {
					updateHandler.sendEmptyMessage(DOWNLOAD_FAILURE);
					e.printStackTrace();
				} catch (IOException e) {
					updateHandler.sendEmptyMessage(DOWNLOAD_FAILURE);
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void showLoginField() {
		final LinearLayout layout = (LinearLayout) findViewById(R.id.login_field);
		final Animation showupAnimation = new DropDownAnimation(layout,
				DisplayUtil.dip2px(getApplicationContext(), 220), true);
		layout.startAnimation(showupAnimation);
		// layout.post(new Runnable() {
		// @Override
		// public void run() {
		// layout.startAnimation(showupAnimation);
		// showupAnimation.start();
		// }
		// });
	}

	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_SUCCESS:
				pBar.cancel();
				update();
				break;
			case DOWNLOAD_FAILURE:
				pBar.cancel();
				forwardToNextActivity();
				break;
			case INIT_FILESIZE:
				pBar.setMax(msg.getData().getInt(FILE_SIZE));
				Log.d(TAG, FILE_SIZE + ":" + msg.getData().getInt(FILE_SIZE));
				pBar.setProgress(0);
			case UPDATE_PROGRESS:
				pBar.incrementProgressBy(msg.getData().getInt(INCREASE_SIZE));
			default:

				break;
			}
		}
	};

	private void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "MyCC98_latest.apk")),
				"application/vnd.android.package-archive");
		forwardToNextActivity();
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
			case INIT_BOARD_DB_SUCCESS:
				checkUpateThread.start();
				break;
			default:
				break;
			}
		}

		/**
		 * 
		 */
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
	};

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

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

	/**
	 * Called when the activity is first created.
	 **/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		intent = new Intent(LoginActivity.this, HomeActivity.class);
		findViews();
		setupRememberedLoginInfo();
		checkNetworkInfo();
		initBoardDB();
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
	}

	/**
	 * 
	 */
	private void initBoardDB() {
		SharedPreferences setting = getSharedPreferences(VERSION, 0);
		prefVersion = setting.getInt(BOARDDBVERSION, 0);
		Log.d(TAG, "prefVersion:" + prefVersion + "CURRENTDBVERSION:"
				+ CURRENTDBVERSION);
		if (prefVersion != CURRENTDBVERSION) {
			initDBThread.start();
			setting.edit().putInt(BOARDDBVERSION, CURRENTDBVERSION).commit();
		} else {
			checkUpateThread.start();
		}
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
			sProxyIP = setting.getString(PROXY_IP, "");
			iProxyPort = setting.getInt(PROXY_PORT, 141);
			useProxy = setting.getBoolean(USE_PROXY, false);
			rememberPassword.setChecked(true);
			if (setting.getBoolean(AUTOLOGIN, false)) {
				autoLoginBox.setChecked(true);
				doLogin();
			}
		}
	}

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

	public int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					"tk.djcrazy.MyCC98", 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}

	public String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					"tk.djcrazy.MyCC98", 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;
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
 		onLoginBegin();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					service.doLogin(mUsername, mPassword);
					handler.sendEmptyMessage(LOGIN_SUCCESS);
				} catch (ClientProtocolException e) {
					handler.sendEmptyMessage(LOGIN_FAILED_WITH_CP_ERROR);
					e.printStackTrace();
				} catch (IOException e) {
					handler.sendEmptyMessage(LOGIN_FAILED_WITH_IO_ERROR);
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					handler.sendEmptyMessage(LOGIN_FAILED_WITH_WRONG_USERNAME_OR_PASSWORD);
					e.printStackTrace();
				} catch (Exception e) {
					handler.sendEmptyMessage(LOGIN_FAILED_WITH_SERVER_ERROR);
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
		forwardToNextActivity();

	}

	private void onLoginFailure(String reason) {
		Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
