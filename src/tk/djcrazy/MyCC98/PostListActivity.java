/**
 * The activity is to show the post list of the chooesed board
 */

package tk.djcrazy.MyCC98;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.adapter.PostListViewAdapter;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.MyCC98.view.HeaderView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.PostEntity;
import tk.djcrazy.libCC98.exception.ParseContentException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

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

	private List<PostEntity> PostList;

	@InjectView(R.id.postlistView)
	private PullToRefreshListView listView;

	@InjectView(R.id.post_next_page)
	private View vNext;
	@InjectView(R.id.post_pre_page)
	private View vPrev;
	@InjectView(R.id.post_newpost)
	private View vNewpost;

	private ProgressDialog dialog;

	private PostListViewAdapter postListViewAdapter;

	@Inject
	private ICC98Service service;

	@Override
	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);
		dialog = ProgressDialog.show(PostListActivity.this, "", "Loading...",
				true);
		dialog.setCancelable(true);
		dialog.show();
		listView.setOnRefreshListener(this);
		vPrev.setOnClickListener(this);
		vNext.setOnClickListener(this);
		vNewpost.setOnClickListener(this);
		fetchContent();
	}

	private void fetchContent() {
		new Thread() {
			@Override
			public void run() {
				try {
					PostList = service.getPostList(boardId, pageNumber);
					handler.sendEmptyMessage(MSG_LIST_SUCC);
				} catch (Exception e) {
					handler.sendEmptyMessage(MSG_LIST_FAILED);
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void nextPage() {
		++pageNumber;
		fetchContent();
	}

	private void prevPage() {
		if (pageNumber != 1) {
			--pageNumber;
			fetchContent();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void setListeners() {

		// headerView.setButtonOnclickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent();
		// intent.setClass(PostListActivity.this, PostSearchActivity.class);
		// intent.putExtra(PostSearchActivity.BOARDID, boardid);
		// intent.putExtra(PostSearchActivity.BOARDNAME, boardName);
		// startActivity(intent);
		// overridePendingTransition(R.anim.forward_activity_move_in,
		// R.anim.forward_activity_move_out);
		// }
		// });
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LIST_SUCC:
				postListViewAdapter = new PostListViewAdapter(
						PostListActivity.this, PostList, boardId, boardName);
				listView.setAdapter(postListViewAdapter);
				if (pageNumber == 1) {
					vPrev.setVisibility(View.GONE);
				} else {
					vPrev.setVisibility(View.VISIBLE);
				}
				listView.onRefreshComplete();
				dialog.dismiss();
				break;
			case MSG_LIST_FAILED:
				ToastUtils.show(PostListActivity.this, "加载列表失败TAT");
			default:
				break;
			}
		}
	};

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
		fetchContent();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.post_pre_page:
			prevPage();
			break;
		case R.id.post_next_page:
			nextPage();
			break;
		case R.id.post_newpost:
			sendNewPost();
			break;
		default:
			break;
		}
	}
}
