package tk.djcrazy.MyCC98.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import com.google.inject.Inject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.PostListActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.SearchResultListAdapter;
import tk.djcrazy.MyCC98.listener.LoadingListener;
import tk.djcrazy.MyCC98.util.ViewUtils;
import tk.djcrazy.libCC98.CC98ClientImpl;
import tk.djcrazy.libCC98.CC98ParserImpl;
import tk.djcrazy.libCC98.ICC98Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

public class SearchBoardFragment extends RoboFragment {
	private int position = 0;
	private static final String TAG = "SearchBoardFragment";
	private List<NameValuePair> currentResult;
	private List<NameValuePair> boardList;

	@InjectView(R.id.search_board_bar)
	private EditText searchContent;
	@InjectView(R.id.search_board_result_list)
	private ListView lvResultList;
	@InjectView(R.id.search_board_loading_bar)
	private ProgressBar progressBar;

	@Inject
	private ICC98Service service;
	
	private SearchResultListAdapter listAdapter;
	private static final int FETCH_SUCC = 0;
	private static final int FETCH_FAIL = 1;

	private int lastquerylen;
	private LoadingListener loadingListener;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (loadingListener == null) {
				throw new IllegalStateException(
						"You must set the LoadingListener first.");
			}
			setListeners();
			switch (msg.what) {
			case FETCH_SUCC:
				searchContent.setText("");
				ViewUtils.setGone(progressBar, true);
				ViewUtils.setGone(lvResultList, false);
				lvResultList.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
				loadingListener.onLoadComplete(position);
				break;
			case FETCH_FAIL:
				ViewUtils.setGone(progressBar, true);
				ViewUtils.setGone(lvResultList, true);
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
				R.layout.search_board, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (boardList != null) {
 			ViewUtils.setGone(progressBar, true);
			ViewUtils.setGone(lvResultList, false);
			lvResultList.setAdapter(listAdapter);
			listAdapter.notifyDataSetChanged();
			lvResultList.invalidate();
		} else {
			ViewUtils.setGone(progressBar, true);
			ViewUtils.setGone(lvResultList, false);
			fetchBoardlist();
			
		}
	}

	private void fetchBoardlist() {
		new Thread() {
			@Override
			public void run() {
				try {
					boardList = service.getTodayBoardList();
					handler.sendEmptyMessage(FETCH_SUCC);
				} catch (ClientProtocolException e) {
					handler.sendEmptyMessage(FETCH_FAIL);
					e.printStackTrace();
				} catch (ParseException e) {
					handler.sendEmptyMessage(FETCH_FAIL);
					e.printStackTrace();
				} catch (IOException e) {
					handler.sendEmptyMessage(FETCH_FAIL);
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void scrollListTo(int x, int y) {
		lvResultList.scrollTo(x, y);
	}

	private void setListeners() {
		searchContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				doSearch(searchContent.getText().toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		lvResultList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bundle bundle = new Bundle();
				bundle.putString(PostListActivity.BOARD_LINK,
						service.getDomain()
								+ currentResult.get(arg2).getValue());
				bundle.putString(PostListActivity.BOARD_NAME, currentResult
						.get(arg2).getName());
				bundle.putInt(PostListActivity.PAGE_NUMBER, 1);

				Intent intent = new Intent().putExtra(
						PostListActivity.BOARD_ENTITY, bundle).setClass(
						getActivity(), PostListActivity.class);
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(
						R.anim.forward_activity_move_in,
						R.anim.forward_activity_move_out);
			}
		});
	}

	protected void doSearch(String string) {
		if (string.equals("")) {
			if (boardList==null) {
				currentResult = new  ArrayList<NameValuePair>();
			}
			currentResult = boardList.subList(0, 30);
		} else if (string.length() < lastquerylen) {
			List<NameValuePair> tmplist = new ArrayList<NameValuePair>();
			for (NameValuePair np : boardList) {
				if (np.getName().toLowerCase().contains(string.toLowerCase())) {
					tmplist.add(np);
				}
			}
			currentResult = tmplist;
		} else {
			List<NameValuePair> tmplist = new ArrayList<NameValuePair>();
			for (NameValuePair np : currentResult) {
				if (np.getName().toLowerCase().contains(string.toLowerCase())) {
					tmplist.add(np);
				}
			}
			currentResult = tmplist;
		}
		lastquerylen = string.length();
		listAdapter = new SearchResultListAdapter(getActivity(), currentResult);
		lvResultList.setAdapter(listAdapter);
		lvResultList.invalidate();
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
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

}
