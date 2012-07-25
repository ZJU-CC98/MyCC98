package tk.djcrazy.MyCC98;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import tk.djcrazy.MyCC98.dialog.MoreEmotChooseDialog;
import tk.djcrazy.MyCC98.dialog.MoreEmotChooseDialog.FaceExpressionChooseListener;
import tk.djcrazy.MyCC98.helper.TextHelper;
import tk.djcrazy.MyCC98.task.GenericTask;
import tk.djcrazy.MyCC98.task.TaskAdapter;
import tk.djcrazy.MyCC98.task.TaskListener;
import tk.djcrazy.MyCC98.task.TaskParams;
import tk.djcrazy.MyCC98.task.TaskResult;
import tk.djcrazy.libCC98.CC98Client;
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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

/**
 * 
 * @author DJ
 * 
 */
public class PushNewPostActivity extends BaseActivity {

	private static final int TITLE_MAX_LENGTH = 100;

	private static final int CONTENT_MAX_LENGTH = 16240;

	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;

	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;

	/* 拍照的照片存储位置 */
	private static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/Camera");
	/* 照片最大尺寸不能超过500KB */
	private static final long MAX_IMAGE_SIZE_IN_BYTE = 500 * 1024;

	private File mCurrentPhotoFile;// 照相机拍照得到的图片

	private String picLink;

	private int boardID;

	private String boardName;

	private String faceGroupChooseString;

	private Button insertFaceExpression;

	private Button pushEnsure;

	private Button upLoadButton;

	private EditText postTitleEditText;

	private EditText postContent;

	private Button backButton;

	private RadioGroup faceRadioGroup;

	private RadioGroup quickEmotGroupOne;

	private RadioGroup quickEmotGroupTwo;

	private String emotChooseString;

	private GenericTask pushNewPostTask;

	private ProgressDialog dialog;

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
			case R.id.quick_insert_em12:
				emotChooseString = "[em11]";
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
			int cursor = postContent.getSelectionStart();
			postContent.getText().insert(cursor, emotChooseString);
		}
	};

	private TaskListener mUploadPictureListener = new TaskAdapter() {

		@Override
		public void onPreExecute(GenericTask task) {
			dialog = ProgressDialog.show(PushNewPostActivity.this, "",
					"正在上传...", true);
			dialog.show();
		}

		@Override
		public void onProgressUpdate(GenericTask task, Object param) {

		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.OK) {
				dialog.dismiss();
				Toast.makeText(PushNewPostActivity.this, "文件上传成功^_^",
						Toast.LENGTH_SHORT).show();
				int cursor = postContent.getSelectionStart();
				postContent.getText().insert(cursor, picLink);
			} else {
				dialog.dismiss();
				Toast.makeText(PushNewPostActivity.this, "文件上传失败T_T",
						Toast.LENGTH_SHORT).show();

			}
		}

		@Override
		public String getName() {

			return "UploadPicture";
		}
	};

	private TaskListener pushNewPostListener = new TaskAdapter() {

		@Override
		public void onPreExecute(GenericTask task) {
			dialog = ProgressDialog.show(PushNewPostActivity.this, "",
					"Submit Reply...", true);
			dialog.show();
		}

		@Override
		public void onProgressUpdate(GenericTask task, Object param) {

		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.OK) {
				onPushSuccess();
			} else {
				onPushFailure("failed");
			}
		}

		@Override
		public String getName() {

			return "Login";
		}
	};

	private FaceExpressionChooseListener faceListener;

	@Override
 	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 		setContentView(R.layout.push_new_post);
		Bundle bundle = getIntent().getBundleExtra("pushNewPost");
		boardID = bundle.getInt("boardID");
		boardName = bundle.getString("boardName");
 		findViews();
		setupListeners();
	}

	/**
     * 
     */
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

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setResult(Activity.RESULT_CANCELED);
				PushNewPostActivity.this.finish();
			}
		});

		pushEnsure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String titleString = postTitleEditText.getText().toString();
				String contentString = postContent.getText().toString();

				if (pushNewPostTask != null
						&& pushNewPostTask.getStatus() == GenericTask.Status.RUNNING) {
					return;
				} else {
					if (!TextHelper.isEmpty(contentString)) {
						pushNewPostTask = new PushNewPostTask();
						pushNewPostTask.setListener(pushNewPostListener);

						TaskParams params = new TaskParams();
						params.put("boardID", boardID);
						params.put("content", contentString);
						params.put("title", titleString);
						params.put("faceExpression", faceGroupChooseString);
						pushNewPostTask.execute(params);
					}
				}
			}
		});

		postTitleEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				if (s.toString().getBytes().length > TITLE_MAX_LENGTH) {
					Toast.makeText(PushNewPostActivity.this,
							R.string.title_length_overflow, Toast.LENGTH_SHORT).show();
				}

			}
		});

		postContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				if (s.toString().getBytes().length > CONTENT_MAX_LENGTH) {
					Toast.makeText(PushNewPostActivity.this,
							R.string.post_content_length_overflow,
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		insertFaceExpression.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MoreEmotChooseDialog dialog = new MoreEmotChooseDialog(
						PushNewPostActivity.this, faceListener);
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

	/**
     * 
     */
	private void findViews() {
		postContent = (EditText) findViewById(R.id.push_new_post_content);
		postTitleEditText = (EditText) findViewById(R.id.push_new_post_title);
		insertFaceExpression = (Button) findViewById(R.id.push_new_post_insert_expression_button);
		pushEnsure = (Button) findViewById(R.id.push_new_post_top_ensure);
		backButton = (Button) findViewById(R.id.push_new_post_top_back);
		faceRadioGroup = (RadioGroup) findViewById(R.id.face_choose_radio_group);
		upLoadButton = (Button) findViewById(R.id.push_new_post_upload_image_button);
		quickEmotGroupOne = (RadioGroup) findViewById(R.id.quick_emot_group_one);
		quickEmotGroupTwo = (RadioGroup) findViewById(R.id.quick_emot_group_two);
	}

	private void onPushFailure(String string) {

		dialog.dismiss();
		Toast.makeText(PushNewPostActivity.this, "发表失败，请检查！",
				Toast.LENGTH_SHORT).show();
	}

	protected void onPushSuccess() {

		dialog.dismiss();
		Toast.makeText(PushNewPostActivity.this, "发表成功！", Toast.LENGTH_SHORT)
				.show();
		setResult(Activity.RESULT_OK);
		this.finish();
	}

	private void doPickPhotoAction() {
		final Context context = PushNewPostActivity.this;

		// Wrap our context to inflate list items using correct theme
		final Context dialogContext = new ContextThemeWrapper(context,
				android.R.style.Theme_Light);
		String[] choices;
		choices = new String[3];
		choices[0] = "拍照"; // 拍照
		choices[1] = "本地上传"; // 从相册中选择
		choices[2] = "取消";
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
							if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
								doTakePhoto();// 用户点击了从照相机获取
							} else {
								Toast.makeText(context, "您的设备没有SD卡！",
										Toast.LENGTH_SHORT).show();
							}
							break;

						}
						case 1:
							doPickPhotoFromGallery();// 从相册中去获取
							break;

						case 2:
							break;
						}
					}
				});
		builder.create().show();
	}

	/**
	 * 拍照获取图片
	 * 
	 */
	protected void doTakePhoto() {
		try {

			PHOTO_DIR.mkdirs();// 创建照片的存储目录
			mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "未找到目标！", Toast.LENGTH_LONG).show();
		}
	}

	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	/**
	 * 用当前时间给取得的图片命名
	 * 
	 */
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date) + ".jpg";
	}

	// 请求Gallery程序
	protected void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			final Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, " ", Toast.LENGTH_LONG).show();
		}
	}

	// 封装请求Gallery的intent
	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		// intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", 1);
		// intent.putExtra("aspectY", 1);
		// intent.putExtra("outputX", 80);
		// intent.putExtra("outputY", 80);
		// intent.putExtra("return-data", true);
		return intent;
	}

	// 因为调用了Camera和Gally所以要判断他们各自的返回情况,他们启动时是这样的startActivityForResult
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
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
		case CAMERA_WITH_DATA: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
			try {
				if (mCurrentPhotoFile.length() > MAX_IMAGE_SIZE_IN_BYTE) {

					doCompressPhoto();
				}
				doUploadPicture(mCurrentPhotoFile);
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "照片处理失败Q_Q",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

			break;
		}
		}
	}

	private void doCompressPhoto() throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoFile.getPath(),
				options);
		options.inSampleSize = (int) Math.ceil(Math.sqrt(mCurrentPhotoFile
				.length() / MAX_IMAGE_SIZE_IN_BYTE));
		bmp = BitmapFactory.decodeFile(mCurrentPhotoFile.getPath(), options);
		BufferedOutputStream bos = null;
		bos = new BufferedOutputStream(new FileOutputStream(mCurrentPhotoFile));
		bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

	private void doUploadPicture(File photo) {

		upLoadPictureTask upTask = new upLoadPictureTask();
		upTask.setListener(mUploadPictureListener);

		TaskParams params = new TaskParams();
		params.put("picture", photo);
		upTask.execute(params);

	}

 
 
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				int cursor = postContent.getSelectionStart();
				postContent.getText().insert(cursor, emotChooseString);
				break;
			default:
				break;
			}
		}
	};

	private class upLoadPictureTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			TaskResult mResult = TaskResult.FAILED;
			TaskParams param = params[0];
			try {
				picLink = CC98Client.uploadPictureToCC98((File) param
						.get("picture"));
				if (picLink != null) {
					mResult = TaskResult.OK;
				} else {
					mResult = TaskResult.FAILED;
				}

			} catch (PatternSyntaxException e) {
				e.printStackTrace();
				mResult = TaskResult.FAILED;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				mResult = TaskResult.FAILED;
			} catch (IOException e) {
				e.printStackTrace();
				mResult = TaskResult.FAILED;
			}
			return mResult;
		}
	}

	private class PushNewPostTask extends GenericTask {

		int boardID;

		String content;

		String title;

		String faceExpression;

		List<NameValuePair> nvpsList;

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {

			TaskParams param = params[0];

			try {
				boardID = param.getInt("boardID");
				content = param.getString("content");
				title = param.getString("title");
				faceExpression = param.getString("faceExpression");

				System.out.println("content:" + content + "  " + "title:"
						+ title + " " + "face:" + faceExpression);
				configPushParams();
				if (CC98Client.pushNewPost(nvpsList, String.valueOf(boardID))) {
					return TaskResult.OK;
				} else {
					return TaskResult.FAILED;
				}
			} catch (Exception e) {

				return TaskResult.FAILED;
			}
		}

		private void configPushParams() {

			nvpsList = new ArrayList<NameValuePair>();
			nvpsList.add(new BasicNameValuePair("upfilername", ""));
			nvpsList.add(new BasicNameValuePair("subject", title));
			nvpsList.add(new BasicNameValuePair("Expression", faceExpression));
			nvpsList.add(new BasicNameValuePair("Content", content));
			nvpsList.add(new BasicNameValuePair("signflag", "yes"));
			nvpsList.add(new BasicNameValuePair("Submit", "发 表"));

		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(R.anim.backward_activity_move_in,
					R.anim.backward_activity_move_out);
			return true;
		}
		return false;
	}

}
