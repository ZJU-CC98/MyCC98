package tk.djcrazy.MyCC98.fragment;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.PostListActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.SearchBoardListAdapter;
import tk.djcrazy.MyCC98.helper.LoadingModelHelper;
import tk.djcrazy.libCC98.NewCC98Service;
import tk.djcrazy.libCC98.data.BoardStatus;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.inject.Inject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

public
class SearchBoardFragment extends PullToRefeshListFragment<BoardStatus>  {
	private int position = 0;
	private static final String TAG = "SearchBoardFragment";
	private List<BoardStatus> totalBoards;

	@InjectView(R.id.search_board_text) 
	private EditText searchContentEditText; 
  	@InjectView(R.id.board_filter)
 	private ImageView mboardFilter;
	@InjectView(R.id.search_board_bar)
	private LinearLayout mSearchBar;
	@Inject
	private NewCC98Service service;

	private int mOriginalSearchBarWidth = 0;
	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			doSearch(searchContentEditText.getText().toString().trim());
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
        helper = new LoadingModelHelper(view.findViewById(R.id.search_board_main_container),
                view.findViewById(android.R.id.empty), view.findViewById(R.id.pb_loading), this);
		super.onViewCreated(view, savedInstanceState);
		setListeners();
 	}

 	private void toggleFilter() {
 		if (searchContentEditText.getVisibility()==View.GONE) {
            searchContentEditText.setVisibility(View.VISIBLE);
		} else {
            searchContentEditText.setVisibility(View.GONE);
		}
 	}
	private void setListeners() {
		searchContentEditText.addTextChangedListener(textWatcher);

        mboardFilter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleFilter();
			}
		});
	}

	private void doSearch(String string) {
		if (string.equals("")) {
			if (totalBoards.size() <= 50) {
                setItems(totalBoards);
			} else {
				setItems(totalBoards.subList(0, 50));
			}
		} else {
			List<BoardStatus> tmplist = new ArrayList<BoardStatus>();
			for (BoardStatus np : items) {
				if (np.getBoardName().toLowerCase()
						.contains(string.toLowerCase())) {
					tmplist.add(np);
				}
			}
	    	setItems(tmplist);
		}
        refreshListViewData();
 	}
 

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        getActivity().startActivity(PostListActivity.createIntent(totalBoards.get(position - 1)
                .getBoardName(), totalBoards.get(position - 1)
                .getBoardId()));
    }

    @Override
    public void onRequestComplete(List<BoardStatus> result) {
        super.onRequestComplete(result);
        totalBoards = new ArrayList<BoardStatus>(items);
        doSearch(searchContentEditText.getText().toString().trim());
     }

    @Override
    protected BaseItemListAdapter<BoardStatus> createAdapter(
            List<BoardStatus> items) {
        return new SearchBoardListAdapter(getActivity(), items);
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        service.submitTodayBoardlList(this.getClass(), this);
    }

    @Override
    public void onCancelRequest() {
        service.cancelRequest(this.getClass());
    }

}
