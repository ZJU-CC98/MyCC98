package tk.djcrazy.MyCC98.dialog;


import tk.djcrazy.MyCC98.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;


public class MoreEmotChooseDialog extends Dialog{
    WebView webView;
    Context context;
    Button okButton;
    FaceExpressionChooseListener listener;
    public MoreEmotChooseDialog(Context context, FaceExpressionChooseListener listener) {
        super(context);
        this.listener = listener;
        this.context = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_emot_choose);
        setTitle("请点击图标添加到内容");
        webView = (WebView) findViewById(R.id.face_expression_web_view);
        okButton = (Button) findViewById(R.id.face_expression_ok_button);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//		webView.setVisibility(View.INVISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_change);
		webView.startAnimation(animation);
        webView.loadUrl("file:///android_asset/face_expression.html");
        okButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	listener.onOkClick();
                dismiss();
            }
        });
        webView.addJavascriptInterface(new Object() {
            @SuppressWarnings("unused")
            public void addEmotCode(final String emot) {
                listener.onFaceExpressionClick(emot);
            }
        }, "faceChoose");
    }
    public interface FaceExpressionChooseListener {
        public void onOkClick();
                                                    
        public void onFaceExpressionClick(String faceExpression);
    }

}
