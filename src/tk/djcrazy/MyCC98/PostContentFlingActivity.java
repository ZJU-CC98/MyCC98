///**
// * 
// */
//package tk.djcrazy.MyCC98;
//
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.apache.http.ParseException;
//import org.apache.http.client.ClientProtocolException;
//
//import tk.djcrazy.MyCC98.data.PostContentsListPage;
//import tk.djcrazy.MyCC98.helper.HtmlGenHelper;
//import tk.djcrazy.MyCC98.view.FlingGallery;
//import tk.djcrazy.libCC98.CC98Client;
//import tk.djcrazy.libCC98.CC98Parser;
//import tk.djcrazy.libCC98.data.PostContentEntity;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Editable;
//import android.text.InputType;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.ContextThemeWrapper;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.webkit.WebSettings.LayoutAlgorithm;
//import android.webkit.WebView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ListAdapter;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
///**
// * @author DJ
// * 
// */
//public class PostContentFlingActivity extends Activity {
//
//	private FlingGallery mGallery;
//	private final String[] mLabelArray = { "View1", "View2", "View3", "View4",
//			"View5" };
//	private static final String TAG = "PostContentFlingActivity";
//
//	private EditText jumpEditText;
//	private EditText searchEditText;
//	private TextView returnToTopTextView;
//	private Button findNextButton;
//	private Button findPrevButton;
//	private Button searchDoneButton;
//	private Button butNext;
//	private Button butPrev;
//	private Button butJump;
//	private Button butRe;
//	private RelativeLayout searchBar;
//	private ProgressDialog progressDialog;
//	private ScrollView scrollView;
//	private int currPageNum = 1;
//	private int prevPageNum = 1;
//	private int totalPageNum = 10;
//	private String boardName;
//	private String postLink;
//	private String postName;
//	private volatile PostContentsListPage currPage = new PostContentsListPage();
//
//	private static final String ITEM_OPEN = "<div class=\"post\">";
//	private static final String ITEM_CLOSE = "</div>";
//	private static final int MNU_REFRESH = Menu.FIRST;
//	private static final int MNU_FIRST = Menu.FIRST + 1;
//	private static final int MNU_LAST = Menu.FIRST + 2;
//	private static final int MNU_PREV = Menu.FIRST + 3;
//	private static final int MNU_JUMP = Menu.FIRST + 4;
//	private static final int MNU_NEXT = Menu.FIRST + 5;
//	private volatile boolean threadCancel = false;
//	private boolean searchMode = false;
//
//	private volatile PostPageWebView mCurrentWebView;
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// boolean a = super.onTouchEvent(event);
//		return mGallery.onGalleryTouchEvent(event);
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		// requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//		Bundle bundle = getIntent().getBundleExtra("post");
//		postLink = bundle.getString("postLink");
//		postName = bundle.getString("postName");
//		currPageNum = bundle.getInt("pageNum");
//		Log.d("aaa", postLink + " " + postName + " " + currPageNum);
//		if (currPageNum == 0)
//			currPageNum = 1;
//		getWindow().setBackgroundDrawableResource(
//				android.R.drawable.editbox_background);
//		mGallery = new FlingGallery(this);
//		mGallery.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
//				android.R.layout.simple_list_item_1, mLabelArray) {
//			@Override
//			public View getView(int position, View convertView, ViewGroup parent) {
//				Log.d(TAG, "count=" + position);
//				PostPageWebView mLoadingWebView = new PostPageWebView(
//						getApplicationContext(), position + 1);
//				dispContents(position + 1, mLoadingWebView);
//
//				return mLoadingWebView;
//			}
//
//			@Override
//			public int getCount() {
//				return totalPageNum;
//			}
//
//		});
//		setLayouts();
//		findViews();
//		// addListeners();
//		// dispContents(currPageNum);
//	}
//
//	/**
//	 * 
//	 */
//	private void setLayouts() {
//		LinearLayout layout = new LinearLayout(getApplicationContext());
//		layout.setBackgroundColor(R.color.post_list_background);
//		layout.setAlwaysDrawnWithCacheEnabled(true);
//		layout.setDrawingCacheBackgroundColor(R.color.post_list_background);
//		layout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
//		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		layout.addView(mGallery, layoutParams);
//		TextView textView = new TextView(getApplicationContext());
//		layoutParams = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		layoutParams.setMargins(0, 15, 0, 15);
//
//		textView.setLayoutParams(layoutParams);
//		textView.setGravity(Gravity.CENTER);
//		textView.setText("回到顶部");
//		textView.setTextColor(R.color.return_to_top_color);
//		textView.setTextSize(25);
//		layout.addView(textView);
//		setContentView(layout);
//
//	}
//
//	private void findViews() {
//		searchBar = (RelativeLayout) findViewById(R.id.search_bar);
//		jumpEditText = (EditText) findViewById(R.id.search_text);
//		searchEditText = (EditText) findViewById(R.id.search_text);
//		findNextButton = (Button) findViewById(R.id.find_next);
//		findPrevButton = (Button) findViewById(R.id.find_prev);
//		searchDoneButton = (Button) findViewById(R.id.search_done);
//		butJump = (Button) findViewById(R.id.but_post_jump);
//		butNext = (Button) findViewById(R.id.but_post_next);
//		butPrev = (Button) findViewById(R.id.but_post_prev);
//		butRe = (Button) findViewById(R.id.but_post_re);
//		scrollView = (ScrollView) findViewById(R.id.post_content_scroll_view);
//		returnToTopTextView = (TextView) findViewById(R.id.return_to_top);
//	}
//
//	private void addListeners() {
//
//		searchEditText.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				searchAndHilight(searchEditText.getText().toString());
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//
//			}
//		});
//
//		findNextButton.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				mCurrentWebView = (PostPageWebView) mGallery.getCurrentView();
//				mCurrentWebView.findNext(true);
//			}
//		});
//
//		findPrevButton.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				mCurrentWebView = (PostPageWebView) mGallery.getCurrentView();
//
//				mCurrentWebView.findNext(false);
//			}
//		});
//
//		searchDoneButton.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dismissSearchDialog();
//			}
//		});
//
//		butJump.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				jumpDialog();
//			}
//		});
//
//		butPrev.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				prevPage();
//			}
//		});
//
//		butNext.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				nextPage();
//			}
//		});
//
//		butRe.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				reply();
//			}
//		});
//
//		returnToTopTextView.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// scrollView.startAnimation(AnimationUtils.loadAnimation(
//				// PostContentsJSActivity.this, R.anim.testanimation));
//				scrollView.scrollTo(0, 0);
//			}
//		});
//	}
//
//	public void jumpTo(int pageNum) {
//		dispContents(pageNum, null);
//		scrollView.scrollTo(0, 0);
//	}
//
//	public void prevPage() {
//		if (currPageNum - 1 > 0) {
//			dispContents(currPageNum - 1, null);
//			scrollView.scrollTo(0, 0);
//		}
//
//	}
//
//	public void refreshPage() {
//		dispContents(currPageNum, null);
//	}
//
//	public void nextPage() {
//		if (currPageNum + 1 <= totalPageNum) {
//			dispContents(currPageNum + 1, null);
//			scrollView.scrollTo(0, 0);
//		}
//	}
//
//	// handle the message
//	private Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 0:
//				PostPageWebView view = (PostPageWebView) msg.obj;
//				view.loadDataWithBaseURL(null, currPage.getString(),
//						"text/html", "utf-8", null);
//				setTitle(currPageNum + "/" + totalPageNum + " " + postName
//						+ "<<" + boardName);
//				break;
//			default:
//				break;
//			}
//		}
//	};
//
//	private void dispContents(final int pageNum, final PostPageWebView view) {
//		// progressDialog = ProgressDialog.show(PostContentFlingActivity.this,
//		// "",
//		// this.getText(R.string.connectting), true, true,
//		// new DialogInterface.OnCancelListener() {
//		//
//		// @Override
//		// public void onCancel(DialogInterface dialog) {
//		// // TODO Auto-generated method stub
//		// threadCancel = true;
//		// }
//		// });
//		// progressDialog.show();
//		new Thread() {
//			@Override
//			public void run() {
//				currPage.setString(fetchContents(currPage, pageNum));
//				Message msg = new Message();
//				msg.obj = view;
//				msg.what = 0;
//				handler.sendMessage(msg);
//			}
//		}.start();
//	}
//
//	private String fetchContents(PostContentsListPage page, final int pageNum) {
//		Log.d(TAG, postLink);
//		if (threadCancel) {
//			threadCancel = false;
//			return "";
//		}
//		List<PostContentEntity> contentList;
//		String pageString = HtmlGenHelper.PAGE_OPEN;
//
//		try {
//			contentList = CC98Parser.getPostContentList(postLink + "&star="
//					+ pageNum);
//
//			Log.d(TAG, postLink + "&star=" + pageNum);
//			page.setList(contentList);
//			String itemString = "";
//			PostContentEntity info = contentList.get(0);
//			totalPageNum = info.getTotalPage();
//			boardName = (String) info.getBoardName();
//			postName = (String) info.getPostTopic();
//			// update page num
//			int tmpNum = (pageNum == 32767) ? totalPageNum : pageNum;
//			for (int i = 1; i < contentList.size() && !threadCancel; ++i) {
//				PostContentEntity item = contentList.get(i);
//				String author = item.getUserName();
//				String content = parseInnerLink(item.getPostContent());
//				Object avatar = item.getUserAvatarLink();
//				String gender = item.getGender();
//				String postTitle = item.getPostTitle();
//				String postTime = item.getPostTime();
//				String postFace = item.getPostFace();
//				int floorNum = (tmpNum - 1) * 10 + i;
//				String avatarUrl = "";
//				if (avatar != null) {
//					avatarUrl = avatar.toString();
//				}
//				if (avatarUrl.equals("")) {
//					avatarUrl = CC98Client.getCC98Domain()
//							+ "face/deaduser.gif";
//				}
//				itemString = ITEM_OPEN
//						+ HtmlGenHelper.addPostInfo(postTitle, avatarUrl,
//								author, gender, floorNum, postTime, i)
//						+ "<img class=\"post-face\" src=\"file:///android_asset/pic/"
//						+ postFace
//						+ "\" /><br />"
//						+ "<div class=\"post-content\">"
//						+ "<span id=\"ubbcode"
//						+ i
//						+ "\">"
//						+ content
//						+ "</span><script>searchubb('ubbcode"
//						+ i
//						+ "',1,'tablebody2');</script></div>"
//						+ "<img src=\"file:///android_asset/images/draw-icon.png\" onclick=\"PostContents.showContentDialog("
//						+ i + ");\" width=48 height=48 class=\"dialog-but\" />"
//						+ ITEM_CLOSE;
//				pageString += itemString;
//			}
//			if (threadCancel) {
//				threadCancel = false;
//				return "";
//			}
//			pageString += HtmlGenHelper.PAGE_CLOSE;
//		} catch (ClientProtocolException e) {
//
//			e.printStackTrace();
//		} catch (ParseException e) {
//
//			e.printStackTrace();
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
//
//		return pageString;
//	}
//
//	public void jumpDialog() {
//		jumpEditText = new EditText(this);
//		jumpEditText.requestFocus();
//		jumpEditText.setFocusableInTouchMode(true);
//		// set numeric touch pad
//		jumpEditText.setInputType(InputType.TYPE_CLASS_PHONE);
//		new AlertDialog.Builder(this).setTitle(R.string.jump_dialog_title)
//				.setIcon(android.R.drawable.ic_dialog_info)
//				.setView(jumpEditText)
//				.setPositiveButton(R.string.jump_button, new OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//
//						int jumpNum = 1;
//						try {
//							jumpNum = Integer.parseInt(jumpEditText.getText()
//									.toString());
//							if (jumpNum <= 0 || jumpNum > totalPageNum) {
//								Toast.makeText(PostContentFlingActivity.this,
//										R.string.search_input_error,
//										Toast.LENGTH_SHORT).show();
//							} else {
//								dispContents(jumpNum, null);
//							}
//						} catch (NumberFormatException e) {
//							Log.e(PostContentFlingActivity.TAG, e.toString());
//							Toast.makeText(PostContentFlingActivity.this,
//									R.string.search_input_error,
//									Toast.LENGTH_SHORT).show();
//						}
//
//					}
//				}).setNegativeButton(R.string.go_back, null).show();
//	}
//
//	/**
//	 * 
//	 * @param currentPage
//	 * @return
//	 * @deprecated Poor Performance
//	 */
//	@SuppressWarnings("unused")
//	private String addButtons(int currentPage) {
//
//		String buts = "</div><div class=\"ui-footer ui-bar-a ui-footer-fixed fade ui-fixed-overlay\""
//				+ "><div class=\"ui-navbar ui-navbar-noicons\" role=\"navigation\""
//				+ "><ul class=\"ui-grid-c\">";
//		String prevClick = "";
//		if (currentPage > 1) {
//			prevClick = "PostContents.prevPage();";
//		}
//		buts += "<li class=\"ui-block-a\"><a id=\"but-prev\" class=\"ui-btn ui-btn-up-a\" ontouchstart=\"onHover('but-prev');\" onmouseup=\"onOut('but-prev');\" onClick=\""
//				+ prevClick
//				+ "\"><span class=\"ui-btn-inner\"><span class=\"ui-btn-text\">Prev</span></span></a></li>";
//		buts += "<li class=\"ui-block-b\"><a id=\"but-jump\" ontouchstart=\"onHover('but-jump');\" onmouseup=\"onOut('but-jump');\" class=\"ui-btn ui-btn-up-a\" onClick=\"PostContents.jumpDialog();\">"
//				+ "<span class=\"ui-btn-inner\"><span class=\"ui-btn-text\">Jump</span></span></a></li>";
//		buts += "<li class=\"ui-block-c\"><a id=\"re\" ontouchstart=\"onHover('re');\" onmouseup=\"onOut('re');\" class=\"ui-btn ui-btn-up-a\" onClick=\"PostContents.reply();\">"
//				+ "<span class=\"ui-btn-inner\"><span class=\"ui-btn-text\">Re</span></span></a></li>";
//		String nextClick = "";
//		if (currentPage < 10) {
//			nextClick = "PostContents.nextPage();";
//		}
//		buts += "<li class=\"ui-block-d\"><a id=\"but-next\" ontouchstart=\"onHover('but-next');\" onmouseup=\"onOut('but-next');\" class=\"ui-btn ui-btn-up-a\" onClick=\""
//				+ nextClick
//				+ "\"><span class=\"ui-btn-inner\"><span class=\"ui-btn-text\">Next</span></span></a></li>";
//		return buts + "</ul></div>";
//	}
//
//	/**
//	 * 
//	 */
//	public void reply() {
//		Intent intent = new Intent(PostContentFlingActivity.this,
//				EditActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putString("topic", postName);
//		bundle.putString("link", postLink);
//		intent.putExtra("bundle", bundle);
//		startActivityForResult(intent, 1);
//	}
//
//	// option menu
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, MNU_REFRESH, 0, R.string.refresh);
//		menu.add(0, MNU_FIRST, 1, R.string.first_page);
//		menu.add(0, MNU_LAST, 2, R.string.last_page);
//		menu.add(0, MNU_PREV, 3, R.string.pre_page);
//		menu.add(0, MNU_JUMP, 4, R.string.jump_dialog_title);
//		menu.add(0, MNU_NEXT, 5, R.string.next_page);
//
//		return super.onCreateOptionsMenu(menu);
//
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case MNU_REFRESH:
//			refreshPage();
//			break;
//		case MNU_PREV:
//			prevPage();
//			break;
//		case MNU_JUMP:
//			jumpDialog();
//			break;
//		case MNU_FIRST:
//			jumpTo(1);
//			break;
//		case MNU_NEXT:
//			nextPage();
//			break;
//		case MNU_LAST:
//			jumpTo(totalPageNum);
//			break;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//	private void searchDialog() {
//		searchMode = true;
//		searchEditText.setText("");
//		searchBar.setVisibility(View.VISIBLE);
//	}
//
//	private void searchAndHilight(String keyword) {
//		mCurrentWebView = (PostPageWebView) mGallery.getCurrentView();
//
//		mCurrentWebView.findAll(keyword);
//		try {
//			Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
//			m.invoke(mCurrentWebView, true);
//		} catch (Throwable ignored) {
//			Log.i("Error", ignored.toString());
//		}
//	}
//
//	public void showContentDialog(int index) {
//		final PostContentEntity item = currPage.getList().get(index);
//		Log.d("MyCC98", "" + index);
//
//		// Wrap our context to inflate list items using correct theme
//		final Context dialogContext = new ContextThemeWrapper(this,
//				android.R.style.Theme_Light);
//		String[] choices;
//		choices = new String[4];
//		choices[0] = this.getString(R.string.quote_and_reply); // quote & reply
//		choices[1] = this.getString(R.string.send_pm); // send pm
//		choices[2] = this.getString(R.string.view_user_info); // view user info
//		choices[3] = this.getString(R.string.cancel); // cancel
//		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
//				android.R.layout.simple_list_item_1, choices);
//
//		final AlertDialog.Builder builder = new AlertDialog.Builder(
//				dialogContext);
//		// builder.setTitle(R.string.attachToContact);
//		builder.setSingleChoiceItems(adapter, -1,
//				new DialogInterface.OnClickListener() {
//
//					public void onClick(DialogInterface dialog, int which) {
//						// dismiss dialog
//						dialog.dismiss();
//						switch (which) {
//						case 0: {
//							// quote & reply
//							String tmp = item.getPostContent()
//									.replaceAll("(<br>|<BR>)", "\n");
//							quote_reply(postLink, item.getPostTitle(), item.getUserName(),
//									item.getPostTime(), tmp);
//							break;
//
//						}
//						case 1:
//							// send pm
//							send_pm(item.getUserName());
//							break;
//
//						case 2:
//						// view user info
//						{
//							viewUserInfo(item.getUserName());
//						}
//							break;
//						case 3:
//							// cancel
//							break;
//						}
//					}
//				});
//		builder.create().show();
//	}
//
//	private void viewUserInfo(String username) {
//		Intent intent = new Intent(this, ProfileActivity.class);
//		intent.putExtra("userName", username);
//		PostContentFlingActivity.this.startActivity(intent);
//	}
//
//	private void send_pm(String target) {
//		Intent intent = new Intent(this, PmViewActivity.class);
//		intent.putExtra("PmId", -1);
//		intent.putExtra("Sender", target);
//		this.startActivity(intent);
//	}
//
//	private void dismissSearchDialog() {
//
//		mCurrentWebView = (PostPageWebView) mGallery.getCurrentView();
//		mCurrentWebView.clearMatches();
//		searchBar.setVisibility(View.GONE);
//		searchMode = false;
//	}
//
//	private void quote_reply(String link, String topic, String sender,
//			String postTime, String postContent) {
//		Intent intent = new Intent(this, EditActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putCharSequence("link", link);
//		bundle.putCharSequence("topic", topic);
//		bundle.putCharSequence("replyUserName", sender);
//		bundle.putCharSequence("replyUserPostTime", postTime);
//		bundle.putCharSequence("replyContent", postContent);
//		intent.putExtra("bundle", bundle);
//		startActivityForResult(intent, 1);
//	}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == Activity.RESULT_OK) {
//			refreshPage();
//		}
//		if (resultCode == Activity.RESULT_CANCELED) {
//
//		}
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//		if (keyCode == KeyEvent.KEYCODE_SEARCH && event.getRepeatCount() == 0) {
//			searchDialog();
//			return false;
//		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (searchMode) {
//				dismissSearchDialog();
//			} else {
//				finish();
//			}
//		} else {
//			super.onKeyDown(keyCode, event);
//		}
//
//		return false;
//
//	}
//
//	public void open(String pageLink, int pageNum) {
//		Log.d("MyCC98", "open new post:" + pageNum);
//		Bundle bundle = new Bundle();
//		bundle.putString("postLink", postLink);
//		bundle.putInt("pageNum", pageNum);
//		bundle.putString("postName", "");
//		Intent intent = new Intent(this, PostContentsJSActivity.class);
//		intent.putExtra("post", bundle);
//		this.startActivity(intent);
//	}
//
//	public static String parseInnerLink(String content) {
//		String regString = "(http://10\\.10\\.98\\.98|http://www\\.cc98\\.org|\\[url\\])/dispbbs\\.asp\\?boardID=\\d+?&ID=\\d+?&star=\\d+?&page=\\d+(\\[/url\\]|)";
//		Pattern pattern = Pattern.compile(regString);
//		Matcher matcher = pattern.matcher(content);
//		StringBuffer stringBuffer = new StringBuffer();
//		String tmp = null, pageLink = null;
//		while (matcher.find()) {
//			tmp = matcher.group();
//			int beg = tmp.indexOf("star");
//			int end = tmp.indexOf("&page");
//			String ttmpString = tmp.substring(beg + 5, end);
//			Log.d(TAG, ttmpString);
//
//			Log.d("MyCC98", "tmp:" + tmp);
//			int pageNum = 1;
//
//			pageNum = Integer.parseInt(ttmpString);
//
//			Log.d("MyCC98", "pn:" + pageNum);
//			pageLink = tmp.replaceAll(
//					"(\\&star=\\d+\\&page=.*)|(\\[url\\])|(\\[/url\\])", "");
//			matcher.appendReplacement(
//					stringBuffer,
//					"[noubb]<a style=\"color:blue;\" onclick=\"PostContents.open('"
//							+ pageLink + "'," + pageNum + ");\">"
//							+ tmp.replaceAll("(\\[url\\])|(\\[/url\\])", "")
//							+ "</a>[/noubb]");
//		}
//		matcher.appendTail(stringBuffer);
//		return stringBuffer.toString();
//	}
//
//	private class PostPageWebView extends WebView {
//		public PostPageWebView(Context context, int position) {
//			super(context);
//			this.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
//			this.setLayoutParams(new LinearLayout.LayoutParams(
//					LinearLayout.LayoutParams.MATCH_PARENT,
//					LinearLayout.LayoutParams.MATCH_PARENT));
//
//			this.getSettings().setJavaScriptEnabled(true);
//			this.getSettings()
//					.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//			this.addJavascriptInterface(this, "PostContents");
//			this.setAnimationCacheEnabled(true);
//			this.setLongClickable(false);
//			this.setDrawingCacheBackgroundColor(R.color.post_list_background);
//		}
//	}
//}
