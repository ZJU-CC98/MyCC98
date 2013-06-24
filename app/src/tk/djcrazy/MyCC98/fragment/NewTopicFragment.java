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
import tk.djcrazy.libCC98.SerializableCache;
import tk.djcrazy.libCC98.data.SearchResultEntity;
import tk.djcrazy.libCC98.util.SerializableCacheUtil;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.google.inject.Inject;

public class NewTopicFragment extends
		PagedPullToRefeshListFragment<SearchResultEntity> {
	private static final String TAG = "NewTopicFragment";

	@Inject
	CachedCC98Service service;

	public void scrollListTo(int x, int y) {
		listView.scrollTo(x, y);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		SearchResultEntity entity = items.get(position - 1);
		getActivity().startActivity(
				PostContentsJSActivity.createIntent(entity.getBoardId(),
						entity.getPostId()));
	}

	@Override
	protected void configureList(Activity activity,
			PagedPullToRefreshListView listView) {
		super.configureList(activity, listView);
		listView.setPageSize(20);
		listView.setTotalPageNumber(5);
	}

	@Override
	public Loader<List<SearchResultEntity>> onCreateLoader(int arg0, Bundle arg1) {
		return new ThrowableLoader<List<SearchResultEntity>>(getActivity(),
				items) {
			@Override
			public List<SearchResultEntity> loadData() throws Exception {
				int pagenum = getListView().getCurrentPage() + 1;
				return service.getNewPostList(pagenum, isClearData);
			}
		};
	}

	@Override
	protected BaseItemListAdapter<SearchResultEntity> createAdapter(
			List<SearchResultEntity> items) {
		return new NewTopicListAdapter(getActivity(), items);
	}
}
