package tk.djcrazy.MyCC98.dialog;

import tk.djcrazy.MyCC98.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class SendFeedBackDialog extends Dialog implements OnClickListener {
	private Button okButton;
	private Button cancelButton;
	private EditText userEmailText;
 	private EditText feedBackText;
 
	ArrayAdapter<String> adapter;
	SendFeedBackListener listener;

	public SendFeedBackDialog(Context context, SendFeedBackListener listener) {
		super(context);
		this.listener = listener;
 	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Test", "Dialog created");
		setContentView(R.layout.send_feed_back);
		
		okButton = (Button) findViewById(R.id.feed_back_okButton);
		cancelButton = (Button) findViewById(R.id.feed_back_cancelbutton);
		userEmailText = (EditText) findViewById(R.id.feed_back_user_email);
 		feedBackText = (EditText) findViewById(R.id.feed_back_content);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

		setTitle(R.string.send_feedback);
	}

	public interface SendFeedBackListener {
		public void onOkClick(String userEmail,
				String content);

		public void onCancelClick();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.feed_back_okButton:
			dismiss();

			listener.onOkClick(userEmailText.getText().toString(), 
					feedBackText.getText().toString());
			break;
		case R.id.feed_back_cancelbutton:
			cancel();
		default:
			break;
		}

	}

}