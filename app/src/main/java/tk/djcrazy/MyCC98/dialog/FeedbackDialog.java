package tk.djcrazy.MyCC98.dialog;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.util.SysInfo;
import tk.djcrazy.libCC98.data.Issue;
import tk.djcrazy.libCC98.util.IssueSender;
import tk.djcrazy.libCC98.util.RequestResultListener;

import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.TextView;
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
    private static final String PROGRESS_DIALOG_TITLE = "正在将以下内容发送至 GitHub";

    private static final String TAG = "FeedbackDialog";

    @Inject
    private IssueSender issueSender;

    private EditText issueTitle;
    private EditText issueBody;
    private Button sendIssue;
    private Button cancelIssue;
    private ProgressDialog progressDialog;

    public FeedbackDialog() {

    }

    private void finish() {
        dismiss();
    }

    private String getIssueBody() {
        return issueBody.getText().toString() + '\n' + SysInfo.getSystemSummary(getActivity());
    }

    private void setViews() {
        getDialog().setTitle(TITLE);
        sendIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = issueTitle.getText().toString();
                if (title.isEmpty() || title.trim().isEmpty()) {
                    Toast.makeText(getActivity(), TITLE_REQUIRED, Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog = new ProgressDialog(getActivity());
                    String issueBodyString = getIssueBody();
                    progressDialog.setTitle(PROGRESS_DIALOG_TITLE);
                    progressDialog.setMessage(issueBodyString);
                    progressDialog.show();
                    issueSender.send(new Issue(title, issueBodyString), getActivity().getClass(),
                            new RequestResultListener<String>() {
                                @Override
                                public void onRequestComplete(String result) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), ISSUE_SEND_SUCCESSFUL, Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onRequestError(String msg) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), ISSUE_SEND_FAILED + "\n" + msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        cancelIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_feedback, container);
        issueTitle = (EditText) view.findViewById(R.id.issue_title);
        issueBody = (EditText) view.findViewById(R.id.issue_body);
        sendIssue = (Button) view.findViewById(R.id.send_issue);
        cancelIssue = (Button) view.findViewById(R.id.cancel_issue);
        setViews();
        return view;
    }
}
