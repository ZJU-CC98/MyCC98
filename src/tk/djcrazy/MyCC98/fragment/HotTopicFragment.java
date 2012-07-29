package tk.djcrazy.MyCC98.fragment;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.HotTopicListAdapter;
import tk.djcrazy.MyCC98.util.ViewUtils;
import tk.djcrazy.MyCC98.view.PullToRefreshListView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import tk.djcrazy.libCC98.CC98Parser;
import tk.djcrazy.libCC98.data.HotTopicEntity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

public class HotTopicFragment extends RoboFragment implements OnRefreshListener{
	private static final String TAG = "HotTopicFragment";

	private static final int GET_HOT_TOPIC_LIST_SUCCESS = 1;
	private static final int GET_HOT_TOPIC_LIST_FAILED = 0;
 	private List<HotTopicEntity> topicList;
	private HotTopicListAdapter hotTopicListAdapter ;
	
	@InjectView(R.id.hot_topic_list)
	private PullToRefreshListView listView;
	
	@InjectView(R.id.hot_topic_loading_bar)
	private ProgressBar progressBar; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
   		return LayoutInflater.from(getActivity()).inflate(
				R.layout.hot_topic, null);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView.setOnRefreshListener(this);
		if (hotTopicListAdapter != null) {
			ViewUtils.setGone(progressBar, true);
			ViewUtils.setGone(listView, false);
			listView.setAdapter(hotTopicListAdapter);
		} else {
			ViewUtils.setGone(progressBar, false);
			ViewUtils.setGone(listView, true);
			onRefresh();
		}
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
				ViewUtils.setGone(progressBar, true);
				ViewUtils.setGone(listView, false);
				listView.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_in));
 				break;
			case GET_HOT_TOPIC_LIST_FAILED:
				ViewUtils.setGone(progressBar, true);
				ViewUtils.setGone(listView, true);
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