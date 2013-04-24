package tk.djcrazy.MyCC98.dialog;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.data.LoginType;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AuthDialog extends Dialog implements OnClickListener {
	private Button okButton;
	private Button cancelButton;
	private EditText userNameText;
	private EditText passwordText;
	private CheckBox rememberPassword;
	private RadioGroup mLoginType;

	private SharedPreferences setting;
	private static final String USERNAME = "USERNAME";
	private static final String PASSWORD = "PASSWORD";
 	private static final String REMEMBERPWD = "REMEMBERPWD";

 	private MyAuthDialogListener listener;

	public AuthDialog(Context context, MyAuthDialogListener listener,
			SharedPreferences setting) {
		super(context);
		this.listener = listener;
		this.setting = setting;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_auth);
		okButton = (Button) findViewById(R.id.okButton);
		cancelButton = (Button) findViewById(R.id.cancelbutton);
		userNameText = (EditText) findViewById(R.id.auth_username);
		passwordText = (EditText) findViewById(R.id.auth_password);
		rememberPassword = (CheckBox) findViewById(R.id.auth_remember_pwd);
		mLoginType = (RadioGroup) findViewById(R.id.login_type);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		if (setting.getBoolean(REMEMBERPWD, false)) {
			userNameText.setText(setting.getString(USERNAME, ""));
			passwordText.setText(setting.getString(PASSWORD, ""));
 			rememberPassword.setChecked(true);
		}
		setTitle("设置代理连接");
	}

	public interface MyAuthDialogListener {
		public void onOkClick(String authUserName, String authPassword,
				Boolean rememberPwd, LoginType loginType);

		public void onCancelClick();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.okButton:
			LoginType type = ((RadioButton)mLoginType.getChildAt(0)).isChecked()?LoginType.RVPN:LoginType.USER_DEFINED;
			listener.onOkClick(userNameText.getText().toString(), passwordText
					.getText().toString(), rememberPassword.isChecked(), type);
			dismiss();
			break;
		case R.id.cancelbutton:
			dismiss();
			listener.onCancelClick();
		default:
			break;
		}

	}

}