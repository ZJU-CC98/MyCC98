package tk.djcrazy.MyCC98;

import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.MyCC98.util.UrlUtils;
import android.os.Bundle;

public class ExportedProxyActivity extends BaseActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String url = getIntent().getDataString();	
		if (UrlUtils.isPostContentLink(url)) {
			startActivity(UrlUtils.getPostContentIntent(url));
			finish();
		} else {
			ToastUtils.show(this, "不合法的链接");
			finish();
		}
	}
}
