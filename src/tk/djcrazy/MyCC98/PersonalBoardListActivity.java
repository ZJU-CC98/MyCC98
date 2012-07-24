///**
// * My board list activity
// */
//
//package tk.djcrazy.MyCC98;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.mail.DefaultAuthenticator;
//import org.apache.commons.mail.Email;
//import org.apache.commons.mail.EmailException;
//import org.apache.commons.mail.SimpleEmail;
//import org.apache.http.NameValuePair;
//import org.apache.http.ParseException;
//import org.apache.http.client.ClientProtocolException;
//
//import tk.djcrazy.MyCC98.view.FooterView;
//import tk.djcrazy.MyCC98.view.PersonalboardListViewAdapter;
//import tk.djcrazy.MyCC98.view.SendFeedBackDialog;
//import tk.djcrazy.MyCC98.view.SendFeedBackDialog.SendFeedBackListener;
//import tk.djcrazy.libCC98.CC98Client;
//import tk.djcrazy.libCC98.CC98Parser;
//import tk.djcrazy.libCC98.data.BoardEntity;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.flurry.android.FlurryAgent;
//
//public class PersonalBoardListActivity extends ChildActivity {
//
//	private static final int GET_USER_AVARTAR_SUCCESS = 21;
//	private static final int GET_USER_AVARTAR_FAILED = 20;
//	private static final int GET_LIST_SUCCESS = 1;
//	private static final int GET_LIST_FAILED = 0;
//	private static final int SEND_FEEDBACK_SUCCESS = 10;
//	private static final int SEND_FEEDBACK_FAILED = 11;
//
//	Bundle bundle = new Bundle();
//	Intent intent = new Intent();
//
//	public static final String ID = "PersonalBoardListActivity";
//	public static final String USERINFO = "USERINFO";
//	public static final String AUTOLOGIN = "AUTOLOGIN";
//	private static final String EMAIL_HOST_NAME = "zjuem.zju.edu.cn";
//	private static final String TAG = "PersonalBoardListActivity";
//
//	ArrayList<String> mypage = new ArrayList<String>();
//
//	List<BoardEntity> boardList;
//	PersonalboardListViewAdapter boardListViewAdapter;
//	List<NameValuePair> aList;
//
//	private ListView listView;
//	private ProgressDialog dialog;
//
//	Thread getboardThread = new Thread() {
//		// child thread
//		@Override
//		public void run() {
//			try {
//				boardList = CC98Parser.getPersonalBoardList();
//				handler.sendEmptyMessage(GET_LIST_SUCCESS);
//			} catch (ClientProtocolException e) {
//				handler.sendEmptyMessage(GET_LIST_FAILED);
//				e.printStackTrace();
//			} catch (ParseException e) {
//				handler.sendEmptyMessage(GET_LIST_FAILED);
//				e.printStackTrace();
//			} catch (IOException e) {
//				handler.sendEmptyMessage(GET_LIST_FAILED);
//				e.printStackTrace();
//			}
//		}
//	};
//
//	@Override
//	public void onStart() {
//		super.onStart();
//		FlurryAgent.onStartSession(this, "5EXV7SIGMTTDKYNXTKR4");
//	}
//
//	@Override
//	public void onStop() {
//		super.onStop();
//		FlurryAgent.onEndSession(this);
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		group.setTitle(CC98Client.getUserName() + "::个人定制区");
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.personal_board);
//		setTitle(R.string.my_fav);
//		// create new thread to get html and parse it
//		findViews();
//		// set the stat
//		dialog = ProgressDialog.show(PersonalBoardListActivity.this, "",
//				"Loading...", true);
//		dialog.setCancelable(true);
//		dialog.show();
//
//		getboardThread.start();
//
//		intent.setClass(this, PostListActivity.class);
//
//		setListeners();
//	}
//
//	private void findViews() {
//		listView = (ListView) findViewById(R.id.personal_board_list);
//	}
//
//	private void setListeners() {
//
//	}
//
//	// handle the message
//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case GET_LIST_SUCCESS:
//				boardListViewAdapter = new PersonalboardListViewAdapter(
//						PersonalBoardListActivity.this, boardList, group);
//				dialog.dismiss();
//				listView.setAdapter(boardListViewAdapter);
//				listView.invalidate();
//				startActivity((new Intent()).setFlags(
//						Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).setClass(
//						PersonalBoardListActivity.this, HomeActivity.class));
//				break;
//			case GET_LIST_FAILED:
//				dialog.dismiss();
//				Toast.makeText(PersonalBoardListActivity.this, "网络或解析出错！",
//						Toast.LENGTH_SHORT);
//				break;
//			case SEND_FEEDBACK_FAILED:
//				Toast.makeText(getApplicationContext(), "无法连接到服务器，请稍候再试",
//						Toast.LENGTH_SHORT).show();
//				break;
//			case SEND_FEEDBACK_SUCCESS:
//				Toast.makeText(getApplicationContext(), "发送成功！",
//						Toast.LENGTH_SHORT).show();
//				break;
//			default:
//				break;
//			}
//		}
//	};
//
//	/**
//	 * override menu
//	 */
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.personal_board_menu, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle item selection
//		switch (item.getItemId()) {
//		case R.id.exit:
//			finish();
//			return true;
//		case R.id.log_out:
//			logOut();
//			return true;
//		case R.id.check_personal_info:
//			Intent profiIntent = new Intent();
//			profiIntent.setClass(PersonalBoardListActivity.this,
//					ProfileActivity.class);
//			profiIntent.putExtra("userName", CC98Client.getUserName());
//			startActivity(profiIntent);
//			return true;
//		case R.id.about:
//			showAboutInfo();
//			return true;
//		case R.id.feedback:
//			doSendFeedBack();
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
//
//	/**
//	 * 
//	 */
//	private void doSendFeedBack() {
//		SendFeedBackDialog sendFeendBackDialog = new SendFeedBackDialog(
//				PersonalBoardListActivity.this, new SendFeedBackListener() {
//
//					@Override
//					public void onOkClick(String userEmail, String content) {
//						final String mUserEmail = userEmail;
//						final String mContent = content;
//
//						Log.d(TAG, mUserEmail + ": " + mContent);
//
//						// begin send feedback
//						new Thread() {
//							@Override
//							public void run() {
//								try {
//									Email mEmail = new SimpleEmail();
//									mEmail.setHostName(EMAIL_HOST_NAME);
//									mEmail.setSmtpPort(25);
//									mEmail.setAuthenticator(new DefaultAuthenticator(
//											"crazydj", "66957860"));
//									mEmail.setTLS(true);
//
//									mEmail.setFrom(mUserEmail);
//									mEmail.setSubject("MyCC98 Client FeedBack");
//									mEmail.setMsg(mContent);
//									mEmail.addTo("djq2272@gmail.com");
//									mEmail.setCharset("UTF-8");
//									mEmail.send();
//
//									handler.sendEmptyMessage(SEND_FEEDBACK_SUCCESS);
//								} catch (EmailException e) {
//									Log.d(TAG, e.getMessage());
//									e.printStackTrace();
//									handler.sendEmptyMessage(SEND_FEEDBACK_FAILED);
//								}
//
//							}
//						}.start();
//					}
//
//					@Override
//					public void onCancelClick() {
//
//					}
//				});
//		sendFeendBackDialog.show();
//	}
//
//	/**
//	 * 
//	 */
//	private void logOut() {
//		getSharedPreferences(USERINFO, 0).edit().putBoolean(AUTOLOGIN, false)
//				.commit();
//		Intent intent = new Intent();
//		intent.setClass(PersonalBoardListActivity.this, LoginActivity.class);
//		startActivity(intent);
//		finish();
//	}
//
//	private void showAboutInfo() {
//
//		AlertDialog.Builder builder = new AlertDialog.Builder(
//				PersonalBoardListActivity.this);
//		builder.setTitle("关于本软件");
//		builder.setMessage(R.string.about_info);
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//
//			}
//		});
//		builder.create().show();
//
//	}
//
//	@Override
//	public void onBackPressed() {
//		Log.d("persoanl Board", "back pressed");
//		AlertDialog.Builder builder = new AlertDialog.Builder(
//				PersonalBoardListActivity.this);
//		builder.setTitle("确定退出？");
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//
//				finish();
//			}
//		});
//		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}
//
//}