/**
 * The activity is to show the post list of the chooesed board
 */

package tk.djcrazy.MyCC98;

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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.inject.Inject;

@ContentView(R.layout.post_list)
public class PostListActivity extends BaseActivity implements OnRefreshListener, OnClickListener {
	private static final String TAG = "PostListActivity";

	public static final String BOARD_ID = "boardId";
	public static final String BOARD_NAME = "boardName";
	public static final String PAGE_NUMBER = "pageNumber";

	private static final int MSG_LIST_SUCC = 0;
	private static final int MSG_LIST_FAILED = 1;

	@InjectExtra(BOARD_NAME)
	private String boardName;
	@InjectExtra(BOARD_ID)
	private String boardId;
	@InjectExtra(PAGE_NUMBER)
	private int pageNumber;

	private List<PostEntity> postList;

	@InjectView(R.id.postlistView)
	private PullToRefreshListView listView;

 	@InjectView(R.id.post_list_header_userimg)
	private ImageView userHeader;
	@InjectView(R.id.post_list_header_title)
	private TextView headerTitle;
	@InjectView(R.id.post_list_push_new_post_btn)
	private ImageView pushNewPostButton;
	@InjectView(R.id.post_list_search_btn)
	private ImageView searchButton;
 	
	private ProgressBar loadMoreProgressBar;
 	private TextView loadMoreTextView;
	private View  footerView;

	private PostListViewAdapter postListViewAdapter;

	@Inject
	private ICC98Service service;

	@Override
	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);
		postList = new ArrayList<PostEntity>();
		postListViewAdapter = new PostListViewAdapter(
				PostListActivity.this, postList, boardId, boardName);
		listView.setAdapter(postListViewAdapter);
 		listView.setOnRefreshListener(this);
 		userHeader.setImageBitmap(service.getUserAvatar());
		headerTitle.setText(boardName);
		headerTitle.setOnClickListener(this);
		pushNewPostButton.setOnClickListener(this);
		searchButton.setOnClickListener(this);
		footerView = LayoutInflater.from(this).inflate(
				R.layout.load_more, null);
		loadMoreProgressBar = (ProgressBar) footerView.findViewById(R.id.load_more_progress);
		loadMoreTextView = (TextView) footerView.findViewById(R.id.load_more_text);
		loadMoreTextView.setOnClickListener(this);
		listView.addFooterView(footerView);
		fetchContent();
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
	public void onRefresh() {
		pageNumber = 1;
		postList.clear();
		postListViewAdapter.notifyDataSetChanged();
		fetchContent();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.post_pre_page:
//			prevPage();
//			break;
//		case R.id.post_next_page:
//			nextPage();
//			break;
//		case R.id.post_newpost:
//			sendNewPost();
//			break;
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
	
	private class LoadPostListTask extends RoboAsyncTask<List<PostEntity>>  {
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
