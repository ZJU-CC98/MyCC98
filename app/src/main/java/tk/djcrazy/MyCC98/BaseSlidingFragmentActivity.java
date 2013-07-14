package tk.djcrazy.MyCC98;

import com.baidu.mobstat.StatService;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseSlidingFragmentActivity extends SlidingFragmentActivity {

	@Override
	protected void onResume() {
		super.onResume();
		 StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		 StatService.onPause(this);
	}
}
