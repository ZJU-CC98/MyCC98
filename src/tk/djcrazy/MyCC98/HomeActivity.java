package tk.djcrazy.MyCC98;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.adapter.HomeFragmentPagerAdapter;
import tk.djcrazy.MyCC98.dialog.AboutDialog;
import tk.djcrazy.MyCC98.listener.LoadingListener;
import tk.djcrazy.MyCC98.view.HeaderView;
import tk.djcrazy.libCC98.CC98Client;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.viewpagerindicator.TitlePageIndicator;

@ContentView(R.layout.home)
public class HomeActivity extends RoboFragmentActivity implements
		LoadingListener {

	private boolean IS_LIFETOY_VERSION = true;
	private static final String UPDATE_LINK_LIFETOY = "http://10.110.19.123/update/lifetoy.html";
	private static final String UPDATE_LINK_NORMAL = "http://10.110.19.123/update/index.html";
	private static final String TAG = "HomeActivity";
	public static final int V_PERSONAL_BOARD = 0;
	public static final int V_BOARD_SEARCH = 1;
	public static final int V_HOT = 2;
	public static final int V_NEW = 3;
	public static final int V_FRIEND = 4;
	public static final String USERINFO = "USERINFO";
	public static final String AUTOLOGIN = "AUTOLOGIN";
	private static final String DOWNLOAD_LINK = "downloadLink";
	private static final String FILE_SIZE = "fileSize";
	private static final String INCREASE_SIZE = "increaseSize";

	private static final int MSG_USERIMG_FAIL = 0;
	private static final int MSG_USERIMG_SUCC = 1;
	private static final int SEND_FEEDBACK_FAILED = 2;
	private static final int SEND_FEEDBACK_SUCCESS = 3;
	private static final int UPDATE_SERVER_IS_UNREACHABLE = 12;
	private static final int NO_UPDATE = 11;
	private static final int AVAIABLE_UPDATE = 10;
	private static final int DOWNLOAD_FAILURE = 30;
	private static final int DOWNLOAD_SUCCESS = 31;
	private static final int INIT_FILESIZE = 32;
	private static final int UPDATE_PROGRESS = 33;

 	@InjectView(R.id.main_pages)
	private ViewPager viewPager;
	@InjectView(R.id.main_titles)
	private TitlePageIndicator indicator;
	@InjectView(R.id.home_header_userimg)
	private ImageView userAvatar;
	@InjectView(R.id.home_header_user_name)
	
	
	private ProgressDialog pBar;

	private Bitmap bmUserImg;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_USERIMG_FAIL:
				Toast.makeText(getApplicationContext(), "获取头像失败",
						Toast.LENGTH_SHORT).show();
				break;
			case MSG_USERIMG_SUCC:
				userAvatar.setImageBitmap(bmUserImg);	
 				break;
			case SEND_FEEDBACK_FAILED:
				Toast.makeText(getApplicationContext(), "无法连接到服务器，请稍候再试",
						Toast.LENGTH_SHORT).show();
				break;
			case SEND_FEEDBACK_SUCCESS:
				Toast.makeText(getApplicationContext(), "发送成功！",
						Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOAD_SUCCESS:
				pBar.cancel();
				update();
				break;
			case DOWNLOAD_FAILURE:
				pBar.cancel();
				Toast.makeText(getApplicationContext(), "无法连接到服务器，请稍候再试",
						Toast.LENGTH_SHORT).show();
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HomeFragmentPagerAdapter adapter = new HomeFragmentPagerAdapter(
				getSupportFragmentManager());
		adapter.setLoadingListener(this);
		viewPager.setAdapter(adapter);
		indicator.setViewPager(viewPager, 0);
 		getUserImg();
	}

	private void getUserImg() {
		new Thread() {
			@Override
			public void run() {
				try {
					bmUserImg = CC98Client.getLoginUserImg();
					handler.sendEmptyMessage(MSG_USERIMG_SUCC);
				} catch (Exception e) {
					handler.sendEmptyMessage(MSG_USERIMG_FAIL);
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void refresh() {
		viewPager.invalidate();
	}

 
	/**
	 * override menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.personal_board_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		// case R.id.exit:
		// finish();
		// return true;
		case R.id.settings:
			goSettings();
			return true;
		case R.id.log_out:
			logOut();
			return true;
		case R.id.check_personal_info:
			Intent profiIntent = new Intent();
			profiIntent.setClass(HomeActivity.this, ProfileActivity.class);
			profiIntent.putExtra("userName", CC98Client.getUserName());
			profiIntent.putExtra(ProfileActivity.USER_IMAGE, bmUserImg);
			startActivity(profiIntent);
			overridePendingTransition(R.anim.forward_activity_move_in,
					R.anim.forward_activity_move_out);
			return true;
		case R.id.about:
			showAboutInfo();
			return true;
		case R.id.feedback:
			doSendFeedBack();
			return true;
		case R.id.check_update:
			new Thread(checkUpateThread).start();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void goSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);
	}

	/**
	 * 
	 */
	private void doSendFeedBack() {
		Bundle bundle = new Bundle();
		bundle.putInt(EditActivity.MOD, EditActivity.MOD_PM);
		bundle.putString(EditActivity.TO_USER, "MyCC.98");
		bundle.putString(EditActivity.PM_TITLE, "MyCC98软件反馈");
		startActivity(new Intent().setClass(getApplicationContext(),
				EditActivity.class).putExtra(EditActivity.BUNDLE, bundle));
		overridePendingTransition(R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);
	}

	/**
	 * 
	 */
	private void logOut() {
		getSharedPreferences(USERINFO, 0).edit().putBoolean(AUTOLOGIN, false)
				.commit();
		Intent intent = new Intent();
		intent.setClass(HomeActivity.this, LoginActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);
		finish();
	}

	private void showAboutInfo() {
		new AboutDialog(this).show();
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

	private Thread checkUpateThread = new Thread() {
		@Override
		public void run() {
			String content = "";
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(
						IS_LIFETOY_VERSION ? UPDATE_LINK_LIFETOY
								: UPDATE_LINK_NORMAL);
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);
				client.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, 1000);
				HttpResponse response = client.execute(get);
				content = EntityUtils.toString(response.getEntity());
				int newVersion = Integer.parseInt(content.split(" ")[0]);
				String downLink = content.split(" ")[1];
				downLink = downLink.replaceAll("(?=.apk).*?", "");
				if (newVersion == getVerCode(HomeActivity.this)) {
					checkUpdateHandler.sendEmptyMessage(NO_UPDATE);
				} else if (newVersion > getVerCode(HomeActivity.this)) {
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
			}

		}
	};

	private Handler checkUpdateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NO_UPDATE:
				Toast.makeText(getApplicationContext(), "未检测到更新",
						Toast.LENGTH_SHORT).show();
				break;
			case UPDATE_SERVER_IS_UNREACHABLE:
				Toast.makeText(getApplicationContext(), "更新服务器不可用",
						Toast.LENGTH_SHORT).show();
				break;
			case AVAIABLE_UPDATE:
				final String dlink = msg.getData().getString(DOWNLOAD_LINK);
				AlertDialog.Builder updateBuilder = new AlertDialog.Builder(
						HomeActivity.this);

				updateBuilder.setTitle("发现更新");
				updateBuilder.setMessage("MyCC98客户端现在有新版本，是否立即更新到最新版本？");
				updateBuilder.setPositiveButton("更新",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								pBar = new ProgressDialog(HomeActivity.this);
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
							}
						});
				updateBuilder.create().show();
				break;
			default:
				break;
			}
		}
	};

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
					handler.sendMessage(msg);
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
						handler.sendMessage(tmpmsg);
						Log.d(TAG, downedSize + "");
					}
					saveFile.close();
					handler.sendEmptyMessage(DOWNLOAD_SUCCESS);
				} catch (ClientProtocolException e) {
					handler.sendEmptyMessage(DOWNLOAD_FAILURE);
					e.printStackTrace();
				} catch (IOException e) {
					handler.sendEmptyMessage(DOWNLOAD_FAILURE);
					e.printStackTrace();
				}
			}
		}.start();
	}

	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "MyCC98_latest.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
		overridePendingTransition(R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);
		finish();
	}

	@Override
	public void onLoadComplete(int postion) {
		if (viewPager.getCurrentItem()==postion) {
			
		}
	}

	@Override
	public void onLoadFailure(int position) {

	}

	private static Boolean isExit = false;
	private static Boolean hasTask = false;
	Timer tExit = new Timer();
	TimerTask task = new TimerTask() {

		@Override
		public void run() {
			isExit = false;
			hasTask = true;
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				Toast.makeText(this,"再按一次退出程序", Toast.LENGTH_SHORT)
						.show();
				if (!hasTask) {
					tExit.schedule(task, 2000);
				}
			} else {
				finish();
 			}
		}
		return false;
	}
}
