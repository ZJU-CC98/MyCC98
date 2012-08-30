package tk.djcrazy.MyCC98.fragment;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.FriendListViewAdapter;
import tk.djcrazy.MyCC98.util.ViewUtils;
import tk.djcrazy.MyCC98.view.PullToRefreshListView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import tk.djcrazy.libCC98.CC98ParserImpl;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.UserStatueEntity;
import tk.djcrazy.libCC98.exception.ParseContentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FriendListFragment extends RoboSherlockFragment implements
		OnRefreshListener {
	private static final String TAG = "FriendListFragment";
	private List<UserStatueEntity> friendList;
	private FriendListViewAdapter friendListViewAdapter;
	@InjectView(R.id.friend_list_new)
	private PullToRefreshListView listView;
	@InjectView(R.id.friend_list_loading_bar)
	private ProgressBar progressBar;
	
	
	@Inject
	private ICC98Service service;
	
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
				ViewUtils.setGone(progressBar, true);
				ViewUtils.setGone(listView, false);
				listView.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_in));
				break;
			case GET_LIST_FAILED:
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return LayoutInflater.from(getActivity()).inflate(
				R.layout.friend_list_new, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView.setOnRefreshListener(this);
		if (friendListViewAdapter != null) {
			ViewUtils.setGone(progressBar, true);
			ViewUtils.setGone(listView, false);
			listView.setAdapter(friendListViewAdapter);
		} else {
			ViewUtils.setGone(progressBar, false);
			ViewUtils.setGone(listView, true);
			onRefresh();
		}
	}

	public void scrollListTo(int x, int y) {
		listView.scrollTo(x, y);
	}

	public void fetchContent() {
		new Thread() {
			// child thread
			@Override
			public void run() {
				try {
					friendList = service.getFriendList();
					friendListViewAdapter = new FriendListViewAdapter(
							getActivity(), friendList);
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
				} catch (ParseContentException e) {
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