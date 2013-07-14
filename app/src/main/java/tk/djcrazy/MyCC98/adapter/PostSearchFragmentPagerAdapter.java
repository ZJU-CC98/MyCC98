package tk.djcrazy.MyCC98.adapter;

import tk.djcrazy.MyCC98.fragment.InboxListFragment;
import tk.djcrazy.MyCC98.fragment.PostSearchListFragment;
import tk.djcrazy.MyCC98.fragment.SearchBoardFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PostSearchFragmentPagerAdapter extends FragmentPagerAdapter {
 	private String[] pageTitle = {"主题搜索","作者搜索"};
	private final int FRAGMENT_NUMBER = 2;
	private String mKeyword;
	private String mboardId;
 	public PostSearchFragmentPagerAdapter(FragmentManager fm, String keyword, String boardId) {
		super(fm);
		mKeyword = keyword;
		mboardId = boardId;
  	}
	@Override
    public CharSequence getPageTitle(int position) {
		return pageTitle[position];
	}
	@Override
	public Fragment getItem(int position) {
  		switch (position) {
		case 0:
			return PostSearchListFragment.createFragment(mKeyword, mboardId, "2");
 		case 1:
			return PostSearchListFragment.createFragment(mKeyword, mboardId, "1");
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
