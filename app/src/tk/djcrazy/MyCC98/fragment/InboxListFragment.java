package tk.djcrazy.MyCC98.fragment;

import java.util.List;

import com.google.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import tk.djcrazy.MyCC98.PmViewActivity;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.PmListViewAdapter;
import tk.djcrazy.MyCC98.util.ThrowableLoader;
import tk.djcrazy.libCC98.CachedCC98Service;
import tk.djcrazy.libCC98.data.InboxInfo;
import tk.djcrazy.libCC98.data.PmInfo;

public class InboxListFragment extends PagedPullToRefeshListFragment<PmInfo> {

	@Inject
	private CachedCC98Service mService;
	private int type=0;
	private InboxInfo inboxInfo = new InboxInfo(0, 0);
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
	public Loader<List<PmInfo>> onCreateLoader(int arg0, Bundle arg1) {
		return new ThrowableLoader<List<PmInfo>>(getActivity(), items) {

			@Override
			public List<PmInfo> loadData() throws Exception {
				List<PmInfo> list = mService.getPmData(getListView().getCurrentPage()+1, inboxInfo, type); 
 				getListView().setTotalPageNumber(inboxInfo.getTotalInPage());
				return list;
			}
		};
	}

	@Override
	protected BaseItemListAdapter<PmInfo> createAdapter(List<PmInfo> items) {
		return new PmListViewAdapter(getActivity(), items); 
	}
}
