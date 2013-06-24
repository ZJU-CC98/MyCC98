package tk.djcrazy.MyCC98.fragment;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.PostListActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.SearchBoardListAdapter;
import tk.djcrazy.MyCC98.util.ViewUtils;
import tk.djcrazy.libCC98.CachedCC98Service;
import tk.djcrazy.libCC98.data.BoardStatus;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;

public class SearchBoardFragment extends RoboSherlockFragment implements
		OnItemClickListener, OnClickListener {
	private int position = 0;
	private static final String TAG = "SearchBoardFragment";
	private List<BoardStatus> currentResult;
	private List<BoardStatus> boardList;

	@InjectView(R.id.search_board_bar) 
	private EditText searchContent;
	@InjectView(R.id.search_board_result_list)
	private ListView mResultListView;  
	@InjectView(R.id.search_board_loading_bar)
	private ProgressBar progressBar;
 	@InjectView(R.id.search_board_main_container)
	private View mContainer;
 	@InjectView(android.R.id.empty)
 	private TextView emptyView;
	@Inject
	private CachedCC98Service service;

	private SearchBoardListAdapter listAdapter;
  
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
				R.layout.fragment_search_board, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setListeners();
		if (boardList != null) {
 			hide(progressBar).hide(emptyView).show(mContainer);
			mResultListView.setAdapter(listAdapter);
			listAdapter.notifyDataSetChanged();
			mResultListView.invalidate();
		} else {
 			hide(mContainer).hide(emptyView).show(progressBar);
 			new FetchBoardListTask(getActivity()).execute();
		}
	}
 	public void scrollListTo(int x, int y) {
		mResultListView.scrollTo(x, y);
	}

	private void setListeners() {
		searchContent.addTextChangedListener(textWatcher);
		mResultListView.setOnItemClickListener(this);
		emptyView.setOnClickListener(this);
	}

	private void doSearch(String string) {
		if (string.equals("")) {
			if (boardList == null) {
				currentResult = new ArrayList<BoardStatus>();
			} else if (boardList.size() <= 50) {
				currentResult = boardList;
			} else {
				currentResult = boardList.subList(0, 50);
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
		mResultListView.setAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();
		mResultListView.invalidate();
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
	private SearchBoardFragment fadeIn(final View view,
			final boolean animate) {
		if (view != null)
			if (animate)
				view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
						R.anim.activity_open_enter));
			else
				view.clearAnimation();
		return this;
	}

	private SearchBoardFragment show(final View view) {
		ViewUtils.setGone(view, false);
		return this;
	}

	private SearchBoardFragment hide(final View view) {
		ViewUtils.setGone(view, true);
		return this;
	}

	private class FetchBoardListTask extends RoboAsyncTask<List<BoardStatus>> {

		protected FetchBoardListTask(Context context) {
			super(context);
 		}

		@Override
		public List<BoardStatus> call() throws Exception {
			return service.getTodayBoardList();
 		}
		
		@Override
		protected void onSuccess(List<BoardStatus> t) throws Exception {
			boardList = t;
			currentResult = boardList;
			searchContent.setText("");
 			hide(progressBar).hide(emptyView).show(mContainer).fadeIn(mContainer, true);
 		}
		
		@Override
		protected void onException(Exception e) throws RuntimeException {
			super.onException(e);
			hide(progressBar).hide(mContainer).show(emptyView).fadeIn(emptyView, true);
		}
	}

	@Override
	public void onClick(View v) {
			hide(mContainer).hide(emptyView).show(progressBar);
			new FetchBoardListTask(getActivity()).execute();
	}
}
