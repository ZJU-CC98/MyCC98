package tk.djcrazy.MyCC98;

import java.io.IOException;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.util.Intents;
import tk.djcrazy.MyCC98.util.ProgressRoboAsyncTask;
import tk.djcrazy.MyCC98.util.Intents.Builder;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.UserProfileEntity;
import tk.djcrazy.libCC98.exception.NoUserFoundException;
import tk.djcrazy.libCC98.exception.ParseContentException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;

@ContentView(R.layout.activity_user_profile)
public class ProfileActivity extends BaseFragmentActivity {

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

	@Inject
	private ICC98Service service;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 		findViews();
		new GetProfileTask(this).execute();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setLogo(R.drawable.personal_profile_icon);
		actionBar.setTitle("用户资料");
		actionBar.setDisplayHomeAsUpEnabled(true);
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
			Intents.Builder builder = new Builder(this, EditActivity.class);
			Intent intent = builder.requestType(EditActivity.REQUEST_PM)
					.pmToUser(mUserName).toIntent();
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

	protected void setContents() {
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

	private class GetProfileTask extends
			ProgressRoboAsyncTask<UserProfileEntity> {

		protected GetProfileTask(Activity context) {
			super(context);
		}

		@Override
		public UserProfileEntity call() throws Exception {
			return service.getUserProfile(Html.fromHtml(mUserName).toString());
		}

		@Override
		protected void onException(Exception e) throws RuntimeException {
			super.onException(e);
			ToastUtils.show(context, "读取网页或解析出错！");
		}

		@Override
		protected void onSuccess(UserProfileEntity t) throws Exception {
			super.onSuccess(t);
			profileEntity = t;
			setContents();
			new GetAvatarTask(context, t.getUserAvatarLink()).execute();
		}
	}

	private class GetAvatarTask extends RoboAsyncTask<Bitmap> {
		private String aUrl;

		protected GetAvatarTask(Context context, String url) {
			super(context);
			aUrl = url;
		}

		@Override
		public Bitmap call() throws Exception {
			return service.getBitmapFromUrl(aUrl);
		}

		@Override
		protected void onSuccess(Bitmap t) throws Exception {
			super.onSuccess(t);
			userPortrait.setImageBitmap(t);
		}
	}
}