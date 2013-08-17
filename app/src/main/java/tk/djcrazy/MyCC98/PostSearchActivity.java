package tk.djcrazy.MyCC98;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.adapter.InboxFragmentPagerAdapter;
import tk.djcrazy.MyCC98.adapter.PostSearchFragmentPagerAdapter;
import tk.djcrazy.libCC98.CachedCC98Service;
import tk.djcrazy.libCC98.ICC98Service;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.google.inject.Inject;

@ContentView(R.layout.activity_post_search)
public class PostSearchActivity extends BaseFragmentActivity implements OnPageChangeListener, TabListener{

	public static final String BOARD_ID = "boardid";
	public static final String BOARD_NAME = "boardname";
	private String boardId;
	private String boardName;
	private String mQueryString;


 	@Inject
	private CachedCC98Service service;
 	
	@InjectView(R.id.post_search_main_pages)
	private ViewPager viewPager;

 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		configureActionBar();
  		handleIntent(getIntent());
	}

	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setLogo(new BitmapDrawable(getResources(), service.getCurrentUserAvatar()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu optionMenu) {
		getSupportMenuInflater().inflate(R.menu.post_search, optionMenu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.post_search_action:
			onSearchRequested();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

 	@Override
 	public boolean onSearchRequested() {
 	     Bundle appData = new Bundle();
 	     appData.putString(PostSearchActivity.BOARD_ID, boardId);
 	     appData.putString(PostSearchActivity.BOARD_NAME, boardName);
  	     startSearch(null, false, appData, false);
 	     return true;
 	 }

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
		
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			mQueryString = query;
			Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
			boardId = appData.getString(BOARD_ID);
			boardName = appData.getString(BOARD_NAME);
			PostSearchFragmentPagerAdapter adapter = new PostSearchFragmentPagerAdapter(
					getSupportFragmentManager(), query, boardId);
			viewPager.setAdapter(adapter);
			viewPager.setOnPageChangeListener(this);
            PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
            tabs.setViewPager(viewPager);
            tabs.setIndicatorColor(Color.parseColor("#1faeff"));
            getSupportActionBar().setTitle("搜索：" + mQueryString+" 在"+boardName);
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
