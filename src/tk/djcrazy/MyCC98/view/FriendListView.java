package tk.djcrazy.MyCC98.view;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.FriendListViewAdapter;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import tk.djcrazy.libCC98.CC98Parser;
import tk.djcrazy.libCC98.data.UserStatueEntity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 
 * @author DJ
 * @author zsy
 * 
 */

public class FriendListView extends LinearLayout implements ChildView, OnRefreshListener{

	private List<UserStatueEntity> friendList;
	private FriendListViewAdapter friendListViewAdapter;
	private PullToRefreshListView listView;
	private ProgressDialog dialog;
	private ParentView pv;
	private Bitmap userImage;
	private static final int GET_LIST_SUCCESS = 1;
	private static final int GET_LIST_FAILED = 0;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_LIST_SUCCESS:
				listView.setAdapter(friendListViewAdapter);
				friendListViewAdapter.notifyDataSetChanged();
				pv.refresh();
				listView.onRefreshComplete();
				listView.invalidate();
				// IMPORTANT! put the dismiss at last to force the refresh
				dialog.dismiss();
				break;
			case GET_LIST_FAILED:
				dialog.dismiss();
				Toast.makeText(getContext(), "网络或解析出错！", Toast.LENGTH_SHORT);
				break;
 			default:
				break;
			}
		}
	};

	public FriendListView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.friend_list_new, this,
				true);
		listView = (PullToRefreshListView) findViewById(R.id.friend_list_new);
		listView.setOnRefreshListener(this);
		dialog = ProgressDialog.show(getContext(), "", "Loading...", true);
		dialog.setCancelable(false);

	}
	public void scrollListTo(int x, int y) {
		listView.scrollTo(x, y);
	}

	public void fetchContent(){
		new Thread() {
			// child thread 
			@Override
			public void run() {
				try {
					friendList = CC98Parser.getUserFriendList();
					friendListViewAdapter = new FriendListViewAdapter((Activity) getContext(), friendList);
					friendListViewAdapter.setUserImage(userImage);
					handler.sendEmptyMessage(GET_LIST_SUCCESS);
				} catch (ClientProtocolException e) {
					handler.sendEmptyMessage(GET_LIST_FAILED);
					e.printStackTrace();
				} catch (ParseException e) {
					handler.sendEmptyMessage(GET_LIST_FAILED);
					e.printStackTrace();
				} catch (IOException e) {
					handler.sendEmptyMessage(GET_LIST_FAILED);
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void setParentView(ParentView pv) {
		this.pv = pv;
	}

	public void setUserImage(Bitmap bitmap) {
		userImage = bitmap;
	}
	@Override
	public void onSwitch() {
		if(friendList==null){
			dialog.show();
			fetchContent();
		}
		pv.setTitle("好友列表"); 
	}

	@Override
	public void onRefresh() {
		fetchContent();
	}
}
