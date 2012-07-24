package tk.djcrazy.MyCC98;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import tk.djcrazy.MyCC98.dialog.AboutDialog;
import tk.djcrazy.MyCC98.view.FooterView;
import tk.djcrazy.MyCC98.view.FriendListView;
import tk.djcrazy.MyCC98.view.HeaderView;
import tk.djcrazy.MyCC98.view.HotTopicView; 
import tk.djcrazy.MyCC98.view.NewTopicView;
import tk.djcrazy.MyCC98.view.ParentView;
import tk.djcrazy.MyCC98.view.PersonalBoardView;
import tk.djcrazy.MyCC98.view.SearchBoardView;
import tk.djcrazy.libCC98.CC98Client;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import static tk.djcrazy.MyCC98.WelcomeActivity.*;
import com.flurry.android.FlurryAgent;

public class HomeActivity extends Activity implements ParentView {
	private static final String TAG = "HomeActivity";
	public static final int V_PERSONAL_BOARD = 0;
	public static final int V_BOARD_SEARCH = 1;
	public static final int V_HOT = 2;
	public static final int V_NEW = 3;
	public static final int V_FRIEND = 4;
	public static final String EMAIL_HOST_NAME = "zjuem.zju.edu.cn";
	public static final String EMAIL_HOST_USER_NAME = "crazydj";
	public static final String EMAIL_HOST_PASSWORD = "66957860";
	public static final String EMAIL_SEND_DESTINATION = "djq2272@gmail.com";
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

	private FooterView footerView;
	private HeaderView headerView;
	private LinearLayout container;
	private PersonalBoardView personalBoardView;
	private SearchBoardView searchBoardView;
	private NewTopicView newTopicView;
	private HotTopicView hotTopicView;
	private FriendListView friendListView;
	private ProgressDialog pBar;

	private View vState;

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
				headerView.setUserImg(bmUserImg);
				personalBoardView.setUserImage(bmUserImg);
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

	public void setTitle(String s) {
		headerView.setTitle(s);
	}

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_frame);
		findViews();
		setListeners();
		getUserImg();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		switchToView(V_PERSONAL_BOARD);
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
		container.invalidate();
	}

	private void setListeners() {
		footerView.setListeners(this);
		headerView.setListeners(this);

		headerView.setTitleOnclickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "title click");
				if (personalBoardView != null) {
					personalBoardView.scrollListTo(0, 0);
				}
				if (friendListView != null) {
					friendListView.scrollListTo(0, 0);
				}
				if (searchBoardView != null) {
					searchBoardView.scrollListTo(0, 0);
				}
				if (hotTopicView != null) {
					hotTopicView.scrollListTo(0, 0);
				}
				if (newTopicView != null) {
					newTopicView.scrollListTo(0, 0);
				}
			}
		});
	}

	private void findViews() {
		footerView = (FooterView) findViewById(R.id.main_footer);
		footerView.setParentView(this);
		headerView = (HeaderView) findViewById(R.id.main_header);
		container = (LinearLayout) findViewById(R.id.main_dynamic);
		vState = findViewById(R.id.v_stat);
		WindowManager manage = getWindowManager();
		Display display = manage.getDefaultDisplay();
		Log.d(TAG, display.getWidth() + "");
		LayoutParams params = vState.getLayoutParams();
		params.width = display.getWidth() / 5;
	}

	@Override
	public void switchToView(int index) {
		container.removeAllViews();
		switch (index) {
		case V_PERSONAL_BOARD:
			headerView.resetButton();
			if (personalBoardView == null) {
				personalBoardView = new PersonalBoardView(this);
				personalBoardView.setParentView(this);
			}
			personalBoardView.onSwitch();
			// personalBoardView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.alpha_change));
			container.addView(personalBoardView);
			break;
		case V_BOARD_SEARCH:
			headerView.resetButton();
			if (searchBoardView == null) {
				searchBoardView = new SearchBoardView(this);
				searchBoardView.setParentView(this);
				searchBoardView.setUserImage(bmUserImg);
			}
			searchBoardView.onSwitch();
			// searchBoardView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.alpha_change));
			container.addView(searchBoardView);
			break;
		case V_FRIEND:
			headerView.setButtonImageResource(R.drawable.message_icon);
			headerView.setButtonOnclickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent().setClass(
							getApplicationContext(), PmActivity.class));
				}
			});
			if (friendListView == null) {
				friendListView = new FriendListView(this);
				friendListView.setParentView(this);
				friendListView.setUserImage(bmUserImg);
			}
			friendListView.onSwitch();
			// friendListView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.alpha_change));
			container.addView(friendListView);
			break;

		case V_HOT:
			headerView.resetButton();
			if (hotTopicView == null) {
				hotTopicView = new HotTopicView(this);
				hotTopicView.setParentView(this);
				hotTopicView.setUserImage(bmUserImg);
			}
			hotTopicView.onSwitch();
			// hotTopicView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.alpha_change));
			container.addView(hotTopicView);
			break;
		case V_NEW:
			headerView.resetButton();
			if (newTopicView == null) {
				newTopicView = new NewTopicView(this);
				newTopicView.setParentView(this);
				newTopicView.setUserImage(bmUserImg);
			}
			newTopicView.onSwitch();
			// newTopicView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.alpha_change));
			container.addView(newTopicView);

			break;
		default:
			break;
		}
		refresh();
	}

	//
	// @Override
	// public void onBackPressed() {
	// Log.d("persoanl Board", "back pressed");
	// new AlertDialog.Builder(this).setTitle("确定退出？")
	// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// finish();
	// }
	// })
	// .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// dialog.dismiss();
	// }
	// }).create().show();
	// }
	//
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
		WindowManager manage = getWindowManager();
		Display display = manage.getDefaultDisplay();
		Log.d(TAG, display.getWidth() + "");
		LayoutParams params = vState.getLayoutParams();
		params.width = display.getWidth() / 5;
		footerView.resetFooterState(display.getWidth());
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
//		case R.id.exit:
//			finish();
//			return true;
		case R.id.settings:
			goto_settings();
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

	private void goto_settings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
 
	/**
	 * 
	 */
	private void doSendFeedBack() {
		Bundle bundle = new Bundle();
		bundle.putInt(EditActivity.MOD, EditActivity.MOD_PM);
		bundle.putString(EditActivity.TO_USER, "MyCC.98");
		bundle.putString(EditActivity.PM_TITLE, "MyCC98软件反馈");
		startActivity(new Intent().setClass(
				getApplicationContext(), EditActivity.class)
				.putExtra(EditActivity.BUNDLE, bundle));
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
		finish();
	}

	private void showAboutInfo() {

//		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//		builder.setTitle("关于本软件");
//		builder.setMessage(R.string.about_info);
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//
//			}
//		});
//		builder.create().show();
		
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

	Thread checkUpateThread = new Thread() {
		@Override
		public void run() {
			String content = "";
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(IS_LIFETOY_VERSION?UPDATE_LINK_LIFETOY:UPDATE_LINK_NORMAL);
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
		finish();
	}

}
