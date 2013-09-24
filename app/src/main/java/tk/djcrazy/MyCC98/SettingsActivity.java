/**
 * 
 */
package tk.djcrazy.MyCC98;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.baidu.mobstat.StatService;
import me.imid.swipebacklayout.lib.app.SwipeBackPreferenceActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tk.djcrazy.MyCC98.helper.ThemeHelper;

/**
 * @author zsy
 * 
 */
public class SettingsActivity extends SwipeBackPreferenceActivity {
	public static final String SETTINGS = "settings";
	public static final String SHOW_TAIL = "show_tail";
	public static final String ENABLE_CACHE = "enable_cache";
	public static final String SHOW_USER_AVATAR = "show_user_avatar";
	public static final String USE_CUSTOM_TAIL = "use_custom_tail";
	public static final String CUSTOM_TAIL_CONTENT = "custom_tail_content";
	public static final String SHOW_IMAGE = "enable_load_image";
	public static final String SHOW_TOP = "enable_show_top";
	public static final String THEME = "post_theme";
	public static final String THEME_DEFAULT = "default";

	private CheckBoxPreference showTailPreference;
	private CheckBoxPreference useCustomPreference;
	private EditTextPreference customTailContentPreference;
	private ListPreference themeListPreference;

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

	@SuppressWarnings("deprecation")
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
		themeListPreference = (ListPreference) findPreference(THEME);
		showTailPreference
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						boolean value = (Boolean) newValue;
						if (!value) {
							useCustomPreference.setEnabled(false);
							customTailContentPreference.setEnabled(false);
						} else {
							useCustomPreference.setEnabled(true);
							if (useCustomPreference.isChecked()) {
								customTailContentPreference.setEnabled(true);
							} else {
								customTailContentPreference.setEnabled(false);
							}
						}
						return true;
					}
				});

		useCustomPreference
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						boolean value = (Boolean) newValue;
						if (value) {
							customTailContentPreference.setEnabled(true);
						} else {
							customTailContentPreference.setEnabled(false);
						}
						return true;
					}
				});
		
		CharSequence[] themes = getThemes().toArray(new String[0]);
		themeListPreference.setEntries(themes);
		themeListPreference.setEntryValues(themes);
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

	private List<String> getThemes() {
		String themeRoot = ThemeHelper.getThemeRootPath();
		File themedir = new File(themeRoot);
		List<String> themes = new ArrayList<String>();
		themes.add(THEME_DEFAULT);
		if (themedir.exists() && themedir.isDirectory()) {
			themes.addAll(Arrays.asList(themedir.list()));
		}
		return themes;
	}

	
}
