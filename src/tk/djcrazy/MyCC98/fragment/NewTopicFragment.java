package tk.djcrazy.MyCC98.fragment;

import static tk.djcrazy.libCC98.CC98Parser.getNewPostList;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.NewTopicListAdapter;
import tk.djcrazy.MyCC98.view.PullToRefreshListView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class NewTopicFragment extends Fragment implements OnRefreshListener{
	private List<Map<String, Object>> topicList;
	private NewTopicListAdapter newTopicListAdapter;
	private PullToRefreshListView listView;
  	private static final int GET_NEW_TOPIC_LIST_SUCCESS = 1;
	private static final int GET_NEW_TOPIC_LIST_FAILED = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRefresh();
     }
 	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.new_topic, null);
		listView = (PullToRefreshListView) view.findViewById(R.id.new_topic_list);
		if (newTopicListAdapter!=null) {
			listView.setAdapter(newTopicListAdapter);
		}
		return view;
	}

	public void scrollListTo(int x, int y) {
		listView.scrollTo(x, y);
	}

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
				newTopicListAdapter = new NewTopicListAdapter(getActivity(),
						topicList);
 				listView.setAdapter(newTopicListAdapter);
				listView.onRefreshComplete();
 				break;
			case GET_NEW_TOPIC_LIST_FAILED:
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
