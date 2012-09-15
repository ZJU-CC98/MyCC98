package tk.djcrazy.MyCC98;

import java.io.IOException;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.UserProfileEntity;
import tk.djcrazy.libCC98.exception.NoUserFoundException;
import tk.djcrazy.libCC98.exception.ParseContentException;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;

@ContentView(R.layout.user_profile)
public class ProfileActivity extends BaseActivity {

	private static final int LOAD_PROFILE_SUCCESS = 1;
	private static final int LOAD_PROFILE_FAILED = 0;
	private static final int LOAD_USER_AVARTAR_SUCCESS = 11;
	private static final int LOAD_USER_AVARTAR_FAILED = 10;
	public static final int ADD_FRIEND_SUCCESS = 2;
	public static final int ADD_FRIEND_FAILED = 3;
	private static final int MENU_SEND_MESSAGE_ID = 8790124;
	private static final String USER_NAME = "userName";

	private TextView userName;
	private ImageView userPortrait;
	private TextView loginStatues;
	private TextView userNickName;
	private TextView userLevel;
	private TextView userGroup;
	private TextView goodPosts;
	private TextView totalPosts;
	private TextView userPrestige;
	private TextView registerTime;
	private TextView loginTimes;
	private TextView deletedPosts;
	private TextView deletedRatio;
	private TextView lastLoginTime;

	private TextView userGender;
	private TextView userBirthday;
	private TextView userConstellation;
	private TextView userEmail;
	private TextView userQQ;
	private TextView userMSN;
	private TextView userPage;
	private UserProfileEntity profileEntity;

	@InjectExtra(USER_NAME)
	private String mUserName;
	private Bitmap userPortraitmBitmap;
	private String url;
	private ProgressDialog dialog;

	@Inject
	private ICC98Service service;

	Thread profileThread = new Thread() {
		// child thread
		@Override
		public void run() {
			try {
				profileEntity = service.getUserProfile(mUserName);
				handler.sendEmptyMessage(LOAD_PROFILE_SUCCESS);

			} catch (NoUserFoundException e) {
				handler.sendEmptyMessage(LOAD_PROFILE_FAILED);
				e.printStackTrace();
			} catch (IOException e) {
				handler.sendEmptyMessage(LOAD_PROFILE_FAILED);
				e.printStackTrace();
			} catch (org.apache.http.ParseException e) {
				handler.sendEmptyMessage(LOAD_PROFILE_FAILED);
				e.printStackTrace();
			} catch (ParseContentException e) {
				handler.sendEmptyMessage(LOAD_PROFILE_FAILED);
				e.printStackTrace();
			}
		}
	};

	Thread headPortraitThread = new Thread() {
		// child thread
		@Override
		public void run() {
			try {
				userPortraitmBitmap = service.getBitmapFromUrl(url);
				handler.sendEmptyMessage(LOAD_USER_AVARTAR_SUCCESS);
			} catch (IOException e) {
				handler.sendEmptyMessage(LOAD_USER_AVARTAR_FAILED);
				e.printStackTrace();
			}

		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);
		findViews();
		dialog = ProgressDialog.show(ProfileActivity.this, "", "Loading...",
				true);
		profileThread.start();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setLogo(R.drawable.personal_profile_icon);
		actionBar.setTitle("用户资料");
		actionBar.setDisplayHomeAsUpEnabled(true);
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu optionMenu) {
		optionMenu.add(android.view.Menu.NONE, MENU_SEND_MESSAGE_ID, 1, "站短")
				.setIcon(R.drawable.sure_btn)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case MENU_SEND_MESSAGE_ID:
			Intent intent = new Intent(ProfileActivity.this, EditActivity.class);
			intent.putExtra(EditActivity.MOD, EditActivity.MOD_PM);
			intent.putExtra(EditActivity.PM_TO_USER, mUserName);
			startActivity(intent);
		}
		return true;
	}

	private void findViews() {
		userName = (TextView) findViewById(R.id.profile_user_name);
		userPortrait = (ImageView) findViewById(R.id.profile_image);
		loginStatues = (TextView) findViewById(R.id.profile_online_statues);
		userNickName = (TextView) findViewById(R.id.profile_user_nick_name);
		userLevel = (TextView) findViewById(R.id.profile_user_level);
		userEmail = (TextView) findViewById(R.id.profile_user_email);
		userGroup = (TextView) findViewById(R.id.profile_user_group);
		goodPosts = (TextView) findViewById(R.id.profile_good_posts);
		totalPosts = (TextView) findViewById(R.id.profile_total_posts);
		userPrestige = (TextView) findViewById(R.id.profile_user_prestige);
		registerTime = (TextView) findViewById(R.id.profile_register_time);
		loginTimes = (TextView) findViewById(R.id.profile_login_times);
		deletedPosts = (TextView) findViewById(R.id.profile_deleted_posts);
		deletedRatio = (TextView) findViewById(R.id.profile_deleted_ratio);
		lastLoginTime = (TextView) findViewById(R.id.profile_last_login_time);
		userGender = (TextView) findViewById(R.id.profile_user_gender);
		userBirthday = (TextView) findViewById(R.id.profile_user_birthday);
		userConstellation = (TextView) findViewById(R.id.profile_user_constellation);
		userQQ = (TextView) findViewById(R.id.profile_user_qq);
		userMSN = (TextView) findViewById(R.id.profile_user_msn);
		userPage = (TextView) findViewById(R.id.profile_user_page);
	}

	// handle the message
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_PROFILE_SUCCESS:
				setContents();
				dialog.dismiss();
				headPortraitThread.start();
				break;
			case LOAD_PROFILE_FAILED:
				Toast.makeText(ProfileActivity.this, "读取网页或解析出错！",
						Toast.LENGTH_SHORT).show();
				break;
			case LOAD_USER_AVARTAR_FAILED:
				Toast.makeText(ProfileActivity.this, "读取网页或解析出错！",
						Toast.LENGTH_SHORT).show();
				break;
			case LOAD_USER_AVARTAR_SUCCESS:
				userPortrait.setImageBitmap(userPortraitmBitmap);
				break;
			default:
				break;
			}
		}
	};

	private void add_friend(final String userName) {
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

	protected void setContents() {
		url = profileEntity.getUserAvatarLink();
		userName.setText(mUserName);
		userNickName.setText(profileEntity.getUserNickName());
		userLevel.setText(profileEntity.getUserLevel());
		userGroup.setText(profileEntity.getUserGroup());
		goodPosts.setText(profileEntity.getGoodPosts());
		totalPosts.setText(profileEntity.getTotalPosts());
		userPrestige.setText(profileEntity.getUserPrestige());
		registerTime.setText(profileEntity.getRegisterTime());
		loginTimes.setText(profileEntity.getLoginTimes());
		deletedPosts.setText(profileEntity.getDeletedPosts());
		deletedRatio.setText(profileEntity.getDeletedRatio());
		lastLoginTime.setText(profileEntity.getLastLoginTime());
		userGender.setText(profileEntity.getUserGender());
		userConstellation.setText(profileEntity.getUserConstellation());
		userEmail.setText(profileEntity.getUserEmail());
		userBirthday.setText(profileEntity.getUserBirthday());
		userQQ.setText(profileEntity.getUserQQ());
		userMSN.setText(profileEntity.getUserMSN());
		userPage.setText(profileEntity.getUserPage());
		loginStatues.setText(profileEntity.getOnlineTime());
	}
}