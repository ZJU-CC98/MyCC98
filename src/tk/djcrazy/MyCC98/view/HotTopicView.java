package tk.djcrazy.MyCC98.view;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.HotTopicListAdapter;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import tk.djcrazy.libCC98.CC98Parser;
import tk.djcrazy.libCC98.data.HotTopicEntity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HotTopicView extends LinearLayout implements ChildView, OnRefreshListener{
	private static final int GET_HOT_TOPIC_LIST_SUCCESS = 1;
	private static final int GET_HOT_TOPIC_LIST_FAILED = 0;
	private List<HotTopicEntity> topicList;
	private HotTopicListAdapter hotTopicListAdapter ;
	private PullToRefreshListView listView;
	private ParentView pv;
	private Bitmap userImage;
	private ProgressDialog dialog;
	public HotTopicView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.hot_topic, this,
				true);
        dialog = ProgressDialog.show(getContext(), "", "Loading...", true);
        dialog.setCancelable(false);
        
		listView = (PullToRefreshListView) findViewById(R.id.hot_topic_list);
		listView.setOnRefreshListener(this);
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
				hotTopicListAdapter = new HotTopicListAdapter(getContext(), topicList);
				hotTopicListAdapter.setUserImage(userImage);
 				listView.setAdapter(hotTopicListAdapter); 
 				listView.onRefreshComplete();
 				dialog.dismiss();
				break;
			case GET_HOT_TOPIC_LIST_FAILED:
				dialog.dismiss();
				Toast.makeText(getContext(), "网络或解析出错！", Toast.LENGTH_SHORT)
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


	@Override
	public void setParentView(ParentView pv) {
		this.pv = pv;
	}

	@Override
	public void onSwitch() {
		pv.setTitle("十大热门话题");
		if (hotTopicListAdapter==null) {
			dialog.show();
			getTopic();
		}
	}

	public void setUserImage(Bitmap bmUserImg) {
		userImage = bmUserImg;
	}

}
