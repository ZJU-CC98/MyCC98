package tk.djcrazy.MyCC98.fragment;

import java.util.List;

import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.BoardListAdapter;
import tk.djcrazy.MyCC98.util.ThrowableLoader;
import tk.djcrazy.libCC98.CachedCC98Service;
import tk.djcrazy.libCC98.data.BoardEntity;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.google.inject.Inject;

public class PersonalBoardFragment extends
		PullToRefeshListFragment<BoardEntity> {

	@Inject
	private CachedCC98Service service;
	private boolean initload = true;

	@Override
	public Loader<List<BoardEntity>> onCreateLoader(int arg0, Bundle arg1) {
		return new ThrowableLoader<List<BoardEntity>>(getActivity(), items) {
			@Override
			public List<BoardEntity> loadData() throws Exception {
				List<BoardEntity> ret = service.getPersonalBoardList(!initload);
				initload = false;
				return ret;
			}
		};
	}

	@Override
	protected BaseItemListAdapter<BoardEntity> createAdapter(
			List<BoardEntity> items) {
		return new BoardListAdapter(getActivity(), items);
	}
}
