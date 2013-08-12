package tk.djcrazy.MyCC98;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.helper.HtmlGenHelper;
import tk.djcrazy.MyCC98.util.Intents;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.MyCC98.util.UrlUtils;
import tk.djcrazy.libCC98.NewCC98Service;
import tk.djcrazy.libCC98.exception.ParseContentException;
import tk.djcrazy.libCC98.util.RequestResultListener;

/**
 * @author zsy
 */
@ContentView(R.layout.activity_pm_reply)
public class
        PmViewActivity extends BaseFragmentActivity implements RequestResultListener<String> {
    private static final int MENU_REPLY_ID = 9237465;
    private static String TAG = "PmReply";
    @InjectView(R.id.pm_reply_view)
    private WebView webView;
    @InjectExtra(Intents.EXTRA_PM_CONTENT)
    private String readTopic;
    @InjectExtra(Intents.EXTRA_PM_SENDER)
    private String sender;
    @InjectExtra(Intents.EXTRA_PM_SEND_TIME)
    private String sendTime;
    @InjectExtra(value = Intents.EXTRA_PM_ID, optional = true)
    private int pmId = -1;
    private String pmContent="";

    @Inject
    private NewCC98Service service;
    private HtmlGenHelper helper = new HtmlGenHelper();


    public static Intent createIntent(String content, String sender,
                                      String sendTime, int pmId) {
        Intent intent = new Intents.Builder("pm.VIEW").pmContent(content)
                .pmSender(sender).pmSendTime(sendTime).pmId(pmId).toIntent();
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("查看短消息");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setLogo(new BitmapDrawable(getResources(), service.getCurrentUserAvatar()));
        setViews();
        preparePage(pmId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(android.view.Menu.NONE, MENU_REPLY_ID, 1, "回复")
                .setIcon(R.drawable.sure_btn)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case MENU_REPLY_ID:
                doReply();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doReply() {
        StringBuilder tmp = new StringBuilder();
        tmp.append("[quote][b]以下是引用").append(sender).append("在[i]")
                .append(sendTime).append("[/i]时发送的短信：[/b]\n")
                .append(pmContent.replaceAll("(<BR>|<br>)", "\n"))
                .append("[/quote]");
        Intents.Builder builder = new Intents.Builder(this, EditActivity.class);
        startActivity(builder.requestType(EditActivity.REQUEST_PM)
                .pmToUser(sender).pmTitle(readTopic).pmContent(tmp.toString())
                .toIntent());
    }


    @Override
    protected void onStop() {
        super.onStop();
        service.cancelRequest(this.getClass());
    }

    /**
     *
     */
    private void setViews() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "shouldOverrideUrlLoading:" + url);
                if (!url.startsWith("http")) {
                    url = service.getDomain() + url;
                }
                if (url.endsWith(".jpg") | url.endsWith(".png")
                        | url.endsWith(".bmp")) {
                    startActivity(PhotoViewActivity.createIntent(url));
                } else if (UrlUtils.isPostContentLink(url)) {
                    startActivity(UrlUtils.getPostContentIntent(url));
                } else {
                    Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(it);
                }
                return true;
            }
        });
        webView.addJavascriptInterface(this, "PmReply");
        webView.setBackgroundColor(Color.parseColor("#f0f0f0"));
    }

    private void preparePage(final int pmId) {
        service.submitGetMsgContent(this.getClass(), pmId, this);
     }

    @Override
    public void onRequestComplete(String result) {
        pmContent = result;
        StringBuilder builder = new StringBuilder(1000);
        HtmlGenHelper.addPostInfo(builder, readTopic,
                "", sender, "", 1, sendTime, -1);
        builder.append(
                "<div class=\"post-content\"><span id=\"ubbcode\">")
                .append("<div class=\"post-content\"><span id=\"ubbcode\">")
                .append(pmContent)
                .append("</span><script>searchubb('ubbcode',1,'tablebody2');</script></div>");
        webView.loadDataWithBaseURL(null, helper.PAGE_OPEN + builder.toString()
                + helper.PAGE_CLOSE, "text/html",
                "utf-8", null);
    }

    @Override
    public void onRequestError(String msg) {
        ToastUtils.show(this, msg);
    }
}
