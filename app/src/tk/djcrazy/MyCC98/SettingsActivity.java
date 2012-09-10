/**
 * 
 */
package tk.djcrazy.MyCC98;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.flurry.android.FlurryAgent;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockPreferenceActivity;

/**
 * @author zsy
 *
 */
public class SettingsActivity extends RoboSherlockPreferenceActivity {
	public static final String SETTINGS = "settings";
	public static final String SETTING_USE_TAIL = "settings_tail";
	public static final String SETTING_USE_DARK = "settings_dark";
	
 	private Spinner spinner;
	
	private ArrayAdapter<CharSequence> ids;
	
	public static boolean addTail = true;
	public static boolean useDark = false;
	
//	@Override
//	public void onStart() {
//		super.onStart();
//		FlurryAgent.onStartSession(this, "5EXV7SIGMTTDKYNXTKR4");
//	}
//
//	@Override
//	public void onStop() {
//		super.onStop();
//		FlurryAgent.onEndSession(this);
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(com.actionbarsherlock.R.style.Sherlock___Theme);
		super.onCreate(savedInstanceState);
 		addPreferencesFromResource(R.xml.settings);
	}
	
 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(R.anim.backward_activity_move_in,
					R.anim.backward_activity_move_out);
			return true;
		}
		return false;
	}


}
