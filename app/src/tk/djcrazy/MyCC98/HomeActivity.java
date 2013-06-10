package tk.djcrazy.MyCC98;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.adapter.HomeFragmentPagerAdapter;
import tk.djcrazy.MyCC98.fragment.HomeBehindMenuFragment;
import tk.djcrazy.MyCC98.listener.LoadingListener;
import tk.djcrazy.MyCC98.service.NewVersionDownloadService;
import tk.djcrazy.MyCC98.util.DisplayUtil;
import tk.djcrazy.libCC98.ICC98Service;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.widget.Toast;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.util.EntityUtils;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;
import com.slidingmenu.lib.SlidingMenu;

public class HomeActivity extends BaseSlidingFragmentActivity implements
		LoadingListener, TabListener {

	
	private static final String UPDATE_LINK = "http://mycc98.sinaapp.com/update.json";
	private static final String UPDATE_LINK_PROXY = "http://mycc98.sinaapp.com/update_proxy.json";

	private static final String TAG = "HomeActivity";
	public static final String USERINFO = "USERINFO";
	public static final String AUTOLOGIN = "AUTOLOGIN";

 	private ViewPager viewPager;
 	
 	@Inject
 	private ICC98Service service;
 
 	
	Fragment mFragment = null;
 
	protected void onStop() {
		super.onStop();
		flushCache();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);	
 		setBehindContentView(R.layout.home_behind_view);
		PreferenceManager.setDefaultValues(this, R.xml.settings, false);
		HomeFragmentPagerAdapter adapter = new HomeFragmentPagerAdapter(
				getSupportFragmentManager());
		adapter.setLoadingListener(this);
		viewPager = (ViewPager) findViewById(R.id.main_pages);
		viewPager.setAdapter(adapter);
		configureActionBar();
 		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
				getSupportActionBar().setSelectedNavigationItem(arg0);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFragment = new HomeBehindMenuFragment();
 			t.replace(R.id.home_behind_view, mFragment);
			t.commit();
		} else {
			mFragment = (Fragment)this.getSupportFragmentManager().findFragmentById(R.id.home_behind_view);
		}

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidth(80); 
		sm.setBehindScrollScale(0f);
 		sm.setBehindOffset(DisplayUtil.dip2px(getApplicationContext(), 150));
		sm.setFadeDegree(0f);
		viewPager.setCurrentItem(0);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
 		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		new CheckUpdateTask(this).execute();
	}
	
	private void flushCache() {
		try {
			Object cache = Class.forName("android.net.http.HttpResponseCache")
					.getMethod("getInstalled").invoke(null);
			if (cache != null) {
				Class.forName("android.net.http.HttpResponseCache")
				.getMethod("flush").invoke(cache);
			}
		} catch (Exception httpResponseCacheNotAvailable) {
			
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText("我的版面").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("热门话题").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("版面列表").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("查看新帖").setTabListener(this));
 		actionBar.setIcon(new BitmapDrawable(service.getCurrentUserAvatar()));
 		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(service.getCurrentUserName());
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
		case android.R.id.home:
			getSlidingMenu().toggle();
			return true;
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
			HttpGet get = new HttpGet(LoginActivity.IS_PROXY_VERSION?UPDATE_LINK_PROXY:UPDATE_LINK);
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

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
}
