package tk.djcrazy.MyCC98.fragment;

import java.util.List;

import com.google.inject.Inject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import tk.djcrazy.MyCC98.PmViewActivity;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.PmListViewAdapter;
import tk.djcrazy.libCC98.NewCC98Service;
import tk.djcrazy.libCC98.data.InboxInfo;
import tk.djcrazy.libCC98.data.PmInfo;
import tk.djcrazy.libCC98.util.RequestResultListener;

public class InboxListFragment extends PagedPullTofreshListFragment<PmInfo> {

	@Inject
	private NewCC98Service service;
	private int type = 0;
	private InboxInfo inboxInfo = new InboxInfo(1, 1);
	public static InboxListFragment createFragment(int type) {
		InboxListFragment fragment = new InboxListFragment();
		fragment.type = type;
		return fragment;
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		PmInfo pmInfo = items.get(position-1);
		replyPm(pmInfo.getPmId(), pmInfo.getSender(),
				pmInfo.getSendTime(), pmInfo.getTopic());
	}
	
	private void replyPm(int pmId, String sender, String sendTime, String topic) {
		Intent intent = PmViewActivity.createIntent(topic, sender, sendTime, pmId);
		startActivity(intent);
	}

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        super.onRefresh(refreshView);
        service.submitPmInfoRequest(this.getClass(), type, 1, new RequestResultListener<InboxInfo>() {
            @Override
            public void onRequestComplete(InboxInfo result) {
                inboxInfo = result;
                InboxListFragment.this.onRequestComplete(result.getPmInfos());
            }

            @Override
            public void onRequestError(String msg) {
                InboxListFragment.this.onRequestError(msg);
            }
        });
    }

    @Override
    public void onLoadMore(int page, final RequestResultListener<List<PmInfo>> listener) {
        service.submitPmInfoRequest(this.getClass(), type, page, new RequestResultListener<InboxInfo>() {
            @Override
            public void onRequestComplete(InboxInfo result) {
                inboxInfo = result;
                listener.onRequestComplete(result.getPmInfos());
            }

            @Override
            public void onRequestError(String msg) {
                listener.onRequestError(msg);
            }
        });
    }

    @Override
    public int getTotalPage() {
        return inboxInfo.getTotalInPage();
    }

    @Override
    public void onCancelRequest() {
        service.cancelRequest(this.getClass());
    }


    @Override
	protected BaseItemListAdapter<PmInfo> createAdapter(List<PmInfo> items) {
		return new PmListViewAdapter(getActivity(), items); 
	}
}
