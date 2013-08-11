package tk.djcrazy.MyCC98.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.inject.Inject;

import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.LoginActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.data.LoginType;

/**
 * Created by Ding on 13-8-11.
 */
public class AuthDialogFragment extends RoboDialogFragment implements View.OnClickListener {

    @InjectView(R.id.okButton)
    private Button okButton;
    @InjectView(R.id.cancelbutton)
    private Button cancelButton;
    @InjectView(R.id.auth_host)
    private EditText authHost;
    @InjectView(R.id.auth_username)
    private EditText userNameText;
    @InjectView(R.id.auth_password)
    private EditText passwordText;
    @InjectView(R.id.auth_remember_pwd)
    private CheckBox rememberPassword;

    private static final String AUTHINFO = "AUTHINFO";
     private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String HOST = "HOST";
    private static final String REMEMBERPWD = "REMEMBERPWD";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return  inflater.inflate(R.layout.dialog_auth,container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        SharedPreferences setting = getActivity().getSharedPreferences(AUTHINFO, Context.MODE_PRIVATE);
        if (setting.getBoolean(REMEMBERPWD, false)) {
            userNameText.setText(setting.getString(USERNAME, ""));
            passwordText.setText(setting.getString(PASSWORD, ""));
            authHost.setText(setting.getString(HOST, ""));
            rememberPassword.setChecked(true);
        }
        getDialog().setTitle("设置代理连接");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okButton:
                String userName = userNameText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();
                String proxyHost = authHost.getText().toString().trim();
                if (!proxyHost.startsWith("http://")) proxyHost = "http://"+proxyHost;
                if (!proxyHost.endsWith("/")) proxyHost = proxyHost+"/";
                getActivity().getSharedPreferences(AUTHINFO, 0).edit().putString(USERNAME, userName)
                        .putString(PASSWORD, password).putString(HOST, proxyHost)
                        .putBoolean(REMEMBERPWD, rememberPassword.isChecked())
                        .commit();
                ((LoginActivity)getActivity()).onOkClick(userName, password, proxyHost, LoginType.USER_DEFINED);
                dismiss();
                break;
            case R.id.cancelbutton:
                dismiss();
                ((LoginActivity)getActivity()).onCancelClick();
            default:
                break;
        }

    }
}
