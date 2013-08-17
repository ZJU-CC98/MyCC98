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
import android.widget.ListView;

import com.google.inject.Inject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

public class PersonalBoardFragment extends
		NewPullToRefeshListFragment<BoardEntity> {

	@Inject
	private NewCC98Service mNewCC98Service;

	@Override
	protected BaseItemListAdapter<BoardEntity> createAdapter(
			List<BoardEntity> items) {
		return new BoardListAdapter(getActivity(), items);
	}


    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        mNewCC98Service.submitPersonalBoardList(this.getClass(), this);
    }

    @Override
    public void onCancelRequest() {
        mNewCC98Service.cancelRequest(this.getClass());
    }
}
