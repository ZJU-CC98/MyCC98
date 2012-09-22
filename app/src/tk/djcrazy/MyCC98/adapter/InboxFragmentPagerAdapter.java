package tk.djcrazy.MyCC98.adapter;

import tk.djcrazy.MyCC98.fragment.InboxListFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class InboxFragmentPagerAdapter extends FragmentPagerAdapter {
 	private String[] pageTitle = {"收件箱","发件箱"};
	private final int FRAGMENT_NUMBER = 2;
 	public InboxFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
  	}
	@Override
    public CharSequence getPageTitle(int position) {
		return pageTitle[position];
	}
	@Override
	public Fragment getItem(int position) {
  		switch (position) {
		case 0:
			return InboxListFragment.createFragment(0);
 		case 1:
			return InboxListFragment.createFragment(1);
 		default:
			break;
		}
		return null;
	}

 	@Override
	public int getCount() {
		return FRAGMENT_NUMBER;
	}
}
