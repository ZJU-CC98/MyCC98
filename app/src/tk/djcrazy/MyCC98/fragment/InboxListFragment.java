package tk.djcrazy.MyCC98.fragment;

import java.util.List;

import com.google.inject.Inject;

import android.os.Bundle;
import android.support.v4.content.Loader;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.adapter.PmListViewAdapter;
import tk.djcrazy.MyCC98.util.ThrowableLoader;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.InboxInfo;
import tk.djcrazy.libCC98.data.PmInfo;

public class InboxListFragment extends PagedPullToRefeshListFragment<PmInfo> {

	@Inject
	private ICC98Service mService;
	private int type=0;
	private InboxInfo inboxInfo = new InboxInfo(0, 0);
	public static InboxListFragment createFragment(int type) {
		InboxListFragment fragment = new InboxListFragment();
		fragment.type = type;
		return fragment;
	}
	
	@Override
	public Loader<List<PmInfo>> onCreateLoader(int arg0, Bundle arg1) {
		return new ThrowableLoader<List<PmInfo>>(getActivity(), items) {

			@Override
			public List<PmInfo> loadData() throws Exception {
				List<PmInfo> list = mService.getPmData(getListView().getCurrentPage()+1, inboxInfo, type); 
				if (isClearData) {
					items = list;
				} else {
					items.addAll(list);
				}
				getListView().setTotalPageNumber(inboxInfo.getTotalInPage());
				return items;
			}
		};
	}

	@Override
	protected BaseItemListAdapter<PmInfo> createAdapter(List<PmInfo> items) {
		return new PmListViewAdapter(getActivity(), items); 
	}
}
