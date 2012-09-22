package tk.djcrazy.MyCC98.adapter;

import tk.djcrazy.MyCC98.fragment.FriendListFragment;
import tk.djcrazy.MyCC98.fragment.HotTopicFragment;
import tk.djcrazy.MyCC98.fragment.NewTopicFragment;
import tk.djcrazy.MyCC98.fragment.PersonalBoardFragment;
import tk.djcrazy.MyCC98.fragment.PersonalBoardFragment;
import tk.djcrazy.MyCC98.fragment.SearchBoardFragment;
import tk.djcrazy.MyCC98.listener.LoadingListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
	private FragmentManager mFragment;
	private String[] pageTitle = {"我的版面","热门","列表","新帖","好友"};
	private final int FRAGMENT_NUMBER = 4;
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
			PersonalBoardFragment pFragment = new PersonalBoardFragment();
 			return pFragment;
		case 1:
			return new HotTopicFragment();
		case 2:
			SearchBoardFragment sFragment =  new SearchBoardFragment();
 			sFragment.setPosition(position);
			return sFragment;
		case 3:
			return new NewTopicFragment();
		case 4:
			return new FriendListFragment();
		default:
			break;
		}
		return null;
	}

	public void setLoadingListener( LoadingListener listener) {
 	}
	@Override
	public int getCount() {
		return FRAGMENT_NUMBER;
	}
}
