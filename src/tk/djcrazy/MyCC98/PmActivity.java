package tk.djcrazy.MyCC98;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.MyCC98.adapter.PmListViewAdapter;
import tk.djcrazy.MyCC98.view.HeaderView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import tk.djcrazy.libCC98.CC98ClientImpl;
import tk.djcrazy.libCC98.CC98ParserImpl;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.InboxInfo;
import tk.djcrazy.libCC98.data.PmInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.inject.Inject;

public class PmActivity extends BaseActivity implements OnRefreshListener {

	private static String TAG = "PmActivity";
	// Switch to Inbox
	// private View butInbox;
	// // Switch to Outbox
	// private View butSendbox;
	// Write new PM
	// To next page
	private View butNext;
	// To prev page
	private View butPrev;
	// Open "jump to dialog"
	private View butJump;
	private PullToRefreshListView listView;
	private HeaderView headerView;
	private PmListViewAdapter listViewAdapter;

	private int currentPageNum = 1;
	private int totalPageNum = 1;
	private List<PmInfo> currPage;
	private List<PmInfo> prevPage;
	private List<PmInfo> nextPage;
	private InboxInfo inboxInfo = new InboxInfo(0, 0);
	private InboxInfo outboxInfo = new InboxInfo(0, 0);
	private boolean threadCancel = false;

	private static final int MNU_REFRESH = Menu.FIRST;
	private static final int MNU_PREV = Menu.FIRST + 1;
	private static final int MNU_JUMP = Menu.FIRST + 2;
	private static final int MNU_NEXT = Menu.FIRST + 3;

	private static final int LOAD_SUCC = 0;

	private EditText edtNum;

	public static final int INBOX = 0;
	public static final int OUTBOX = 1;
	// current mod is INBOX or OUTBOX
	private int currentMod = INBOX;
	private int prevPageNum = 0;
	@Inject
	private ICC98Service service;
	
	private List<PmInfo> fetchList(int page_num, int mod)
			throws ClientProtocolException, ParseException, IOException {
		// Get the correct list
		List<PmInfo> page;
		if (mod == INBOX) {
			page = service.getPmData(page_num, inboxInfo, mod);
			totalPageNum = inboxInfo.getTotalInPage();
		} else {
			page = service.getPmData(page_num, outboxInfo, mod);
			totalPageNum = outboxInfo.getTotalInPage();
		}
		return page;
	}

	public int getCurrentPageNum() {
		return currentPageNum;
	}

	// handle the message
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_SUCC:

				// load list complete
				String mod;
				InboxInfo currInfo = null;
				if (currentMod == INBOX) {
					mod = "收件箱";
					currInfo = inboxInfo;
				} else if (currentMod == OUTBOX) {
					mod = "发件箱";
					currInfo = outboxInfo;
				} else {
					mod = "";
				}
				headerView.setTitle(mod + " (" + currentPageNum + "/"
						+ currInfo.getTotalInPage() + ")");
				listViewAdapter = new PmListViewAdapter(PmActivity.this,
						currPage);
				listView.setAdapter(listViewAdapter);

				preFetch();
				onLoadDone();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pm);
		setTitle(R.string.pm_activity_title);
		findViews();
		headerView.setUserImg(service.getUserAvatar());
		setListeners();
		currentPageNum = 1;
		switchToMod(currentMod);
		displayPmList(currentPageNum, currentMod);
	}

	private void onLoadDone() {
		butPrev.setVisibility(currentPageNum == 1 ? View.GONE : View.VISIBLE);
		butNext.setVisibility(currentPageNum == inboxInfo.getTotalInPage() ? View.GONE
				: View.VISIBLE);
		listView.onRefreshComplete();
	}

	/**
	 * Use multi-thread to prefetch the next page and the prev page. Store the
	 * result in prevPage and nextPage
	 */
	private void preFetch() {
		if (threadCancel) {
			threadCancel = false;
			return;
		}
		// prefetch prev page
		if (currentPageNum - 1 > 0) {
			if (prevPageNum != currentPageNum - 1) { // if not forward one step
				new Thread() {
					@Override
					public void run() {

						try {
							prevPage = fetchList(currentPageNum - 1, currentMod);
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
			prevPage = null;
		}
		if (threadCancel) {
			threadCancel = false;
			return;
		}
		// prefetch next page
		int maxPageNum = (currentMod == INBOX) ? inboxInfo.getTotalInPage()
				: outboxInfo.getTotalInPage();
		if (currentPageNum + 1 < maxPageNum) {
			if (prevPageNum != currentPageNum + 1) { // if not backward
				new Thread() {
					@Override
					public void run() {

						try {
							nextPage = fetchList(currentPageNum + 1, currentMod);
							Log.d(TAG, "prefetch next");

						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}.start(); // on step
			} else {
				nextPage = null;
			}
		}
	}

	private void displayPmList(final int page_num, final int mod) {
		new Thread() {

			@Override
			public void run() {
				if (threadCancel) {
					threadCancel = false;
					return;
				}
				if (mod == currentMod) {
					if (page_num == currentPageNum - 1 && prevPage != null) {
						// hit! backward one step
						nextPage = currPage;
						currPage = prevPage;
						Log.d(TAG, "hit");
					} else if (page_num == currentPageNum + 1
							&& nextPage != null) { // hit! forward one step
						prevPage = currPage;
						currPage = nextPage;
						Log.d(TAG, "hit");
					} else {
						try {
							currPage = fetchList(page_num, mod);
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						Log.d(TAG, "miss");
					}
				} else {

					try {
						currPage = fetchList(page_num, mod);
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Log.d(TAG, "miss");

				}
				prevPageNum = currentPageNum;
				currentPageNum = page_num;
				currentMod = mod;
				handler.sendEmptyMessage(LOAD_SUCC);
			}
		}.start();
	}

	private void switchToMod(int mod) {
		if (mod == INBOX) {
			headerView.setButtonImageResource(R.drawable.outbox);
			headerView.setButtonOnclickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					switchToMod(OUTBOX);
				}
			});
		} else if (mod == OUTBOX) {
			headerView.setButtonImageResource(R.drawable.inbox);
			headerView.setButtonOnclickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					switchToMod(INBOX);
				}
			});
		}
		currentMod = mod;
		currentPageNum = 1;
		displayPmList(currentPageNum, currentMod);
	}

	private void setListeners() {
		listView.setOnRefreshListener(this);

		butNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				nextPage();
			}
		});

		butPrev.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				prevPage();
			}
		});

		butJump.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				jumpDialog();
			}
		});
	}

	private void findViews() {
		butNext = findViewById(R.id.pm_tv_next);
		butPrev = findViewById(R.id.pm_tv_prev);
		butJump = findViewById(R.id.pm_tv_jump);
		listView = (PullToRefreshListView) findViewById(R.id.pm_listview);
		headerView = (HeaderView) findViewById(R.id.pm_header);
	}

	public void nextPage() {
		jumpTo(currentPageNum + 1);
	}

	public void prevPage() {
		jumpTo(currentPageNum - 1);
	}

	public void jumpTo(int pageNum) {
		if (pageNum > 0 && pageNum <= totalPageNum) {
			displayPmList(pageNum, currentMod);
		}
	}

	public void newPm() {
		Intent intent = new Intent(this, PmViewActivity.class);
		intent.putExtra("PmId", -1);
		startActivity(intent);
		overridePendingTransition(R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);
	}

	public void jumpDialog() {
		edtNum = new EditText(this);
		edtNum.requestFocus();
		edtNum.setFocusableInTouchMode(true);
		// set numeric touch pad
		edtNum.setInputType(InputType.TYPE_CLASS_PHONE);
		new AlertDialog.Builder(this).setTitle(R.string.jump_dialog_title)
				.setIcon(android.R.drawable.ic_dialog_info).setView(edtNum)
				.setPositiveButton(R.string.jump_button, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						int jumpNum = 1;
						try {
							jumpNum = Integer.parseInt(edtNum.getText()
									.toString());
							if (jumpNum <= 0
									|| jumpNum > inboxInfo.getTotalInPage()) {
								Toast.makeText(PmActivity.this,
										R.string.search_input_error,
										Toast.LENGTH_SHORT).show();
							} else {
								displayPmList(jumpNum, currentMod);
							}
						} catch (NumberFormatException e) {
							Log.e(PmActivity.TAG, e.toString());
							Toast.makeText(PmActivity.this,
									R.string.search_input_error,
									Toast.LENGTH_SHORT).show();
						}

					}
				}).setNegativeButton(R.string.go_back, null).show();
	}

	private void refresh() {
		displayPmList(currentPageNum, currentMod);
	}

	// option menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MNU_REFRESH, 0, R.string.refresh);
		menu.add(0, MNU_PREV, 1, R.string.pre_page);
		menu.add(0, MNU_JUMP, 2, R.string.jump_dialog_title);
		menu.add(0, MNU_NEXT, 3, R.string.next_page);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MNU_REFRESH:
			refresh();
			break;
		case MNU_PREV:
			prevPage();
			break;
		case MNU_JUMP:
			jumpDialog();
			break;
		case MNU_NEXT:
			nextPage();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRefresh() {
		refresh();
	}

}
