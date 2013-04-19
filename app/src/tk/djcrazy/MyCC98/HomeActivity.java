package tk.djcrazy.MyCC98;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.adapter.HomeActionListAdapter;
import tk.djcrazy.MyCC98.adapter.HomeFragmentPagerAdapter;
import tk.djcrazy.MyCC98.dialog.AboutDialog;
import tk.djcrazy.MyCC98.listener.LoadingListener;
import tk.djcrazy.MyCC98.service.NewVersionDownloadService;
import tk.djcrazy.MyCC98.util.Intents;
import tk.djcrazy.MyCC98.util.Intents.Builder;
import tk.djcrazy.libCC98.ICC98Service;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.util.EntityUtils;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.viewpagerindicator.TitlePageIndicator;

@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseFragmentActivity implements
		LoadingListener, ActionBar.OnNavigationListener {

	private static final String UPDATE_LINK = "http://mycc98.sinaapp.com/update.json";

	private static final String TAG = "HomeActivity";
	public static final String USERINFO = "USERINFO";
	public static final String AUTOLOGIN = "AUTOLOGIN";

	@InjectView(R.id.main_pages)
	private ViewPager viewPager;
	@InjectView(R.id.main_titles)
	private TitlePageIndicator indicator;

	@Inject
	private ICC98Service service;

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		getSupportActionBar().setSelectedNavigationItem(0);
		switch (itemPosition) {
		case 0:
 			break;
		case 1:
			goSettings();
			break;
		case 2:
			Intent profiIntent = new Intent();
			profiIntent.setClass(HomeActivity.this, ProfileActivity.class);
			profiIntent.putExtra("userName", service.getUserName());
			startActivity(profiIntent);
			break;
		case 3:
			doSendFeedBack();
			break;
		case 4:
			showAboutInfo();
			break;
		case 5:
			logOut();
		}
		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceManager.setDefaultValues(this, R.xml.settings, false);
		configureActionBar();
		HomeFragmentPagerAdapter adapter = new HomeFragmentPagerAdapter(
				getSupportFragmentManager());
		adapter.setLoadingListener(this);
		viewPager.setAdapter(adapter);
		indicator.setViewPager(viewPager, 0);
		new CheckUpdateTask(this).execute();
		
	}

	/**
	 * 
	 */
	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		HomeActionListAdapter list = new HomeActionListAdapter(actionBar.getThemedContext(),
				service.getUserName(), service.getUserAvatar());
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(list, this);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu optionMenu) {
		getSupportMenuInflater().inflate(R.menu.home, optionMenu);
		return true;
	}

	public void refresh() {
		viewPager.invalidate();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_search:
			onSearchRequested();
			return true;
		case R.id.menu_message:
			Intent intent = new Intent(HomeActivity.this, PmActivity.class);
			startActivity(intent);
			return true;
		default:
			break;
		}
		return false;
	}

	@Override
	public boolean onSearchRequested() {
		Bundle appData = new Bundle();
		appData.putString(PostSearchActivity.BOARD_ID, "0");
		appData.putString(PostSearchActivity.BOARD_NAME, "全站");
		startSearch(null, false, appData, false);
		return true;
	}

	//
	private void goSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	private void doSendFeedBack() {
		Builder builder = new Intents.Builder(this, EditActivity.class);
		Intent intent = builder.requestType(EditActivity.REQUEST_PM)
				.pmToUser("MyCC.98").pmTitle("MyCC98软件反馈").toIntent();
		startActivity(intent);
	}

	private void logOut() {
		getSharedPreferences(USERINFO, 0).edit().putBoolean(AUTOLOGIN, false)
				.commit();
		Intent intent = new Intent();
		intent.setClass(HomeActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	private void showAboutInfo() {
		new AboutDialog(this).show();
	}

	@Override
	public void onLoadComplete(int postion) {
		if (viewPager.getCurrentItem() == postion) {
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
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				if (!hasTask) {
					tExit.schedule(task, 2000);
				}
			} else {
				finish();
				java.lang.System.exit(0);
			}
		}
		return false;
	}

	private class CheckUpdateTask extends RoboAsyncTask<String> {

		protected CheckUpdateTask(Context context) {
			super(context);
		}

		@Override
		public String call() throws Exception {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(UPDATE_LINK);
			HttpResponse response = client.execute(get);
			return EntityUtils.toString(response.getEntity(), "UTF-8");
 		}

		@Override
		protected void onSuccess(String t) throws Exception {
			super.onSuccess(t); 
			String aString =new String(t.getBytes(),"UTF-8" );
			aString = aString.replaceAll(".*?\\{", "{");
			JSONObject object = new JSONObject(t);
			int verionCode = object.getInt("versionCode");
			if (verionCode>getVersionCode()) {
				final String downloadLink = object.getString("downloadLink");
				String versionName = object.getString("versionName");
				String updateHint = object.getString("hint");
	 			AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("发现新版本");
				builder.setMessage("版本号："+versionName+"\n"+"更新内容："+updateHint);
				builder.setPositiveButton("下载", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
 						startService(NewVersionDownloadService.createIntent(HomeActivity.this, downloadLink));
					}
				});
				builder.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		}

		private int getVersionCode() throws NameNotFoundException {
			// 获取packagemanager的实例
			PackageManager packageManager = getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			return packInfo.versionCode;
		}
 
	}
}
