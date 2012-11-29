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
import tk.djcrazy.MyCC98.helper.HtmlGenHelper;
import tk.djcrazy.MyCC98.view.MyWebView;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.Gender;
import tk.djcrazy.libCC98.data.PostContentEntity;
import tk.djcrazy.libCC98.data.PostContentsListPage;
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
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
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

	private static final int FETCH_CONTENT_SUCCESS = 1;
	private static final int FETCH_CONTENT_FAILED = 0;
	private static final int ADD_FRIEND_SUCCESS = 2;
	private static final int ADD_FRIEND_FAILED = 3;

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

	private int prevPageNum = 1;
	private int totalPageNum = 1;

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

	private static PostContentsListPage currPage = new PostContentsListPage();
	private static PostContentsListPage nextPage = new PostContentsListPage();
	private static PostContentsListPage prevPage = new PostContentsListPage();

	private static final int MENU_SHOW_IMG_ID = 266482903;
	private static final int MENU_SHOW_REFRESH_ID = 496823;

	private static final String ITEM_OPEN = "<div class=\"post\"><div class=\"post-content-wrapper\">";
	private static final String ITEM_CLOSE = "</div>";
	private boolean threadCancel = false;
	private ProgressDialog progressDialog;

	@Inject
	private ICC98Service service;

	private HtmlGenHelper helper = new HtmlGenHelper();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(com.actionbarsherlock.R.style.Theme_Sherlock);
		setContentView(R.layout.post_contents);
		configureActionBar();
		setViews();
		addListeners();
		progressDialog = ProgressDialog.show(PostContentsJSActivity.this, "",
				this.getText(R.string.connectting));
		progressDialog.show();
		dispContents(currPageNum);
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
		optionMenu.add(android.view.Menu.NONE, MENU_SHOW_IMG_ID, 1, "显示图片")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		optionMenu.add(android.view.Menu.NONE, MENU_SHOW_REFRESH_ID, 1, "刷新")
				.setIcon(R.drawable.ic_action_refresh)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case MENU_SHOW_REFRESH_ID:
			refreshPage();
			return true;
		case MENU_SHOW_IMG_ID:
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

	private void addListeners() {
		vJump.setOnClickListener(this);
		vPrev.setOnClickListener(this);
		vNext.setOnClickListener(this);
		vRe.setOnClickListener(this);
	}

	private void setViews() {
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
		progressDialog.dismiss();
		vPrev.setVisibility(currPageNum == 1 ? View.GONE : View.VISIBLE);
		vNext.setVisibility(currPageNum == totalPageNum ? View.GONE
				: View.VISIBLE);
	}

	public void jumpTo(int pageNum) {
		dispContents(pageNum);
	}

	public void prevPage() {
		if (currPageNum - 1 > 0) {
			dispContents(currPageNum - 1);
		}
	}

	public void refreshPage() {
		dispContents(currPageNum);
	}

	public void nextPage() {
		if (currPageNum + 1 <= totalPageNum) {
			dispContents(currPageNum + 1);
		}
	}

	// handle the message
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FETCH_CONTENT_SUCCESS:
				webView.loadDataWithBaseURL(null, currPage.getString(),
						"text/html", "utf-8", null);
				getSupportActionBar().setTitle(postName);
				getSupportActionBar().setSubtitle(
						"第" + currPageNum + "页 | " + "共" + totalPageNum + "页");
				prefetch();
				break;
			case FETCH_CONTENT_FAILED:
				progressDialog.dismiss();
				Toast.makeText(PostContentsJSActivity.this, "网络连接或解析失败！",
						Toast.LENGTH_SHORT).show();
				break;
			case ADD_FRIEND_SUCCESS:
				Toast.makeText(PostContentsJSActivity.this, "成功添加好友:)",
						Toast.LENGTH_SHORT).show();
				break;
			case ADD_FRIEND_FAILED:
				Toast.makeText(PostContentsJSActivity.this, "添加好友失败T_T",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	private void dispContents(final int pageNum) {
		progressDialog.show();
		webView.getSettings().setBlockNetworkImage(true);
		new Thread() {
			@Override
			public void run() {
				if (pageNum == currPageNum - 1 && prevPage.getList() != null) {
					nextPage.setList(currPage.getList());
					nextPage.setString(currPage.getString());
					currPage.setList(prevPage.getList());
					currPage.setString(prevPage.getString());
					prevPageNum = currPageNum;
					currPageNum = (pageNum == LAST_PAGE) ? totalPageNum
							: pageNum;
					handler.sendEmptyMessage(FETCH_CONTENT_SUCCESS);

				} else if (pageNum == currPageNum + 1
						&& nextPage.getList() != null) {
					prevPage.setList(currPage.getList());
					prevPage.setString(currPage.getString());
					currPage.setList(nextPage.getList());
					currPage.setString(nextPage.getString());
					Log.d("MyCC98", "hit");
					prevPageNum = currPageNum;
					currPageNum = (pageNum == LAST_PAGE) ? totalPageNum
							: pageNum;
					handler.sendEmptyMessage(FETCH_CONTENT_SUCCESS);

				} else {
					try {
						currPage.setString(fetchContents(currPage, pageNum));
						prevPageNum = currPageNum;
						currPageNum = (pageNum == LAST_PAGE) ? totalPageNum
								: pageNum;
						handler.sendEmptyMessage(FETCH_CONTENT_SUCCESS);
					} catch (ClientProtocolException e) {
						handler.sendEmptyMessage(FETCH_CONTENT_FAILED);
						e.printStackTrace();
					} catch (ParseException e) {
						handler.sendEmptyMessage(FETCH_CONTENT_FAILED);
						e.printStackTrace();
					} catch (Exception e) {
						handler.sendEmptyMessage(FETCH_CONTENT_FAILED);
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	private String fetchContents(PostContentsListPage page, final int pageNum)
			throws ClientProtocolException, ParseException, IOException,
			ParseContentException, java.text.ParseException {
		List<PostContentEntity> contentList = service.getPostContentList(
				boardId, postId, pageNum);
		page.setList(contentList);

		StringBuilder builder = new StringBuilder(5000);
		builder.append(helper.PAGE_OPEN).append(
				"<a href=\"javascript:;\" id=\"showAllImages\"></a>");
		PostContentEntity info = contentList.get(0);
		totalPageNum = info.getTotalPage();
		boardName = (String) info.getBoardName();
		postName = (String) info.getPostTopic();
		// update page num
		int tmpNum = (pageNum == LAST_PAGE) ? totalPageNum : pageNum;
		for (int i = 1; i < contentList.size() && !threadCancel; ++i) {
			PostContentEntity item = contentList.get(i);
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
			mBuilder.append(ITEM_OPEN)
					.append(helper.addPostInfo(postTitle, avatarUrl, author,
							gender.getName(), floorNum,
							DateFormatUtil.convertDateToString(postTime, true),
							i))
					.append("<img class=\"post-face\" src=\"file:///android_asset/pic/")
					.append(postFace)
					.append("\" /><br />")
					.append("<div class=\"post-content\">")
					.append("<span id=\"ubbcode")
					.append(i)
					.append("\">")
					.append(content)
					.append("</span><script>searchubb('ubbcode")
					.append(i)
					.append("',1,'tablebody2');</script></div>")
					.append("</div>")
					.append("<div class=\"btn-group\">")
					.append("<a class=\"btn\" onclick=\"PostContentsJSActivity.showContentDialog("
							+ i + "," + 0 + ");\">吐槽</a>")
					.append("<a class=\"btn\" onclick=\"PostContentsJSActivity.showContentDialog("
							+ i + "," + 1 + ");\">站短</a>")
					.append("<a class=\"btn\" onclick=\"PostContentsJSActivity.showContentDialog("
							+ i + "," + 3 + ");\">查看</a>")
					.append("<a class=\"btn\" onclick=\"PostContentsJSActivity.showContentDialog("
							+ i + "," + 2 + ");\">加好友</a>").append("</div>")
					.append(ITEM_CLOSE);
			builder.append(mBuilder.toString());
		}
		builder.append(helper.PAGE_CLOSE);
		return builder.toString();
	}

	private void prefetch() {
		if (currPageNum - 1 > 0) {
			if (currPageNum != prevPageNum + 1) { // not forward one step
				new Thread() {
					@Override
					public void run() {
						try {
							prevPage.setString(fetchContents(prevPage,
									currPageNum - 1));
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseContentException e) {
							e.printStackTrace();
						} catch (java.text.ParseException e) {
							e.printStackTrace();
						}

					}
				}.start();
			}
		} else {
			prevPage.setList(null).setString(null);
		}
		if (currPageNum + 1 <= totalPageNum) {
			if (currPageNum != prevPageNum - 1) { // not backward one step
				new Thread() {
					@Override
					public void run() {
						try {
							nextPage.setString(fetchContents(nextPage,
									currPageNum + 1));
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseContentException e) {
							e.printStackTrace();
						} catch (java.text.ParseException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		} else {
			nextPage.setList(null).setString(null);
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
										dispContents(jumpNum);
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
		final PostContentEntity item = currPage.getList().get(index);
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
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					service.addFriend(userName);
					handler.sendEmptyMessage(ADD_FRIEND_SUCCESS);
				} catch (ParseException e) {
					handler.sendEmptyMessage(ADD_FRIEND_FAILED);
					e.printStackTrace();
				} catch (NoUserFoundException e) {
					handler.sendEmptyMessage(ADD_FRIEND_FAILED);
					e.printStackTrace();
				} catch (IOException e) {
					handler.sendEmptyMessage(ADD_FRIEND_FAILED);
					e.printStackTrace();
				}
			}
		}).start();
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
}
