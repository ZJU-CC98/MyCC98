package tk.djcrazy.MyCC98;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import com.google.inject.Inject;

import tk.djcrazy.MyCC98.helper.HtmlGenHelper;
import tk.djcrazy.MyCC98.view.HeaderView;
import tk.djcrazy.libCC98.CC98ClientImpl;
import tk.djcrazy.libCC98.CC98ParserImpl;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.Gender;
import tk.djcrazy.libCC98.data.PostContentEntity;
import tk.djcrazy.libCC98.data.PostContentsListPage;
import tk.djcrazy.libCC98.exception.NoUserFoundException;
import tk.djcrazy.libCC98.util.DateFormatUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * 
 * @author zsy
 * 
 */
public class PostContentsJSActivity extends BaseActivity {

	public static final int FETCH_CONTENT_SUCCESS = 1;
	public static final int FETCH_CONTENT_FAILED = 0;
	public static final int ADD_FRIEND_SUCCESS = 2;
	public static final int ADD_FRIEND_FAILED = 3;

	public static final String POST = "post";
	public static final String POST_LINK = "postLink";
	public static final String POST_NAME = "postName";
	public static final String PAGE_NUMBER = "pageNumber";
	public static final String USER_IMAGE = "userImage";
	public static final int LAST_PAGE = 32767;

	private WebView webView;
	private EditText jumpEditText;
	private EditText searchEditText;
	private View showAllImageTextView;
	private View findNextButton;
	private View findPrevButton;
	private View searchDoneButton;
	private View vNext;
	private View vPrev;
	private View vJump;
	private View vRe;
	private View vButtomPostReturn;
	private Bitmap userImage;
	private RelativeLayout searchBar;
	private ProgressDialog progressDialog;
	private ScrollView scrollView;
	private HeaderView mHeaderView;
	private int currPageNum = 1;
	private int prevPageNum = 1;
	private int totalPageNum = 1;
	private String boardName;
	private String postLink;
	private String postName;
	private AnimationDrawable aDrawable;
	private static PostContentsListPage currPage = new PostContentsListPage();
	private static PostContentsListPage nextPage = new PostContentsListPage();
	private static PostContentsListPage prevPage = new PostContentsListPage();
	private static final String JS_INTERFACE = "PostContents";

	private static final String ITEM_OPEN = "<div class=\"post\"><div class=\"post-content-wrapper\">";
	private static final String ITEM_CLOSE = "</div>";
	private static final String TAG = "PostContentsJS";
	private static final int MNU_REFRESH = Menu.FIRST;
	private static final int MNU_FIRST = Menu.FIRST + 1;
	private static final int MNU_LAST = Menu.FIRST + 2;
	private static final int MNU_PREV = Menu.FIRST + 3;
	private static final int MNU_JUMP = Menu.FIRST + 4;
	private static final int MNU_NEXT = Menu.FIRST + 5;
	private boolean threadCancel = false;
	private boolean searchMode = false;

	@Inject
	private ICC98Service service;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.post_contents_js);

		Bundle bundle = getIntent().getBundleExtra(POST);
		postLink = bundle.getString(POST_LINK);
		int starIndex = postLink.indexOf("&star=");
		if (starIndex == -1) {
			starIndex = postLink.length();
		}
		postLink = postLink.substring(0, starIndex);
		postName = bundle.getString(POST_NAME);
		currPageNum = bundle.getInt(PAGE_NUMBER);
		userImage = (Bitmap) bundle.getParcelable(USER_IMAGE);
		Log.d(TAG, postLink + " " + postName + " " + currPageNum);
		if (currPageNum == 0)
			currPageNum = 1;
		findViews();
		setViews();
		addListeners();
		progressDialog = ProgressDialog.show(PostContentsJSActivity.this, "",
				this.getText(R.string.connectting));
		progressDialog.show();
		dispContents(currPageNum);
	}

	private void addListeners() {

		mHeaderView.setTitleOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scrollView.scrollTo(0, 0);
			}
		});

		mHeaderView.setButtonOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshPage();

			}
		});
		searchEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				searchAndHilight(searchEditText.getText().toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		findNextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.findNext(true);
			}
		});

		findPrevButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.findNext(false);
			}
		});

		searchDoneButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismissSearchDialog();
			}
		});

		vJump.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpDialog();
			}
		});

		vPrev.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				prevPage();
			}
		});

		vNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nextPage();
			}
		});
		vRe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reply();
			}
		});

		showAllImageTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// webView.loadUrl("javascript:showAllImages.fireEvent('click');");
				webView.loadUrl("javascript:showAllImages.fireEvent('click');");
			}
		});
	}

	/**
	 * 
	 */
	private void setViews() {
		mHeaderView.setUserImg(userImage);
		mHeaderView.setTitleTextSize(12.0f);
		mHeaderView.setButtonImageResource(R.drawable.top_refresh);
		mHeaderView.setButtonBackgroundResource(R.color.transparent);
		mHeaderView.setButtonPadding(2, 2, 2, 2);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setPluginsEnabled(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setAppCacheEnabled(true);
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
				overridePendingTransition(R.anim.forward_activity_move_in,
						R.anim.forward_activity_move_out);
				return true;
			}
		});
	}

	private void onLoadDone() {
		// progressDialog.dismiss();
		mHeaderView.getButton().clearAnimation();
		aDrawable.stop();
		progressDialog.dismiss();
		vPrev.setVisibility(currPageNum == 1 ? View.GONE : View.VISIBLE);
		vNext.setVisibility(currPageNum == totalPageNum ? View.GONE
				: View.VISIBLE);
	}

	private void findViews() {
		webView = (WebView) findViewById(R.id.post_contents);
		searchBar = (RelativeLayout) findViewById(R.id.search_bar);
		jumpEditText = (EditText) findViewById(R.id.search_text);
		searchEditText = (EditText) findViewById(R.id.search_text);
		findNextButton = findViewById(R.id.find_next);
		findPrevButton = findViewById(R.id.find_prev);
		searchDoneButton = findViewById(R.id.search_done);
		vJump = findViewById(R.id.but_post_jump);
		vNext = findViewById(R.id.but_post_next);
		vPrev = findViewById(R.id.but_post_prev);
		vRe = findViewById(R.id.but_post_re);
		vButtomPostReturn = findViewById(R.id.but_post_return);
		scrollView = (ScrollView) findViewById(R.id.post_content_scroll_view);
		showAllImageTextView = findViewById(R.id.post_content_show_all_imgs);
		mHeaderView = (HeaderView) findViewById(R.id.main_header);
	}

	public void jumpTo(int pageNum) {
		dispContents(pageNum);
		scrollView.scrollTo(0, 0);
	}

	public void prevPage() {
		if (currPageNum - 1 > 0) {
			dispContents(currPageNum - 1);
			scrollView.scrollTo(0, 0);
		}

	}

	public void refreshPage() {
		dispContents(currPageNum);
	}

	public void nextPage() {
		if (currPageNum + 1 <= totalPageNum) {
			dispContents(currPageNum + 1);
			scrollView.scrollTo(0, 0);
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
				setTitle(boardName);
				mHeaderView.setTitle((new StringBuilder().append(currPageNum)
						.append("/").append(totalPageNum).append(" ")
						.append(postName).append("\n").append(boardName)
						.toString()));

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
		aDrawable = (AnimationDrawable) mHeaderView.getButtomDrawable();
		aDrawable.start();
		webView.getSettings().setBlockNetworkImage(true);
		new Thread() {
			@Override
			public void run() {
				if (threadCancel) {
					threadCancel = false;
					return;
				}

				if (pageNum == currPageNum - 1 && prevPage.getList() != null) { // backward
																				// one
																				// step
					nextPage.setList(currPage.getList());
					nextPage.setString(currPage.getString());
					currPage.setList(prevPage.getList());
					currPage.setString(prevPage.getString());
					Log.d("MyCC98", "hit");
					prevPageNum = currPageNum;
					currPageNum = (pageNum == LAST_PAGE) ? totalPageNum
							: pageNum;
					handler.sendEmptyMessage(FETCH_CONTENT_SUCCESS);

				} else if (pageNum == currPageNum + 1
						&& nextPage.getList() != null) { // forward one step
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
						Log.d("MyCC98", "miss");
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
			throws ClientProtocolException, ParseException, IOException {
		Log.d(TAG, postLink);
		if (threadCancel) {
			threadCancel = false;
			return "";
		}
		List<PostContentEntity> contentList = service.getPostContentList(
				boardId, postId, pageNum);
		page.setList(contentList);

		StringBuilder builder = new StringBuilder(5000);
		builder.append(HtmlGenHelper.PAGE_OPEN).append(
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
			String content = HtmlGenHelper.parseInnerLink(
					item.getPostContent(), JS_INTERFACE);
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
					.append(HtmlGenHelper.addPostInfo(postTitle, avatarUrl,
							author, gender.getName(), floorNum,
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
					.append("<a class=\"btn\" onclick=\"PostContents.showContentDialog("
							+ i + "," + 0 + ");\">吐槽</a>")
					.append("<a class=\"btn\" onclick=\"PostContents.showContentDialog("
							+ i + "," + 1 + ");\">站短</a>")
					.append("<a class=\"btn\" onclick=\"PostContents.showContentDialog("
							+ i + "," + 3 + ");\">查看</a>")
					.append("<a class=\"btn\" onclick=\"PostContents.showContentDialog("
							+ i + "," + 2 + ");\">加好友</a>").append("</div>")
					.append(ITEM_CLOSE);
			builder.append(mBuilder.toString());
		}
		if (threadCancel) {
			threadCancel = false;
			return "";
		}
		builder.append(HtmlGenHelper.PAGE_CLOSE);
		return builder.toString();
	}

	private void prefetch() {
		if (threadCancel) {
			threadCancel = false;
			return;
		}

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
						}

					}
				}.start();
			}
		} else {
			prevPage.setList(null).setString(null);
		}
		if (threadCancel) {
			threadCancel = false;
			return;
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
						}

					}
				}.start();
			}
		} else {
			nextPage.setList(null).setString(null);
		}
	}

	public void jumpDialog() {
		jumpEditText = new EditText(this);
		jumpEditText.requestFocus();
		jumpEditText.setFocusableInTouchMode(true);
		// set numeric touch pad
		jumpEditText.setInputType(InputType.TYPE_CLASS_PHONE);
		new AlertDialog.Builder(this).setTitle(R.string.jump_dialog_title)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(jumpEditText)
				.setPositiveButton(R.string.jump_button, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						int jumpNum = 1;
						try {
							jumpNum = Integer.parseInt(jumpEditText.getText()
									.toString());
							if (jumpNum <= 0 || jumpNum > totalPageNum) {
								Toast.makeText(PostContentsJSActivity.this,
										R.string.search_input_error,
										Toast.LENGTH_SHORT).show();
							} else {
								dispContents(jumpNum);
							}
						} catch (NumberFormatException e) {
							Log.e(PostContentsJSActivity.TAG, e.toString());
							Toast.makeText(PostContentsJSActivity.this,
									R.string.search_input_error,
									Toast.LENGTH_SHORT).show();
						}

					}
				}).setNegativeButton(R.string.go_back, null).show();
	}

	/**
	 * 
	 */
	public void reply() {
		Intent intent = new Intent(PostContentsJSActivity.this,
				EditActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(EditActivity.POST_NAME, postName);
		bundle.putString(EditActivity.POST_LINK, postLink);
		bundle.putInt(EditActivity.MOD, EditActivity.MOD_REPLY);
		bundle.putParcelable(EditActivity.USER_IMAGE, userImage);
		intent.putExtra(EditActivity.BUNDLE, bundle);
		startActivityForResult(intent, 1);
		overridePendingTransition(R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);
	}

	// option menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MNU_REFRESH, 0, R.string.refresh);
		menu.add(0, MNU_FIRST, 1, R.string.first_page);
		menu.add(0, MNU_LAST, 2, R.string.last_page);
		menu.add(0, MNU_PREV, 3, R.string.pre_page);
		menu.add(0, MNU_JUMP, 4, R.string.jump_dialog_title);
		menu.add(0, MNU_NEXT, 5, R.string.next_page);

		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MNU_REFRESH:
			refreshPage();
			break;
		case MNU_PREV:
			prevPage();
			break;
		case MNU_JUMP:
			jumpDialog();
			break;
		case MNU_FIRST:
			jumpTo(1);
			break;
		case MNU_NEXT:
			nextPage();
			break;
		case MNU_LAST:
			jumpTo(totalPageNum);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void searchDialog() {
		searchMode = true;
		searchEditText.setText("");
		searchBar.setVisibility(View.VISIBLE);
	}

	private void searchAndHilight(String keyword) {
		webView.findAll(keyword);
		try {
			Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
			m.invoke(webView, true);
		} catch (Throwable ignored) {
			Log.i("Error", ignored.toString());
		}
	}

	public void showContentDialog(final int index, int which) {
		final PostContentEntity item = currPage.getList().get(index);
		switch (which) {
		case 0: {
			// quote & reply
			String tmp = item.getPostContent().replaceAll("(<br>|<BR>)", "\n");
			quoteReply(postLink, item.getPostTitle(), item.getUserName(),
					DateFormatUtil.convertDateToString(item.getPostTime(),
							false), tmp, index, currPageNum);
		}
			break;
		case 1:
			// send pm
			send_pm(item.getUserName());
			break;
		case 2:
			AlertDialog.Builder builder = new AlertDialog.Builder(
					PostContentsJSActivity.this);
			builder.setTitle("提示");
			builder.setMessage("确认添加 " + item.getUserName() + " 为好友？");
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					addFriend(item.getUserName());
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {

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
//		case 4:
//			if (item.getUserName().equals(service.getUserName())) {
//				String tmp = item.getPostContent().replaceAll("(<br>|<BR>)",
//						"\n");
//				String topic = item.getPostTitle();
//				editPost(item.getEditPostLink(), tmp, topic);
//			}
//			break;
		case 5:
			// cancel
			break;
		}
	}

//	private void editPost(String link, String content, String topic) {
//		Bundle bundle = new Bundle();
//		bundle.putString(EditActivity.EDIT_CONTENT,
//				content.replaceAll("<.*?>|searchubb.*?;", ""));
//		bundle.putString(EditActivity.EDIT_TOPIC, topic);
//		bundle.putString(EditActivity.EDIT_LINK, link);
//		bundle.putInt(EditActivity.MOD, EditActivity.MOD_EDIT);
//		Intent intent = new Intent(this, EditActivity.class);
//		intent.putExtra(EditActivity.BUNDLE, bundle);
//		startActivity(intent);
//		overridePendingTransition(R.anim.forward_activity_move_in,
//				R.anim.forward_activity_move_out);
//	}

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
		intent.putExtra(ProfileActivity.USER_IMAGE, userImage);
		PostContentsJSActivity.this.startActivity(intent);
		PostContentsJSActivity.this.overridePendingTransition(
				R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);

	}

	private void send_pm(String target) {
		Intent intent = new Intent(this, EditActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(EditActivity.MOD, EditActivity.MOD_PM);
		bundle.putString(EditActivity.TO_USER, target);
		intent.putExtra(EditActivity.BUNDLE, bundle);
		intent.putExtra(EditActivity.BUNDLE, bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);
	}

	private void dismissSearchDialog() {
		webView.clearMatches();
		searchBar.setVisibility(View.GONE);
		searchMode = false;
	}

	private void quoteReply(String link, String topic, String sender,
			String postTime, String postContent, int floorNum, int pageNum) {
		Intent intent = new Intent(this, EditActivity.class);
		Bundle bundle = new Bundle();
		bundle.putCharSequence(EditActivity.POST_LINK, link);
		bundle.putCharSequence(EditActivity.POST_NAME, topic);
		bundle.putCharSequence(EditActivity.REPLY_USER_NAME, sender);
		bundle.putCharSequence(EditActivity.REPLY_USER_POST_TIME, postTime);
		bundle.putCharSequence(EditActivity.REPLY_CONTENT, postContent);
		bundle.putInt(EditActivity.FLOOR_NUMBER, floorNum);
		bundle.putInt(EditActivity.PAGE_NUMBER, pageNum);
		bundle.putParcelable(EditActivity.USER_IMAGE, userImage);
		bundle.putInt(EditActivity.MOD, EditActivity.MOD_REPLY);
		intent.putExtra(EditActivity.BUNDLE, bundle);
		startActivityForResult(intent, 1);
		overridePendingTransition(R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_SEARCH && event.getRepeatCount() == 0) {
			searchDialog();
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (searchMode) {
				dismissSearchDialog();
			} else {
				finish();
				overridePendingTransition(R.anim.backward_activity_move_in,
						R.anim.backward_activity_move_out);
			}
		} else {
			super.onKeyDown(keyCode, event);
		}
		return false;
	}

	public void open(String pageLink, int pageNum) {
		Log.d(TAG, "open new post:" + pageNum);
		Bundle bundle = new Bundle();
		bundle.putString(POST_LINK, pageLink);
		bundle.putInt(PAGE_NUMBER, pageNum);
		bundle.putString(POST_NAME, "");
		Intent intent = new Intent(this, PostContentsJSActivity.class);
		intent.putExtra(POST, bundle);
		this.startActivity(intent);
		overridePendingTransition(R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);
	}
}
