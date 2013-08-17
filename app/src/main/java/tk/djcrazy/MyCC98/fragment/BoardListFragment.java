package tk.djcrazy.MyCC98.fragment;

import java.util.List;

import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.BoardListAdapter;
import tk.djcrazy.MyCC98.util.ThrowableLoader;
import tk.djcrazy.libCC98.CachedCC98Service;
import tk.djcrazy.libCC98.NewCC98Service;
import tk.djcrazy.libCC98.data.BoardEntity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.google.inject.Inject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

public class BoardListFragment extends
		NewPullToRefeshListFragment<BoardEntity> {
	private static final String TAG = "PostListFragment";
	private static final String BOARD_ID = "boardId";
	private static final String BOARD_NAME = "boardName";
 	
 	@Inject
	private NewCC98Service service;
	private String boardId;
	private String boardName;

	public static BoardListFragment createInstance(String boardId,
			String boardName) {
		BoardListFragment fragment = new BoardListFragment();
		Bundle bundle = new Bundle();
		bundle.putString(BOARD_ID, boardId);
		bundle.putString(BOARD_NAME, boardName);
		fragment.setArguments(bundle);
 		return fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        boardId = bundle.getString(BOARD_ID);
        boardName = bundle.getString(BOARD_NAME);
		super.onViewCreated(view, savedInstanceState);
 	}

 	@Override
	protected BaseItemListAdapter<BoardEntity> createAdapter(
			List<BoardEntity> items) {
		return new BoardListAdapter(getActivity(), items);
	}

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        service.submitBoardList(getClass(),boardId, this);
    }

    @Override
    public void onCancelRequest() {
        service.cancelRequest(getClass());
    }
}
