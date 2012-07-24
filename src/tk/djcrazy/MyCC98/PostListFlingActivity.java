///**
// * The activity is to show the post list of the chooesed board
// */
//
//package tk.djcrazy.MyCC98;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.http.ParseException;
//import org.apache.http.client.ClientProtocolException;
//
//import com.flurry.android.FlurryAgent;
//
//import tk.djcrazy.MyCC98.view.ListViewFlingGallery;
//import tk.djcrazy.MyCC98.view.PostListViewAdapter;
//import tk.djcrazy.libCC98.CC98Parser;
//import tk.djcrazy.libCC98.data.PostEntity;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class PostListFlingActivity extends Activity {
//
//	public static final String BOARD_ENTITY = "boardList";
//	public static final String BOARD_LINK = "boardLink";
//	public static final String BOARD_NAME = "boardName";
//	public static final String PAGE_NUMBER = "pageNumber";
//
//	private static final String TAG = "PostListFlingActivity";
//
//	private static final int FINISH_LOADING_CONTENTS = 1;
//	private static final int ERROR_LOADING_CONTENTS = 0;
//
//	private Bundle bundle = new Bundle();
//
//	private ListViewFlingGallery mGallery;
//
//	private static String boardlink = "";
//
//	private static String boardName = "";
//
//	private int totalPageNum = 100;
//
//	private final String[] mLabelArray = { "View1", "View2", "View3", "View4",
//			"View5" };
//
//	private int pageNumber = 1;
//
//	private List<PostEntity> PostList;
//
//	private PostListViewAdapter postListViewAdapter;
//
//	private ProgressDialog dialog;
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		return mGallery.onGalleryTouchEvent(event);
//	}
//	@Override 
//	public void onStart()
//	{
//	   super.onStart();
//	   FlurryAgent.onStartSession(this, "5EXV7SIGMTTDKYNXTKR4");
//	}
//	
//	@Override
//	public void onStop()
//	{
//	   super.onStop();
//	   FlurryAgent.onEndSession(this);
//	}
//
//	@Override
//	public void onCreate(Bundle SavedInstanceState) {
//		super.onCreate(SavedInstanceState);
//		// 设置无标题栏
//		// requestWindowFeature(Window.FEATURE_NO_TITLE);
//		// 设置为全屏模式
//		/*
//		 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		 */
//		bundle = getIntent().getBundleExtra(BOARD_ENTITY);
//		boardlink = bundle.getString(BOARD_LINK).toString();
//		boardName = bundle.getString(BOARD_NAME).toString();
//		pageNumber = bundle.getInt(PAGE_NUMBER);
//		setTitle(boardName);
//
//		mGallery = new ListViewFlingGallery(this);
//		mGallery.setAdapter(pageNumber - 1, new ArrayAdapter<String>(
//				getApplicationContext(), android.R.layout.simple_list_item_1,
//				mLabelArray) {
//
//			@Override
//			public View getView(int position, View convertView, ViewGroup parent) {
//				Log.d(TAG, "count=" + position);
//				final PostListView mListView = new PostListView(
//						getApplicationContext(), position + 1);
//				final int ps = position;
//
//				new Thread() {
//					@Override
//					public void run() {
//						try {
//							PostList = CC98Parser.getPostList(boardlink
//									+ (boardlink.contains("&page=")?(ps + 1):"&page="+(ps+1)));
//							Message msg = new Message();
//							msg.obj = mListView;
//							msg.what = FINISH_LOADING_CONTENTS;
//							handler.sendMessage(msg);
//
//						} catch (ClientProtocolException e) {
//							handler.sendEmptyMessage(ERROR_LOADING_CONTENTS);
//							e.printStackTrace();
//						} catch (ParseException e) {
//							handler.sendEmptyMessage(ERROR_LOADING_CONTENTS);
//							e.printStackTrace();
//						} catch (IOException e) {
//							handler.sendEmptyMessage(ERROR_LOADING_CONTENTS);
//							e.printStackTrace();
//						}
//
//					}
//				}.start();
//
//				return mListView;
//			}
//
//			@Override
//			public int getCount() {
//				return totalPageNum;
//			}
//
//		});
//		mGallery.setIsGalleryCircular(false);
//
//		setUpContent();
//
//	}
//
//	private void setUpContent() {
//
//		LinearLayout layout = new LinearLayout(getApplicationContext());
//		layout.setBackgroundResource(R.color.post_list_background);
//		layout.setOrientation(LinearLayout.VERTICAL);
//
//		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		layoutParams.weight = 1.0f;
//
//		layout.addView(mGallery, layoutParams);
//
//		LinearLayout subLayout = new LinearLayout(getApplicationContext());
//
//		layoutParams = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//		subLayout.setLayoutParams(layoutParams);
//		subLayout.setBackgroundResource(R.drawable.maintab_toolbar_bg);
//		layoutParams = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		layoutParams.weight = 1.0f;
//		subLayout.addView(getButton1(), layoutParams);
//		subLayout.addView(getButton2(), layoutParams);
//		subLayout.addView(getButton3(), layoutParams);
//		subLayout.addView(getButton4(), layoutParams);
//
//		layout.addView(subLayout);
//
//		setContentView(layout);
//	}
//
//	private View getButton4() {
//		TextView refresh = new TextView(getApplicationContext());
//		setStyle(refresh);
//		refresh.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.refresh,
//				0, 0);
//		refresh.setText(R.string.refresh);
//		refresh.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				mGallery.refreshCurrentPage();
//
//			}
//		});
//		return refresh;
//	}
//
//	private View getButton3() {
//		TextView search = new TextView(getApplicationContext());
//		setStyle(search);
//		search.setCompoundDrawablesWithIntrinsicBounds(0,
//				R.drawable.search_icon, 0, 0);
//		search.setText(R.string.search);
//		return search;
//	}
//
//	private View getButton2() {
//		TextView tvInbox = new TextView(getApplicationContext());
//		setStyle(tvInbox);
//		tvInbox.setCompoundDrawablesWithIntrinsicBounds(0,
//				R.drawable.message_box_icon, 0, 0);
//		tvInbox.setText(R.string.message_box);
//		tvInbox.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				startActivity((new Intent()).setClass(
//						PostListFlingActivity.this, PmActivity2.class));
//			}
//		});
//		return tvInbox;
//	}
//
//	private TextView getButton1() {
//
//		TextView returnToPersonalBoard = new TextView(getApplicationContext());
//		setStyle(returnToPersonalBoard);
//		returnToPersonalBoard.setCompoundDrawablesWithIntrinsicBounds(0,
//				R.drawable.bd_list_ico, 0, 0);
//		returnToPersonalBoard.setText(R.string.return_to_personal_board);
//		returnToPersonalBoard.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				finish();
//			}
//		});
//		return returnToPersonalBoard;
//
//	}
//
//	private void setStyle(TextView textView) {
//
//		textView.setGravity(Gravity.CENTER_HORIZONTAL);
//		textView.setBackgroundResource(R.drawable.detail_btn_selector);
//		textView.setTextSize(12.0f);
//		textView.setTextColor(Color.parseColor("#f9f9f9"));
//		textView.setPadding(5, 5, 5, 5);
//		textView.setClickable(true);
//	}
//
//	private Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case FINISH_LOADING_CONTENTS:
//				PostListView view = (PostListView) msg.obj;
//				postListViewAdapter = new PostListViewAdapter(
//						PostListFlingActivity.this, PostList);
//				view.setAdapter(postListViewAdapter);
//				view.setBackgroundResource(R.color.post_list_background);
//				break;
//			case ERROR_LOADING_CONTENTS:
//				Toast.makeText(PostListFlingActivity.this, "网络连接或解析出错", Toast.LENGTH_SHORT)
//				.show();
//				break;
//			default:
//				break;
//			}
//		}
//	};
//
//	/**
//	 * override menu
//	 */
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.post_list_menu, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle item selection
//		switch (item.getItemId()) {
//		case R.id.post_list_menu_exit:
//			finish();
//			return true;
//		case R.id.post_list_menu_log_out:
//			Intent intent = new Intent();
//			intent.setClass(PostListFlingActivity.this, LoginActivity.class);
//			startActivity(intent);
//			finish();
//			return true;
//		case R.id.post_list_menu_new_post:
//			Bundle bundle = new Bundle();
//			int i = boardlink.indexOf("boardid=");
//			int j = boardlink.indexOf("&page");
//			Log.d("aaa", boardlink.substring(i + 8, j));
//			bundle.putString("boardName", boardName);
//			bundle.putString("boardID", boardlink.substring(i + 8, j));
//			Intent intent2 = new Intent();
//			intent2.putExtra("pushNewPost", bundle);
//			intent2.setClass(PostListFlingActivity.this,
//					PushNewPostActivity.class);
//			startActivityForResult(intent2, 0);
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == Activity.RESULT_OK) {
//
//		}
//		if (resultCode == Activity.RESULT_CANCELED) {
//
//		}
//	}
//
//	private class PostListView extends ListView {
//		public PostListView(Context context, int position) {
//			super(context);
//			this.setDivider(context.getResources().getDrawable(
//					R.drawable.divider_horizontal_timeline));
//			this.setDividerHeight(2);
//			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//					LinearLayout.LayoutParams.MATCH_PARENT,
//					LinearLayout.LayoutParams.MATCH_PARENT);
//			layoutParams.weight = (float) 1.0;
//			this.setLayoutParams(layoutParams);
//			// this.setBackgroundResource(R.color.post_list_background);
//			//this.setBackgroundResource(R.drawable.listview_back);
//			this.setCacheColorHint(0);
//
//		}
//	}
//}
