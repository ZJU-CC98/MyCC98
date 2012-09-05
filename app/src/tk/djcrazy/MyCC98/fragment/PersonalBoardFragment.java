package tk.djcrazy.MyCC98.fragment;

import java.util.List;

import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.PersonalboardListAdapter;
import tk.djcrazy.MyCC98.util.ThrowableLoader;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.BoardEntity;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.google.inject.Inject;

public class PersonalBoardFragment extends
		PagedPullToRefeshListFragment<BoardEntity> {

	@Inject
	private ICC98Service service;

	@Override
	public Loader<List<BoardEntity>> onCreateLoader(int arg0, Bundle arg1) {
		return new ThrowableLoader<List<BoardEntity>>(getActivity(), items) {
			@Override
			public List<BoardEntity> loadData() throws Exception {
				return service.getPersonalBoardList();
			}
		};
	}

	@Override
	protected BaseItemListAdapter<BoardEntity> createAdapter(
			List<BoardEntity> items) {
		return new PersonalboardListAdapter(getActivity(), items);
	}

	@Override
	public void OnLoadMore(int currentPage, int pageSize) {
		
	}
}
