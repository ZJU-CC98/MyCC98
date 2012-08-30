package tk.djcrazy.MyCC98.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.NewTopicListAdapter;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.MyCC98.util.ViewUtils;
import tk.djcrazy.MyCC98.view.PullToRefreshListView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.SearchResultEntity;
import tk.djcrazy.libCC98.exception.ParseContentException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;

public class NewTopicFragment extends RoboSherlockFragment implements
		OnRefreshListener, OnItemClickListener {
	private static final String TAG = "NewTopicFragment";

	private static final int GET_NEW_TOPIC_LIST_SUCCESS = 1;
	private static final int GET_NEW_TOPIC_LIST_FAILED = 0;

	private List<SearchResultEntity> topicList = new ArrayList<SearchResultEntity>();
	private NewTopicListAdapter newTopicListAdapter;

	@InjectView(R.id.new_topic_list)
	private PullToRefreshListView listView;

	@InjectView(R.id.new_topic_loading_bar)
	private ProgressBar progressBar;

	private View footerView;
	private ProgressBar loadMoreProgressBar;
	private TextView loadMoreTextView;

	private int currentPage = 1;

	@Inject
	ICC98Service service;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		footerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.load_more, null);
		return LayoutInflater.from(getActivity()).inflate(R.layout.new_topic,
				null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView.setOnRefreshListener(this);
		listView.addFooterView(footerView);
		loadMoreProgressBar = (ProgressBar) footerView
				.findViewById(R.id.load_more_progress);
		loadMoreTextView = (TextView) footerView
				.findViewById(R.id.load_more_text);
		loadMoreTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentPage < 5) {
					currentPage++;
					new LoadNewTopicTask(getActivity(), currentPage).execute();
				} else {
					loadMoreTextView.setText("没有更多了");
				}
			}
		});
		if (newTopicListAdapter != null) {
			ViewUtils.setGone(progressBar, true);
			ViewUtils.setGone(listView, false);
			listView.setAdapter(newTopicListAdapter);
		} else {
			ViewUtils.setGone(progressBar, false);
			ViewUtils.setGone(listView, true);
			onRefresh();
		}
		listView.setOnItemClickListener(this);
	}

	public void scrollListTo(int x, int y) {
		listView.scrollTo(x, y);
	}

	private void getTopic() {

		new Thread() {
			@Override
			public void run() {
				try {
					topicList.addAll(service.getNewPostList(1));
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
				} catch (ParseContentException e) {
					getTopicHandler.sendEmptyMessage(GET_NEW_TOPIC_LIST_FAILED);
					e.printStackTrace();
				} catch (java.text.ParseException e) {
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
				listView.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
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
		topicList.clear();
		currentPage =1;
		getTopic();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SearchResultEntity entity = topicList.get(position - 1);
		Intent intent = new Intent(getActivity(), PostContentsJSActivity.class);
		intent.putExtra(PostContentsJSActivity.BOARD_ID, entity.getBoardId());
		intent.putExtra(PostContentsJSActivity.POST_ID, entity.getPostId());
		intent.putExtra(PostContentsJSActivity.POST_NAME, entity.getTitle());
		intent.putExtra(PostContentsJSActivity.PAGE_NUMBER, 1);
		getActivity().startActivity(intent);
		getActivity().overridePendingTransition(
				R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);

	}

	private class LoadNewTopicTask extends
			RoboAsyncTask<List<SearchResultEntity>> {

		private Activity mContext;
		private int mCurrentPageNumber;
		@Inject
		private ICC98Service mService;

		protected LoadNewTopicTask(Activity context, int currentPageNum) {
			super(context);
			mContext = context;
			mCurrentPageNumber = currentPageNum;
		}

		@Override
		protected void onPreExecute() {
			ViewUtils.setGone(loadMoreProgressBar, false);
			loadMoreTextView.setText("正在加载...");
		}

		@Override
		public List<SearchResultEntity> call() throws Exception {
			return mService.getNewPostList(mCurrentPageNumber);
		}

		@Override
		protected void onSuccess(List<SearchResultEntity> list) {
			topicList.addAll(list);
 		}

		@Override
		protected void onException(Exception e) {
			ToastUtils.show(mContext, "加载失败，请检查网络连接");
		}

		@Override
		protected void onFinally() {
			ViewUtils.setGone(loadMoreProgressBar, true);
			loadMoreTextView.setText("点击加载更多");
			listView.onRefreshComplete();
			newTopicListAdapter.notifyDataSetChanged();
		}

	}
}
