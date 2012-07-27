package tk.djcrazy.MyCC98.fragment;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.FriendListViewAdapter;
import tk.djcrazy.MyCC98.view.PullToRefreshListView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import tk.djcrazy.libCC98.CC98Parser;
import tk.djcrazy.libCC98.data.UserStatueEntity;
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

public class FriendListFragment extends Fragment implements OnRefreshListener{
	private static final String TAG = "FriendListFragment";
	private List<UserStatueEntity> friendList;
	private FriendListViewAdapter friendListViewAdapter;
	private PullToRefreshListView listView;
  	private static final int GET_LIST_SUCCESS = 1;
	private static final int GET_LIST_FAILED = 0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_LIST_SUCCESS:
				listView.setAdapter(friendListViewAdapter);
				friendListViewAdapter.notifyDataSetChanged();
 				listView.onRefreshComplete();
				listView.invalidate();
 				break;
			case GET_LIST_FAILED:
 				Toast.makeText(getActivity(), "网络或解析出错！", Toast.LENGTH_SHORT).show();
				break;
 			default:
				break;
			}
		}
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRefresh();
     }
 	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("FriendListFragment", "onCreateView");
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.friend_list_new, null);
		listView = (PullToRefreshListView) view.findViewById(R.id.friend_list_new);
		listView.setOnRefreshListener(this);
		if (friendListViewAdapter!=null) {
			listView.setAdapter(friendListViewAdapter);
		}
 		return view;
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
					friendListViewAdapter = new FriendListViewAdapter(getActivity(), friendList);
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
	public void onRefresh() {
		fetchContent();
	}
 
}