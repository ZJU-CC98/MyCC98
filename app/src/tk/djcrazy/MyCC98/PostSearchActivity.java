package tk.djcrazy.MyCC98;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
<<<<<<< HEAD
import tk.djcrazy.MyCC98.adapter.NewTopicListAdapter;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.libCC98.CachedCC98Service;
import tk.djcrazy.libCC98.data.SearchResultEntity;
import android.app.ProgressDialog;
=======
import tk.djcrazy.MyCC98.adapter.InboxFragmentPagerAdapter;
import tk.djcrazy.MyCC98.adapter.PostSearchFragmentPagerAdapter;
import tk.djcrazy.libCC98.ICC98Service;
>>>>>>> 7d66653806d6165d4f004f584bb2a36db92b84cc
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.google.inject.Inject;

@ContentView(R.layout.activity_post_search)
public class PostSearchActivity extends BaseFragmentActivity implements OnPageChangeListener, TabListener{

	public static final String BOARD_ID = "boardid";
	public static final String BOARD_NAME = "boardname";
<<<<<<< HEAD
	public static final String SEARCH_TYPE = "searchType";
	public static final String SEARCH_TYPE_TITLE = "2";
	public static final String SEARCH_TYPE_AUTHOR = "1";
	private static final int FETCH_SUCC = 0;
	private static final int NOTFOUND = 1;
	private static final int FETCH_ERROR = 2;

	@InjectView(R.id.tv_postsearch_next)
	private View vNext;
	@InjectView(R.id.tv_postsearch_prev)
	private View vPrev;
	@InjectView(R.id.rg_postsearch_stype)
	private RadioGroup rg;
	@InjectView(R.id.rb_postsearch_by_title)
	private RadioButton rbByTitle;
	@InjectView(R.id.lv_postsearch)
	private ListView listView;

=======
    
>>>>>>> 7d66653806d6165d4f004f584bb2a36db92b84cc
	private String boardId;
	private String boardName;
	private String mQueryString;

<<<<<<< HEAD
	private List<SearchResultEntity> mResList;
	private NewTopicListAdapter newTopicListAdapter;

	private int currentPage = 1;
	private int totalPage = 1;
	private String currentType = SEARCH_TYPE_TITLE;

	private ProgressDialog pg;

	@Inject
	private CachedCC98Service service;
=======
 	@Inject
	private ICC98Service service;
 	
	@InjectView(R.id.post_search_main_pages)
	private ViewPager viewPager;
>>>>>>> 7d66653806d6165d4f004f584bb2a36db92b84cc

 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		configureActionBar();
  		handleIntent(getIntent());
	}

	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setLogo(new BitmapDrawable(service.getCurrentUserAvatar()));
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText("主题搜索").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("作者搜索").setTabListener(this));
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
<<<<<<< HEAD

	@Override
	public boolean onSearchRequested() {
		Bundle appData = new Bundle();
		appData.putString(PostSearchActivity.BOARD_ID, boardId);
		appData.putString(PostSearchActivity.BOARD_NAME, boardName);
		appData.putString(SEARCH_TYPE, currentType);
		startSearch(null, false, appData, false);
		return true;
	}
=======
 	@Override
 	public boolean onSearchRequested() {
 	     Bundle appData = new Bundle();
 	     appData.putString(PostSearchActivity.BOARD_ID, boardId);
 	     appData.putString(PostSearchActivity.BOARD_NAME, boardName);
  	     startSearch(null, false, appData, false);
 	     return true;
 	 }
>>>>>>> 7d66653806d6165d4f004f584bb2a36db92b84cc

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
<<<<<<< HEAD
			currentType = appData.getString(SEARCH_TYPE);
			currentPage = 1;
			totalPage = 1;
			if (currentType == null) {
				currentType = SEARCH_TYPE_TITLE;
			}
			getSupportActionBar().setTitle(
					"搜索：" + mQueryString + " 在" + boardName);
			fetchContent(query);
		}
	}

	private void setListeners() {
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if (arg1 == R.id.rb_postsearch_by_author) {
					currentType = SEARCH_TYPE_AUTHOR;
				} else if (arg1 == R.id.rb_postsearch_by_title) {
					currentType = SEARCH_TYPE_TITLE;
				}
			}
		});
		vNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentPage < totalPage) {
					++currentPage;
					fetchContent(mQueryString);
				}
			}
		});
		vPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentPage > 1) {
					--currentPage;
					fetchContent(mQueryString);
				}
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent2 = PostContentsJSActivity.createIntent(mResList
						.get(arg2).getBoardId(),
						mResList.get(arg2).getPostId(), 1, false);
				startActivity(intent2);
			}
		});
=======
			PostSearchFragmentPagerAdapter adapter = new PostSearchFragmentPagerAdapter(
					getSupportFragmentManager(), query, boardId);
			viewPager.setAdapter(adapter);
			viewPager.setOnPageChangeListener(this);
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
		
>>>>>>> 7d66653806d6165d4f004f584bb2a36db92b84cc
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		getSupportActionBar().setSelectedNavigationItem(arg0);
	}

}
