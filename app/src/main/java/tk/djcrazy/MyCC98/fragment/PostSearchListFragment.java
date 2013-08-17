package tk.djcrazy.MyCC98.fragment;

import java.util.List;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.NewTopicListAdapter;
import tk.djcrazy.libCC98.NewCC98Service;
import tk.djcrazy.libCC98.data.SearchResultEntity;
import tk.djcrazy.libCC98.util.RequestResultListener;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.inject.Inject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

public class PostSearchListFragment extends PagedPullTofreshListFragment<SearchResultEntity> {

	@Inject
	private NewCC98Service mService;
	private static final String SEARCH_TYPE="search_type";
	private static final String SEARCH_KEYWORD="search_keyword";
	private static final String SEARCH_BOARDID="search_boardid";
	
	private String mType="0";
	private String mKeyword="";
	private String mBoardId="0";
    private int totalPage = 1;
	
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
        mType =  getArguments().getString(SEARCH_TYPE);
        mKeyword =  getArguments().getString(SEARCH_KEYWORD);
        mBoardId =  getArguments().getString(SEARCH_BOARDID);
 		super.onViewCreated(view, savedInstanceState);
 	}

 	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		startActivity(PostContentsJSActivity.createIntent(items.get(position-1).getBoardId(), items.get(position-1).getPostId()));
	}

	@Override
	protected BaseItemListAdapter<SearchResultEntity> createAdapter(List<SearchResultEntity> items) {
		return new NewTopicListAdapter(getActivity(), items); 
	}

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        super.onRefresh(refreshView);
        mService.submitPostSearch(this.getClass(), mKeyword, mBoardId, mType, 1, this);
    }

    @Override
    public void onRequestComplete(List<SearchResultEntity> result) {
        totalPage = result.size()>0? (int)Math.ceil(Integer.parseInt(result.get(0).getTotalResult())*1.0/25):0;
        super.onRequestComplete(result);
    }

    @Override
    public void onLoadMore(int page, RequestResultListener<List<SearchResultEntity>> listener) {
        mService.submitPostSearch(this.getClass(), mKeyword, mBoardId, mType, page, listener);
    }

    @Override
    public int getTotalPage() {
        return totalPage;
    }

    @Override
    public void onCancelRequest() {
        mService.cancelRequest(this.getClass());
    }
}
