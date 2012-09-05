package tk.djcrazy.MyCC98.fragment;

import java.util.List;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.HotTopicListAdapter;
import tk.djcrazy.MyCC98.util.ThrowableLoader;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.HotTopicEntity;
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
	private ICC98Service service;
 
	 
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		HotTopicEntity entity = items.get(position - 1);
		Intent intent = new Intent(getActivity(), PostContentsJSActivity.class);
		intent.putExtra(PostContentsJSActivity.BOARD_ID, entity.getBoardId());
		intent.putExtra(PostContentsJSActivity.BOARD_NAME,
				entity.getBoardName());
		intent.putExtra(PostContentsJSActivity.POST_ID, entity.getPostId());
		intent.putExtra(PostContentsJSActivity.POST_NAME, entity.getTopicName());
		intent.putExtra(PostContentsJSActivity.PAGE_NUMBER, 1);
		getActivity().startActivity(intent);
		getActivity().overridePendingTransition(
				R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);
	}

	@Override
	public Loader<List<HotTopicEntity>> onCreateLoader(int arg0, Bundle arg1) {
		return new ThrowableLoader<List<HotTopicEntity>>(getActivity(), items) {
			@Override
			public List<HotTopicEntity> loadData() throws Exception {
				return service.getHotTopicList();
			}
		};
	}

	@Override
	protected BaseItemListAdapter<HotTopicEntity> createAdapter(
			List<HotTopicEntity> items) {
		return new HotTopicListAdapter(getActivity(), items);
	}
}
