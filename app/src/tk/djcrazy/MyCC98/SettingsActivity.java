/**
 * 
 */
package tk.djcrazy.MyCC98;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.baidu.mobstat.StatService;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockPreferenceActivity;

/**
 * @author zsy
 *
 */
public class SettingsActivity extends RoboSherlockPreferenceActivity {
	public static final String SETTINGS = "settings";
	public static final String SHOW_TAIL = "show_tail";
	public static final String ENABLE_CACHE = "enable_cache";
	public static final String SHOW_USER_AVATAR = "show_user_avatar";
	public static final String USE_CUSTOM_TAIL = "use_custom_tail";
	public static final String CUSTOM_TAIL_CONTENT = "custom_tail_content";
	public static final String SHOW_IMAGE = "enable_load_image";
	
 	private CheckBoxPreference showTailPreference;
 	private CheckBoxPreference useCustomPreference;
 	private EditTextPreference customTailContentPreference;
 	
 	
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
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setLogo(R.drawable.setting_btn);
		actionBar.setTitle("设置");
		actionBar.setDisplayHomeAsUpEnabled(true);
 		addPreferencesFromResource(R.xml.settings);
 		showTailPreference = (CheckBoxPreference) findPreference(SHOW_TAIL);
 		useCustomPreference = (CheckBoxPreference) findPreference(USE_CUSTOM_TAIL);
 		customTailContentPreference = (EditTextPreference) findPreference(CUSTOM_TAIL_CONTENT);
 		showTailPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean value = (Boolean) newValue;
				if (!value) {
					useCustomPreference.setEnabled(false);
					customTailContentPreference.setEnabled(false);
				} else {
					useCustomPreference.setEnabled(true);
					if (useCustomPreference.isChecked()) {
						customTailContentPreference.setEnabled(true);
					}else {
						customTailContentPreference.setEnabled(false);
					}
				}
				return true;
			}
		});
 		
 		useCustomPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean value = (Boolean) newValue;
				if (value) {
					customTailContentPreference.setEnabled(true);
				} else {
					customTailContentPreference.setEnabled(false);
				}
				return true;
			}
		});
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
