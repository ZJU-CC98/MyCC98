package tk.djcrazy.MyCC98.view;

import tk.djcrazy.MyCC98.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;

public class PagedPullToRefreshListView extends PullToRefreshListView {
	private static final String TAG = "PagedPullToRefreshListView";

	private int currentPage = 0;
	private int totalPageNumber = 1;
	private int pageSize = 10;

	private boolean isLoading = false;
	private LoadMoreListener loadMoreListener;
	private View footerView;

	public PagedPullToRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public PagedPullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PagedPullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		footerView = LayoutInflater.from(context).inflate(
				R.layout.loading_item, null);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
 
		if (totalPageNumber <= currentPage && getFooterViewsCount() >= 1) {
			removeFooterView(view);
			return;
		} else if (totalPageNumber > currentPage
				&& getLastVisiblePosition() >= getAdapter().getCount()-2
				&& !isLoading) {
			isLoading = true;
			if (loadMoreListener != null) {
				loadMoreListener.OnLoadMore(currentPage + 1, pageSize);
			}
		}
	}

	@Override
	public void onRefresh() {
		super.onRefresh();
		currentPage = 0;
		isLoading = true;
	}

	@Override
	public void onRefreshComplete() {
		super.onRefreshComplete();
		onLoadDone();
	}

	public void onLoadDone() {
		isLoading = false;
		currentPage++;
		if (currentPage >= totalPageNumber && getFooterViewsCount() > 0) {
			removeFooterView(footerView);
		} else if (currentPage < totalPageNumber && getFooterViewsCount() == 0) {
			addFooterView(footerView);
		}
	}

	public void onLoadFailed() {
		isLoading = false;
		if (currentPage >= totalPageNumber) {
			removeFooterView(footerView);
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public LoadMoreListener getLoadMoreListener() {
		return loadMoreListener;
	}

	public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
		this.loadMoreListener = loadMoreListener;
	}

	public interface LoadMoreListener {
		public void OnLoadMore(int currentPage, int pageSize);
	}

	public int getTotalPageNumber() {
		return totalPageNumber;
	}

	public void setTotalPageNumber(int totalPageNumber) {
		this.totalPageNumber = totalPageNumber;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
