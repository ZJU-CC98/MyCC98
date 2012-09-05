package tk.djcrazy.MyCC98.fragment;

import java.util.List;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.HotTopicListAdapter;
import tk.djcrazy.MyCC98.adapter.PostListViewAdapter;
import tk.djcrazy.MyCC98.util.ThrowableLoader;
import tk.djcrazy.MyCC98.view.PagedPullToRefreshListView;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.HotTopicEntity;
import tk.djcrazy.libCC98.data.PostEntity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.inject.Inject;

public class PostListFragment extends PagedPullToRefeshListFragment<PostEntity> {
	private static final String TAG = "PostListFragment";
	@Inject
	private ICC98Service service;
	private String boardId; 
	private String boardName;
	
	public static PostListFragment createInstance(String boardId, String boardName) {
		PostListFragment fragment = new PostListFragment();
		fragment.boardId = boardId;
		fragment.boardName = boardName;
		return fragment;
	}

	@Override
	protected void configureList(Activity activity,
			PagedPullToRefreshListView listView) {
		super.configureList(activity, listView);
		listView.setTotalPageNumber(32767);
 	}
	@Override
	public Loader<List<PostEntity>> onCreateLoader(int arg0, Bundle arg1) {
		return new ThrowableLoader<List<PostEntity>>(getActivity(), items) {
			@Override
			public List<PostEntity> loadData() throws Exception {
				return service.getPostList(boardId, getListView().getCurrentPage()+1);
			}
		};
	}

	@Override
	protected BaseItemListAdapter<PostEntity> createAdapter(
			List<PostEntity> items) {
		return new PostListViewAdapter(getActivity(), items, boardId, boardName);
	}
}
