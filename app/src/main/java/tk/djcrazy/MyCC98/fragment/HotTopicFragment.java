package tk.djcrazy.MyCC98.fragment;

import java.io.Serializable;
import java.util.List;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.HotTopicListAdapter;
import tk.djcrazy.MyCC98.application.MyApplication;
import tk.djcrazy.MyCC98.util.ThrowableLoader;
import tk.djcrazy.libCC98.CachedCC98Service;
import tk.djcrazy.libCC98.SerializableCache;
import tk.djcrazy.libCC98.data.BoardEntity;
import tk.djcrazy.libCC98.data.HotTopicEntity;
import tk.djcrazy.libCC98.util.SerializableCacheUtil;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.inject.Inject;

public class HotTopicFragment extends PullToRefeshListFragment<HotTopicEntity> {
	private static final String TAG = "HotTopicFragment";
	@Inject
	private CachedCC98Service service;
	private boolean initload = true;
 
	 
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		HotTopicEntity entity = items.get(position - 1);
  		getActivity().startActivity(PostContentsJSActivity.createIntent(entity.getBoardId(), entity.getPostId()));
 	}

	@Override
	public Loader<List<HotTopicEntity>> onCreateLoader(int arg0, Bundle arg1) {
		return new ThrowableLoader<List<HotTopicEntity>>(getActivity(), items) {
			@Override
			public List<HotTopicEntity> loadData() throws Exception {
				List<HotTopicEntity> ret = service.getHotTopicList(!initload);
				initload = false;
				return ret;
			}
		};
	}

	@Override
	protected BaseItemListAdapter<HotTopicEntity> createAdapter(
			List<HotTopicEntity> items) {
		return new HotTopicListAdapter(getActivity(), items);
	}
}
