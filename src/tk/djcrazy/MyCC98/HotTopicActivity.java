/**
 * 
 */
package tk.djcrazy.MyCC98;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.MyCC98.adapter.HotTopicListAdapter;
import tk.djcrazy.MyCC98.view.PullToRefreshListView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import tk.djcrazy.libCC98.CC98Parser;
import tk.djcrazy.libCC98.data.HotTopicEntity;
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
public class HotTopicActivity extends Activity implements OnRefreshListener{
	private static final int GET_HOT_TOPIC_LIST_SUCCESS = 1;
	private static final int GET_HOT_TOPIC_LIST_FAILED = 0;
	public static final String ID = "HotTopicActivity";

	List<HotTopicEntity> topicList;
	HotTopicListAdapter hotTopicListAdapter ;
	PullToRefreshListView listView;
	
	ProgressDialog dialog;
	
	private void getTopic(){
		new Thread() {
			// child thread
			@Override
			public void run() {
				try {
					topicList = CC98Parser.getHotTopicList();
					getTopicHandler.sendEmptyMessage(GET_HOT_TOPIC_LIST_SUCCESS);

				} catch (ClientProtocolException e) {
					getTopicHandler.sendEmptyMessage(GET_HOT_TOPIC_LIST_FAILED);
					e.printStackTrace();
				} catch (ParseException e) {
					getTopicHandler.sendEmptyMessage(GET_HOT_TOPIC_LIST_FAILED);
					e.printStackTrace();
				} catch (IOException e) {
					getTopicHandler.sendEmptyMessage(GET_HOT_TOPIC_LIST_FAILED);
					e.printStackTrace();
				}
			}
		}.start();
	}
	 
	Handler getTopicHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_HOT_TOPIC_LIST_SUCCESS:
				hotTopicListAdapter = new HotTopicListAdapter(HotTopicActivity.this, topicList);
 				listView.setAdapter(hotTopicListAdapter); 
 				listView.onRefreshComplete();
 				dialog.dismiss();
				break;
			case GET_HOT_TOPIC_LIST_FAILED:
				dialog.dismiss();
				Toast.makeText(HotTopicActivity.this, "网络或解析出错！", Toast.LENGTH_SHORT)
				.show();
				break;
  			default:
				break;
			}
		}

	};
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	public void onStart()
	{
	   super.onStart();
	   FlurryAgent.onStartSession(this, "5EXV7SIGMTTDKYNXTKR4");
	}
	
	@Override
	public void onStop()
	{
	   super.onStop();
	   FlurryAgent.onEndSession(this);
	}

	/**
     * Called when the activity is first created.
     **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_topic);
        setTitle("十大热门");
        findViews();
        setListeners();
        dialog = ProgressDialog.show(HotTopicActivity.this, "", "Loading...", true);
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
