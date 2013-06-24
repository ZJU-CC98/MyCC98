package tk.djcrazy.MyCC98;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.sax.TemplatesHandler;

import org.apache.commons.lang3.StringUtils;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.helper.ThemeHelper;
import tk.djcrazy.MyCC98.task.ProgressRoboAsyncTask;
import tk.djcrazy.MyCC98.template.PostContentFactory;
import tk.djcrazy.MyCC98.template.PostContentTemplateFactory;
import tk.djcrazy.MyCC98.util.DisplayUtil;
import tk.djcrazy.MyCC98.util.Intents;
import tk.djcrazy.MyCC98.util.Intents.Builder;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.MyCC98.view.ObservableWebView;
import tk.djcrazy.MyCC98.view.ObservableWebView.OnScrollChangedCallback;
import tk.djcrazy.libCC98.CachedCC98Service;
import tk.djcrazy.libCC98.SerializableCache;
import tk.djcrazy.libCC98.data.LoginType;
import tk.djcrazy.libCC98.data.PostContentEntity;
import tk.djcrazy.libCC98.data.UserData;
import tk.djcrazy.libCC98.util.DateFormatUtil;
import tk.djcrazy.libCC98.util.SerializableCacheUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import ch.boye.httpclientandroidlib.cookie.Cookie;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;

@ContentView(R.layout.activity_post_contents)
public class PostContentsJSActivity extends BaseActivity implements
		OnScrollChangedCallback {
	private static final String TAG = "PostContentsJSActivity";
	private static final String JS_INTERFACE = "PostContentsJSActivity";

	public static final int LAST_PAGE = 32767;
	// WebView cache max size, in bytes
	private static final long CACHE_SIZE = 32 * 1024 * 1024;

	@InjectView(R.id.post_contents)
	private ObservableWebView webView;

	@InjectExtra(value = Intents.EXTRA_BOARD_NAME, optional = true)
	private String boardName = "";
	@InjectExtra(Intents.EXTRA_POST_ID)
	private String postId;
	@InjectExtra(Intents.EXTRA_BOARD_ID)
	private String boardId;
	@InjectExtra(value = Intents.EXTRA_POST_NAME, optional = true)
	private String postName = "";
	@InjectExtra(value = Intents.EXTRA_PAGE_NUMBER, optional = true)
	private int currPageNum = 1;

	private int totalPageNum = 1;

	private List<PostContentEntity> mContentEntities;
	@Inject
	private CachedCC98Service service;

	private Menu mOptionsMenu;
	private GestureDetector gestureDetector;
	private boolean isRefreshing = false;

	public static Intent createIntent(String boardId, String postId,
			int pageNumber, boolean forceRefresh) {
		return new Builder("post_content.VIEW").boardId(boardId).postId(postId)
				.pageNumber(pageNumber).forceRefresh(forceRefresh).toIntent();
	}

	public static Intent createIntent(String boardId, String postId) {
		return createIntent(boardId, postId, 1, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		configureActionBar();
		configureWebView();
		gestureDetector = new GestureDetector(this,
				new DefaultGestureDetector());
		webView.postDelayed(new Runnable() {
			@Override
			public void run() {
				new GetPostContentTask(PostContentsJSActivity.this, boardId,
						postId, currPageNum).execute();
			}
		}, 50);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		getSupportActionBar().show();
		postId = intent.getStringExtra(Intents.EXTRA_POST_ID);
		boardId = intent.getStringExtra(Intents.EXTRA_BOARD_ID);
		currPageNum = intent.getIntExtra(Intents.EXTRA_PAGE_NUMBER, 1);
		new GetPostContentTask(this, boardId, postId, currPageNum).execute();
	}

	public void onPause() {
		super.onPause();
		this.callHiddenWebViewMethod("onPause");
	}

	public void onResume() {
		super.onResume();
		this.callHiddenWebViewMethod("onResume");
	}

	private void callHiddenWebViewMethod(String name) {
		if (webView != null) {
			try {
				Method method = ObservableWebView.class.getMethod(name);
				method.invoke(webView);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				Log.e("No such method: " + name, e.getMessage());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				Log.e("Illegal Access: " + name, e.getMessage());
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				Log.e("Invocation Target Exception: " + name, e.getMessage());
			}
		}
	}

	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setLogo(new BitmapDrawable(service.getCurrentUserAvatar()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu optionMenu) {
		this.mOptionsMenu = optionMenu;
		getSupportMenuInflater().inflate(R.menu.menu_post_content, optionMenu);
		return super.onCreateOptionsMenu(optionMenu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = PostListActivity.createIntent(boardName, boardId);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 			startActivity(intent);
			break;
		case R.id.menu_jump:
			jumpDialog();
			break;
		case R.id.menu_next_page:
			nextPage();
			break;
		case R.id.menu_pre_page:
			prevPage();
			break;
		case R.id.menu_reply:
			reply();
			break;
		case R.id.refresh:
			refreshPage();
			break;
		case R.id.show_all_image:
			webView.loadUrl("javascript:showAllImages.fireEvent('click');");
			break;
 		default:
			break;
		}
		return false;
	}

	private void configureWebView() {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean enableCache = sharedPref.getBoolean(
				SettingsActivity.ENABLE_CACHE, true);
		boolean showImage = sharedPref.getBoolean(SettingsActivity.SHOW_IMAGE,
				true);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setPluginsEnabled(true);
		webSettings.setDefaultFontSize(14);
		webSettings.setLoadsImagesAutomatically(showImage);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		if (enableCache) {
			webSettings.setAppCacheMaxSize(CACHE_SIZE);
			webSettings.setAllowFileAccess(true);
			webView.getSettings().setDomStorageEnabled(true);
			webSettings.setAppCachePath(getApplicationContext().getCacheDir()
					.getPath());
			webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		}
		webSettings.setAppCacheEnabled(enableCache);
		webView.setOnScrollChangedCallback(this);
		webView.addJavascriptInterface(this, JS_INTERFACE);
		webView.setWebChromeClient(new FullscreenableChromeClient(this));
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i(TAG, "shouldOverrideUrlLoading:" + url);
				if (!url.startsWith("http")) {
					url = service.getDomain() + url;
				}
				if (url.endsWith(".jpg") | url.endsWith(".png")
						| url.endsWith(".bmp")) {
					startActivity(PhotoViewActivity.createIntent(url));
					return true;
				} else {
					Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(it);
					return true;
				}
			}

			@Override
			public void onReceivedHttpAuthRequest(WebView view,
					HttpAuthHandler handler, String host, String realm) {
				handler.proceed(service.getusersInfo().getCurrentUserData()
						.getProxyUserName(), service.getusersInfo()
						.getCurrentUserData().getProxyPassword());
			}
		});
		webView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});
	}

	public void jumpTo(int pageNum) {
		if (pageNum <= totalPageNum) {
			startActivity(PostContentsJSActivity.createIntent(boardId, postId,
					pageNum, false));
		}
	}

	public void prevPage() {
		if (currPageNum >= 2) {
			startActivity(PostContentsJSActivity.createIntent(boardId, postId,
					currPageNum - 1, false));
		} else {
			ToastUtils.show(this, "已经到第一页啦");
		}
	}

	public void refreshPage() {
		startActivity(PostContentsJSActivity.createIntent(boardId, postId,
				currPageNum, true));
	}

	public void nextPage() {
		if (currPageNum + 1 <= totalPageNum) {
			startActivity(PostContentsJSActivity.createIntent(boardId, postId,
					currPageNum + 1, false));
		} else {
			ToastUtils.show(this, "已经到最后一页啦");
		}
	}

	public void jumpDialog() {
		final EditText jumpEditText = new EditText(this);
		jumpEditText.requestFocus();
		jumpEditText.setFocusableInTouchMode(true);
		// set numeric touch pad
		jumpEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
		new AlertDialog.Builder(this)
				.setTitle(R.string.jump_dialog_title)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(jumpEditText)
				.setPositiveButton(R.string.jump_button,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								int jumpNum = 1;
								try {
									jumpNum = Integer.parseInt(jumpEditText
											.getText().toString());
									if (jumpNum <= 0 || jumpNum > totalPageNum) {
										Toast.makeText(
												PostContentsJSActivity.this,
												R.string.search_input_error,
												Toast.LENGTH_SHORT).show();
									} else {
										jumpTo(jumpNum);
									}
								} catch (NumberFormatException e) {
									Log.e(PostContentsJSActivity.TAG,
											e.toString());
									Toast.makeText(PostContentsJSActivity.this,
											R.string.search_input_error,
											Toast.LENGTH_SHORT).show();
								}

							}
						}).setNegativeButton(R.string.go_back, null).show();
	}

	public void reply() {
		Intents.Builder builder = new Intents.Builder(this, EditActivity.class);
		Intent intent = builder.requestType(EditActivity.REQUEST_REPLY)
				.postId(postId).postName(postName).boardId(boardId)
				.boardName(boardName).toIntent();
		startActivityForResult(intent, 1);
	}

	public void showContentDialog(final int index, int which) {
		final PostContentEntity item = mContentEntities.get(index);
		switch (which) {
		case 0: {
			// quote & reply
			String tmp = item.getPostContent().replaceAll("(<br>|<BR>)", "\n");
			quoteReply(item.getUserName(), DateFormatUtil.convertDateToString(
					item.getPostTime(), false), tmp, index, currPageNum);
		}
			break;
		case 1:
			// send pm
			sendPm(item.getUserName());
			break;
		case 2:
			AlertDialog.Builder builder = new AlertDialog.Builder(
					PostContentsJSActivity.this);
			builder.setTitle("提示");
			builder.setMessage(Html.fromHtml("确认添加 " + item.getUserName()
					+ " 为好友？"));
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							addFriend(item.getUserName());
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			builder.create().show();
			break;
		case 3:
			// view user info
			viewUserInfo(item.getUserName());
			break;
		// case 4:
		// if (item.getUserName().equals(service.getUserName())) {
		// String tmp = item.getPostContent().replaceAll("(<br>|<BR>)",
		// "\n");
		// String topic = item.getPostTitle();
		// editPost(item.getEditPostLink(), tmp, topic);
		// }
		// break;
		case 5:
			// cancel
			break;
		}
	}

	// private void editPost(String link, String content, String topic) {
	// Bundle bundle = new Bundle();
	// bundle.putString(EditActivity.EDIT_CONTENT,
	// content.replaceAll("<.*?>|searchubb.*?;", ""));
	// bundle.putString(EditActivity.EDIT_TOPIC, topic);
	// bundle.putString(EditActivity.EDIT_LINK, link);
	// bundle.putInt(EditActivity.MOD, EditActivity.MOD_EDIT);
	// Intent intent = new Intent(this, EditActivity.class);
	// intent.putExtra(EditActivity.BUNDLE, bundle);
	// startActivity(intent);
	// }

	private void addFriend(final String userName) {
		new AddFriendTask(this, userName).execute();
	}

	private void viewUserInfo(String username) {
		Intent intent = new Intent(this, ProfileActivity.class);
		intent.putExtra("userName", username);
		startActivity(intent);
	}

	private void sendPm(String target) {
		Intents.Builder builder = new Builder(this, EditActivity.class);
		Intent intent = builder.requestType(EditActivity.REQUEST_PM)
				.pmToUser(target).toIntent();
		startActivity(intent);
	}

	private void quoteReply(String sender, String postTime, String postContent,
			int floorNum, int pageNum) {
		Intents.Builder builder = new Builder(this, EditActivity.class);
		Intent intent = builder.requestType(EditActivity.REQUEST_QUOTE_REPLY)
				.boardId(boardId).boardName(boardName).postId(postId)
				.postName(postName).replyUserName(sender)
				.replyUserPostTime(postTime).replyContent(postContent)
				.floorNumber(floorNum).pageNumber(pageNum).toIntent();
		startActivityForResult(intent, 1);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			refreshPage();
		}
		if (resultCode == Activity.RESULT_CANCELED) {

		}
	}

	private String assemblyContent(List<PostContentEntity> list)
			throws IOException {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		int tmpNum = (currPageNum == LAST_PAGE) ? totalPageNum : currPageNum;
		PostContentFactory pMustache = new PostContentFactory(list, tmpNum);

		InputStream templateIn = null;
		String theme = sharedPref.getString(SettingsActivity.THEME,
				SettingsActivity.THEME_DEFAULT);
		if (!theme.equals(SettingsActivity.THEME_DEFAULT)) {
			String[] stylePaths = ThemeHelper.getStyleSheets(theme);
			templateIn = ThemeHelper.getTemplateStream(theme);
			if (stylePaths != null && templateIn != null) {
				pMustache.addAllStyle(stylePaths);
			}
		}
		if (templateIn == null) {
			pMustache.addStyle(PostContentFactory.CLASSICAL_STYLE);
			templateIn = getAssets().open(PostContentFactory.DEFAULT_TEMPLATE);
		}

		if (service.getusersInfo().getCurrentUserData().getLoginType() == LoginType.NORMAL) {
			pMustache.addScript(PostContentFactory.DEFAULT_UBB_SCRIPT);
		} else if (service.getusersInfo().getCurrentUserData().getLoginType() == LoginType.USER_DEFINED) {
			pMustache.addScript(PostContentFactory.LIFETOY_UBB_SCRIPT);
		} else {
			pMustache.addScript(PostContentFactory.DEFAULT_UBB_SCRIPT);
		}
 		return pMustache.genContent(templateIn);
	}

	//
	// public void open(String pageLink, int pageNum) {
	// Log.d(TAG, "open new post:" + pageNum);
	// Bundle bundle = new Bundle();
	// bundle.putString(POST_ID, pageLink);
	// bundle.putInt(PAGE_NUMBER, pageNum);
	// bundle.putString(POST_NAME, "");
	// Intent intent = new Intent(this, PostContentsJSActivity.class);
	// // intent.putExtra(POST, bundle);
	// this.startActivity(intent);
	// }

	private void setRefreshActionButtonState(boolean refreshing) {
		isRefreshing = refreshing;
		if (mOptionsMenu == null) {
			return;
		}
		final MenuItem refreshItem = mOptionsMenu.findItem(R.id.refresh);
		if (refreshItem != null) {
			if (refreshing) {
				refreshItem
						.setActionView(R.layout.actionbar_indeterminate_progress);
			} else {
				refreshItem.setActionView(null);
			}
		}
	}

	/**
	 * 同步一下cookie
	 */
	public void synCookies() {
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();
		UserData userData = service.getusersInfo().getCurrentUserData();
		// if (userData.getLoginType() == LoginType.USER_DEFINED) {
		// webView.setHttpAuthUsernamePassword(service.getDomain(), "",
		// userData.getProxyUserName(), userData.getProxyPassword());
		// }
		for (Cookie cookie : service.getusersInfo().getCurrentUserData()
				.getCookieStore().getCookies()) {
			cookieManager.setCookie(service.getDomain(),
					"version=" + cookie.getVersion());
			cookieManager.setCookie(service.getDomain(), cookie.getName() + "="
					+ cookie.getValue());
			cookieManager.setCookie(service.getDomain(),
					"domain=" + cookie.getDomain());
			cookieManager.setCookie(service.getDomain(),
					"path=" + cookie.getPath());
			cookieManager.setCookie(service.getDomain(),
					"expiry=" + cookie.getExpiryDate());
			Log.d(TAG, cookie.toString());
		}
		CookieSyncManager.getInstance().sync();
	}

	private class GetPostContentTask extends
			RoboAsyncTask<List<PostContentEntity>> {
		private Activity aContext;
		private String aBoardId;
		private String aPostId;
		private int aPageNum;

		protected GetPostContentTask(Activity context, String boardId,
				String postId, int pageNum) {
			super(context);
			aContext = context;
			aBoardId = boardId;
			aPostId = postId;
			aPageNum = pageNum;
		}

		@Override
		protected void onPreExecute() throws Exception {
			super.onPreExecute();
			setRefreshActionButtonState(true);
		}

		@Override
		public List<PostContentEntity> call() throws Exception {
			return service.getPostContentList(aBoardId, aPostId, aPageNum,
					false);
		}

		private void prefetch() {
			if (currPageNum < totalPageNum) {
				new Thread() {
					public void run() {
						try {
							service.getPostContentList(aBoardId, aPostId,
									currPageNum + 1, false);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();
			}
			if (currPageNum > 1) {
				new Thread() {
					public void run() {
						try {
							service.getPostContentList(aBoardId, aPostId,
									currPageNum - 1, false);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();
			}
		}

		@Override
		protected void onException(Exception e) throws RuntimeException {
			super.onException(e);
			ToastUtils.show(aContext, "获取内容失败");
		}

		@Override
		protected void onSuccess(List<PostContentEntity> t) throws Exception {
			super.onSuccess(t);
			mContentEntities = t;
			PostContentEntity info = t.get(0);
			totalPageNum = info.getTotalPage();
			if (currPageNum > totalPageNum || currPageNum == LAST_PAGE) {
				currPageNum = totalPageNum;
			}
			boardName = (String) info.getBoardName();
			postName = (String) info.getPostTopic();
			synCookies();
			webView.loadDataWithBaseURL(null, assemblyContent(t), "text/html",
					"utf-8", null);
			getSupportActionBar().setTitle(postName);
			getSupportActionBar().setSubtitle(
					"第" + currPageNum + "页 | " + "共" + totalPageNum + "页    "
							+ boardName);
			prefetch();
		}

		@Override
		protected void onFinally() throws RuntimeException {
			super.onFinally();
			setRefreshActionButtonState(false);
		}
	}

	private class AddFriendTask extends ProgressRoboAsyncTask<String> {
		private String aUserName;

		protected AddFriendTask(Activity context, String userName) {
			super(context);
			aUserName = userName;
		}

		@Override
		public String call() throws Exception {
			service.addFriend(aUserName);
			return null;
		}

		@Override
		protected void onException(Exception e) throws RuntimeException {
			super.onException(e);
			ToastUtils.show(context, "添加好友失败");
		}

		@Override
		protected void onSuccess(String t) throws Exception {
			super.onSuccess(t);
			ToastUtils.show(context, "添加好友成功");

		}
	}

	private int hideStartPos = 0;
	private int showStartPos = 0;
	private final int TRIGER_DIS = 100;

	@Override
	public void onScroll(int pre, int curr) {
		if (curr > pre) {
			showStartPos = curr;
			if (curr - hideStartPos > TRIGER_DIS && !isRefreshing) {
				getSupportActionBar().hide();
			}
		} else {
			hideStartPos = curr;
			if (showStartPos - curr > TRIGER_DIS) {
				getSupportActionBar().show();
			}
		}
	}

	public class DefaultGestureDetector extends SimpleOnGestureListener {
		final int FLING_MIN_DISTANCE = DisplayUtil.dip2px(
				PostContentsJSActivity.this, 150);
		final int FLING_MIN_VELOCITY = DisplayUtil.dip2px(
				PostContentsJSActivity.this, 100);
		final int FLING_MAX_Y_DISTANCE = DisplayUtil.dip2px(
				PostContentsJSActivity.this, 50);

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.i("DefaultGestureDetector",
					StringUtils.join(
							Arrays.asList(e1.getX(), e1.getY(), e2.getX(),
									e2.getY(), velocityX, velocityY), ","));
			float distX = e1.getX() - e2.getX();
			float distY = Math.abs(e1.getY() - e2.getY());
			if (distX > FLING_MIN_DISTANCE
					&& Math.abs(velocityX) > FLING_MIN_VELOCITY
					&& distY < FLING_MAX_Y_DISTANCE)
				nextPage();
			else if (-distX > FLING_MIN_DISTANCE
					&& Math.abs(velocityX) > FLING_MIN_VELOCITY
					&& distY < FLING_MAX_Y_DISTANCE)
				prevPage();
			return false;
		}
	}
}

class FullscreenableChromeClient extends WebChromeClient {
	protected Activity mActivity = null;

	private View mCustomView;
	private WebChromeClient.CustomViewCallback mCustomViewCallback;
	private int mOriginalOrientation;

	private FrameLayout mContentView;
	private FrameLayout mFullscreenContainer;

	private static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT);

	public FullscreenableChromeClient(Activity activity) {
		this.mActivity = activity;
	}

	@Override
	public void onShowCustomView(View view, int requestedOrientation,
			WebChromeClient.CustomViewCallback callback) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			if (mCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}

			mOriginalOrientation = mActivity.getRequestedOrientation();
			FrameLayout decor = (FrameLayout) mActivity.getWindow()
					.getDecorView();
			mFullscreenContainer = new FullscreenHolder(mActivity);
			mFullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
			decor.addView(mFullscreenContainer, COVER_SCREEN_PARAMS);
			mCustomView = view;
			setFullscreen(true);
			mCustomViewCallback = callback;
			mActivity.setRequestedOrientation(requestedOrientation);
		}

		super.onShowCustomView(view, requestedOrientation, callback);
	}

	@Override
	public void onHideCustomView() {
		if (mCustomView == null) {
			return;
		}

		setFullscreen(false);
		FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
		decor.removeView(mFullscreenContainer);
		mFullscreenContainer = null;
		mCustomView = null;
		mCustomViewCallback.onCustomViewHidden();
		mActivity.setRequestedOrientation(mOriginalOrientation);
	}

	private void setFullscreen(boolean enabled) {
		Window win = mActivity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (enabled) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
			if (mCustomView != null) {
				mCustomView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
			} else {
				mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
			}
		}
		win.setAttributes(winParams);
	}

	private static class FullscreenHolder extends FrameLayout {
		public FullscreenHolder(Context ctx) {
			super(ctx);
			setBackgroundColor(ctx.getResources().getColor(
					android.R.color.black));
		}

		@Override
		public boolean onTouchEvent(MotionEvent evt) {
			return true;
		}
	}
}
