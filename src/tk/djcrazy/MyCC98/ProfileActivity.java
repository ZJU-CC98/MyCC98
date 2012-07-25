package tk.djcrazy.MyCC98;

import java.io.IOException;

import tk.djcrazy.MyCC98.view.HeaderView;
import tk.djcrazy.libCC98.CC98Client;
import tk.djcrazy.libCC98.CC98Parser;
import tk.djcrazy.libCC98.data.UserProfileEntity;
import tk.djcrazy.libCC98.exception.NoUserFoundException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

public class ProfileActivity extends BaseActivity {

	private static final int LOAD_PROFILE_SUCCESS = 1;
	private static final int LOAD_PROFILE_FAILED = 0;
	private static final int LOAD_USER_AVARTAR_SUCCESS = 11;
	private static final int LOAD_USER_AVARTAR_FAILED = 10;
	public static final int ADD_FRIEND_SUCCESS = 2;
	public static final int ADD_FRIEND_FAILED = 3;

	public static final String USER_IMAGE="userImage";
	
	private TextView userName;
	private ImageView userPortrait;
	private TextView boardMasterInfo;
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
	private String mUserName;
	private Bitmap userPortraitmBitmap;
	private String url;
	private HeaderView mHeaderView;
	private ProgressDialog dialog;
	private Bitmap userImage;
	Thread profileThread = new Thread() {
		// child thread
		@Override
		public void run() {
			try {
				profileEntity = CC98Parser.getUserProfile(mUserName);
				handler.sendEmptyMessage(LOAD_PROFILE_SUCCESS);

			} catch (NoUserFoundException e) {
				handler.sendEmptyMessage(LOAD_PROFILE_FAILED);
				e.printStackTrace();
			} catch (IOException e) {
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
				userPortraitmBitmap = CC98Client.getBitmapFromUrl(url);
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mUserName = getIntent().getStringExtra("userName");
		userImage = (Bitmap)getIntent().getParcelableExtra(USER_IMAGE);
		setContentView(R.layout.user_profile);
		findViews();
		setViews();
		setListeners();
		profileThread.start();

		dialog.setCancelable(true);
		dialog.show();

	}

	private void setViews() {
		
		mHeaderView.setTitle("用户资料");
		mHeaderView.setButtonImageResource(R.drawable.write_message);
		mHeaderView.setButtonPadding(2, 2, 2, 2);
		mHeaderView.setUserImg(userImage);
		dialog = ProgressDialog.show(ProfileActivity.this, "", "Loading...",
				true);
		
	}

	private void findViews() {

		userName = (TextView) findViewById(R.id.profile_user_name);
		userPortrait = (ImageView) findViewById(R.id.profile_image);
		boardMasterInfo = (TextView) findViewById(R.id.profile_board_master);
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
 		mHeaderView = (HeaderView) findViewById(R.id.main_header);
	}

	private void setListeners() {
		mHeaderView.setButtonOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProfileActivity.this, EditActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt(EditActivity.MOD, EditActivity.MOD_PM);
				bundle.putString(EditActivity.TO_USER, mUserName);
				intent.putExtra(EditActivity.BUNDLE, bundle);
				intent.putExtra(EditActivity.BUNDLE, bundle);
				startActivity(intent);
				overridePendingTransition(R.anim.forward_activity_move_in, R.anim.forward_activity_move_out);
			}
		});
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
				Toast.makeText(ProfileActivity.this, "读取网页或解析出错！", Toast.LENGTH_SHORT)
				.show();
				break;
			case LOAD_USER_AVARTAR_FAILED:
				Toast.makeText(ProfileActivity.this, "读取网页或解析出错！", Toast.LENGTH_SHORT)
				.show();
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
					CC98Client.addFriend(userName);
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
		boardMasterInfo.setText(profileEntity.getBbsMasterInfo());
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