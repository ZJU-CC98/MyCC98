package tk.djcrazy.MyCC98;

import java.util.Timer;
import java.util.TimerTask;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.adapter.HomeActionListAdapter;
import tk.djcrazy.MyCC98.adapter.HomeFragmentPagerAdapter;
import tk.djcrazy.MyCC98.dialog.AboutDialog;
import tk.djcrazy.MyCC98.listener.LoadingListener;
import tk.djcrazy.libCC98.ICC98Service;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.viewpagerindicator.TitlePageIndicator;

@ContentView(R.layout.home)
public class HomeActivity extends RoboSherlockFragmentActivity implements
		LoadingListener, ActionBar.OnNavigationListener {

	private static final String TAG = "HomeActivity";
	public static final int V_PERSONAL_BOARD = 0;
	public static final int V_BOARD_SEARCH = 1;
	public static final int V_HOT = 2;
	public static final int V_NEW = 3;
	public static final int V_FRIEND = 4;
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
		setTheme(com.actionbarsherlock.R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
		PreferenceManager.setDefaultValues(this, R.xml.settings,
				false);
		configureActionBar();
		HomeFragmentPagerAdapter adapter = new HomeFragmentPagerAdapter(
				getSupportFragmentManager());
		adapter.setLoadingListener(this);
		viewPager.setAdapter(adapter);
		indicator.setViewPager(viewPager, 0);
	}

	/**
	 * 
	 */
	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		HomeActionListAdapter list = new HomeActionListAdapter(this,
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

	/**
	 * 
	 */
	private void doSendFeedBack() {
		Intent intent = new Intent(getApplicationContext(), EditActivity.class);
		intent.putExtra(EditActivity.MOD, EditActivity.MOD_PM);
		intent.putExtra(EditActivity.PM_TO_USER, "MyCC.98");
		intent.putExtra(EditActivity.PM_TITLE, "MyCC98软件反馈");
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
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
}
