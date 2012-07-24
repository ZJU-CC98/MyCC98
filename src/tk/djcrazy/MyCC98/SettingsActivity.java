/**
 * 
 */
package tk.djcrazy.MyCC98;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * @author zsy
 *
 */
public class SettingsActivity extends PreferenceActivity {
	public static final String SETTINGS = "settings";
	public static final String SETTING_USE_TAIL = "settings_tail";
	public static final String SETTING_USE_DARK = "settings_dark";
	
//	private CheckBox cbTail;
//	private CheckBox cbDark;
//	private View vSave;
	private Spinner spinner;
	
	private ArrayAdapter<CharSequence> ids;
	
	public static boolean addTail = true;
	public static boolean useDark = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.settings);
//		findViews();
//		setListeners();
//		initViews();
		addPreferencesFromResource(R.xml.settings);
	}
	
	
	
//	private void initViews() {
//		cbTail.setChecked(addTail);
//		cbDark.setChecked(useDark);
//	}


//	private void saveSettings() {
//		addTail = cbTail.isChecked();
//		useDark = cbDark.isChecked();
//		Editor editor = getSharedPreferences(SETTINGS, 0).edit();
//		editor.putBoolean(SETTING_USE_TAIL, addTail);
//		editor.putBoolean(SETTING_USE_DARK, useDark);
//		editor.commit();
//	}
	
//	private void setListeners() {
//		vSave.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				saveSettings();
//				finish();
//			}
//			
//		});
//	}

//	private void findViews() {
//		cbTail = (CheckBox) findViewById(R.id.settings_cb_tail);
//		cbDark = (CheckBox) findViewById(R.id.settings_cb_welcome);
//		vSave = findViewById(R.id.settings_btn_save);
//		spinner = (Spinner) findViewById(R.id.settings_sp_id);
//		ids = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
//	}
}
