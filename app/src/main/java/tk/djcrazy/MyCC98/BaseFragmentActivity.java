package tk.djcrazy.MyCC98;

import com.baidu.mobstat.StatService;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class BaseFragmentActivity extends RoboSherlockFragmentActivity {

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
