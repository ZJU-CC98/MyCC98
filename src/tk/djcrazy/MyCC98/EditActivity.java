package tk.djcrazy.MyCC98;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.PatternSyntaxException;

import org.apache.http.client.ClientProtocolException;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.dialog.MoreEmotChooseDialog;
import tk.djcrazy.MyCC98.dialog.MoreEmotChooseDialog.FaceExpressionChooseListener;
import tk.djcrazy.MyCC98.helper.TextHelper;
import tk.djcrazy.MyCC98.task.GenericTask;
import tk.djcrazy.MyCC98.task.TaskAdapter;
import tk.djcrazy.MyCC98.task.TaskListener;
import tk.djcrazy.MyCC98.task.TaskParams;
import tk.djcrazy.MyCC98.task.TaskResult;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.MyCC98.view.HeaderView;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.ICC98UrlManager;
import tk.djcrazy.libCC98.exception.ParseContentException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.google.inject.Inject;

@ContentView(R.layout.reply)
public class EditActivity extends BaseActivity {

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
			+ Build.MODEL + " via MyCC98[/color][/right]";;

	private static final String TAG = "EditActivity";
	private static final int TITLE_MAX_LENGTH = 100;
	private static final int CONTENT_MAX_LENGTH = 16240;
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 照片最大尺寸不能超过500KB */
	private static final long MAX_IMAGE_SIZE_IN_BYTE = 500 * 1024;
	/**
	 * get image from sketch
	 */
	public static final int GET_SKETCH = 3022;

	/* 拍照的照片存储位置 */
	private static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/Camera");

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

	@InjectView(R.id.reply_ensure_btn)
	private Button ensureButton;
	@InjectView(R.id.reply_header_userimg)
	private ImageView userHeader;
	@InjectView(R.id.reply_header_title)
	private TextView headerTitle;
	@InjectView(R.id.insert_expression_button)
	private View insertFaceExpression;
	@InjectView(R.id.upload_image_button)
	private View upLoadButton;
	@InjectView(R.id.reply_title_edit)
	private EditText replyTitleEditText;
	@InjectView(R.id.reply_content)
	private EditText replyContentEditText;
	@InjectView(R.id.preview_reply)
	private View previewButton;
	@InjectView(R.id.face_choose_radio_group)
	private RadioGroup faceRadioGroup;
	@InjectView(R.id.quick_emot_group_one)
	private RadioGroup quickEmotGroupOne;
	@InjectView(R.id.quick_emot_group_two)
	private RadioGroup quickEmotGroupTwo;

	private String emotChooseString;
	@InjectExtra(value=PM_TO_USER, optional=true)
	private String pmReplyName;
	@InjectExtra(value=PM_TITLE, optional=true)
	private String pmReplyTopic;
	@InjectExtra(value=PM_CONTENT, optional=true)
	private String pmReplyContent;
	
	private String editContent;
	private String editTopic;
	private File mCurrentPhotoFile;
	private String faceGroupChooseString;
	private String picLink;
	@Inject
	private ICC98Service service;
	@Inject
	private ICC98UrlManager manager;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int cursor = replyContentEditText.getSelectionStart();
			replyContentEditText.getText().insert(cursor, emotChooseString);
		}
	};
	private OnCheckedChangeListener emotChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			switch (checkedId) {
			case R.id.quick_insert_em00:
				emotChooseString = "[em00]";
				break;
			case R.id.quick_insert_em01:
				emotChooseString = "[em01]";
				break;
			case R.id.quick_insert_em02:
				emotChooseString = "[em02]";
				break;
			case R.id.quick_insert_em03:
				emotChooseString = "[em03]";
				break;
			case R.id.quick_insert_em04:
				emotChooseString = "[em04]";
				break;
			case R.id.quick_insert_em05:
				emotChooseString = "[em05]";
				break;
			case R.id.quick_insert_em06:
				emotChooseString = "[em06]";
				break;
			case R.id.quick_insert_em07:
				emotChooseString = "[em07]";
				break;
			case R.id.quick_insert_em08:
				emotChooseString = "[em08]";
				break;
			case R.id.quick_insert_em09:
				emotChooseString = "[em09]";
				break;
			case R.id.quick_insert_em10:
				emotChooseString = "[em10]";
				break;
			case R.id.quick_insert_em11:
				emotChooseString = "[em11]";
				break;
			case R.id.quick_insert_em12:
				emotChooseString = "[em12]";
				break;
			case R.id.quick_insert_em72:
				emotChooseString = "[em12]";
				break;
			case R.id.quick_insert_em73:
				emotChooseString = "[em73]";
				break;
			case R.id.quick_insert_em74:
				emotChooseString = "[em74]";
				break;
			case R.id.quick_insert_em75:
				emotChooseString = "[em75]";
				break;
			case R.id.quick_insert_em76:
				emotChooseString = "[em76]";
				break;
			case R.id.quick_insert_em77:
				emotChooseString = "[em77]";
				break;
			case R.id.quick_insert_em78:
				emotChooseString = "[em78]";
				break;
			case R.id.quick_insert_em79:
				emotChooseString = "[em79]";
				break;
			case R.id.quick_insert_em80:
				emotChooseString = "[em80]";
				break;
			case R.id.quick_insert_em81:
				emotChooseString = "[em81]";
				break;
			case R.id.quick_insert_em82:
				emotChooseString = "[em82]";
				break;
			case R.id.quick_insert_em83:
				emotChooseString = "[em83]";
				break;
			case R.id.quick_insert_em84:
				emotChooseString = "[em84]";
				break;
			case R.id.quick_insert_em85:
				emotChooseString = "[em85]";
				break;
			case R.id.quick_insert_em86:
				emotChooseString = "[em86]";
				break;
			case R.id.quick_insert_em87:
				emotChooseString = "[em87]";
				break;
			case R.id.quick_insert_em88:
				emotChooseString = "[em88]";
				break;
			case R.id.quick_insert_em89:
				emotChooseString = "[em89]";
				break;
			case R.id.quick_insert_em90:
				emotChooseString = "[em90]";
				break;
			case R.id.quick_insert_em91:
				emotChooseString = "[em91]";
				break;

			default:
				return;

			}

			group.clearCheck();
			int cursor = replyContentEditText.getSelectionStart();
			replyContentEditText.getText().insert(cursor, emotChooseString);
		}
	};

	private FaceExpressionChooseListener faceListener;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userHeader.setImageBitmap(service.getUserAvatar());
		if (mod == MOD_REPLY) {
			headerTitle.setText("回复："+postName);
		} else if (mod == MOD_QUOTE_REPLY) {
			headerTitle.setText("回复："+postName);
			replyUserPostContent = replyUserPostContent.replaceAll(
					"<.*?>|searchubb.*?;", "");
			replyContentEditText.setText("[quote][b]以下是引用[i]" + replyUserName + "在"
					+ replyUserPostTime + "[/i]的发言：[/b]\n"
					+ replyUserPostContent.replaceAll("<BR>|<br>", "\r\n")
					+ "[/quote]\n");
		} else if (mod == MOD_NEW_POST) {
			headerTitle.setText("发表新话题："+boardName);
		} else if (mod == MOD_PM) {
			headerTitle.setText("站短："+pmReplyName);
			pmReplyTopic = pmReplyTopic == null ? "" : "回复: " + pmReplyTopic;
			replyContentEditText.setText(pmReplyContent);
			replyTitleEditText.setText(pmReplyTopic);
		} else if (mod == MOD_EDIT) {
			replyContentEditText.setText(editContent);
			replyTitleEditText.setText(editTopic);
		}
		setupListeners();
	}

	private void setupListeners() {
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

		previewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(EditActivity.this,
						PreviewActivity.class).putExtra(
						PreviewActivity.CONTENT, replyContentEditText.getText()
								.toString()));
				overridePendingTransition(R.anim.forward_activity_move_in,
						R.anim.forward_activity_move_out);
			}
		});

		ensureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String titleString = replyTitleEditText.getText()
						.toString();
				final String contentString = replyContentEditText.getText().toString();
				if (TextHelper.isEmpty(contentString)) {
					ToastUtils.show(EditActivity.this, "内容不能为空");
					return;
				}
				if (!checkReplyContentLimit(titleString, contentString)) {
					return ;
				};
				if (mod == MOD_QUOTE_REPLY) {
					setupRefDialog(titleString, contentString);
				}
				else if (mod == MOD_REPLY) {
					PushReplyTask task = new PushReplyTask(EditActivity.this,
							postId, boardID, contentString, titleString,
							faceGroupChooseString);
					task.execute();
				} else if (mod == MOD_NEW_POST) {
					PushNewPostTask task = new PushNewPostTask(
							EditActivity.this, boardID, contentString,
							titleString, faceGroupChooseString);
					task.execute();
				} else if (mod == MOD_PM) {
					new SendPMTask(EditActivity.this, pmReplyName, titleString,
							contentString
									+ (SettingsActivity.addTail ? TAIL : ""))
							.execute();
				} else if (mod == MOD_EDIT) {
					// submitEdit(editLink, titleString, contentString);
				}
			}

			private void setupRefDialog(final String titleString,
					final String contentString) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						EditActivity.this);
				builder.setTitle("提示");
				builder.setMessage("是否给用户：" + replyUserName + " 发送引用通知？");
				builder.setPositiveButton("是",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String pmTitle = "用户：" + service.getUserName()+ " 在帖子中回复了你";
								String pmContent = "详情请点击："+ manager.getPostUrl(boardID,postId,
										quotePageNumber) + "#"+ quoteFloorNumber 
										+ (SettingsActivity.addTail ? TAIL: "");
								new SendPMTask(EditActivity.this,pmReplyName,pmTitle, pmContent)
								.execute();
								PushReplyTask task = new PushReplyTask(EditActivity.this,
										postId, boardID, contentString, titleString,
										faceGroupChooseString);
								task.execute();
  							}
						});
				builder.setNegativeButton("不",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								PushReplyTask task = new PushReplyTask(EditActivity.this,
										postId, boardID, contentString, titleString,
										faceGroupChooseString);
								task.execute();
							}
						});
				builder.show();
			}
		});
		insertFaceExpression.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MoreEmotChooseDialog dialog = new MoreEmotChooseDialog(
						EditActivity.this, faceListener);
				dialog.show();
			}
		});

		faceListener = new FaceExpressionChooseListener() {
			@Override
			public void onOkClick() {
			}

			@Override
			public void onFaceExpressionClick(String faceExpression) {
				emotChooseString = faceExpression;
				handler.sendEmptyMessage(0);
			}
		};
		upLoadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doPickPhotoAction();
			}
		});

		quickEmotGroupOne.setOnCheckedChangeListener(emotChangeListener);
		quickEmotGroupTwo.setOnCheckedChangeListener(emotChangeListener);
	}

	private void doPickPhotoAction() {
		final Context context = EditActivity.this;

		// Wrap our context to inflate list items using correct theme
		final Context dialogContext = new ContextThemeWrapper(context,
				android.R.style.Theme_Light);
		String[] choices = { "拍照", "本地上传", "涂鸦", "取消" };
 		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				dialogContext);
 		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0: {
							String status = Environment
									.getExternalStorageState();
							if (status.equals(Environment.MEDIA_MOUNTED)) {
								doTakePhoto();
							} else {
								ToastUtils
										.show(EditActivity.this, "您的设备没有SD卡！");
							}
							break;
						}
						case 1:
							doPickPhotoFromGallery();
							break;
						case 2:
							if (Environment.getExternalStorageState().equals(
									Environment.MEDIA_MOUNTED)) {
								uploadSketch();
							} else {
								ToastUtils
										.show(EditActivity.this, "您的设备没有SD卡！");
							}
							break;
						case 3:
							break;
						default:
							break;
						}
					}

				});
		builder.create().show();
	}

	private boolean checkReplyContentLimit(String title, String content) {
		if (title.getBytes().length > TITLE_MAX_LENGTH) {
			ToastUtils.show(this, "标题长度超过，请修改");
			return false;
		}
		if (content.getBytes().length > CONTENT_MAX_LENGTH) {
			ToastUtils.show(this, "内容超过限制，请修改");
			return false;
		}
		return true;
	}

	private void uploadSketch() {
		startActivityForResult(new Intent(this, SketchActivity.class),
				GET_SKETCH);
		overridePendingTransition(R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);
	}

	private void doTakePhoto() {
		try {
			PHOTO_DIR.mkdirs();// 创建照片的存储目录
			Log.d(TAG, "doTakePhoto");
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
			overridePendingTransition(R.anim.forward_activity_move_in,
					R.anim.forward_activity_move_out);
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
			overridePendingTransition(R.anim.forward_activity_move_in,
					R.anim.forward_activity_move_out);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, " ", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			Log.d(TAG, resultCode + "");
//			ToastUtils.show(this, "程序返回错误");
			return;
		}
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的
			Uri uri = data.getData();
			ContentResolver cr = this.getContentResolver();
			Cursor cursor = cr.query(uri, null, null, null, null);
			cursor.moveToFirst();
			String picURI = cursor.getString(cursor.getColumnIndex("_data"));
			doUploadPicture(new File(picURI));
			break;
		}
		case CAMERA_WITH_DATA: {// 照相机程序返回的
			Log.d(TAG, "Returned from camera.");
			try {
				while (mCurrentPhotoFile.length() > MAX_IMAGE_SIZE_IN_BYTE) {
					doCompressPhoto();
				}
				doUploadPicture(mCurrentPhotoFile);
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "照片处理失败Q_Q",
						Toast.LENGTH_SHORT).show();
				ToastUtils.show(this, "照片处理失败");
				e.printStackTrace();
			}
			break;
		}
		case GET_SKETCH:
			Log.d(TAG, data.getStringExtra(SketchActivity.FILE_PATH));
			doUploadPicture(new File(
					data.getStringExtra(SketchActivity.FILE_PATH)));
			break;
		default:
			break;
		}
	}

	private void doCompressPhoto() throws IOException {
		Log.d(TAG, "doCompress");
		Log.d(TAG, "FilePath: " + mCurrentPhotoFile.getPath());
		Log.d(TAG, mCurrentPhotoFile.getPath());
		Log.d(TAG, mCurrentPhotoFile.length() + "");
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
		Log.d(TAG, "doUploadPicture");
		upLoadPictureTask upTask = new upLoadPictureTask(this, photo);
		upTask.execute();
	}

	private class upLoadPictureTask extends RoboAsyncTask<String> {
		private Activity mContext;
		private File mUploadFile;
		private ProgressDialog mDialog;
		@Inject
		private ICC98Service mService;

		protected upLoadPictureTask(Activity context, File file) {
			super(context);
			mContext = context;
			mUploadFile = file;
		}

		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(mContext);
			mDialog.setMessage("正在上传...");
			mDialog.show();
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
			ToastUtils.show(mContext, "上传图片失败，请检查网络或图片");
		}

		@Override
		protected void onFinally() {
			if (mDialog.isShowing())
				mDialog.dismiss();
		}
	}

	private class PushReplyTask extends RoboAsyncTask<Void> {
		private Activity mContext;
		private String mPostId;
		private String mBoardID;
		private String mContent;
		private String mTitle;
		private String mFaceExpression;
		private ProgressDialog mDialog;
		@Inject
		private ICC98Service mService;

		protected PushReplyTask(Activity context, String postId,
				String boardId, String content, String title, String face) {
			super(context);
			mContext = context;
			mPostId = postId;
			mBoardID = boardId;
			mContent = content;
			mTitle = title;
			mFaceExpression = face;
		}

		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(mContext);
			mDialog.setMessage("正在回复...");
			mDialog.show();
		}

		@Override
		public Void call() throws Exception {
			mService.reply(mBoardID, mPostId, mTitle, mFaceExpression, mContent);
			return null;
		}

		@Override
		protected void onSuccess(Void v) {
			ToastUtils.show(mContext, "回复成功");
			setResult(Activity.RESULT_OK);
			mContext.finish();
			overridePendingTransition(R.anim.backward_activity_move_in,
					R.anim.backward_activity_move_out);
		}

		@Override
		protected void onException(Exception e) {
			ToastUtils.show(mContext, "请求失败，请检查网络或连接");
		}

		@Override
		protected void onFinally() {
			if (mDialog.isShowing())
				mDialog.dismiss();
		}

	}

	private class PushNewPostTask extends RoboAsyncTask<Void> {
		private Activity mContext;
		private String mBoardID;
		private String mContent;
		private String mTitle;
		private String mFaceExpression;
		private ProgressDialog mDialog;
		@Inject
		private ICC98Service mService;

		protected PushNewPostTask(Activity context, String boardId,
				String content, String title, String face) {
			super(context);
			mContext = context;
			mBoardID = boardId;
			mContent = content;
			mTitle = title;
			mFaceExpression = face;
		}

		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(mContext);
			mDialog.setMessage("正在发表主题...");
			mDialog.show();
		}

		@Override
		public Void call() throws Exception {
			mService.pushNewPost(mBoardID, mTitle, mFaceExpression, mContent);
			return null;
		}

		@Override
		protected void onSuccess(Void v) {
			ToastUtils.show(mContext, "发表成功");
			setResult(Activity.RESULT_OK);
			mContext.finish();
			overridePendingTransition(R.anim.backward_activity_move_in,
					R.anim.backward_activity_move_out);
		}

		@Override
		protected void onException(Exception e) {
			ToastUtils.show(mContext, "请求失败，请检查网络连接");
		}

		@Override
		protected void onFinally() {
			if (mDialog.isShowing())
				mDialog.dismiss();
		}
	}

	private class SendPMTask extends RoboAsyncTask<Void> {
		private Activity mContext;
		private String mToUser;
		private String mContent;
		private String mTitle;
		private ProgressDialog mDialog;
		@Inject
		private ICC98Service mService;

		protected SendPMTask(Activity context, String toUser, String title,
				String content) {
			super(context);
			mToUser = toUser;
			mContext = context;
			mContent = content;
			mTitle = title;
		}

		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(mContext);
			mDialog.setMessage("正在发送站短...");
			mDialog.show();
		}

		@Override
		public Void call() throws Exception {
			mService.sendPm(mToUser, mTitle, mContent);
			return null;
		}

		@Override
		protected void onSuccess(Void v) {
			ToastUtils.show(mContext, "发送站短成功");
			if (mod==MOD_PM) {
				setResult(Activity.RESULT_OK);
				mContext.finish();
				overridePendingTransition(R.anim.backward_activity_move_in,
						R.anim.backward_activity_move_out);
			}
		}

		@Override
		protected void onException(Exception e) {
			ToastUtils.show(mContext, "发送失败，请检查网络连接");
		}

		@Override
		protected void onFinally() {
			if (mDialog.isShowing())
				mDialog.dismiss();
		}
	}
}
