package tk.djcrazy.MyCC98;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.adapter.InboxFragmentPagerAdapter;
import tk.djcrazy.libCC98.ICC98Service;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
 
@ContentView(R.layout.activity_pm)
public class PmActivity extends BaseFragmentActivity implements OnPageChangeListener, TabListener{

	private static String TAG = "PmActivity";
	@Inject
	private ICC98Service service;

	@InjectView(R.id.pm_main_pages)
	private ViewPager viewPager;
//	@InjectView(R.id.pm_main_titles)
//	private TitlePageIndicator indicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pm);
		configureActionBar();
		InboxFragmentPagerAdapter adapter = new InboxFragmentPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
//		indicator.setViewPager(viewPager);
	}

	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setLogo(new BitmapDrawable(service.getCurrentUserAvatar()));
		actionBar.setTitle("论坛短消息");
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText("收件箱").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("发件箱").setTabListener(this));
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
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

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		getSupportActionBar().setSelectedNavigationItem(arg0);
	}
}
