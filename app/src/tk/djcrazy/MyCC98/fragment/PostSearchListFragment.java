package tk.djcrazy.MyCC98.fragment;

import java.util.List;

import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.NewTopicListAdapter;
import tk.djcrazy.MyCC98.util.ThrowableLoader;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.SearchResultEntity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

import com.google.inject.Inject;

public class PostSearchListFragment extends PagedPullToRefeshListFragment<SearchResultEntity> {

	@Inject
	private ICC98Service mService;
	private static final String SEARCH_TYPE="search_type";
	private static final String SEARCH_KEYWORD="search_keyword";
	private static final String SEARCH_BOARDID="search_boardid";
	
	private String mType="0";
	private String mKeyword="";
	private String mBoardId="0";
	
 	public static PostSearchListFragment createFragment(String keyword, String boardId, String type) {
		PostSearchListFragment fragment = new PostSearchListFragment();
		Bundle bundle = new Bundle();
		bundle.putString(SEARCH_TYPE, type);
		bundle.putString(SEARCH_KEYWORD, keyword);
		bundle.putString(SEARCH_BOARDID, boardId);
		fragment.setArguments(bundle);
 		return fragment;
	}
	
 	
 	@Override
 	public void onViewCreated(View view, Bundle savedInstanceState) {
 		super.onViewCreated(view, savedInstanceState);
 		mType =  getArguments().getString(SEARCH_TYPE);
 		mKeyword =  getArguments().getString(SEARCH_KEYWORD);
 		mBoardId =  getArguments().getString(SEARCH_BOARDID);
 	}
	@Override
	public Loader<List<SearchResultEntity>> onCreateLoader(int arg0, Bundle arg1) {
		return new ThrowableLoader<List<SearchResultEntity>>(getActivity(), items) {

			@Override
			public List<SearchResultEntity> loadData() throws Exception {
				List<SearchResultEntity> list = mService.searchPost(mKeyword, mBoardId, mType, getListView().getCurrentPage()+1); 
				if (isClearData) {
					items = list;
				} else {
					items.addAll(list);
				}
				getListView().setTotalPageNumber(list.size()>0? (int)Math.ceil(Integer.parseInt(list.get(0).getTotalResult())*1.0/25):0);
				return items;
			}
		};
	}

	@Override
	protected BaseItemListAdapter<SearchResultEntity> createAdapter(List<SearchResultEntity> items) {
		return new NewTopicListAdapter(getActivity(), items); 
	}
}
