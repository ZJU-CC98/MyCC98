package tk.djcrazy.MyCC98;

import android.content.Intent;

import com.baidu.mobstat.StatService;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import de.keyboardsurfer.android.widget.crouton.Crouton;

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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
    }
}
