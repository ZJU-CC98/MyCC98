/**
 * The activity is to show the post list of the chooesed board
 */

package tk.djcrazy.MyCC98;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.adapter.PostListViewAdapter;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.MyCC98.util.ViewUtils;
import tk.djcrazy.MyCC98.view.PullToRefreshListView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.PostEntity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;

@ContentView(R.layout.post_list)
public class PostListActivity extends BaseActivity implements
		OnRefreshListener, OnClickListener {
	private static final String TAG = "PostListActivity";

	public static final String BOARD_ID = "boardId";
	public static final String BOARD_NAME = "boardName";
	public static final String PAGE_NUMBER = "pageNumber";

	private static final int MENU_SEARCH_ID = 1;
	private static final int MENU_NEW_POST_ID = 2;

	@InjectExtra(BOARD_NAME)
	private String boardName;
	@InjectExtra(BOARD_ID)
	private String boardId;
	@InjectExtra(PAGE_NUMBER)
	private int pageNumber;

	private List<PostEntity> postList;

	@InjectView(R.id.postlistView)
	private PullToRefreshListView listView;

	private ProgressBar loadMoreProgressBar;
	private TextView loadMoreTextView;
	private View footerView;

	private PostListViewAdapter postListViewAdapter;

	@Inject
	private ICC98Service service;

	@Override
	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);
		configureActionBar();
		postList = new ArrayList<PostEntity>();
		postListViewAdapter = new PostListViewAdapter(PostListActivity.this,
				postList, boardId, boardName);
		listView.setAdapter(postListViewAdapter);
		listView.setOnRefreshListener(this);
		footerView = LayoutInflater.from(this)
				.inflate(R.layout.load_more, null);
		loadMoreProgressBar = (ProgressBar) footerView
				.findViewById(R.id.load_more_progress);
		loadMoreTextView = (TextView) footerView
				.findViewById(R.id.load_more_text);
		loadMoreTextView.setOnClickListener(this);
		listView.addFooterView(footerView);
		fetchContent();
	}

	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setLogo(new BitmapDrawable(service.getUserAvatar()));
		actionBar.setTitle(boardName);
	}

	private void fetchContent() {
		new LoadPostListTask(this, boardId, pageNumber).execute();
	}

	/**
	 * 
	 */
	private void sendNewPost() {
		Intent intent = new Intent(PostListActivity.this, EditActivity.class);
		intent.putExtra(EditActivity.MOD, EditActivity.MOD_NEW_POST);
		intent.putExtra(EditActivity.BOARD_ID, boardId);
		intent.putExtra(EditActivity.BOARD_NAME, boardName);
		startActivityForResult(intent, 0);
		overridePendingTransition(R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			fetchContent();
		}
		if (resultCode == Activity.RESULT_CANCELED) {
		}
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.backward_activity_move_in,
					R.anim.backward_activity_move_out);
			return true;
		case R.id.post_list_menu_search:
			onSearchRequested();
			return true;
		case R.id.post_list_menu_new_post:
			sendNewPost();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu optionMenu) {
		getSupportMenuInflater().inflate(R.menu.post_list, optionMenu);
		return true;
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
	public void onRefresh() {
		pageNumber = 1;
		postList.clear();
		postListViewAdapter.notifyDataSetChanged();
		fetchContent();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.post_pre_page:
		// prevPage();
		// break;
		// case R.id.post_next_page:
		// nextPage();
		// break;
		// case R.id.post_newpost:
		// sendNewPost();
		// break;
		case R.id.post_list_header_title:
			listView.smoothScrollToPosition(1);
			break;
		case R.id.load_more_text:
			pageNumber++;
			fetchContent();
			break;
		default:
			break;
		}
	}

	private class LoadPostListTask extends RoboAsyncTask<List<PostEntity>> {
		private Activity mContext;
		private int mPageNum;
		private String mBoardId;
		@Inject
		private ICC98Service mService;

		protected LoadPostListTask(Activity context, String boardId, int pageNum) {
			super(context);
			mContext = context;
			mPageNum = pageNum;
			mBoardId = boardId;
		}

		@Override
		protected void onPreExecute() {
			ViewUtils.setGone(loadMoreProgressBar, false);
			loadMoreTextView.setText("正在加载...");
		}

		@Override
		public List<PostEntity> call() throws Exception {
			return mService.getPostList(mBoardId, mPageNum);
		}

		@Override
		protected void onSuccess(List<PostEntity> list) {
			postList.addAll(list);
		}

		@Override
		protected void onException(Exception e) {
			e.printStackTrace();
			ToastUtils.show(mContext, "加载失败，请检查网络连接");
		}

		@Override
		protected void onFinally() {
			ViewUtils.setGone(loadMoreProgressBar, true);
			loadMoreTextView.setText("点击加载更多");
			listView.onRefreshComplete();
			postListViewAdapter.notifyDataSetChanged();
		}
	}
}
