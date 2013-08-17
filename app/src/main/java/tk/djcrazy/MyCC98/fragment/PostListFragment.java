package tk.djcrazy.MyCC98.fragment;

import java.util.Iterator;
import java.util.List;

import tk.djcrazy.MyCC98.SettingsActivity;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.PostListViewAdapter;
import tk.djcrazy.libCC98.NewCC98Service;
import tk.djcrazy.libCC98.data.PostEntity;
import tk.djcrazy.libCC98.data.PostType;
import tk.djcrazy.libCC98.util.RequestResultListener;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;

import com.google.inject.Inject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

public class PostListFragment extends PagedPullTofreshListFragment<PostEntity> {
	private static final String TAG = "PostListFragment";
	private static final String BOARD_ID = "boardId";
	private static final String BOARD_NAME = "boardName";

	@Inject
	private NewCC98Service service;
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
        Bundle bundle = getArguments();
        boardId = bundle.getString(BOARD_ID);
        boardName = bundle.getString(BOARD_NAME);
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        enableTopPost = sharedPref.getBoolean(
                SettingsActivity.SHOW_TOP, true);
		super.onViewCreated(view, savedInstanceState);
 	}

    @Override
    public void onRequestComplete(List<PostEntity> result) {
        if (!enableTopPost) {
            Iterator<PostEntity> iterator = result.iterator();
            while (iterator.hasNext()) {
                PostEntity postEntity = (PostEntity) iterator.next();
                if (postEntity.getPostType().equals(PostType.TOP)|
                        postEntity.getPostType().equals(PostType.Z_TOP)|
                        postEntity.getPostType().equals(PostType.TOP_B)) {
                    iterator.remove();
                }
            }
        }
        super.onRequestComplete(result);
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        super.onRefresh(refreshView);
        service.submitPostList(this.getClass(), boardId, 1, this);
    }

    @Override
    public void onLoadMore(int page, RequestResultListener<List<PostEntity>> listener) {
        service.submitPostList(this.getClass(), boardId, page, listener);
    }

    @Override
    public int getTotalPage() {
        return 32767;
    }

    @Override
    public void onCancelRequest() {
        service.cancelRequest(this.getClass());
    }

	@Override
	protected BaseItemListAdapter<PostEntity> createAdapter(
			List<PostEntity> items) {
		return new PostListViewAdapter(getActivity(), items, boardId, boardName);
	}
}
