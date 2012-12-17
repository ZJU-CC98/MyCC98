package tk.djcrazy.MyCC98;

import android.os.Bundle;
import android.view.KeyEvent;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

public class BaseActivity extends RoboSherlockFragmentActivity {

	@Override
	public void onStart() {
		super.onStart();
		// FlurryAgent.onStartSession(this, "5EXV7SIGMTTDKYNXTKR4");
	}

	@Override
	public void onStop() {
		super.onStop();
		// FlurryAgent.onEndSession(this);
	}

	public void onCreate(Bundle savedInstanceState) {
//		setTheme(com.actionbarsherlock.R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
	}
}
