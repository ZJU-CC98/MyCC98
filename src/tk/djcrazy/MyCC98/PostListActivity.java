/**
 * The activity is to show the post list of the chooesed board
 */

package tk.djcrazy.MyCC98;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.MyCC98.adapter.PostListViewAdapter;
import tk.djcrazy.MyCC98.view.HeaderView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import tk.djcrazy.libCC98.CC98Client;
import tk.djcrazy.libCC98.CC98Parser;
import tk.djcrazy.libCC98.data.PostEntity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class PostListActivity extends Activity implements OnRefreshListener {
	public static final String BOARD_ENTITY = "boardList";
	public static final String BOARD_LIST = "boardList";
	public static final String BOARD_LINK = "boardLink";
	public static final String BOARD_NAME = "boardName";
	public static final String PAGE_NUMBER = "pageNumber";
	public static final String USER_IMAGE = "userImage";

	private static final String TAG = "PostListActivity";
	private static final int MSG_LIST_SUCC = 0;

	private Bundle bundle = new Bundle();

	private Intent pageIntent = new Intent();

	private static String boardlink = "";

	private static String boardName = "";
	private int boardid;

	private int pageNumber = 1;

	private List<PostEntity> PostList;
	private PullToRefreshListView listView;
	private PostListViewAdapter postListViewAdapter;
	private View vNext;
	private View vPrev;
	private View vNewpost;
	private View vReturnToBoardList;
	private ProgressDialog dialog;

	private Bitmap userImage;
	private HeaderView headerView;

	@Override
	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.post_list);
		bundle = getIntent().getBundleExtra(BOARD_LIST);

		boardlink = bundle.getString(BOARD_LINK);
		Log.d(TAG, boardlink);
		boardName = bundle.getString(BOARD_NAME);
		pageNumber = bundle.getInt(PAGE_NUMBER);

		userImage = (Bitmap) bundle.getParcelable(USER_IMAGE);
		boardid = Integer.parseInt(boardlink.replace(CC98Client.getCC98Domain()
				+ "list.asp?boardid=", ""));
		pageIntent.setClass(this, PostListActivity.class);
		dialog = ProgressDialog.show(PostListActivity.this, "", "Loading...",
				true);
		dialog.setCancelable(true);
		dialog.show();
		findViews();
		setViews();
		setListeners();
		fetchContent();
	}

	private void fetchContent() {

		new Thread() {
			@Override
			public void run() {
				try {
					PostList = CC98Parser.getPostList(boardlink + "&page="
							+ pageNumber);
					handler.sendEmptyMessage(MSG_LIST_SUCC);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
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
		setTitle(boardName);
	}

	private void setListeners() {
		vPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				prevPage();
			}
		});
		vNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextPage();
			}
		});
		headerView.setButtonOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PostListActivity.this, PostSearchActivity.class);
				intent.putExtra(PostSearchActivity.BOARDID, boardid);
				intent.putExtra(PostSearchActivity.BOARDNAME, boardName);
				startActivity(intent);
			}
		});
		vNewpost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sendNewPost();
			}
		});

		vReturnToBoardList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent().setClass(getApplicationContext(),
						HomeActivity.class));
				finish();
			}
		});
	}

	/**
     * 
     */
	private void findViews() {
		listView = (PullToRefreshListView) findViewById(R.id.postlistView);
		listView.setOnRefreshListener(this);
		vPrev = findViewById(R.id.post_pre_page);
		vNext = findViewById(R.id.post_next_page);
		vNewpost = findViewById(R.id.post_newpost);
		headerView = (HeaderView) findViewById(R.id.main_header);
		vReturnToBoardList = findViewById(R.id.post_return_to_personal_board);
	}

	private void setViews() {
		headerView.setUserImg(userImage);
		headerView.setTitle(boardName + "\n" + "第" + pageNumber + "页");
		headerView.setTitleTextSize(13f);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LIST_SUCC:
				postListViewAdapter = new PostListViewAdapter(
						PostListActivity.this, PostList);
				listView.setAdapter(postListViewAdapter);
				postListViewAdapter.setUserImage(userImage);
				if (pageNumber == 1) {
					vPrev.setVisibility(View.GONE);
				} else {
					vPrev.setVisibility(View.VISIBLE);
				}
				headerView
						.setTitle(boardName + "\n" + "第" + pageNumber + "页");
				listView.onRefreshComplete();
				dialog.dismiss();
				break; 
			default:
				break; 
			}
		}
	};

	/**
	 * override menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.post_list_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.post_list_menu_exit:
			finish();
			return true;
		case R.id.post_list_menu_log_out:
			Intent intent = new Intent();
			intent.setClass(PostListActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
			return true;
		case R.id.post_list_menu_new_post:
			sendNewPost();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 
	 */
	private void sendNewPost() {
		Bundle bundle = new Bundle();
		bundle.putString(EditActivity.BOARD_NAME, boardName);
		bundle.putInt(EditActivity.BOARD_ID, boardid);
		bundle.putInt(EditActivity.MOD, EditActivity.MOD_NEW_POST);
		bundle.putParcelable(EditActivity.USER_IMAGE, userImage);
		Intent intent2 = new Intent();
		intent2.putExtra(EditActivity.BUNDLE, bundle);
		intent2.setClass(PostListActivity.this, EditActivity.class);
		startActivityForResult(intent2, 0);
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
}
