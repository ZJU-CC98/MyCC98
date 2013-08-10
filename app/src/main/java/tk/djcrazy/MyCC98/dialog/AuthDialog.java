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

    private static final String AUTHINFO = "AUTHINFO";
	private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String HOST = "HOST";
 	private static final String REMEMBERPWD = "REMEMBERPWD";

 	private MyAuthDialogListener listener;

	public AuthDialog(Context context, MyAuthDialogListener listener) {
		super(context);
		this.listener = listener;
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
        SharedPreferences setting = getContext().getSharedPreferences(AUTHINFO,Context.MODE_PRIVATE);
		if (setting.getBoolean(REMEMBERPWD, false)) {
			userNameText.setText(setting.getString(USERNAME, ""));
			passwordText.setText(setting.getString(PASSWORD, ""));
 			rememberPassword.setChecked(true);
		}
		setTitle("设置代理连接");
	}

	public interface MyAuthDialogListener {
		public void onOkClick(String authUserName, String authPassword,
				String host, LoginType loginType);

		public void onCancelClick();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.okButton:
            String userName = userNameText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();
            String proxyHost = "http://hz.cc98.lifetoy.org/";
            getContext().getSharedPreferences(AUTHINFO, 0).edit().putString(USERNAME, userName)
                    .putString(PASSWORD, password).putString(HOST, proxyHost)
                    .putBoolean(REMEMBERPWD, rememberPassword.isChecked())
                    .commit();
 			listener.onOkClick(userName, password, proxyHost, LoginType.USER_DEFINED);
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