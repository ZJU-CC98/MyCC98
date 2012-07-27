package tk.djcrazy.MyCC98.fragment;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.MyCC98.R;
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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class HotTopicFragment extends Fragment implements OnRefreshListener{
	private static final String TAG = "HotTopicFragment";

	private static final int GET_HOT_TOPIC_LIST_SUCCESS = 1;
	private static final int GET_HOT_TOPIC_LIST_FAILED = 0;
 	private List<HotTopicEntity> topicList;
	private HotTopicListAdapter hotTopicListAdapter ;
	private PullToRefreshListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRefresh();
     }
 	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.hot_topic, null);
         
		listView = (PullToRefreshListView) view.findViewById(R.id.hot_topic_list);
		listView.setOnRefreshListener(this);
 		if (hotTopicListAdapter!=null) {
			listView.setAdapter(hotTopicListAdapter);
		}
		return view;
	}

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
	public void scrollListTo(int x, int y) {
		listView.scrollTo(x, y);
	}

	Handler getTopicHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_HOT_TOPIC_LIST_SUCCESS:
				hotTopicListAdapter = new HotTopicListAdapter(getActivity(), topicList);
  				listView.setAdapter(hotTopicListAdapter); 
 				listView.onRefreshComplete();
 				break;
			case GET_HOT_TOPIC_LIST_FAILED:
 				Toast.makeText(getActivity(), "网络或解析出错！", Toast.LENGTH_SHORT)
				.show();
				break;
  			default:
				break;
			}
		}
	};
	
	@Override
	public void onRefresh() {
		getTopic();
	}
}
