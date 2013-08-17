package tk.djcrazy.MyCC98.fragment;

import java.io.Serializable;
import java.util.List;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.NewTopicListAdapter;
import tk.djcrazy.MyCC98.application.MyApplication;
import tk.djcrazy.MyCC98.util.ThrowableLoader;
import tk.djcrazy.MyCC98.view.PagedPullToRefreshListView;
import tk.djcrazy.libCC98.CachedCC98Service;
import tk.djcrazy.libCC98.NewCC98Service;
import tk.djcrazy.libCC98.SerializableCache;
import tk.djcrazy.libCC98.data.SearchResultEntity;
import tk.djcrazy.libCC98.util.RequestResultListener;
import tk.djcrazy.libCC98.util.SerializableCacheUtil;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.google.inject.Inject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

public class NewTopicFragment extends
		NewPagedPullTofreshListFragment<SearchResultEntity> {
	private static final String TAG = "NewTopicFragment";

	@Inject
    private NewCC98Service service;


	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		SearchResultEntity entity = items.get(position - 1);
		getActivity().startActivity(
				PostContentsJSActivity.createIntent(entity.getBoardId(),
						entity.getPostId()));
	}

	@Override
	protected BaseItemListAdapter<SearchResultEntity> createAdapter(
			List<SearchResultEntity> items) {
		return new NewTopicListAdapter(getActivity(), items);
	}

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        super.onRefresh(refreshView);
        service.submitNewTopicList(this.getClass(), 1, this);
    }

    @Override
    public void onLoadMore(int page, RequestResultListener<List<SearchResultEntity>> listener) {
        service.submitNewTopicList(this.getClass(), page, listener);
    }

    @Override
    public int getTotalPage() {
        return 5;
    }

    @Override
    public void onCancelRequest() {
        service.cancelRequest(this.getClass());
    }
}
