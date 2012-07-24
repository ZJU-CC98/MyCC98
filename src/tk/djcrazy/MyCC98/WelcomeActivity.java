/**
 * 
 */
package tk.djcrazy.MyCC98;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import tk.djcrazy.MyCC98.db.BoardInfoDbAdapter;
import tk.djcrazy.MyCC98.dialog.AuthDialog;
import tk.djcrazy.MyCC98.dialog.AuthDialog.MyAuthDialogListener;
import tk.djcrazy.libCC98.CC98Client;
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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

/**
 * @author DJ
 * 
 */
public class WelcomeActivity extends Activity {

	public static final String UPDATE_LINK_LIFETOY = "http://10.110.19.123:80/update/lifetoy.html";
	public static final String UPDATE_LINK_NORMAL = "http://10.110.19.123:80/update/index.html";
	/**
	 * configure version
	 */
	public static final boolean IS_LIFETOY_VERSION = true;
	private Intent intent = new Intent();
	private String authUserName = "";
	private String authPassword = "";
	private Boolean authRememberPwd = false;
	private String arlarmTitle = "";
	private ProgressDialog progressDialog;
	private AuthDialog authDialog;
	private BoardInfoDbAdapter adapter;
	private TextView initMessage;
	private ProgressDialog pBar;
	private static final String AUTHINFO = "AUTHINFO";
	private static final String USERNAME = "USERNAME";
	private static final String PASSWORD = "PASSWORD";
	private static final String AUTOLOGIN = "AUTOLOGIN";
	private static final String REMEMBERPWD = "REMEMBERPWD";
	public static final int CURRENTDBVERSION = 9;
	private static final String VERSION = "VERSION";
	private static final String BOARDDBVERSION = "BOARDDBVERSION";
	private int prefVersion = 0;
	private static final String TAG = "WelcomeActivity";

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
			progressDialog = ProgressDialog.show(WelcomeActivity.this, "",
					"正在进行认证...");
		}

		@Override
		public void onCancelClick() {
			Log.d(TAG, "cancel");
			startActivity(intent);
			overridePendingTransition(R.anim.alpha_change, R.anim.alpha_change2);
			finish();
		}
	};
	Thread authThread = new Thread() {
		@Override
		public void run() {

			try {
				if (CC98Client.doHttpBasicAuthorization(authUserName,
						authPassword)) {
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

	Thread initDBThread = new Thread() {
		@Override
		public void run() {
			adapter = new BoardInfoDbAdapter(WelcomeActivity.this,
					CURRENTDBVERSION);
			try {
				adapter.createDataBase();
			} catch (IOException e) {
				e.printStackTrace();
			}
			authHandler.sendEmptyMessage(4);
		}
	};

	Thread checkUpateThread = new Thread() {
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
				if (newVersion == getVerCode(WelcomeActivity.this)) {
					checkUpdateHandler.sendEmptyMessage(NO_UPDATE);
				} else if (newVersion > getVerCode(WelcomeActivity.this)) {
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

	private void readSettings() {
		// settings manager, get settings
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		SettingsActivity.addTail = sharedPreferences.getBoolean(
				SettingsActivity.SETTING_USE_TAIL, true);
		SettingsActivity.useDark = sharedPreferences.getBoolean(
				SettingsActivity.SETTING_USE_DARK, false);
	}

	/**
	 * Called when the activity is first created.
	 **/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Settings with full screen

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Settings with no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		intent.setClass(this, LoginActivity.class);

		checkNetworkInfo();
		readSettings();
		setContentView(R.layout.welcome);

		findviews();
		initMessage.setText("正在检查更新...");
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

		setting = getSharedPreferences(AUTHINFO, 0);

		authDialog = new AuthDialog(this, listener, setting);

		authBuilder = new AlertDialog.Builder(this);
		authBuilder.setTitle(arlarmTitle);
		authBuilder.setPositiveButton("启用",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						CC98Client.setCC98Domain("http://hz.cc98.lifetoy.org/");

						authDialog.show();
					}
				});
		authBuilder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						CC98Client.setCC98Domain("http://www.cc98.org/");
						startActivity(intent);
						overridePendingTransition(R.anim.alpha_change,
								R.anim.alpha_change2);

						finish();
					}
				});
	}

	private void findviews() {
		initMessage = (TextView) findViewById(R.id.initial_message);
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

	// handle the message
	private Handler authHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LIFETOY_AUTHORIZE_FAILED:
				progressDialog.dismiss();
				Toast.makeText(WelcomeActivity.this, "lifetoy认证失败",
						Toast.LENGTH_SHORT).show();
				break;
			case LIFETOY_AUTHORIZE_SUCCESS:
				progressDialog.dismiss();
				Toast.makeText(WelcomeActivity.this, "成功通过lifetoy认证",
						Toast.LENGTH_SHORT).show();
				saveAuthInfo();
				startActivity(intent);
				overridePendingTransition(R.anim.alpha_change,
						R.anim.alpha_change2);
				finish();
				break;
			case LIFETOY_AUTHORIZE_FAILED_WITH_EXCEPTION:
				progressDialog.dismiss();
				Toast.makeText(WelcomeActivity.this, "网络错误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4:
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
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					startActivity(intent);
					overridePendingTransition(R.anim.alpha_change,
							R.anim.alpha_change2);
					finish();
				}
				break;
			case AVAIABLE_UPDATE:
				final String dlink = msg.getData().getString(DOWNLOAD_LINK);
				AlertDialog.Builder updateBuilder = new AlertDialog.Builder(
						WelcomeActivity.this);

				updateBuilder.setTitle("发现更新");
				updateBuilder.setMessage("MyCC98客户端现在有新版本，是否立即更新到最新版本？");
				updateBuilder.setPositiveButton("更新",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								pBar = new ProgressDialog(WelcomeActivity.this);
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
								startActivity(intent);
								overridePendingTransition(R.anim.alpha_change,
										R.anim.alpha_change2);
								finish();

							}
						});
				updateBuilder.create().show();
				break;
			default:
				break;
			}
		}
	};

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

	private void doNewVersionUpdate(final String downLink) {
		int verCode = getVerCode(this);
		String verName = getVerName(this);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(", 发现新版本:");
		sb.append("aaa");
		sb.append(" Code:");
		sb.append("aa");
		sb.append(", 是否更新?");
		Dialog dialog = new AlertDialog.Builder(WelcomeActivity.this)
				.setTitle("软件更新")
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								pBar = new ProgressDialog(WelcomeActivity.this);
								pBar.setTitle("正在下载");
								pBar.setMessage("请稍候...");
								pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								downFile(downLink);
							}
						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	void downFile(final String url) {

		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"MyCC98_latest.apk");
						fileOutputStream = new FileOutputStream(file);

						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}

					aHandler.sendEmptyMessage(DOWNLOAD_SUCCESS);
				} catch (ClientProtocolException e) {
					aHandler.sendEmptyMessage(DOWNLOAD_FAILURE);
					e.printStackTrace();
				} catch (IOException e) {
					aHandler.sendEmptyMessage(DOWNLOAD_FAILURE);
					e.printStackTrace();
				}
			}
		}.start();
	}

	void downFileAnother(final String url) {

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
					aHandler.sendMessage(msg);
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
						aHandler.sendMessage(tmpmsg);
						Log.d(TAG, downedSize + "");
					}
					saveFile.close();
					aHandler.sendEmptyMessage(DOWNLOAD_SUCCESS);
				} catch (ClientProtocolException e) {
					aHandler.sendEmptyMessage(DOWNLOAD_FAILURE);
					e.printStackTrace();
				} catch (IOException e) {
					aHandler.sendEmptyMessage(DOWNLOAD_FAILURE);
					e.printStackTrace();
				}
			}
		}.start();
	}

	Handler aHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_SUCCESS:
				pBar.cancel();
				update();
				break;
			case DOWNLOAD_FAILURE:
				pBar.cancel();
				startActivity(intent);
				finish();
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

	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "MyCC98_latest.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
		finish();
	}
}
