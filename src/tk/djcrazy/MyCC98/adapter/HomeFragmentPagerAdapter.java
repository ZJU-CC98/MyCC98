package tk.djcrazy.MyCC98.adapter;

import tk.djcrazy.MyCC98.fragment.FriendListFragment;
import tk.djcrazy.MyCC98.fragment.HotTopicFragment;
import tk.djcrazy.MyCC98.fragment.NewTopicFragment;
import tk.djcrazy.MyCC98.fragment.PersonalBoardFragment;
import tk.djcrazy.MyCC98.fragment.SearchBoardFragment;
import tk.djcrazy.MyCC98.listener.LoadingListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
	private FragmentManager mFragment;
	private String[] pageTitle = {"我的版面","列表","好友","热门","新帖"};
	private final int FRAGMENT_NUMBER = 5;
	private LoadingListener loadingListener;
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
		if (loadingListener==null) {
			throw new IllegalStateException("You must set loadinglistener first.");
		}
 		switch (position) {
		case 0:
			PersonalBoardFragment pFragment = new PersonalBoardFragment();
			pFragment.setLoadingListener(loadingListener);
			pFragment.setPosition(position);
			return pFragment;
		case 1:
			SearchBoardFragment sFragment =  new SearchBoardFragment();
			sFragment.setLoadingListener(loadingListener);
			sFragment.setPosition(position);
			return sFragment;
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

	public void setLoadingListener( LoadingListener listener) {
		loadingListener = listener;
	}
	@Override
	public int getCount() {
		return FRAGMENT_NUMBER;
	}
}
