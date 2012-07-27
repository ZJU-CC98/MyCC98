package tk.djcrazy.MyCC98.adapter;

import tk.djcrazy.MyCC98.fragment.FriendListFragment;
import tk.djcrazy.MyCC98.fragment.HotTopicFragment;
import tk.djcrazy.MyCC98.fragment.NewTopicFragment;
import tk.djcrazy.MyCC98.fragment.PersonalBoardFragment;
import tk.djcrazy.MyCC98.fragment.SearchBoardFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
	private FragmentManager mFragment;
	private String[] pageTitle = {"我的版面","列表","好友","热门","新帖"};
	private final int FRAGMENT_NUMBER = 5;
	
	public HomeFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		mFragment = fm;
 	}
	@Override
    public CharSequence getPageTitle(int position) {
		return pageTitle[position];
	}
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return new PersonalBoardFragment();
		case 1:
			return new SearchBoardFragment();
		case 2:
			return new FriendListFragment();
		case 3:
			return new HotTopicFragment();
		case 4:
			return new NewTopicFragment();
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
