package tk.djcrazy.MyCC98.fragment;

import java.io.Serializable;
import java.util.List;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.NewTopicListAdapter;
import tk.djcrazy.MyCC98.application.MyApplication;
import tk.djcrazy.MyCC98.helper.SerializableCacheHelper;
import tk.djcrazy.MyCC98.util.ThrowableLoader;
import tk.djcrazy.MyCC98.view.PagedPullToRefreshListView;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.SerializableCache;
import tk.djcrazy.libCC98.data.PostEntity;
import tk.djcrazy.libCC98.data.SearchResultEntity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.inject.Inject;

public class NewTopicFragment extends
		PagedPullToRefeshListFragment<SearchResultEntity> {
	private static final String TAG = "NewTopicFragment";

	@Inject
	ICC98Service service;

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
			@SuppressWarnings("unchecked")
			@Override
			public List<SearchResultEntity> loadData() throws Exception {
				List<SearchResultEntity> list = null;
				int pagenum = getListView().getCurrentPage() + 1;
				String keyString = SerializableCacheHelper.newTopicKey(pagenum);
				if (isClearData) {
					list = service.getNewPostList(pagenum);
					items = list;
					// invalidate all cache of new topic
					SerializableCache cache = SerializableCache
							.getInstance(MyApplication.getAppContext());
					cache.removeAllWithPrefix(SerializableCacheHelper
							.newTopicKey());
					cache.put(keyString, (Serializable) list);
				} else {
					SerializableCache cache = SerializableCache
							.getInstance(MyApplication.getAppContext());
					Object object = cache.get(keyString);
					if (object instanceof List) {
						items.addAll((List<SearchResultEntity>) object);
					} else {
						list = service.getNewPostList(pagenum);
						items.addAll(list);
						cache.put(keyString, (Serializable) list);
					}

				}
				return items;
			}
		};
	}

	@Override
	protected BaseItemListAdapter<SearchResultEntity> createAdapter(
			List<SearchResultEntity> items) {
		return new NewTopicListAdapter(getActivity(), items);
	}
}
