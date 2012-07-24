package tk.djcrazy.MyCC98.dialog;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.dialog.MoreEmotChooseDialog.FaceExpressionChooseListener;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;

public class AboutDialog extends Dialog {
	WebView webView;
	Button okButton;

	public AboutDialog(Context context) {
		super(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_dialog);
		setTitle("About");
		webView = (WebView) findViewById(R.id.about_web_view);
		okButton = (Button) findViewById(R.id.about_ok);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		Animation animation = AnimationUtils.loadAnimation(getContext(),
				R.anim.alpha_change);
		webView.startAnimation(animation);
		webView.loadUrl("file:///android_asset/about.html");
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

}
