package tk.djcrazy.MyCC98.dialog;

import tk.djcrazy.MyCC98.R;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class AuthDialog extends Dialog implements OnClickListener {
	private Button okButton;
	private Button cancelButton;
	private EditText userNameText;
	private EditText passwordText;
	private CheckBox rememberPassword;

	private SharedPreferences setting;
	private static final String USERNAME = "USERNAME";
	private static final String PASSWORD = "PASSWORD";
	private static final String AUTHINFO = "AUTHINFO";
	private static final String AUTOLOGIN = "AUTOLOGIN";
	private static final String REMEMBERPWD = "REMEMBERPWD";

	ArrayAdapter<String> adapter;
	MyAuthDialogListener listener;

	public AuthDialog(Context context, MyAuthDialogListener listener,
			SharedPreferences setting) {
		super(context);
		this.listener = listener;
		this.setting = setting;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Test", "Dialog created");
		setContentView(R.layout.auth_dialog);
		okButton = (Button) findViewById(R.id.okButton);
		cancelButton = (Button) findViewById(R.id.cancelbutton);
		userNameText = (EditText) findViewById(R.id.auth_username);
		passwordText = (EditText) findViewById(R.id.auth_password);
		rememberPassword = (CheckBox) findViewById(R.id.auth_remember_pwd);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

		if (setting.getBoolean(REMEMBERPWD, false)) {
			userNameText.setText(setting.getString(USERNAME, ""));
			passwordText.setText(setting.getString(PASSWORD, ""));
			Log.d("authlog", setting.getString(USERNAME, ""));
			Log.d("authlog", setting.getString(PASSWORD, ""));
			rememberPassword.setChecked(true);
		}

		setTitle("设置代理连接");
	}

	public interface MyAuthDialogListener {
		public void onOkClick(String authUserName, String authPassword,
				Boolean rememberPwd);

		public void onCancelClick();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.okButton:
			listener.onOkClick(userNameText.getText().toString(), passwordText
					.getText().toString(), rememberPassword.isChecked());
			dismiss();
			break;
		case R.id.cancelbutton:
			listener.onCancelClick();
		default:
			break;
		}

	}

}