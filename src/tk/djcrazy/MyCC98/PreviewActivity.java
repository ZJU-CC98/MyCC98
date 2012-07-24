package tk.djcrazy.MyCC98;

import tk.djcrazy.MyCC98.helper.HtmlGenHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.flurry.android.FlurryAgent;

public class PreviewActivity extends Activity {
	
	public static final String CONTENT = "content";
	WebView webView;
	String content = "";
	String tagedContent = "";
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

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// Get Html header
		Intent intent = getIntent();
		content=intent.getStringExtra("content");

		setContentView(R.layout.preview);
		setTitle(R.string.pm_reply);

		findViews();
		
		setViews();
		
		loadPreview();
	}
	
	// handle the message
		private Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					webView.loadDataWithBaseURL(null, tagedContent, "text/html",
							"utf-8", null);
					Log.d("WebView", tagedContent);
					break;
				default:
					break;
				}
			}
		};

	
	private void loadPreview(){
		new Thread() {
			@Override
			public void run(){
				tagedContent = HtmlGenHelper.PAGE_OPEN
						+ "<br><span id=\"ubbcode1\">"
						+ content.replaceAll("\n", "<BR>")
						+ "</span><script type=\"text/javascript\">searchubb('ubbcode1',1,'tablebody2');</script>"
						+ HtmlGenHelper.PAGE_CLOSE;
				System.err.println(content);
				handler.sendEmptyMessage(0);
			}
			
		}.start();
	}
	
	private void findViews(){
		webView = (WebView) findViewById(R.id.preview);
	}
	
	private void setViews() {
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(this, "PmReply");
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
	}
}
