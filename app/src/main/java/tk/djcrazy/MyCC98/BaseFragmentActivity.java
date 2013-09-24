package tk.djcrazy.MyCC98;

import android.content.Intent;

import com.baidu.mobstat.StatService;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class BaseFragmentActivity extends SwipeBackActivity {

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
