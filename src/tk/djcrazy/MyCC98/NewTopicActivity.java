/**
 * 
 */
package tk.djcrazy.MyCC98;

import static tk.djcrazy.libCC98.CC98Parser.getNewPostList;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.MyCC98.adapter.NewTopicListAdapter;
import tk.djcrazy.MyCC98.view.PullToRefreshListView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

/**
 * @author DJ
 * 
 */
public class NewTopicActivity extends Activity implements OnRefreshListener {
	public static final String ID = "NewTopicActivity";
	private static final int GET_NEW_TOPIC_LIST_SUCCESS = 1;
	private static final int GET_NEW_TOPIC_LIST_FAILED = 0;

	private List<Map<String, Object>> topicList;
	private NewTopicListAdapter newTopicListAdapter;
	private PullToRefreshListView listView;
	private ProgressDialog dialog;

	private void getTopic() {
		new Thread() {
			@Override
			public void run() {
				try {
					topicList = getNewPostList();
					getTopicHandler
							.sendEmptyMessage(GET_NEW_TOPIC_LIST_SUCCESS);
				} catch (ClientProtocolException e) {
					getTopicHandler.sendEmptyMessage(GET_NEW_TOPIC_LIST_FAILED);
					e.printStackTrace();
				} catch (ParseException e) {
					getTopicHandler.sendEmptyMessage(GET_NEW_TOPIC_LIST_FAILED);
					e.printStackTrace();
				} catch (IOException e) {
					getTopicHandler.sendEmptyMessage(GET_NEW_TOPIC_LIST_FAILED);
					e.printStackTrace();
				}
			}
		}.start();
	}

	Handler getTopicHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_NEW_TOPIC_LIST_SUCCESS:
				topicList.remove(0);
				newTopicListAdapter = new NewTopicListAdapter(
						NewTopicActivity.this, topicList);
				listView.setAdapter(newTopicListAdapter);
				listView.onRefreshComplete();
				dialog.dismiss();
				break;
			case GET_NEW_TOPIC_LIST_FAILED:
				dialog.dismiss();
				Toast.makeText(NewTopicActivity.this, "网络或解析出错！",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "5EXV7SIGMTTDKYNXTKR4");

	}

	@Override
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
	};

	/**
	 * Called when the activity is first created.
	 **/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Settings with no title
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Settings with full screen
		/*
		 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
		setContentView(R.layout.hot_topic);
		setTitle("查看新贴");
		findViews();
		setListeners();
		dialog = ProgressDialog.show(NewTopicActivity.this, "", "Loading...",
				true);
		dialog.setCancelable(true);
		dialog.show();
		getTopic();
	}

	private void findViews() {
		listView = (PullToRefreshListView) findViewById(R.id.hot_topic_list);
		listView.setOnRefreshListener(this);
	}

	private void setListeners() {

	}

	@Override
	public void onRefresh() {
		getTopic();
	}
}
