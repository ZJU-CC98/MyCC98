package tk.djcrazy.MyCC98.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.MyCC98.util.ViewUtils;
import tk.djcrazy.libCC98.util.RequestResultListener;

/**
 * Created by Ding on 13-8-17.
 */
public
abstract class PagedPullTofreshListFragment<E> extends PullToRefeshListFragment<E>
        implements PullToRefreshBase.OnLastItemVisibleListener{
    private int currentPage = 1;
    private boolean mIsLoadingMore = false;
    private RequestResultListener<List<E>> mListener;
    private View footView;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView.setOnLastItemVisibleListener(this);
        mListener = new RequestResultListener<List<E>>() {
            @Override
            public void onRequestComplete(List<E> result) {
                currentPage++;
                items.addAll(result);
                refreshListViewData();
                mIsLoadingMore =false;
            }

            @Override
            public void onRequestError(String msg) {
                ToastUtils.alert(getActivity(), msg);
                mIsLoadingMore = false;
            }
        };

    }

    @Override
    protected void shouldConfigureListViewBeforeSetAdapter(PullToRefreshListView view) {
        super.shouldConfigureListViewBeforeSetAdapter(view);
        footView = LayoutInflater.from(getActivity()).inflate(
                R.layout.loading_item, null);
        view.getRefreshableView().addFooterView(footView, null, false);
    }

    @Override
    public void onLastItemVisible() {
        if (!mIsLoadingMore&& currentPage<getTotalPage()) {
            ViewUtils.setGone(footView, false);
            onLoadMore(currentPage + 1, mListener);
            mIsLoadingMore = true;
        } else {
            ViewUtils.setGone(footView, true);
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        mIsLoadingMore = true;
    }

    @Override
    public void onRequestComplete(List<E> result) {
        super.onRequestComplete(result);
        ViewUtils.setGone(footView, true);
        mIsLoadingMore = false;
        currentPage = 1;
    }

    @Override
    public void onRequestError(String msg) {
        super.onRequestError(msg);
        ViewUtils.setGone(footView, true);
        mIsLoadingMore = false;
        currentPage = 1;
    }

    public abstract int getTotalPage();

    public abstract void onLoadMore(int page, RequestResultListener<List<E>> listener);

}
