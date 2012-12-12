package tk.djcrazy.MyCC98;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.helper.HtmlGenHelper;
import tk.djcrazy.MyCC98.task.ProgressRoboAsyncTask;
import tk.djcrazy.MyCC98.util.Intents;
import tk.djcrazy.MyCC98.util.Intents.Builder;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.Gender;
import tk.djcrazy.libCC98.data.PostContentEntity;
import tk.djcrazy.libCC98.data.PostContentsListPage;
import tk.djcrazy.libCC98.data.UserData;
import tk.djcrazy.libCC98.exception.NoUserFoundException;
import tk.djcrazy.libCC98.exception.ParseContentException;
import tk.djcrazy.libCC98.util.DateFormatUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.google.inject.Inject;

public class PostContentsJSActivity extends RoboSherlockActivity implements
		OnClickListener {
	private static final String TAG = "PostContentsJSActivity";
	private static final String JS_INTERFACE = "PostContentsJSActivity";
 
	public static final String POST_ID = "postId";
	public static final String BOARD_ID = "boardId";
	public static final String BOARD_NAME = "boardName";
	public static final String POST_NAME = "postName";
	public static final String PAGE_NUMBER = "pageNumber";
	public static final int LAST_PAGE = 32767;

	@InjectView(R.id.post_contents)
	private WebView webView;
	@InjectView(R.id.but_post_next)
	private View vNext;
	@InjectView(R.id.but_post_prev)
	private View vPrev;
	@InjectView(R.id.but_post_jump)
	private View vJump;
	@InjectView(R.id.but_post_re)
	private View vRe;

	@InjectExtra(value = BOARD_NAME, optional = true)
	private String boardName = "";
	@InjectExtra(POST_ID)
	private String postId;
	@InjectExtra(BOARD_ID)
	private String boardId;
	@InjectExtra(POST_NAME)
	private String postName;
	@InjectExtra(value = PAGE_NUMBER, optional = true)
	private int currPageNum = 1;
 	private int totalPageNum = 1;
 
 	private List<PostContentEntity> mContentEntities;
	@Inject
	private ICC98Service service;

	private HtmlGenHelper helper = new HtmlGenHelper();
	private Menu mOptionsMenu;

	public static Intent createIntent(String boardId, String postId,
			int pageNumber) {
		return new Builder("post_content.VIEW").boardId(boardId).postId(postId)
				.pageNumber(pageNumber).toIntent();
	}

	public static Intent createIntent(String boardId, String postId) {
		return createIntent(boardId, postId, 1);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(com.actionbarsherlock.R.style.Theme_Sherlock);
		setContentView(R.layout.post_contents);
		configureActionBar();
		configureWebView();
		vJump.setOnClickListener(this);
		vPrev.setOnClickListener(this);
		vNext.setOnClickListener(this);
		vRe.setOnClickListener(this);
		vRe.postDelayed(new Runnable() {
			@Override
			public void run() {
				new GetPostContentTask(PostContentsJSActivity.this, boardId, postId, currPageNum).execute();
			}
		}, 50);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		postId = intent.getStringExtra(Intents.EXTRA_POST_ID);
		boardId = intent.getStringExtra(Intents.EXTRA_BOARD_ID);
		currPageNum = intent.getIntExtra(Intents.EXTRA_PAGE_NUMBER, 1);
		new GetPostContentTask(this, boardId, postId, currPageNum).execute();
	}

	private String assemblyContent(List<PostContentEntity> list) {
		int tmpNum = (currPageNum == LAST_PAGE) ? totalPageNum : currPageNum;
		StringBuilder builder = new StringBuilder(5000);
		builder.append(helper.PAGE_OPEN).append(
				"<a href=\"javascript:;\" id=\"showAllImages\"></a>");
		for (int i = 1; i < list.size(); ++i) {
			PostContentEntity item = list.get(i);
			String author = item.getUserName();
			// String content = helper.parseInnerLink(
			// item.getPostContent(), JS_INTERFACE);
			String content = item.getPostContent();
			String avatar = item.getUserAvatarLink();
			Gender gender = item.getGender();
			String postTitle = item.getPostTitle();
			Date postTime = item.getPostTime();
			String postFace = item.getPostFace();
			int floorNum = (tmpNum - 1) * 10 + i;
			String avatarUrl = "";
			if (avatar != null) {
				avatarUrl = avatar.toString();
			}
			if (avatarUrl.equals("")) {
				avatarUrl = service.getDomain() + "face/deaduser.gif";
			}
			StringBuilder mBuilder = new StringBuilder(300);
			mBuilder.append(HtmlGenHelper.ITEM_OPEN);
			HtmlGenHelper.postInfo(mBuilder, postTitle, avatarUrl, author,
					gender.getName(), floorNum,
					DateFormatUtil.convertDateToString(postTime, true), i);
			HtmlGenHelper.postContent(mBuilder, postFace, content, i);
			HtmlGenHelper.btnsBegin(mBuilder);
			HtmlGenHelper.jsBtn(mBuilder, "吐槽",
					"PostContentsJSActivity.showContentDialog",
					String.valueOf(i), "0");
			HtmlGenHelper.jsBtn(mBuilder, "站短",
					"PostContentsJSActivity.showContentDialog",
					String.valueOf(i), "1");
			HtmlGenHelper.jsBtn(mBuilder, "查看",
					"PostContentsJSActivity.showContentDialog",
					String.valueOf(i), "3");
			HtmlGenHelper.jsBtn(mBuilder, "加好友",
					"PostContentsJSActivity.showContentDialog",
					String.valueOf(i), "2");
			HtmlGenHelper.btnsEnd(mBuilder);
			mBuilder.append(HtmlGenHelper.ITEM_CLOSE);
			builder.append(mBuilder.toString());
		}
		builder.append(helper.PAGE_CLOSE);
		return builder.toString();
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
				Method method = WebView.class.getMethod(name);
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
		actionBar.setLogo(new BitmapDrawable(service.getUserAvatar()));
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
			finish();
			return true;
		case R.id.refresh:
			refreshPage();
			return true;
		case R.id.show_all_image:
			webView.loadUrl("javascript:showAllImages.fireEvent('click');");
			return true;
		default:
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_post_jump:
			jumpDialog();
			break;
		case R.id.but_post_next:
			nextPage();
			break;
		case R.id.but_post_prev:
			prevPage();
			break;
		case R.id.but_post_re:
			reply();
			break;
		default:
			break;
		}
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
		webSettings.setAppCacheEnabled(enableCache);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webView.addJavascriptInterface(this, JS_INTERFACE);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				webView.getSettings().setBlockNetworkImage(false);
				onLoadDone();
				super.onPageFinished(view, url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("dispbbs")) {
					url = service.getDomain() + url;
				}
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				return true;
			}
		});
	}

	private void onLoadDone() {
		vPrev.setVisibility(currPageNum == 1 ? View.GONE : View.VISIBLE);
		vNext.setVisibility(currPageNum == totalPageNum ? View.GONE
				: View.VISIBLE);
	}

	public void jumpTo(int pageNum) {
		if (pageNum <= totalPageNum) {
			startActivity(PostContentsJSActivity.createIntent(boardId, postId,
					pageNum));
		}
	}

	public void prevPage() {
		if (currPageNum >= 2) {
			startActivity(PostContentsJSActivity.createIntent(boardId, postId,
					currPageNum - 1));
		}
	}

	public void refreshPage() {
		startActivity(PostContentsJSActivity.createIntent(boardId, postId,
				currPageNum));
	}

	public void nextPage() {
		if (currPageNum + 1 <= totalPageNum) {
			startActivity(PostContentsJSActivity.createIntent(boardId, postId,
					currPageNum + 1));
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
		Intent intent = new Intent(PostContentsJSActivity.this,
				EditActivity.class);
		intent.putExtra(EditActivity.MOD, EditActivity.MOD_REPLY);
		intent.putExtra(EditActivity.POST_Id, postId);
		intent.putExtra(EditActivity.POST_NAME, postName);
		intent.putExtra(EditActivity.BOARD_ID, boardId);
		intent.putExtra(EditActivity.BOARD_NAME, boardName);
		startActivityForResult(intent, 1);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// menu.add(0, MNU_REFRESH, 0, R.string.refresh);
	// menu.add(0, MNU_FIRST, 1, R.string.first_page);
	// menu.add(0, MNU_LAST, 2, R.string.last_page);
	// menu.add(0, MNU_PREV, 3, R.string.pre_page);
	// menu.add(0, MNU_JUMP, 4, R.string.jump_dialog_title);
	// menu.add(0, MNU_NEXT, 5, R.string.next_page);
	// return super.onCreateOptionsMenu(menu);
	//
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case MNU_REFRESH:
	// refreshPage();
	// break;
	// case MNU_PREV:
	// prevPage();
	// break;
	// case MNU_JUMP:
	// jumpDialog();
	// break;
	// case MNU_FIRST:
	// jumpTo(1);
	// break;
	// case MNU_NEXT:
	// nextPage();
	// break;
	// case MNU_LAST:
	// jumpTo(totalPageNum);
	// break;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	public void showContentDialog(final int index, int which) {
		Log.d(TAG, "showContentDialog: " + which);
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
			builder.setMessage("确认添加 " + item.getUserName() + " 为好友？");
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
		Intent intent = new Intent(getApplicationContext(), EditActivity.class);
		intent.putExtra(EditActivity.MOD, EditActivity.MOD_PM);
		intent.putExtra(EditActivity.PM_TO_USER, target);
		startActivity(intent);

	}

	private void quoteReply(String sender, String postTime, String postContent,
			int floorNum, int pageNum) {
		Intent intent = new Intent(this, EditActivity.class);
		intent.putExtra(EditActivity.BOARD_ID, boardId);
		intent.putExtra(EditActivity.BOARD_NAME, boardName);
		intent.putExtra(EditActivity.POST_Id, postId);
		intent.putExtra(EditActivity.POST_NAME, postName);
		intent.putExtra(EditActivity.REPLY_USER_NAME, sender);
		intent.putExtra(EditActivity.REPLY_USER_POST_TIME, postTime);
		intent.putExtra(EditActivity.REPLY_CONTENT, postContent);
		intent.putExtra(EditActivity.FLOOR_NUMBER, floorNum);
		intent.putExtra(EditActivity.PAGE_NUMBER, pageNum);
		intent.putExtra(EditActivity.MOD, EditActivity.MOD_QUOTE_REPLY);
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

	public void open(String pageLink, int pageNum) {
		Log.d(TAG, "open new post:" + pageNum);
		Bundle bundle = new Bundle();
		bundle.putString(POST_ID, pageLink);
		bundle.putInt(PAGE_NUMBER, pageNum);
		bundle.putString(POST_NAME, "");
		Intent intent = new Intent(this, PostContentsJSActivity.class);
		// intent.putExtra(POST, bundle);
		this.startActivity(intent);
	}
	
    private void setRefreshActionButtonState(boolean refreshing) {
        if (mOptionsMenu == null) {
            return;
        }

        final MenuItem refreshItem = mOptionsMenu.findItem(R.id.refresh);
        if (refreshItem != null) {
            if (refreshing) {
                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
            } else {
                refreshItem.setActionView(null);
            }
        }
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
			return service.getPostContentList(aBoardId, aPostId, aPageNum);
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
			boardName = (String) info.getBoardName();
			postName = (String) info.getPostTopic();
			webView.loadDataWithBaseURL(null, assemblyContent(t), "text/html",
					"utf-8", null);
			getSupportActionBar().setTitle(postName);
			getSupportActionBar().setSubtitle(
					"第" + currPageNum + "页 | " + "共" + totalPageNum + "页");
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

}
