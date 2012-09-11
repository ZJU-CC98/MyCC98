package tk.djcrazy.MyCC98;

import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.adapter.NewTopicListAdapter;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.SearchResultEntity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;

@ContentView(R.layout.post_search)
public class PostSearchActivity extends BaseActivity {

	public static final String BOARD_ID = "boardid";
	public static final String BOARD_NAME = "boardname";
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

	private String boardId;
	private String boardName;
	private String mQueryString;

	private List<SearchResultEntity> mResList;
	private NewTopicListAdapter newTopicListAdapter;

	private int currentPage = 1;
	private int totalPage = 1;
	private String currentType = SEARCH_TYPE_TITLE;

	private ProgressDialog pg;

	@Inject
	private ICC98Service service;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FETCH_SUCC:
				int totalpost = Integer.parseInt(mResList.get(0)
						.getTotalResult());
				mResList.remove(0);
				totalPage = totalpost % 20 == 0 ? totalpost / 20
						: totalpost / 20 + 1;
				getSupportActionBar().setSubtitle(
						"共" + totalpost + "个结果  " + "第" + currentPage + "页 | 共"
								+ totalPage + "页");
				if (currentPage <= 1) {
					vPrev.setVisibility(View.GONE);
				} else {
					vPrev.setVisibility(View.VISIBLE);
				}
				if (currentPage >= totalPage) {
					vNext.setVisibility(View.GONE);
				} else {
					vNext.setVisibility(View.VISIBLE);
				}
				newTopicListAdapter = new NewTopicListAdapter(
						PostSearchActivity.this, mResList);
				listView.setAdapter(newTopicListAdapter);
				pg.dismiss();
				getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
				break;
			case NOTFOUND:
				pg.dismiss();
				Toast.makeText(PostSearchActivity.this, "木有找到...",
						Toast.LENGTH_SHORT).show();
			case FETCH_ERROR:
				pg.dismiss();
				ToastUtils.show(PostSearchActivity.this, "加载失败");
			default:
				break;
			}
		}
	};

	private void fetchContent(final String keyWord) {
		pg.show();
		new Thread() {
			@Override
			public void run() {
				try {
					mResList = service.searchPost(keyWord, boardId,
							currentType, currentPage);
					if (mResList.size() > 1) {
						handler.sendEmptyMessage(FETCH_SUCC);
					} else {
						handler.sendEmptyMessage(NOTFOUND);
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(FETCH_ERROR);
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		configureActionBar();
		setListeners();
		pg = new ProgressDialog(this);
		pg.setMessage("正在加载数据...");
		rbByTitle.setChecked(true);
		handleIntent(getIntent());
	}

	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setLogo(new BitmapDrawable(service.getUserAvatar()));
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
 	     appData.putString(SEARCH_TYPE, currentType);
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
			currentType = appData.getString(SEARCH_TYPE);
			currentPage = 1;
			totalPage   = 1;
			if (currentType==null) {
				currentType=SEARCH_TYPE_TITLE;
			}
			getSupportActionBar().setTitle("搜索：" + mQueryString+" 在"+boardName);
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
				Intent intent = new Intent(PostSearchActivity.this,
						PostContentsJSActivity.class);
				intent.putExtra(PostContentsJSActivity.BOARD_ID,
						mResList.get(arg2).getBoardId());
				intent.putExtra(PostContentsJSActivity.PAGE_NUMBER, 1);
				intent.putExtra(PostContentsJSActivity.POST_ID,
						mResList.get(arg2).getPostId());
				intent.putExtra(PostContentsJSActivity.POST_NAME,
						mResList.get(arg2).getTitle());
				startActivity(intent);
 			}
		});
	}
}
