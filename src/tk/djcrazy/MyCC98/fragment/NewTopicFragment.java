package tk.djcrazy.MyCC98.fragment;

import static tk.djcrazy.libCC98.CC98Parser.getNewPostList;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.NewTopicListAdapter;
import tk.djcrazy.MyCC98.util.ViewUtils;
import tk.djcrazy.MyCC98.view.PullToRefreshListView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NewTopicFragment extends RoboFragment implements OnRefreshListener{
	private static final String TAG = "NewTopicFragment";

  	private static final int GET_NEW_TOPIC_LIST_SUCCESS = 1;
	private static final int GET_NEW_TOPIC_LIST_FAILED = 0;	
	
	private List<Map<String, Object>> topicList;
	private NewTopicListAdapter newTopicListAdapter;
	
	@InjectView(R.id.new_topic_list)
	private PullToRefreshListView listView;
	
	@InjectView(R.id.new_topic_loading_bar)
	private ProgressBar progressBar; 


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
 		return LayoutInflater.from(getActivity()).inflate(
				R.layout.new_topic, null);	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView.setOnRefreshListener(this);
		if (newTopicListAdapter != null) {
			ViewUtils.setGone(progressBar, true);
			ViewUtils.setGone(listView, false);
			listView.setAdapter(newTopicListAdapter);
		} else {
			ViewUtils.setGone(progressBar, false);
			ViewUtils.setGone(listView, true);
			onRefresh();
		}
	}

	public void scrollListTo(int x, int y) {
		listView.scrollTo(x, y);
	}

	private void getTopic() {

		new Thread() {
			@Override
			public void run() {
				try {
					Log.d(TAG, "get topic");
					topicList = getNewPostList();
					topicList.remove(0);
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
				newTopicListAdapter = new NewTopicListAdapter(getActivity(),
						topicList);
 				listView.setAdapter(newTopicListAdapter);
				listView.onRefreshComplete();
				ViewUtils.setGone(progressBar, true);
				ViewUtils.setGone(listView, false);
				listView.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_in));
 				break;
			case GET_NEW_TOPIC_LIST_FAILED:
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
