package tk.djcrazy.MyCC98;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.template.PostContentTemplateFactory;
import tk.djcrazy.MyCC98.util.DisplayUtil;
import tk.djcrazy.MyCC98.util.Intents;
import tk.djcrazy.MyCC98.util.Intents.Builder;
import tk.djcrazy.MyCC98.util.ProgressDialogBuilder;
import tk.djcrazy.MyCC98.util.ProgressRoboAsyncTask;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.MyCC98.util.UrlUtils;
import tk.djcrazy.MyCC98.view.ObservableWebView;
import tk.djcrazy.MyCC98.view.ObservableWebView.OnScrollChangedCallback;
import tk.djcrazy.libCC98.CachedCC98Service;
import tk.djcrazy.libCC98.NewCC98Service;
import tk.djcrazy.libCC98.data.LoginType;
import tk.djcrazy.libCC98.data.PostContentEntity;
import tk.djcrazy.libCC98.util.DateFormatUtil;
import tk.djcrazy.libCC98.util.RequestResultListener;
import tk.djcrazy.libCC98.util.SerializableCacheUtil;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;

@ContentView(R.layout.activity_post_contents)
public class PostContentsJSActivity extends BaseActivity implements
		OnScrollChangedCallback, RequestResultListener<List<PostContentEntity>> {
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
	private NewCC98Service service;

	private Menu mOptionsMenu;
	private GestureDetector gestureDetector;
	private boolean isRefreshing = false;
	private boolean forceRefresh = false;

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
		try {
			configureActionBar();
			configureWebView();
			gestureDetector = new GestureDetector(this,
					new DefaultGestureDetector());
            service.submitPostContentRequest(this.getClass(), boardId,
                    postId, currPageNum, forceRefresh, this);
            webView.post(new Runnable() {
                @Override
                public void run() {
                    setRefreshActionButtonState(true);
                }
            });
        } catch (IllegalArgumentException e) {
			ToastUtils.show(this, "请先登录应用");
			finish();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		getSupportActionBar().show();
		postId = intent.getStringExtra(Intents.EXTRA_POST_ID);
		boardId = intent.getStringExtra(Intents.EXTRA_BOARD_ID);
		currPageNum = intent.getIntExtra(Intents.EXTRA_PAGE_NUMBER, 1);
		forceRefresh = intent.getBooleanExtra(Intents.EXTRA_FORCE_REFRESH, false);
        service.cancelRequest(this.getClass());
        service.submitPostContentRequest(this.getClass(), boardId,
                postId, currPageNum, forceRefresh, this);
        setRefreshActionButtonState(true);
	}


	private void callHiddenWebViewMethod(String name) {
		if (webView != null) {
			try {
				Method method = ObservableWebView.class.getMethod(name);
				method.invoke(webView);
			} catch (Exception e) {
				Log.e("Invocation Target Exception: " + name, e.getMessage());
			}
		}
	}


    @Override
    public void onRequestComplete(List<PostContentEntity> result) {
        mContentEntities = result;
        PostContentEntity info = result.get(0);
        totalPageNum = info.getTotalPage();
        if (currPageNum > totalPageNum || currPageNum == LAST_PAGE) {
            currPageNum = totalPageNum;
        }
        boardName = info.getBoardName();
        postName = info.getPostTopic();
        webView.loadDataWithBaseURL(null, assemblyContent(result), "text/html",
                "utf-8", null);
        getSupportActionBar().setTitle(postName);
        getSupportActionBar().setSubtitle(getSubtitle());
        setRefreshActionButtonState(false);
    }

    @Override
    public void onRequestError(String msg) {
        setRefreshActionButtonState(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.callHiddenWebViewMethod("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        service.cancelRequest(this.getClass());
    }

    @Override
    public void onResume() {
        super.onResume();
        this.callHiddenWebViewMethod("onResume");
    }

    @SuppressWarnings("deprecation")
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
 			finish();
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

	@SuppressWarnings("deprecation")
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
 		setWebChromeClient();
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
				} else if (UrlUtils.isPostContentLink(url)) {
					startActivity(UrlUtils.getPostContentIntent(url));
				} else {
					Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(it);
				}		
				return true;
			}

			@Override
			public void onReceivedHttpAuthRequest(WebView view,
					HttpAuthHandler handler, String host, String realm) {
				handler.proceed(service.getCurrentUserData()
                        .getProxyUserName(), service
                        .getCurrentUserData().getProxyPassword());
			}
		});
		webView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});
 		webView.setBackgroundColor(Color.parseColor("#e3e3e3"));
	}

	/**
	 * 
	 */
	private void setWebChromeClient() {
		if (Build.VERSION.SDK_INT>=14) {
			webView.setWebChromeClient(new FullscreenableChromeClient(this));
		}
	}

	public void jumpTo(int pageNum) {
		if (pageNum <= totalPageNum) {
            Intent intent = PostContentsJSActivity.createIntent(boardId, postId,
                    pageNum, false);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	public void prevPage() {
		if (currPageNum >= 2) {
			Intent intent = PostContentsJSActivity.createIntent(boardId, postId,
					currPageNum - 1, false);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		} else {
			ToastUtils.show(this, "已经到第一页啦");
		}
	}

	public void refreshPage() {
		Intent intent = PostContentsJSActivity.createIntent(boardId, postId,
				currPageNum, true);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	public void nextPage() {
		if (currPageNum + 1 <= totalPageNum) {
			Intent intent = PostContentsJSActivity.createIntent(boardId, postId,
					currPageNum + 1, false);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		} else {
			ToastUtils.show(this, "已经到最后一页啦");
		}
	}

    private void dialogDoJump(EditText editText) {
        try {
            int jumpNum = Integer.parseInt(editText
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

	public void jumpDialog() {
		final EditText jumpEditText = new EditText(this);
		jumpEditText.setFocusableInTouchMode(true);
		// set numeric touch pad
		jumpEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
		final AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle(R.string.jump_dialog_title)
				.setView(jumpEditText)
				.setPositiveButton(R.string.jump_button,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
                                dialogDoJump(jumpEditText);
							}
						}).setNegativeButton(R.string.go_back, null).create();
        jumpEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (EditorInfo.IME_ACTION_DONE == i) {
                    dialogDoJump(jumpEditText);
                    dialog.dismiss();
                }
                return false;
            }
        });
        jumpEditText.requestFocus();
        // this should be done before dialog.show(), or the ime won't appear automatically o.0
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
	}

	public void reply() {
		Intents.Builder builder = new Intents.Builder(this, EditActivity.class);
		Intent intent = builder.requestType(EditActivity.REQUEST_REPLY)
				.postId(postId).postName(postName).boardId(boardId)
				.boardName(boardName).toIntent();
		startActivity(intent);
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
 		case 5:
			// cancel
			break;
		}
	}

	private void addFriend(final String userName) {
        final ProgressDialog dialog = ProgressDialogBuilder.buildNew(service, this);
        dialog.show();
		service.submitAddFriendRequest(this.getClass(),userName, new RequestResultListener<Boolean>() {
            @Override
            public void onRequestComplete(Boolean result) {
                ToastUtils.show(PostContentsJSActivity.this, "添加好友成功");
                dialog.dismiss();
            }

            @Override
            public void onRequestError(String msg) {
                ToastUtils.show(PostContentsJSActivity.this, msg);
                dialog.dismiss();
            }
        });
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

	private String assemblyContent(List<PostContentEntity> list) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean showUserAvatar = sharedPref.getBoolean(SettingsActivity.SHOW_USER_AVATAR, false);

		int tmpNum = (currPageNum == LAST_PAGE) ? totalPageNum : currPageNum;
		if (service.getCurrentUserData().getLoginType() == LoginType.NORMAL) {
			if (showUserAvatar) {
				return PostContentTemplateFactory.getDefault().genContent(getApplicationContext(), list, tmpNum);
			} else {
				return PostContentTemplateFactory.getSimple().genContent(getApplicationContext(), list, tmpNum);
			}
		} else if (service.getCurrentUserData().getLoginType() == LoginType.USER_DEFINED) {
			return PostContentTemplateFactory.getLifetoy().genContent(getApplicationContext(), list, tmpNum);
 		} else {
 			return PostContentTemplateFactory.getDefault().genContent(getApplicationContext(), list, tmpNum);
		}
	}

 
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

    private String getSubtitle() {
        return "第" + currPageNum + "页 | " + "共" + totalPageNum + "页    "
                + boardName;
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
		
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			reply();
			return true;
		}
	}
}

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
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
