package tk.djcrazy.MyCC98.dialog;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.data.Issue;
import tk.djcrazy.libCC98.util.IssueSender;
import tk.djcrazy.libCC98.util.RequestResultListener;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockDialogFragment;
import com.google.inject.Inject;

/**
 * Created by zsy on 8/13/13.
 */
public class FeedbackDialog extends RoboSherlockDialogFragment {
    private static final String TITLE = "发送反馈";
    private static final String TITLE_REQUIRED = "别忘了填写标题哦";
    private static final String ISSUE_SEND_SUCCESSFUL = "非常感谢，我们会尽快处理您的反馈";
    private static final String ISSUE_SEND_FAILED = "啊噢，反馈发送失败";

    private static final String TAG = "FeedbackDialog";

    @Inject
    private IssueSender issueSender;

    private EditText issue_title;
    private EditText issue_body;
    private Button send_issue;
    private Button cancel_issue;

    public FeedbackDialog() {

    }

    private void finish() {
        dismiss();
    }

    private void setViews() {
        getDialog().setTitle(TITLE);
        send_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = issue_title.getText().toString();
                if (title.isEmpty() || title.trim().isEmpty()) {
                    Toast.makeText(getActivity(), TITLE_REQUIRED, Toast.LENGTH_SHORT).show();
                } else {
                    issueSender.send(new Issue(title, issue_body.getText().toString()), getActivity().getClass(),
                            new RequestResultListener<String>() {
                                @Override
                                public void onRequestComplete(String result) {
                                    Log.d(TAG, result);
                                    Toast.makeText(getActivity(), ISSUE_SEND_SUCCESSFUL, Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onRequestError(String msg) {
                                    Toast.makeText(getActivity(), ISSUE_SEND_FAILED + "\n" + msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        cancel_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_feedback, container);
        issue_title = (EditText) view.findViewById(R.id.issue_title);
        issue_body = (EditText) view.findViewById(R.id.issue_body);
        send_issue = (Button) view.findViewById(R.id.send_issue);
        cancel_issue = (Button) view.findViewById(R.id.cancel_issue);
        setViews();

        return view;
    }
}
