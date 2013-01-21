package tk.djcrazy.MyCC98.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.PostListActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.SearchBoardListAdapter;
import tk.djcrazy.MyCC98.listener.LoadingListener;
import tk.djcrazy.MyCC98.util.ViewUtils;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.BoardStatus;
import tk.djcrazy.libCC98.exception.ParseContentException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;

public class SearchBoardFragment extends RoboSherlockFragment implements
		OnItemClickListener {
	private int position = 0;
	private static final String TAG = "SearchBoardFragment";
	private List<BoardStatus> currentResult;
	private List<BoardStatus> boardList;

	@InjectView(R.id.search_board_bar) 
	private EditText searchContent;
	@InjectView(R.id.search_board_result_list)
	private ListView lvResultList;
	@InjectView(R.id.search_board_loading_bar)
	private ProgressBar progressBar;
	@InjectView(R.id.search_board_search_area)
	private View mSearchArea;

	@Inject
	private ICC98Service service;

	private SearchBoardListAdapter listAdapter;
	private static final int FETCH_SUCC = 0;
	private static final int FETCH_FAIL = 1;

 
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
 			setListeners();
			switch (msg.what) {
			case FETCH_SUCC:
				searchContent.setText("");
				ViewUtils.setGone(progressBar, true);
				ViewUtils.setGone(mSearchArea, false);
				ViewUtils.setGone(lvResultList, false);
				lvResultList.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
 				break;
			case FETCH_FAIL:
				ViewUtils.setGone(progressBar, true);
				ViewUtils.setGone(lvResultList, true);
 				break;
			default:
				break;
			}
		}
	};

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			doSearch(searchContent.getText().toString().trim());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
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
			ViewUtils.setGone(mSearchArea, false);
			lvResultList.setAdapter(listAdapter);
			listAdapter.notifyDataSetChanged();
			lvResultList.invalidate();
			setListeners();

		} else {
			ViewUtils.setGone(progressBar, false);
			ViewUtils.setGone(lvResultList, true);
			ViewUtils.setGone(mSearchArea, true);
			fetchBoardlist();
		}
	}

	private void fetchBoardlist() {
		new Thread() {
			@Override
			public void run() {
				try {
					boardList = service.getTodayBoardList();
					currentResult = boardList;
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
				} catch (ParseContentException e) {
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
		searchContent.addTextChangedListener(textWatcher);
		lvResultList.setOnItemClickListener(this);
	}

	private void doSearch(String string) {
		if (string.equals("")) {
			if (boardList == null) {
				currentResult = new ArrayList<BoardStatus>();
			} else if (boardList.size() <= 30) {
				currentResult = boardList;
			} else {
				currentResult = boardList.subList(0, 30);
			}
		} else {
			List<BoardStatus> tmplist = new ArrayList<BoardStatus>();
			for (BoardStatus np : boardList) {
				if (np.getBoardName().toLowerCase()
						.contains(string.toLowerCase())) {
					tmplist.add(np);
				}
			}
			currentResult = tmplist;
		}
 		if (listAdapter == null) {
			listAdapter = new SearchBoardListAdapter(getActivity(),
					currentResult);
		} else {
			listAdapter.setBoardList(currentResult);
		}
		lvResultList.setAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();
		lvResultList.invalidate();
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
  		getActivity().startActivity(PostListActivity.createIntent(currentResult.get(arg2)
				.getBoardName(), currentResult.get(arg2)
				.getBoardId()));
 	}

}
