package tk.djcrazy.MyCC98.fragment;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import com.google.inject.Inject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.PersonalboardListViewAdapter;
import tk.djcrazy.MyCC98.listener.LoadingListener;
import tk.djcrazy.MyCC98.util.ViewUtils;
import tk.djcrazy.MyCC98.view.PullToRefreshListView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import tk.djcrazy.libCC98.CC98ParserImpl;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.BoardEntity;
import tk.djcrazy.libCC98.exception.ParseContentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

public class PersonalBoardFragment extends RoboFragment implements
		OnRefreshListener {
	private static final int GET_LIST_SUCCESS = 1;
	private static final int GET_LIST_FAILED = 0;
	private int position=0;
	private static final String TAG = "PersonalBoardFragment";
	private List<BoardEntity> boardList;
	private PersonalboardListViewAdapter boardListViewAdapter;
	
	@InjectView(R.id.personal_board_list)
 	private PullToRefreshListView listView;
	
	@InjectView(R.id.personal_board_loading_bar)
	private ProgressBar progressBar; 
	
	@Inject
	private ICC98Service service;
	
	private LoadingListener loadingListener;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_LIST_SUCCESS:
				listView.setAdapter(boardListViewAdapter);
				boardListViewAdapter.notifyDataSetChanged();
				listView.onRefreshComplete();
 				loadingListener.onLoadComplete(position);
				ViewUtils.setGone(progressBar, true);
				ViewUtils.setGone(listView, false);
				listView.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_in));
				break;
			case GET_LIST_FAILED:
				ViewUtils.setGone(progressBar, true);
				ViewUtils.setGone(listView, true);
				loadingListener.onLoadFailure(position);
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
				R.layout.personal_board, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView.setOnRefreshListener(this);
		if (boardListViewAdapter != null) {
			ViewUtils.setGone(progressBar, true);
			ViewUtils.setGone(listView, false);
			listView.setAdapter(boardListViewAdapter);
		} else {
			ViewUtils.setGone(progressBar, false);
			ViewUtils.setGone(listView, true);
			onRefresh();
		}
	}

	public void fetchContent() {
		if (loadingListener == null) {
			throw new IllegalStateException("You must set the LoadingListener");
		}
		new Thread() {
			@Override
			public void run() {
				try {
					boardList = service.getPersonalBoardList();
					boardListViewAdapter = new PersonalboardListViewAdapter(
							getActivity(), boardList);
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
				} catch (java.text.ParseException e) {
					handler.sendEmptyMessage(GET_LIST_FAILED);
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void scrollListTo(int x, int y) {
		listView.scrollTo(x, y);
	}

	@Override
	public void onRefresh() {
		fetchContent();
	}

	/**
	 * @param loadingListener
	 *            the loadingListener to set
	 */
	public void setLoadingListener(LoadingListener loadingListener) {
		this.loadingListener = loadingListener;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}
}
