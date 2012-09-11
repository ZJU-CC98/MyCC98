package tk.djcrazy.MyCC98;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.adapter.PmListViewAdapter;
import tk.djcrazy.MyCC98.view.HeaderView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.InboxInfo;
import tk.djcrazy.libCC98.data.PmInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;

@ContentView(R.layout.pm)
public class PmActivity extends BaseActivity implements OnRefreshListener {

	private static String TAG = "PmActivity";
	private static final int MENU_SWITCH_BOX = 23423146;

	@InjectView(R.id.pm_tv_next)
	private View butNext;
	@InjectView(R.id.pm_tv_prev)
	private View butPrev;
	@InjectView(R.id.pm_tv_jump)
	private View butJump;
	@InjectView(R.id.pm_listview)
	private PullToRefreshListView listView;

	private PmListViewAdapter listViewAdapter;

	private int currentPageNum = 1;
	private int totalPageNum = 1;
	private List<PmInfo> currPage;
	private List<PmInfo> prevPage;
	private List<PmInfo> nextPage;
	private InboxInfo inboxInfo = new InboxInfo(0, 0);
	private InboxInfo outboxInfo = new InboxInfo(0, 0);
	private boolean threadCancel = false;

	private static final int LOAD_SUCC = 0;

	private EditText edtNum;

	public static final int INBOX = 0;
	public static final int OUTBOX = 1;
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
				getSupportActionBar().setSubtitle(
						mod + " (" + currentPageNum + "/"
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
		setContentView(R.layout.pm);
		configureActionBar();
		findViews();
		setListeners();
		currentPageNum = 1;
		switchToMod(currentMod);
		displayPmList(currentPageNum, currentMod);
	}

	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setLogo(new BitmapDrawable(service.getUserAvatar()));
		actionBar.setTitle("论坛短消息");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu optionMenu) {
		optionMenu
				.add(Menu.NONE, MENU_SWITCH_BOX, 1, "切换")
				.setIcon(R.drawable.switch_mode)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
 			return true;
		case MENU_SWITCH_BOX:
			if (currentMod == INBOX) {
				switchToMod(OUTBOX);
			} else {
				switchToMod(INBOX);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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

	@Override
	public void onRefresh() {
		refresh();
	}

}
