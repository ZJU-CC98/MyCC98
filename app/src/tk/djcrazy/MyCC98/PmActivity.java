package tk.djcrazy.MyCC98;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.adapter.InboxFragmentPagerAdapter;
import tk.djcrazy.libCC98.ICC98Service;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
 
@ContentView(R.layout.pm)
public class PmActivity extends RoboSherlockFragmentActivity {

	private static String TAG = "PmActivity";
	@Inject
	private ICC98Service service;

	@InjectView(R.id.pm_main_pages)
	private ViewPager viewPager;
	@InjectView(R.id.pm_main_titles)
	private TitlePageIndicator indicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
		setContentView(R.layout.pm);
		configureActionBar();
		InboxFragmentPagerAdapter adapter = new InboxFragmentPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		indicator.setViewPager(viewPager);
	}

	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setLogo(new BitmapDrawable(service.getUserAvatar()));
		actionBar.setTitle("论坛短消息");
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
