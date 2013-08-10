package tk.djcrazy.MyCC98.adapter;

import tk.djcrazy.MyCC98.fragment.HotTopicFragment;
import tk.djcrazy.MyCC98.fragment.NewTopicFragment;
import tk.djcrazy.MyCC98.fragment.PersonalBoardFragment;
import tk.djcrazy.MyCC98.fragment.SearchBoardFragment;
import tk.djcrazy.MyCC98.listener.LoadingListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
	private FragmentManager mFragment;
	private String[] pageTitle = {"我的版面","热门话题","版面列表","查看新帖"};
	private final int FRAGMENT_NUMBER = 4;
    private JazzyViewPager mJazzyViewPager;

 	public HomeFragmentPagerAdapter(FragmentManager fm, JazzyViewPager viewPager) {
		super(fm);
		mFragment = fm;
        mJazzyViewPager = viewPager;
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

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Object obj = super.instantiateItem(container, position);
        mJazzyViewPager.setObjectForPosition(obj, position);
        return obj;
    }
}
