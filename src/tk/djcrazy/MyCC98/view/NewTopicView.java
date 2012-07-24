package tk.djcrazy.MyCC98.view;

import static tk.djcrazy.libCC98.CC98Parser.getNewPostList;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.NewTopicListAdapter;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

public class NewTopicView extends LinearLayout implements ChildView, OnRefreshListener {
	private List<Map<String, Object>> topicList;
	private NewTopicListAdapter newTopicListAdapter;
	private PullToRefreshListView listView;
	private ProgressDialog dialog;
	private ParentView pv;
	private Bitmap userImage;
	private static final int GET_NEW_TOPIC_LIST_SUCCESS = 1;
	private static final int GET_NEW_TOPIC_LIST_FAILED = 0;
	public NewTopicView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.new_topic, this,
				true);
		
		dialog = ProgressDialog.show(getContext(), "", "Loading...",
				true);
		dialog.setCancelable(false);
		findViews();
		
	}

	private void findViews() {
		listView = (PullToRefreshListView) findViewById(R.id.new_topic_list);
		listView.setOnRefreshListener(this);
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
				newTopicListAdapter = new NewTopicListAdapter(
						getContext(), topicList);
				newTopicListAdapter.setUserImage(userImage);
				listView.setAdapter(newTopicListAdapter);
				listView.onRefreshComplete();
				dialog.dismiss();
				break;
			case GET_NEW_TOPIC_LIST_FAILED:
				dialog.dismiss();
				Toast.makeText(getContext(), "网络或解析出错！",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void setParentView(ParentView pv) {
		this.pv = pv;
	}

	@Override
	public void onSwitch() {
		pv.setTitle("查看新帖");
		if (newTopicListAdapter==null) {
			dialog.show();
			getTopic();
		}
	}


	@Override
	public void onRefresh() {
		getTopic();
	}

	public void setUserImage(Bitmap bmUserImg) {
		userImage = bmUserImg;
	}
}
