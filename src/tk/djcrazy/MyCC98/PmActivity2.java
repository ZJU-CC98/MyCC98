//package tk.djcrazy.MyCC98;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.apache.http.ParseException;
//import org.apache.http.client.ClientProtocolException;
//
//import com.flurry.android.FlurryAgent;
//
//import tk.djcrazy.MyCC98.data.PmListPage;
//import tk.djcrazy.libCC98.CC98Parser;
//import tk.djcrazy.libCC98.data.InboxInfo;
//import tk.djcrazy.libCC98.data.PmInfo;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.InputType;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.WindowManager;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//public class PmActivity2 extends Activity {
//
//	private static String TAG = "PmActivity2";
//	// Switch to Inbox
//	private Button butInbox;
//	// Switch to Outbox
//	private Button butSendbox;
//	// Write new PM
//	private Button butNewMsg;
//	// To next page
//	private Button butNext;
//	// To prev page
//	private Button butPrev;
//	// Open "jump to dialog"
//	private Button butJump;
//	// Main WebView
//	private WebView webView;
//	private ProgressDialog progressDialog;
//
//	private int currentPageNum = 1;
//	private int totalPageNum = 1;
//	private PmListPage currPage = new PmListPage();
//	private PmListPage prevPage = new PmListPage();
//	private PmListPage nextPage = new PmListPage();
//	private InboxInfo inboxInfo = new InboxInfo(0, 0);
//	private InboxInfo outboxInfo = new InboxInfo(0, 0);
//	private boolean threadCancel = false;
//
//	// html tags
//	private static final String pageOpen = "<!DOCTYPE html><html class=\"ui-mobile landscape\">"// min-width-320px
//																								// min-width-480px
//																								// min-width-768px
//																								// min-width-1024px\">"
//			+ "<head>"
//			+ "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"><link rel=\"stylesheet\" href=\"file:///android_asset/jquery.mobile-1.0b3.css\" />"
//			+ "<script src=\"file:///android_asset/tools.js\"></script>"
//			// +"<script src=\"file:///android_asset/jquery-1.6.2.min.js\"></script>"
//			// +"<script src=\"file:///android_asset/jquery.mobile-1.0b2.min.js\"></script>"
//			+ "</head><body>"
//			+ "<div class=\"type-interior ui-page ui-body-c ui-page-active\">"
//			+ "<div class=\"ui-content ui-body-c\">";
//	private static final String pageClose = "</div></div></body></html>\n";
//	private static final String listClose = "</ul>";
//	// private static final String ulOpen =
//	// "<ul data-role=\"listview\" class=\"ui-listview\">";
//	private static final String ulOpen = "<ul class=\"ui-listview\">";
//	// private static final String dividerOpen =
//	// "<li data-role=\"list-divider\" role=\"heading\" class=\"ui-li ui-li-divider ui-btn ui-bar-b ui-btn-up-undefined\">";
//	private static final String dividerOpen = "<li class=\"ui-li ui-li-divider ui-btn ui-bar-b ui-btn-up-undefined\">";
//	private static final String dividerGroupCount = "<span class=\"ui-li-count ui-btn-up-c ui-btn-corner-all\">";
//	private static final String dividerClose = "</span></li>";
//	// private static final String itemOpen =
//	// "<li data-theme=\"c\" class=\"ui-btn ui-btn-icon-right ui-li ui-btn-up-c\"><div class=\"ui-btn-inner ui-li\"><div class=\"ui-btn-text\">";
//	private static final String itemOpen = "<li class=\"ui-btn ui-btn-icon-right ui-li ui-btn-up-c\"><div class=\"ui-btn-inner ui-li\"><div class=\"ui-btn-text\">";
//	private static final String timeOpen = "<p class=\"ui-li-aside ui-li-desc\"><strong>";
//	private static final String timeClose = "</strong></p>";
//	private static final String senderOpen = "<h3 class=\"ui-li-heading\">";
//	private static final String senderClose = "</h3>";
//	private static final String topicOpen = "<p class=\"ui-li-desc\"><strong>";
//	private static final String topicClose = "</strong></p>";
//	private static final String contentOpen = "<p class=\"ui-li-desc\">";
//	private static final String contentClose = "</p></a></div>";
//	private static final String itemClose = "</div></li>";
//
//	private static final int MNU_REFRESH = Menu.FIRST;
//	private static final int MNU_PREV = Menu.FIRST + 1;
//	private static final int MNU_JUMP = Menu.FIRST + 2;
//	private static final int MNU_NEXT = Menu.FIRST + 3;
//
//	private EditText edtNum;
//
//	public static final int INBOX = 0;
//	public static final int OUTBOX = 1;
//	// current mod is INBOX or OUTBOX
//	private int currentMod = INBOX;
//	private int prevPageNum = 0;
//
//	/**
//	 * Parse HTML, get pm list.
//	 * 
//	 * @param page
//	 *            Get pm list, and store it in the given PmListPage.
//	 * @param page_num
//	 *            The number of the page to get.
//	 * @param mod
//	 *            Inbox or Outbox.
//	 * @return The HTML made to be displayed in the webview.
//	 * @throws IOException 
//	 * @throws ParseException 
//	 * @throws ClientProtocolException 
//	 */
//	private String fetchList(PmListPage page, int page_num, int mod) throws ClientProtocolException, ParseException, IOException {
//		// Get the correct list
//		if (mod == INBOX) {
//			page.setList(CC98Parser.getPmData(page_num,
//					inboxInfo, mod));
//			totalPageNum = inboxInfo.getTotalInPage();
//		} else {
//			page.setList(CC98Parser.getPmData(page_num,
//					outboxInfo, mod));
//			totalPageNum = outboxInfo.getTotalInPage();
//		}
//		// make the HTML
//		String pageString = ulOpen;
//		String dateGroup = "";
//		String date = null;
//		String time = null;
//		List<PmInfo> pmList = page.getList();
//		int i = 0;
//		int groupCount = 1;
//		// make the list to display, group the pm sent/received in the same day
//		for (; i < pmList.size(); ++i) {
//			PmInfo pm = pmList.get(i);
//			date = pm.getSendTime().substring(0, 10);
//			if (!date.equals(dateGroup)) {
//				// new group
//				dateGroup = date;
//				groupCount = 1;
//				for (int j = i + 1; j < pmList.size(); ++j) {
//					PmInfo pm2 = pmList.get(j);
//					if (pm2.getSendTime().substring(0, 10).equals(dateGroup)) {
//						++groupCount;
//					} else {
//						break;
//					}
//				}
//				// add group divider
//				pageString += getDivider(date, groupCount);
//			}
//			time = pm.getSendTime().substring(11, 16);
//			pageString += getItem(pm.getPmId(), pm.isNew() ? pm.getSender()
//					+ " (New!)" : pm.getSender(), pm.getTopic(), time);
//
//		}
//		return pageOpen + pageString + listClose + pageClose;
//	}
//
//	public int getCurrentPageNum() {
//		return currentPageNum;
//	}
//
//	// handle the message
//	private Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 0:
//				// load list complete
//				webView.loadDataWithBaseURL(null, currPage.getString(),
//						"text/html", "utf-8", null);
//				String mod;
//				InboxInfo currInfo = null;
//				if (currentMod == INBOX) {
//					mod = "-Inbox";
//					currInfo = inboxInfo;
//				} else if (currentMod == OUTBOX) {
//					mod = "-Outbox";
//					currInfo = outboxInfo;
//				} else {
//					mod = "";
//				}
//				PmActivity2.this.setTitle(getString(R.string.pm_activity_title)
//						+ mod + " Total: " + currInfo.getTotalPmIn() + " Page:"
//						+ currentPageNum + "/" + currInfo.getTotalInPage());
//				// Log.d("WebView", currPage.getString());
//				preFetch();
//				break;
//			default:
//				break;
//			}
//		}
//	};
//	
//	@Override
//	public void onStart()
//	{
//	   super.onStart();
//	   FlurryAgent.onStartSession(this, "5EXV7SIGMTTDKYNXTKR4");
//	}
//	
//	@Override
//	public void onStop()
//	{
//	   super.onStop();
//	   FlurryAgent.onEndSession(this);
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//
//		super.onCreate(savedInstanceState);
//		// Get Html header
//		// pageOpen = this.getString(R.string.pm_list_html_header);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setContentView(R.layout.pm2);
//		setTitle(R.string.pm_activity_title);
//		findViews();
//
//		setListeners();
//		currentPageNum = 1;
//		setViews();
//		displayPmList(currentPageNum, currentMod);
//	}
//
//	/**
//	 * 
//	 */
//	private void setViews() {
//		webView.getSettings().setJavaScriptEnabled(true);
//		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//		webView.setWebViewClient(new WebViewClient() {
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				onLoadDone();
//				super.onPageFinished(view, url);
//			}
//		});
//		webView.addJavascriptInterface(this, "PmActivity");
//		webView.setScrollbarFadingEnabled(false);
//	}
//
//	private void onLoadDone() {
//		progressDialog.dismiss();
//		butPrev.setVisibility(currentPageNum == 1 ? View.GONE : View.VISIBLE);
//		butNext.setVisibility(currentPageNum == inboxInfo.getTotalInPage() ? View.GONE
//				: View.VISIBLE);
//	}
//
//	/**
//	 * Use multi-thread to prefetch the next page and the prev page. Store the
//	 * result (pm list and the html string) in prevPage and nextPage
//	 */
//	private void preFetch() {
//		if (threadCancel) {
//			threadCancel = false;
//			return;
//		}
//		// prefetch prev page
//		if (currentPageNum - 1 > 0) {
//			if (prevPageNum != currentPageNum - 1) { // if not forward one step
//				new Thread() {
//					@Override
//					public void run() {
//
//						try {
//							prevPage.setString(fetchList(prevPage,
//									currentPageNum - 1, currentMod));
//						} catch (ClientProtocolException e) {
//							e.printStackTrace();
//						} catch (ParseException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//
//					}
//				}.start();
//			}
//		} else {
//			prevPage.setList(null).setString(null);
//		}
//		if (threadCancel) {
//			threadCancel = false;
//			return;
//		}
//		// prefetch next page
//		int maxPageNum = (currentMod == INBOX) ? inboxInfo.getTotalInPage()
//				: outboxInfo.getTotalInPage();
//		if (currentPageNum + 1 < maxPageNum) {
//			if (prevPageNum != currentPageNum + 1) { // if not backward
//				new Thread() {
//					@Override
//					public void run() {
//
//						try {
//							nextPage.setString(fetchList(nextPage,
//									currentPageNum + 1, currentMod));	
//							Log.d(LoginActivity.TAG, "prefetch next");
//
//						} catch (ClientProtocolException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//
//				}.start(); // on step
//			} else {
//				nextPage.setList(null).setString(null);
//			}
//		}
//	}
//
//	/**
//	 * When called, first find whether the page has already been prefetch. If is
//	 * prefetched, simply assign the prefetched page to currPage else fetch the
//	 * page.
//	 * 
//	 * @param page_num
//	 *            The page wanted to be displayed.
//	 * @param mod
//	 *            Inbox or Outbox
//	 */
//	private void displayPmList(final int page_num, final int mod) {
//		progressDialog = ProgressDialog.show(PmActivity2.this, "",
//				this.getText(R.string.connectting), true, true,
//				new DialogInterface.OnCancelListener() {
//
//					@Override
//					public void onCancel(DialogInterface dialog) {
//						threadCancel = true;
//					}
//				});
//		new Thread() {
//
//			@Override
//			public void run() {
//				if (threadCancel) {
//					threadCancel = false;
//					return;
//				}
//				if (mod == currentMod) {
//					if (page_num == currentPageNum - 1
//							&& prevPage.getList() != null) { // hit! backward
//																// one
//																// step
//						nextPage.setList(currPage.getList());
//						nextPage.setString(currPage.getString());
//						currPage.setList(prevPage.getList());
//						currPage.setString(prevPage.getString());
//						Log.d(LoginActivity.TAG, "hit");
//					} else if (page_num == currentPageNum + 1
//							&& nextPage.getList() != null) { // hit! forward one
//																// step
//						prevPage.setList(currPage.getList());
//						prevPage.setString(currPage.getString());
//						currPage.setList(nextPage.getList());
//						currPage.setString(nextPage.getString());
//						Log.d(LoginActivity.TAG, "hit");
//					} else {
//						try {
//							currPage.setString(fetchList(currPage, page_num, mod));
//						} catch (ClientProtocolException e) {
//							e.printStackTrace();
//						} catch (ParseException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//						Log.d(LoginActivity.TAG, "miss");
//					}
//				} else {
//
//					try {
//						currPage.setString(fetchList(currPage, page_num, mod));
//					} catch (ClientProtocolException e) {
//						e.printStackTrace();
//					} catch (ParseException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					Log.d(LoginActivity.TAG, "miss");
//
//				}
//				prevPageNum = currentPageNum;
//				currentPageNum = page_num;
//				currentMod = mod;
//				handler.sendEmptyMessage(0);
//			}
//		}.start();
//	}
//
//	private void setListeners() {
//		butInbox.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (currentMod != INBOX) {
//					displayPmList(1, INBOX);
//				}
//			}
//		});
//
//		butSendbox.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (currentMod != OUTBOX) {
//					displayPmList(1, OUTBOX);
//				}
//			}
//		});
//
//		butNewMsg.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				newPm();
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
//		butPrev.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				prevPage();
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
//	}
//
//	private void findViews() {
//		butInbox = (Button) findViewById(R.id.but_inbox);
//		butSendbox = (Button) findViewById(R.id.but_sendbox);
//		butNewMsg = (Button) findViewById(R.id.but_new_message);
//		butNext = (Button) findViewById(R.id.pmbut_next);
//		butPrev = (Button) findViewById(R.id.pmbut_prev);
//		butJump = (Button) findViewById(R.id.pmbut_jump);
//		webView = (WebView) findViewById(R.id.inbox_list);
//	}
//
//	private String getItem(int pmId, String sender, String topic, String time) {
//		return getItemOpen(pmId)
//				+ getAction("PmActivity.reply_pm(" + pmId + ");")
//				+ getTime(time) + getSender(sender) + getTopic(topic)
//				+ getContent("") + itemClose;
//	}
//
//	private String getItemOpen(int id) {
//		return "<li class=\"ui-btn ui-btn-icon-right ui-li ui-btn-up-c\" id=\""
//				+ id
//				+ "\"><div class=\"ui-btn-inner ui-li\"><div class=\"ui-btn-text\">";
//	}
//
//	private String getDivider(String date, int count) {
//		return dividerOpen + date + dividerGroupCount + count + dividerClose;
//	}
//
//	private String getAction(String action) {
//		return "<a class=\"ui-link-inherit\" onClick=\"" + action + "\">";
//	}
//
//	private String getTime(String time) {
//		return timeOpen + time + timeClose;
//	}
//
//	private String getSender(String sender) {
//		return senderOpen + sender + senderClose;
//	}
//
//	private String getTopic(String topic) {
//		return topicOpen + topic + topicClose;
//	}
//
//	private String getContent(String content) {
//		return contentOpen + content + contentClose;
//	}
//
//	public void nextPage() {
//		jumpTo(currentPageNum + 1);
//	}
//
//	public void prevPage() {
//		jumpTo(currentPageNum - 1);
//	}
//
//	public void jumpTo(int pageNum) {
//		if (pageNum > 0 && pageNum <= totalPageNum) {
//			displayPmList(pageNum, currentMod);
//		}
//	}
//
//	/**
//	 * Add nav bar
//	 * 
//	 * @param currentPage
//	 * @return
//	 * @deprecated Removed! Poor performance.
//	 */
//	private String addButtons(int currentPage) {
//
//		String buts = "</div><div class=\"ui-footer ui-bar-a ui-footer-fixed fade ui-fixed-overlay\""
//				+ "><div class=\"ui-navbar ui-navbar-noicons\" role=\"navigation\""
//				+ "><ul class=\"ui-grid-b\">";
//		String prevClick = "";
//		if (currentPage > 1) {
//			prevClick = "PmActivity.prePage();";
//		}
//		buts += "<li class=\"ui-block-a\"><a id=\"but-prev\" onmouseup=\"onOut('but-prev');\" class=\"ui-btn ui-btn-up-a\" ontouchstart=\"onHover('but-prev');\" onClick=\""
//				+ prevClick
//				+ "\"><span class=\"ui-btn-inner\"><span class=\"ui-btn-text\">Prev</span></span></a></li>";
//		buts += "<li class=\"ui-block-b\"><a id=\"but-jump\" onmouseup=\"onOut('but-jump');\" ontouchstart=\"onHover('but-jump');\" class=\"ui-btn ui-btn-up-a\" onClick=\"PmActivity.jumpDialog();\">"
//				+ "<span class=\"ui-btn-inner\"><span class=\"ui-btn-text\">Jump</span></span></a></li>";
//		String nextClick = "";
//		if (currentPage < inboxInfo.getTotalInPage()) {
//			nextClick = "PmActivity.nextPage();";
//		}
//		buts += "<li class=\"ui-block-c\"><a id=\"but-next\" onmouseup=\"onOut('but-next');\" ontouchstart=\"onHover('but-next');\" class=\"ui-btn ui-btn-up-a\" onClick=\""
//				+ nextClick
//				+ "\"><span class=\"ui-btn-inner\"><span class=\"ui-btn-text\">Next</span></span></a></li>";
//		return buts + "</ul></div>";
//	}
//
//	/**
//	 * Reply to the pm with the pm id. Start the PmReply Activity.
//	 * 
//	 * @param pmId
//	 */
//	public void reply_pm(int pmId) {
//		Intent intent = new Intent(this, PmViewActivity.class);
//		intent.putExtra("PmId", pmId);
//		PmInfo pmInfo = null;
//		List<PmInfo> currPmList = currPage.getList();
//		// get the information of the pm to reply from the list
//		// instead of fetch it again from CC98
//		for (int i = 0; i < currPmList.size(); ++i) {
//			if (currPmList.get(i).getPmId() == pmId) {
//				pmInfo = currPmList.get(i);
//			}
//		}
//		intent.putExtra("Sender", pmInfo.getSender());
//		intent.putExtra("SendTime", pmInfo.getSendTime());
//		intent.putExtra("Topic", pmInfo.getTopic());
//		startActivity(intent);
//	}
//
//	public void newPm() {
//		Intent intent = new Intent(this, PmViewActivity.class);
//		intent.putExtra("PmId", -1);
//		startActivity(intent);
//	}
//
//	public void jumpDialog() {
//		edtNum = new EditText(this);
//		edtNum.requestFocus();
//		edtNum.setFocusableInTouchMode(true);
//		// set numeric touch pad
//		edtNum.setInputType(InputType.TYPE_CLASS_PHONE);
//		new AlertDialog.Builder(this).setTitle(R.string.jump_dialog_title)
//				.setIcon(android.R.drawable.ic_dialog_info).setView(edtNum)
//				.setPositiveButton(R.string.jump_button, new OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						int jumpNum = 1;
//						try {
//							jumpNum = Integer.parseInt(edtNum.getText()
//									.toString());
//							if (jumpNum <= 0
//									|| jumpNum > inboxInfo.getTotalInPage()) {
//								Toast.makeText(PmActivity2.this,
//										R.string.search_input_error,
//										Toast.LENGTH_SHORT).show();
//							} else {
//								displayPmList(jumpNum, currentMod);
//							}
//						} catch (NumberFormatException e) {
//							Log.e(PmActivity2.TAG, e.toString());
//							Toast.makeText(PmActivity2.this,
//									R.string.search_input_error,
//									Toast.LENGTH_SHORT).show();
//						}
//
//					}
//				}).setNegativeButton(R.string.go_back, null).show();
//	}
//
//	private void refresh() {
//		displayPmList(currentPageNum, currentMod);
//	}
//
//	// option menu
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, MNU_REFRESH, 0, R.string.refresh);
//		menu.add(0, MNU_PREV, 1, R.string.pre_page);
//		menu.add(0, MNU_JUMP, 2, R.string.jump_dialog_title);
//		menu.add(0, MNU_NEXT, 3, R.string.next_page);
//		return super.onCreateOptionsMenu(menu);
//
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case MNU_REFRESH:
//			refresh();
//			break;
//		case MNU_PREV:
//			prevPage();
//			break;
//		case MNU_JUMP:
//			jumpDialog();
//			break;
//		case MNU_NEXT:
//			nextPage();
//			break;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//}
