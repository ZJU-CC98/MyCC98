///**
// * view the content of the post
// */
//
//package tk.djcrazy.MyCC98;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.message.BasicNameValuePair;
//
//import tk.djcrazy.MyCC98.adapter.contentListViewAdapter;
//import tk.djcrazy.MyCC98.dialog.AuthDialog.MyAuthDialogListener;
//import tk.djcrazy.libCC98.CC98Client;
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.ListView;
//import android.widget.Spinner;
//import android.widget.TextView;
//
///**
// * @deprecated It is not used any more!
// * @author DJ
// * 
// */
//public class PostContentListActivity extends Activity implements
//		OnClickListener {
//
//	Bundle bundle = new Bundle();
//	Intent intent = new Intent();
//
//	// 翻页用
//	Bundle pageBundle = new Bundle();
//	Intent pageIntent = new Intent();
//
//	static String postlink = "";
//	static String postName = "";
//	String subject;
//	String Expression;
//	String Content;
//	String boardID;
//	String rootID;
//	MyAuthDialogListener listener;
//	int contentPageNumber = 1;
//	List<NameValuePair> PostList;
//	ArrayList<String> postcontentlist = new ArrayList<String>();
//	contentListViewAdapter listViewAdapter;
//	ListView listView;
//	TextView posTextView;
//	Spinner faceSpinner;
//
//	List<Map<String, Object>> listItem;
//	Map<String, Object> map = new HashMap<String, Object>();
//	contentListViewAdapter listadapter;
//
//	@Override
//	public void onCreate(Bundle SavedInstanceState) {
//		super.onCreate(SavedInstanceState);
//		// 设置无标题栏
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		// 设置为全屏模式
//		/*
//		 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		 */
//		// 页面布局
//		setContentView(R.layout.post_content);
//
//		// 获取传递内容
//		bundle = getIntent().getBundleExtra("postContent");
//		postlink = bundle.getString("postlink").toString();
//		postName = bundle.getString("postName").toString();
//		contentPageNumber = bundle.getInt("contentPageNumber");
//
//		// 翻页
//		pageIntent.setClass(this, PostContentListActivity.class);
//
//		// listview的适配器
//		/*
//		 * adapter = new ArrayAdapter<String>(this,
//		 * android.R.layout.simple_list_item_1,postcontentlist);
//		 */
//
//		posTextView = (TextView) findViewById(R.id.PostName);
//
//		// 新的自定义的listview适配器
//		listView = (ListView) findViewById(R.id.postlistView);
//
//		setTitle(postName);
//		// 监听按钮
//		View nextPage = findViewById(R.id.content_next_page);
//		nextPage.setOnClickListener(this);
//		View prePage = findViewById(R.id.content_pre_page);
//		prePage.setOnClickListener(this);
//		View reply = findViewById(R.id.reply);
//		reply.setOnClickListener(this);
//		// 开启多线程获取网页
//		Thread getPostListThread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//
//				for (Iterator<NameValuePair> iterator = PostList.iterator(); iterator
//						.hasNext();) {
//					NameValuePair nPair = (NameValuePair) iterator.next();
//					postcontentlist.add(nPair.getValue());
//				}
//				handler.sendEmptyMessage(0);
//			}
//		});
//		getPostListThread.start();
//
//	}
//
//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 0:
//				Log.d("set", "set");
//				listItem = getListItem();
//				listViewAdapter = new contentListViewAdapter(
//						PostContentListActivity.this, listItem);
//				listView.setAdapter(listViewAdapter);
//
//				break;
//			case 1:
//				setTitle(R.string.reply_succseed);
//			default:
//				break;
//			}
//		}
//	};
//
//	@Override
//	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.content_pre_page:
//			if (contentPageNumber > 1)
//				--contentPageNumber;
//			else {
//				break;
//			}
//
//			turnToNextPage();
//			break;
//
//		case R.id.content_next_page:
//			contentPageNumber = 1 + contentPageNumber;
//			turnToNextPage();
//			break;
//
//		case R.id.reply:
//			// 自定义的回帖对话框
//			/**
//			 * ReplyDialogView myReplyDialog = new ReplyDialogView(this,
//			 * listener); myReplyDialog.show();
//			 */
//			Intent intent = new Intent();
//			intent.setClass(this, EditActivity.class);
//			startActivity(intent);
//		default:
//			break;
//		}
//	}
//
//	/**
//	 * Get a
//	 * 
//	 * @return
//	 */
//	private List<Map<String, Object>> getListItem() {
//		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
//
//		for (Iterator<NameValuePair> iterator = PostList.iterator(); iterator
//				.hasNext();) {
//			NameValuePair nPair = iterator.next();
//			Map<String, Object> map = new HashMap<String, Object>();
//			Log.d("sss", nPair.getName() + nPair.getValue());
//			map.put("replyID", nPair.getName());
//			map.put("reply_content", nPair.getValue());
//			map.put("reply_time", "");
//			map.put("image", null);
//			maps.add(map);
//		}
//		return maps;
//	}
//
//	void turnToNextPage() {
//		postlink = postlink.replaceAll("&star=\\d*", "&star="
//				+ contentPageNumber);
//		Log.d("fanye", postlink);
//		Log.d("fanye", "" + contentPageNumber);
//		pageBundle.putString("postlink", postlink);
//		pageBundle.putString("postName", postName);
//		pageBundle.putInt("contentPageNumber", contentPageNumber);
//		pageIntent.putExtra("postContent", pageBundle);
//		startActivity(pageIntent);
//	}
//
//	private boolean pushReply() {
//		Log.d("dd", postlink);
//		int indexbegin = postlink.indexOf("&ID");
//		int indexend = postlink.indexOf("&page");
//		rootID = postlink.substring(indexbegin + 4, indexend);
//		Log.d("rootID", rootID);
//		indexbegin = postlink.indexOf("boardID=");
//		indexend = postlink.indexOf("&ID");
//		boardID = postlink.substring(indexbegin + 8, indexend);
//
//		Log.d("boardID", boardID + " " + indexbegin + " " + indexend);
//		final List<NameValuePair> nvpsList = new ArrayList<NameValuePair>();
//		nvpsList.add(new BasicNameValuePair("upfilername", ""));
//		nvpsList.add(new BasicNameValuePair("followup", rootID));
//		nvpsList.add(new BasicNameValuePair("rootID", rootID));
//		nvpsList.add(new BasicNameValuePair("star", ""));
//		nvpsList.add(new BasicNameValuePair("TotalUseTable", "bbs5"));
//		nvpsList.add(new BasicNameValuePair("subject", subject));
//		nvpsList.add(new BasicNameValuePair("Expression", Expression));
//		nvpsList.add(new BasicNameValuePair("Content", Content));
//		nvpsList.add(new BasicNameValuePair("signflag", "yes"));
//
//		Thread thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				Log.d("thread2", "thread2");
//				Message msg = new Message();
//				msg.what = 1;
//				try {
//					CC98Client.submitReply(nvpsList, boardID, rootID);
//				} catch (ClientProtocolException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//
//		thread.start();
//		return true;
//	}
//}