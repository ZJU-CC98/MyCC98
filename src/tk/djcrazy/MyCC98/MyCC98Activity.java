/**
 * Enterance of program
 * The login activity
 * 
 */

package tk.djcrazy.MyCC98;

import com.flurry.android.FlurryAgent;

import tk.djcrazy.MyCC98.helper.TextHelper;
import tk.djcrazy.MyCC98.task.GenericTask;
import tk.djcrazy.MyCC98.task.TaskAdapter;
import tk.djcrazy.MyCC98.task.TaskListener;
import tk.djcrazy.MyCC98.task.TaskParams;
import tk.djcrazy.MyCC98.task.TaskResult;
import tk.djcrazy.libCC98.CC98Client;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MyCC98Activity extends Activity implements OnClickListener {

	// Please always use the strings here, for it is easier to modify in the
	// future
	public static final String TAG = "MyCC98";

	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";
	public static final String USERINFO = "USERINFO";
	public static final String AUTOLOGIN = "AUTOLOGIN";
	public static final String REMEMBERPWD = "REMEMBERPWD";

	private EditText mUsernameEdit;

	private EditText mPasswordEdit;

	private String mUsername;

	private String mPassword;

	private Button mSigninButton;

	private Button mWanderButton;

	private CheckBox rememberPassword;

	private CheckBox autoLoginBox;

	// Tasks.
	private GenericTask mLoginTask;

	Dialog dialog;

	Intent intent = new Intent();

	private TaskListener mLoginTaskListener = new TaskAdapter() {

		@Override
		public void onPreExecute(GenericTask task) {
			onLoginBegin();
		}

		@Override
		public void onProgressUpdate(GenericTask task, Object param) {
			updateProgress((String) param);
		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.OK) {
				onLoginSuccess();
			} else {
				onLoginFailure("failed");
			}
		}

		@Override
		public String getName() {

			return "Login";
		}
	};
	@Override
	public void onStart()
	{
	   super.onStart();
	   FlurryAgent.onStartSession(this, "5EXV7SIGMTTDKYNXTKR4");
	}
	
	@Override
	public void onStop()
	{
	   super.onStop();
	   FlurryAgent.onEndSession(this);
	}

	/**
	 * Called when the activity is first created.
	 **/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Settings with no title
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Settings with full screen
		/*
		 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
		setContentView(R.layout.login_dj);
		findViews();
		SharedPreferences setting = getSharedPreferences(USERINFO, 0);
		if (setting.getBoolean(REMEMBERPWD, false)) {
			mUsernameEdit.setText(setting.getString(USERNAME, ""));
			mPasswordEdit.setText(setting.getString(PASSWORD, ""));
			rememberPassword.setChecked(true);
			if (setting.getBoolean(AUTOLOGIN, false)) {
				autoLoginBox.setChecked(true);
				doLogin();
			}
		}
//		intent.setClass(this, PersonalBoardListActivity.class);
	}

	/**
	 * 
	 */
	private void findViews() {
		mUsernameEdit = (EditText) findViewById(R.id.username);
		mPasswordEdit = (EditText) findViewById(R.id.password);

		mSigninButton = (Button) findViewById(R.id.login);
		mSigninButton.setOnClickListener(this);
//		mWanderButton = (Button) findViewById(R.id.wander);
		mWanderButton.setOnClickListener(this);
		rememberPassword = (CheckBox) findViewById(R.id.remember_password);
		autoLoginBox = (CheckBox) findViewById(R.id.auto_login);
	}

	/**
	 * Click event driven, callback function
	 * 
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			doLogin();
			break;
		default:
			break;
		}
	}

	/**
     * 
     */
	private void doLogin() {
		mUsername = mUsernameEdit.getText().toString();
		mPassword = mPasswordEdit.getText().toString();
		if (mLoginTask != null
				&& mLoginTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			if (!TextHelper.isEmpty(mUsername) & !TextHelper.isEmpty(mPassword)) {
				mLoginTask = new LoginTask();
				mLoginTask.setListener(mLoginTaskListener);

				TaskParams params = new TaskParams();
				params.put("username", mUsername);
				params.put("password", mPassword);
				mLoginTask.execute(params);
			} else {
				updateProgress("aaaaaaaa");
			}
		}
	}

	// UI helpers.

	private void updateProgress(String progress) {
	}

	private void onLoginBegin() {
		dialog = ProgressDialog.show(MyCC98Activity.this, "",
				"Begin Logining...", true);
		dialog.setCancelable(true);
	}

	private void onLoginSuccess() {
		dialog.dismiss();
		CC98Client.setUserName(mUsername);
		CC98Client.setPassword(mPassword);
		Editor editor = getSharedPreferences(USERINFO, 0).edit();
		// save info
		if (autoLoginBox.isChecked()) {
			editor.putBoolean(AUTOLOGIN, true);
		} else {
			editor.putBoolean(AUTOLOGIN, false);
		}
		if (rememberPassword.isChecked()) {

			editor.putString(USERNAME, mUsername)
					.putString(PASSWORD, mPassword)
					.putBoolean(REMEMBERPWD, true);

		} else {
			editor.putString(USERNAME, "").putString(PASSWORD, "")
					.putBoolean(REMEMBERPWD, false);
		}
		editor.commit();

		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(MyCC98Activity.this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		updateProgress("");
		mUsernameEdit.setText("");
		mPasswordEdit.setText("");

		Toast.makeText(this, R.string.msg_login_ok, Toast.LENGTH_SHORT).show();
		startActivity(intent);
		finish();
	}

	private void onLoginFailure(String reason) {
		Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	private class LoginTask extends GenericTask {

		// private String msg = getString(R.string.login_status_failure);

 
		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			TaskParams param = params[0];
			// publishProgress(getString(R.string.login_status_logging_in) +
			// "...");

			try {
				String username = param.getString("username");
				String password = param.getString("password");
				CC98Client.doLogin(username, password);
				
				return TaskResult.OK;
			} catch (Exception e) {

				return TaskResult.FAILED;
			}
		}
	}

}
