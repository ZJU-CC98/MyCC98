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
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.inject.Inject;

public class PostListFragment extends PagedPullToRefeshListFragment<PostEntity> {
	private static final String TAG = "PostListFragment";
	private static final String BOARD_ID = "boardId";
	private static final String BOARD_NAME = "boardName";
	
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
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
 		if (savedInstanceState!=null) {
			boardId = savedInstanceState.getString(BOARD_ID);
			boardName = savedInstanceState.getString(BOARD_NAME);
		}
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
				List<PostEntity> list =  service.getPostList(boardId, getListView().getCurrentPage()+1) ;
				if (isClearData) {
					 items = list;
				} else {
					items.addAll(service.getPostList(boardId, getListView().getCurrentPage()+1));
				}
				return items;
 			}
		};
	}

	@Override
	protected BaseItemListAdapter<PostEntity> createAdapter(
			List<PostEntity> items) {
		return new PostListViewAdapter(getActivity(), items, boardId, boardName);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
 		outState.putString(BOARD_ID, boardId);
		outState.putString(BOARD_NAME, boardName);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
 		super.onConfigurationChanged(newConfig);
	}
	
 }
