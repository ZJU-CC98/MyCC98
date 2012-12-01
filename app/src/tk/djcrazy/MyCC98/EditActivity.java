package tk.djcrazy.MyCC98;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.adapter.EmotionGridViewAdapter;
import tk.djcrazy.MyCC98.dialog.MoreEmotChooseDialog;
import tk.djcrazy.MyCC98.dialog.MoreEmotChooseDialog.FaceExpressionChooseListener;
import tk.djcrazy.MyCC98.helper.TextHelper;
import tk.djcrazy.MyCC98.task.ProgressRoboAsyncTask;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.MyCC98.util.ViewUtils;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.ICC98UrlManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;

@ContentView(R.layout.reply)
public class EditActivity extends BaseActivity implements OnClickListener {

	public static final int MOD_REPLY = 0;
	public static final int MOD_QUOTE_REPLY = 1;
	public static final int MOD_NEW_POST = 2;
	public static final int MOD_PM = 3;
	public static final int MOD_EDIT = 4;
	public static final String MOD = "mod";
	public static final String BOARD_ID = "boardID";
	public static final String BOARD_NAME = "boardName";
	public static final String POST_Id = "postId";
	public static final String POST_NAME = "postName";
	public static final String REPLY_USER_NAME = "replyUserName";
	public static final String REPLY_USER_POST_TIME = "replyUserPostTime";
	public static final String REPLY_CONTENT = "replyContent";
	public static final String FLOOR_NUMBER = "floorNumber";
	public static final String PAGE_NUMBER = "pageNumber";
	public static final String PM_TO_USER = "touser";
	public static final String PM_CONTENT = "pmcontent";
	public static final String PM_TITLE = "pmtitle";
	public static final String EDIT_LINK = "editlink";
	public static final String EDIT_CONTENT = "editcontent";
	public static final String EDIT_TOPIC = "edittopic";
	public static final String IS_QUOTE_USER = "isQuoteUser";

	public static final String TAIL = "\n\n[right][color=gray]From  "
			+ Build.MODEL + " via MyCC98[/color][/right]";

	private String appendTail = TAIL;

	private static final String TAG = "EditActivity";
	private static final int TITLE_MAX_LENGTH = 100;
	private static final int CONTENT_MAX_LENGTH = 16240;
	private static final int CAMERA_WITH_DATA = 3023;
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* image upload 500KB limited */
	private static final long MAX_IMAGE_SIZE_IN_BYTE = 500 * 1024;

	/* 拍照的照片存储位置 */
	private static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/Camera");
	private static final int MENU_REPLY_ID = 87356;

	@InjectExtra(MOD)
	private int mod;
	@InjectExtra(value = BOARD_ID, optional = true)
	private String boardID;
	@InjectExtra(value = BOARD_NAME, optional = true)
	private String boardName;
	@InjectExtra(value = POST_Id, optional = true)
	private String postId;
	@InjectExtra(value = POST_NAME, optional = true)
	private String postName;
	@InjectExtra(value = REPLY_USER_NAME, optional = true)
	private String replyUserName;
	@InjectExtra(value = REPLY_USER_POST_TIME, optional = true)
	private String replyUserPostTime;
	@InjectExtra(value = REPLY_CONTENT, optional = true)
	private String replyUserPostContent;
	@InjectExtra(value = FLOOR_NUMBER, optional = true)
	private int quoteFloorNumber;
	@InjectExtra(value = PAGE_NUMBER, optional = true)
	private int quotePageNumber;
	@InjectExtra(value = IS_QUOTE_USER, optional = true)
	private boolean isQuoteUser;
	@InjectExtra(value = PM_TO_USER, optional = true)
	private String pmReplyName;
	@InjectExtra(value = PM_TITLE, optional = true)
	private String pmReplyTopic;
	@InjectExtra(value = PM_CONTENT, optional = true)
	private String pmReplyContent;

	@InjectView(R.id.reply_title_edit)
	private EditText replyTitleEditText;
	@InjectView(R.id.reply_content)
	private EditText replyContentEditText;
	@InjectView(R.id.face_choose_radio_group)
	private RadioGroup faceRadioGroup;
	@InjectView(R.id.edit_btn_photo)
	private ImageView mPhotoBtn;
	@InjectView(R.id.edit_btn_camera)
	private ImageView mCameraBtn;
	@InjectView(R.id.edit_btn_emotion)
	private ImageView mEmotionBtn;
	@InjectView(R.id.edit_btn_at)
	private ImageView mAtBtn;
	@InjectView(R.id.edit_btn_sure)
	private Button mSubmitBtn;
	@InjectView(R.id.edit_emotion_grid)
	private GridView mEmotionGrid;
	@InjectView(R.id.reply_container)
	private View mContainer;

	private String editContent;
	private String editTopic;
	private File mCurrentPhotoFile;
	private String faceGroupChooseString = "face7.gif";
	private String picLink;
	@Inject
	private ICC98Service service;
	@Inject
	private ICC98UrlManager manager;

	// group.clearCheck();
	// int cursor = replyContentEditText.getSelectionStart();
	// replyContentEditText.getText().insert(cursor, emotChooseString);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupTailContent();
		configureActionBar();
		setupListeners();
		mEmotionGrid.setAdapter(new EmotionGridViewAdapter(this));
	}

	/**
	 * 
	 */
	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setLogo(new BitmapDrawable(service.getUserAvatar()));
		if (mod == MOD_REPLY) {
			actionBar.setTitle("回复：" + postName);
		} else if (mod == MOD_QUOTE_REPLY) {
			actionBar.setTitle("回复：" + postName);
			replyUserPostContent = replyUserPostContent.replaceAll(
					"<.*?>|searchubb.*?;", "");
			replyContentEditText.setText(generateQuoteContent());
		} else if (mod == MOD_NEW_POST) {
			actionBar.setTitle("发表新话题：" + boardName);
		} else if (mod == MOD_PM) {
			actionBar.setTitle("站短：" + pmReplyName);
			pmReplyTopic = pmReplyTopic == null ? "" : "回复: " + pmReplyTopic;
			replyContentEditText.setText(pmReplyContent);
			replyTitleEditText.setText(pmReplyTopic);
		} else if (mod == MOD_EDIT) {
			replyContentEditText.setText(editContent);
			replyTitleEditText.setText(editTopic);
		}
	}


	  private void lockContainerHeight(int paramInt)
	  {
	    LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.mContainer.getLayoutParams();
	    localLayoutParams.height = paramInt;
	    localLayoutParams.weight = 0.0F;
	    unlockContainerHeightDelayed();
 	  }

	  public void unlockContainerHeightDelayed()
	  {
	    this.mEmotionGrid.postDelayed(new Runnable()
	    {
	      public void run()
	      {
	        ((LinearLayout.LayoutParams)mContainer.getLayoutParams()).weight = 1.0F;
	      }
	    }
	    , 200L);
	  }

	/**
	 * 
	 */
	private void setupTailContent() {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean showTail = sharedPref.getBoolean(SettingsActivity.SHOW_TAIL,
				true);
		boolean useCustomTail = sharedPref.getBoolean(
				SettingsActivity.USE_CUSTOM_TAIL, false);
		String customTailContent = sharedPref.getString(
				SettingsActivity.CUSTOM_TAIL_CONTENT, "");
		if (!showTail) {
			appendTail = "";
		} else if (!useCustomTail) {
			appendTail = TAIL;
		} else {
			appendTail = "\n\n[right][color=gray]" + customTailContent
					+ "[/color][/right]";
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu optionMenu) {
		optionMenu.add(android.view.Menu.NONE, MENU_REPLY_ID, 1, "预览")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case MENU_REPLY_ID:
			startActivity(new Intent(EditActivity.this, PreviewActivity.class)
					.putExtra(PreviewActivity.CONTENT, replyContentEditText
							.getText().toString()));
			// ensure();
			return true;
		default:
			break;
		}
		return false;
	}

	/**
	 * 
	 */
	private void ensure() {
		final String titleString = replyTitleEditText.getText().toString();
		final String contentString = replyContentEditText.getText().toString()
				+ appendTail;
		if (TextHelper.isEmpty(contentString)) {
			ToastUtils.show(EditActivity.this, "内容不能为空");
			return;
		}
		if (!checkReplyContentLimit(titleString, contentString)) {
			return;
		}
		if (mod == MOD_QUOTE_REPLY) {
			setupRefDialog(titleString, contentString);
		} else if (mod == MOD_REPLY) {
			PushReplyTask task = new PushReplyTask(EditActivity.this, postId,
					boardID, contentString, titleString, faceGroupChooseString,
					false);
			task.execute();
		} else if (mod == MOD_NEW_POST) {
			PushReplyTask task = new PushReplyTask(EditActivity.this, null,
					boardID, contentString, titleString, faceGroupChooseString,
					true);
			task.execute();
		} else if (mod == MOD_PM) {
			new SendPMTask(EditActivity.this, pmReplyName, titleString,
					contentString).execute();
		} else if (mod == MOD_EDIT) {
			// submitEdit(editLink, titleString, contentString);
		}
	}

	private void setupRefDialog(final String titleString,
			final String contentString) {
		AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
		builder.setTitle("提示");
		builder.setMessage("是否给用户：" + replyUserName + " 发送引用通知？");
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String pmTitle = "用户：" + service.getUserName() + " 在帖子中回复了你";
				String pmContent = "详情请点击："
						+ manager.getPostUrl(boardID, postId, quotePageNumber)
						+ "#" + quoteFloorNumber;
				new SendPMTask(EditActivity.this, pmReplyName, pmTitle,
						pmContent).execute();
				PushReplyTask task = new PushReplyTask(EditActivity.this,
						postId, boardID, contentString, titleString,
						faceGroupChooseString, false);
				task.execute();
			}
		});
		builder.setNegativeButton("不", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PushReplyTask task = new PushReplyTask(EditActivity.this,
						postId, boardID, contentString, titleString,
						faceGroupChooseString, false);
				task.execute();
			}
		});
		builder.show();
	}

	/**
	 * @return
	 */
	private String generateQuoteContent() {
		return "[quote][b]以下是引用[i]" + replyUserName + "在" + replyUserPostTime
				+ "[/i]的发言：[/b]\n"
				+ replyUserPostContent.replaceAll("<BR>|<br>", "\r\n")
				+ "[/quote]\n";
	}

	private void setupListeners() {
		mAtBtn.setOnClickListener(this);
		mCameraBtn.setOnClickListener(this);
		mEmotionBtn.setOnClickListener(this);
		mPhotoBtn.setOnClickListener(this);
		mSubmitBtn.setOnClickListener(this);
		replyTitleEditText.setOnClickListener(this);
		replyContentEditText.setOnClickListener(this);
		mEmotionGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int cursor = replyContentEditText.getSelectionStart();
				if (position < 10) {
					replyContentEditText.getText().insert(cursor,
							"[em0" + position + "]");
				} else {
					replyContentEditText.getText().insert(cursor,
							"[em" + position + "]");
				}
			}
		});
//		replyContentEditText
//				.setOnFocusChangeListener(new OnFocusChangeListener() {
//					@Override
//					public void onFocusChange(View v, boolean hasFocus) {
//						if (hasFocus) {
//							getSupportActionBar().hide();
//							// ViewUtils.setGone(mEmotionGrid, true);
//						} else {
//							// if
//							// (!(EditActivity.this.getCurrentFocus()==mEmotionBtn))
//							// {
//							getSupportActionBar().show();
//							// }
//						}
//					}
//				});

		faceRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.face1:
							faceGroupChooseString = "face1.gif";
							break;
						case R.id.face2:
							faceGroupChooseString = "face2.gif";
							break;
						case R.id.face3:
							faceGroupChooseString = "face3.gif";
							break;
						case R.id.face4:
							faceGroupChooseString = "face4.gif";
							break;
						case R.id.face5:
							faceGroupChooseString = "face5.gif";
							break;
						case R.id.face6:
							faceGroupChooseString = "face6.gif";
							break;
						case R.id.face7:
							faceGroupChooseString = "face7.gif";
							break;
						case R.id.face8:
							faceGroupChooseString = "face8.gif";
							break;
						case R.id.face9:
							faceGroupChooseString = "face9.gif";
							break;
						case R.id.face10:
							faceGroupChooseString = "face10.gif";
							break;
						case R.id.face11:
							faceGroupChooseString = "face11.gif";
							break;
						case R.id.face12:
							faceGroupChooseString = "face12.gif";
							break;
						case R.id.face13:
							faceGroupChooseString = "face13.gif";
							break;
						case R.id.face14:
							faceGroupChooseString = "face14.gif";
							break;
						case R.id.face15:
							faceGroupChooseString = "face15.gif";
							break;
						case R.id.face16:
							faceGroupChooseString = "face16.gif";
							break;
						case R.id.face17:
							faceGroupChooseString = "face17.gif";
							break;
						case R.id.face18:
							faceGroupChooseString = "face18.gif";
							break;
						case R.id.face19:
							faceGroupChooseString = "face19.gif";
							break;
						case R.id.face20:
							faceGroupChooseString = "face20.gif";
							break;
						case R.id.face21:
							faceGroupChooseString = "face21.gif";
							break;
						case R.id.face22:
							faceGroupChooseString = "face22.gif";
							break;

						default:
							break;
						}
					}
				});
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		ViewUtils.setGone(mEmotionGrid, true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_btn_camera:
			doTakePhoto();
			break;
		case R.id.edit_btn_photo:
			doPickPhotoFromGallery();
			break;
		case R.id.edit_btn_sure:
			ensure();
			break;
		case R.id.edit_btn_at:
			int cursor = replyContentEditText.getSelectionStart();
			replyContentEditText.getText().insert(cursor, "@");
			break;
		case R.id.edit_btn_emotion:
			getSupportActionBar().hide();
			if (mEmotionGrid.getVisibility() == View.GONE) {
				hideKeyBoard();
				mEmotionBtn.postDelayed((new Runnable() {
					@Override
					public void run() {
						ViewUtils.setGone(mEmotionGrid, false);
					}
				}), 200L);
				
			} else {
				ViewUtils.setGone(mEmotionGrid, true);
				showKeyBoard();
			}
			break;
		case R.id.reply_content:
		case R.id.reply_title_edit:
 			ViewUtils.setGone(mEmotionGrid, true);
			break;
		default:
			break;
		}
	}

 	private void hideKeyBoard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(this
				.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
 	private void showKeyBoard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(getCurrentFocus(), 0);
	}

	private boolean checkReplyContentLimit(String title, String content) {
		if (title.getBytes().length > TITLE_MAX_LENGTH) {
			ToastUtils.show(this, "标题长度超过限制，请修改");
			return false;
		}
		if (content.getBytes().length > CONTENT_MAX_LENGTH) {
			ToastUtils.show(this, "内容超过限制，请修改");
			return false;
		}
		return true;
	}

	private void doTakePhoto() {
		try {
			PHOTO_DIR.mkdirs();// 创建照片的存储目录
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"'IMG'_yyyy-MM-dd HH-mm-ss");
			mCurrentPhotoFile = new File(PHOTO_DIR, dateFormat.format(date)
					+ ".jpg");
			mCurrentPhotoFile.createNewFile();
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);

		} catch (ActivityNotFoundException e) {
			ToastUtils.show(this, "未找到目标！");
		} catch (IOException e) {
			ToastUtils.show(this, "未知错误");
		}
	}

	private void doPickPhotoFromGallery() {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);

		} catch (ActivityNotFoundException e) {
			ToastUtils.show(this, "未找到目标！");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mEmotionGrid.getVisibility() == View.VISIBLE) {
				ViewUtils.setGone(mEmotionGrid, true);
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA:
			Uri uri = data.getData();
			ContentResolver cr = this.getContentResolver();
			Cursor cursor = cr.query(uri, null, null, null, null);
			cursor.moveToFirst();
			String picURI = cursor.getString(cursor.getColumnIndex("_data"));
			// doUploadPicture(new File(picURI));
			mCurrentPhotoFile = new File(picURI);
			try {
				while (mCurrentPhotoFile.length() > MAX_IMAGE_SIZE_IN_BYTE) {
					doCompressPhoto();
				}
				doUploadPicture(mCurrentPhotoFile);
			} catch (IOException e) {
				ToastUtils.show(this, "照片处理失败");
				e.printStackTrace();
			}
			break;
		case CAMERA_WITH_DATA:
			try {
				while (mCurrentPhotoFile.length() > MAX_IMAGE_SIZE_IN_BYTE) {
					doCompressPhoto();
				}
				doUploadPicture(mCurrentPhotoFile);
			} catch (IOException e) {
				ToastUtils.show(this, "照片处理失败");
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	private void doCompressPhoto() throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoFile.getPath(),
				options);
		options.inSampleSize = (int) Math.ceil(Math.sqrt(mCurrentPhotoFile
				.length() / MAX_IMAGE_SIZE_IN_BYTE));
		options.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(mCurrentPhotoFile.getPath(), options);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(mCurrentPhotoFile));
		bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

	private void doUploadPicture(File photo) {
		UpLoadPictureTask upTask = new UpLoadPictureTask(this, photo);
		upTask.execute();
	}

	private class UpLoadPictureTask extends ProgressRoboAsyncTask<String> {
		private File mUploadFile;
		@Inject
		private ICC98Service mService;

		protected UpLoadPictureTask(Activity context, File file) {
			super(context);
			mUploadFile = file;
		}

		@Override
		protected void onPreExecute() throws Exception {
			dialog.setMessage("正在上传...");
			super.onPreExecute();
		}

		@Override
		public String call() throws Exception {
			return mService.uploadFile(mUploadFile);
		}

		@Override
		protected void onSuccess(String result) {
			picLink = result;
			int cursor = replyContentEditText.getSelectionStart();
			replyContentEditText.getText().insert(cursor, picLink);
		}

		@Override
		protected void onException(Exception e) {
			super.onException(e);
			ToastUtils.show(context, "上传图片失败，请检查网络或图片");
		}
	}

	private class PushReplyTask extends ProgressRoboAsyncTask<Void> {
		private String mPostId;
		private String mBoardID;
		private String mContent;
		private String mTitle;
		private String mFaceExpression;
		private boolean mIsNewPost;
		@Inject
		private ICC98Service mService;

		protected PushReplyTask(Activity context, String postId,
				String boardId, String content, String title, String face,
				boolean isNewPost) {
			super(context);
			mPostId = postId;
			mBoardID = boardId;
			mContent = content;
			mTitle = title;
			mFaceExpression = face;
			mIsNewPost = isNewPost;
		}

		@Override
		protected void onPreExecute() throws Exception {
			super.onPreExecute();
			dialog.setMessage("正在发表...");
		}

		@Override
		public Void call() throws Exception {
			if (mIsNewPost) {
				mService.pushNewPost(mBoardID, mTitle, mFaceExpression,
						mContent);
			} else {
				mService.reply(mBoardID, mPostId, mTitle, mFaceExpression,
						mContent);
			}
			return null;
		}

		@Override
		protected void onSuccess(Void v) {
			ToastUtils.show(context, "发表成功");
			setResult(Activity.RESULT_OK);
			context.finish();
		}

		@Override
		protected void onException(Exception e) {
			ToastUtils.show(context, "请求失败，请检查网络或连接");
		}

	}

	private class SendPMTask extends ProgressRoboAsyncTask<Void> {
		private String mToUser;
		private String mContent;
		private String mTitle;
		@Inject
		private ICC98Service mService;

		protected SendPMTask(Activity context, String toUser, String title,
				String content) {
			super(context);
			mToUser = toUser;
			mContent = content;
			mTitle = title;
		}

		@Override
		protected void onPreExecute() throws Exception {
			super.onPreExecute();
			dialog.setMessage("正在发送站短...");
		}

		@Override
		public Void call() throws Exception {
			mService.sendPm(mToUser, mTitle, mContent);
			return null;
		}

		@Override
		protected void onSuccess(Void v) throws Exception {
			super.onSuccess(v);
			ToastUtils.show(context, "发送站短成功");
			if (mod == MOD_PM) {
				setResult(Activity.RESULT_OK);
				context.finish();
			}
		}

		@Override
		protected void onException(Exception e) {
			super.onException(e);
			ToastUtils.show(context, "发送失败，请检查网络连接");
		}
	}

}
