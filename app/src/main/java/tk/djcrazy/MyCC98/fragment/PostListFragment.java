package tk.djcrazy.MyCC98.fragment;

import java.util.Iterator;
import java.util.List;

import tk.djcrazy.MyCC98.SettingsActivity;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.PostListViewAdapter;
import tk.djcrazy.MyCC98.util.ThrowableLoader;
import tk.djcrazy.MyCC98.view.PagedPullToRefreshListView;
import tk.djcrazy.libCC98.CachedCC98Service;
import tk.djcrazy.libCC98.data.PostEntity;
import tk.djcrazy.libCC98.data.PostType;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.Loader;
import android.view.View;

import com.google.inject.Inject;

public class PostListFragment extends PagedPullToRefeshListFragment<PostEntity> {
	private static final String TAG = "PostListFragment";
	private static final String BOARD_ID = "boardId";
	private static final String BOARD_NAME = "boardName";

	@Inject
	private CachedCC98Service service;
	private String boardId;
	private String boardName;
	private boolean enableTopPost;
	
	public static PostListFragment createInstance(String boardId,
			String boardName) {
		PostListFragment fragment = new PostListFragment();
		Bundle bundle = new Bundle();
		bundle.putString(BOARD_ID, boardId);
		bundle.putString(BOARD_NAME, boardName);
		fragment.setArguments(bundle);
 		return fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Bundle bundle = getArguments();
 		boardId = bundle.getString(BOARD_ID);
		boardName = bundle.getString(BOARD_NAME);
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
	    enableTopPost = sharedPref.getBoolean(
				SettingsActivity.SHOW_TOP, true);

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
				int pagenum = getListView().getCurrentPage() + 1;
				List<PostEntity> list = service.getPostList(boardId, pagenum, true);
				if (!enableTopPost) {
					Iterator<PostEntity> iterator = list.iterator();
					while (iterator.hasNext()) {
						PostEntity postEntity = (PostEntity) iterator.next();
						if (postEntity.getPostType().equals(PostType.TOP)|
							postEntity.getPostType().equals(PostType.Z_TOP)|
							postEntity.getPostType().equals(PostType.TOP_B)) {
							iterator.remove();
						}
					}
				}
				return list;
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
