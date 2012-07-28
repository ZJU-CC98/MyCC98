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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import tk.djcrazy.MyCC98.dialog.MoreEmotChooseDialog;
import tk.djcrazy.MyCC98.dialog.MoreEmotChooseDialog.FaceExpressionChooseListener;
import tk.djcrazy.MyCC98.helper.TextHelper;
import tk.djcrazy.MyCC98.security.md5;
import tk.djcrazy.MyCC98.task.GenericTask;
import tk.djcrazy.MyCC98.task.TaskAdapter;
import tk.djcrazy.MyCC98.task.TaskListener;
import tk.djcrazy.MyCC98.task.TaskParams;
import tk.djcrazy.MyCC98.task.TaskResult;
import tk.djcrazy.MyCC98.view.HeaderView;
import tk.djcrazy.libCC98.CC98Client;
import android.R.bool;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

public class EditActivity extends BaseActivity {

	public static final int MOD_REPLY = 0;
	public static final int MOD_NEW_POST = 1;
	public static final int MOD_PM = 2;
	public static final int MOD_EDIT = 3;
	public static final String MOD = "mod";
	public static final String BUNDLE = "bundle";
	public static final String BOARD_ID = "boardID";
	public static final String BOARD_NAME = "boardName";
	public static final String POST_LINK = "postLink";
	public static final String POST_NAME = "postName";
	public static final String REPLY_USER_NAME = "replyUserName";
	public static final String REPLY_USER_POST_TIME = "replyUserPostTime";
	public static final String REPLY_CONTENT = "replyContent";
	public static final String FLOOR_NUMBER = "floorNumber";
	public static final String PAGE_NUMBER = "pageNumber";
	public static final String USER_IMAGE = "userImage";
	public static final String TO_USER = "touser";
	public static final String PM_CONTENT = "pmcontent";
	public static final String PM_TITLE = "pmtitle";
	public static final String EDIT_LINK = "editlink";
	public static final String EDIT_CONTENT = "editcontent";
	public static final String EDIT_TOPIC = "edittopic";

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

	private int mod;
	private int boardID;
	private String boardName;

	private File mCurrentPhotoFile;// 照相机拍照得到的图片

	private String postLink;

	private String postName;

	private String picLink;

	private String replyUserName;

	private String replyUserPostTime;

	private String replyUserPostContent;

	private String faceGroupChooseString;

	private int mQuoteFloorNumber;

	private int mQuotePageNumber;

	private View insertFaceExpression;

	private View upLoadButton;

	private EditText replyTitleEditText;

	private EditText replyContent;

	private View previewButton;

	private RadioGroup faceRadioGroup;

	private RadioGroup quickEmotGroupOne;

	private RadioGroup quickEmotGroupTwo;

	private HeaderView mHeaderView;
	private String emotChooseString;
	private GenericTask mReplyTask;
	private GenericTask pushNewPostTask;

	private ProgressDialog dialog;

	private String pmreplyid;
	private String pmreplytopic;
	private String pmreplycontent;

	private String editLink;
	private String editContent;
	private String editTopic;

	private boolean mIsQuoteUser = false;

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
			int cursor = replyContent.getSelectionStart();
			replyContent.getText().insert(cursor, emotChooseString);
		}
	};

	private TaskListener mUploadPictureListener = new TaskAdapter() {

		@Override
		public void onPreExecute(GenericTask task) {
			dialog = ProgressDialog
					.show(EditActivity.this, "", "正在上传...", true);
			dialog.show();
		}

		@Override
		public void onProgressUpdate(GenericTask task, Object param) {

		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.OK) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "文件上传成功！",
						Toast.LENGTH_SHORT).show();
				int cursor = replyContent.getSelectionStart();
				replyContent.getText().insert(cursor, picLink);
			} else {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "文件上传失败！",
						Toast.LENGTH_SHORT).show();

			}
		}

		@Override
		public String getName() {

			return "UploadPicture";
		}
	};

	private TaskListener mReplyListener = new TaskAdapter() {

		@Override
		public void onPreExecute(GenericTask task) {
			dialog = ProgressDialog.show(EditActivity.this, "",
					"Submit Reply...", true);
		}

		@Override
		public void onProgressUpdate(GenericTask task, Object param) {

		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.OK) {
				onReplySuccess();
			} else {
				onReplyFailure("failed");
			}
		}

		@Override
		public String getName() {

			return "Login";
		}
	};

	private FaceExpressionChooseListener faceListener;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply);
		Bundle bundle = getIntent().getBundleExtra(BUNDLE);
		mod = bundle.getInt(MOD, MOD_REPLY);
		findViews();
		mHeaderView.setUserImg(CC98Client.getLoginUserImg());
		mHeaderView.setButtonImageResource(R.drawable.reply_send_ico);
		if (mod == MOD_REPLY) {
			postLink = bundle.getString(POST_LINK);
			Log.d(TAG, postLink);
			postName = bundle.getString(POST_NAME);
			replyUserName = bundle.getString(REPLY_USER_NAME);
			mHeaderView.setTitle("发表回帖");
			mHeaderView.setTitleTextSize(12f);
			if (replyUserName != null) {
				mHeaderView.setTitle("回复->" + replyUserName);
				mIsQuoteUser = true;
				replyUserPostTime = bundle.getString(REPLY_USER_POST_TIME);
				replyUserPostContent = bundle.getString(REPLY_CONTENT);
				mQuoteFloorNumber = bundle.getInt(FLOOR_NUMBER);
				mQuotePageNumber = bundle.getInt(PAGE_NUMBER);
				replyUserPostContent = replyUserPostContent.replaceAll(
						"<.*?>|searchubb.*?;", "");
				replyContent.setText("[quote][b]以下是引用[i]" + replyUserName + "在"
						+ replyUserPostTime + "[/i]的发言：[/b]\n"
						+ replyUserPostContent.replaceAll("<BR>|<br>", "\r\n")
						+ "[/quote]\n");
			}
		} else if (mod == MOD_NEW_POST) {
			boardID = bundle.getInt(BOARD_ID);
			boardName = bundle.getString(BOARD_NAME);

			System.err.println("boardID:" + boardID);
			System.out.println("boardName:" + boardName);
			setTitle("发新帖: " + boardName);
			mHeaderView.setTitle("发新帖: " + boardName);
		} else if (mod == MOD_PM) {
			pmreplyid = bundle.getString(TO_USER);
			pmreplycontent = bundle.getString(PM_CONTENT);
			pmreplytopic = bundle.getString(PM_TITLE);
			pmreplytopic = pmreplytopic == null ? "" : "回复: " + pmreplytopic;
			mHeaderView.setTitle("站短" + pmreplyid);
			replyContent.setText(pmreplycontent);
			replyTitleEditText.setText(pmreplytopic);
		} else if (mod == MOD_EDIT) {
			editLink = bundle.getString(EDIT_LINK);
			editContent = bundle.getString(EDIT_CONTENT);
			editTopic = bundle.getString(EDIT_TOPIC);
			replyContent.setText(editContent);
			mHeaderView.setTitle("编辑帖子");
			replyTitleEditText.setText(editTopic);
		}
		setupListeners();
	}

	private TaskListener pushNewPostListener = new TaskAdapter() {

		@Override
		public void onPreExecute(GenericTask task) {
			dialog = ProgressDialog.show(EditActivity.this, "",
					"Submit Reply...", true);
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

	private void onPushFailure(String string) {

		dialog.dismiss();
		Toast.makeText(this, "发表失败，请检查！", Toast.LENGTH_SHORT).show();
	}

	protected void onPushSuccess() {

		dialog.dismiss();
		Toast.makeText(this, "发表成功！", Toast.LENGTH_SHORT).show();
		setResult(Activity.RESULT_OK);
		this.finish();
	}

	private void submitEdit(String editLink, String editTopic,
			String editContent) {

		List<NameValuePair> nvpsList = new ArrayList<NameValuePair>();
		nvpsList.add(new BasicNameValuePair("upfilerename", ""));
		nvpsList.add(new BasicNameValuePair("Expression", faceGroupChooseString));
		nvpsList.add(new BasicNameValuePair("subject", editTopic));
		nvpsList.add(new BasicNameValuePair("content", editContent));
		nvpsList.add(new BasicNameValuePair("followup", editLink.substring(
				editLink.indexOf("&id=") + 4, editLink.indexOf("&star"))));
		nvpsList.add(new BasicNameValuePair("username", CC98Client
				.getUserName()));
		nvpsList.add(new BasicNameValuePair("passwd", md5.MyMD5(CC98Client
				.getPasswd())));
		nvpsList.add(new BasicNameValuePair("signflag", "yes"));
		nvpsList.add(new BasicNameValuePair("TotalUseTable", "bbs2"));
		nvpsList.add(new BasicNameValuePair("star", editLink.substring(
				editLink.indexOf("&star=") + 6, editLink.indexOf("&bm"))));
		try {
			if (CC98Client.editPost(nvpsList,
					editLink.replace("editannounce", "SaveditAnnounce"))) {
				Toast.makeText(EditActivity.this, "编辑成功", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(EditActivity.this, "编辑失败", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			Toast.makeText(EditActivity.this, "编辑失败", Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
		}
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

		previewButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent().setClass(getApplicationContext(),
						PreviewActivity.class).putExtra(
						PreviewActivity.CONTENT,
						replyContent.getText().toString()));
				overridePendingTransition(R.anim.forward_activity_move_in,
						R.anim.forward_activity_move_out);
			}
		});

		mHeaderView.setButtonOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final String titleString = replyTitleEditText.getText()
						.toString();
				final String contentString = replyContent.getText().toString();

				if (mIsQuoteUser) {

					setupRefDialog(titleString, contentString);
				}

				else {
					if (mod == MOD_REPLY) {
						if (mReplyTask != null
								&& mReplyTask.getStatus() == GenericTask.Status.RUNNING) {
							return;
						} else {
							if (!TextHelper.isEmpty(contentString)) {
								mReplyTask = new PushReplyTask();
								mReplyTask.setListener(mReplyListener);

								TaskParams params = new TaskParams();
								params.put("postLink", postLink + "&page=");
								params.put("content", contentString);
								params.put("title", titleString);
								params.put("faceExpression",
										faceGroupChooseString);
								mReplyTask.execute(params);
							}
						}
					} else if (mod == MOD_NEW_POST) {
						if (pushNewPostTask != null
								&& pushNewPostTask.getStatus() == GenericTask.Status.RUNNING) {
							return;
						} else {
							if (!TextHelper.isEmpty(contentString)) {
								pushNewPostTask = new PushNewPostTask();
								pushNewPostTask
										.setListener(pushNewPostListener);

								TaskParams params = new TaskParams();
								params.put("boardID", boardID);
								params.put("content", contentString);
								params.put("title", titleString);
								params.put("faceExpression",
										faceGroupChooseString);
								pushNewPostTask.execute(params);
							}
						}
					} else if (mod == MOD_PM) {
						try {
							sendPm(pmreplyid, replyTitleEditText.getText()
									.toString(), replyContent.getText()
									.toString());
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (mod == MOD_EDIT) {
						submitEdit(editLink, titleString, contentString);
					}
				}
			}

			/**
			 * @param titleString
			 * @param contentString
			 */
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

								new Thread() {
									public void run() {
										try {
											CC98Client
													.sendPm(replyUserName,
															new StringBuilder(
																	30)
																	.append("用户：")
																	.append(CC98Client
																			.getUserName())
																	.append(" 在帖子中回复了你")
																	.toString(),
															new StringBuilder(
																	50)
																	.append("详情请点击：")
																	.append(postLink)
																	.append("&star=")
																	.append(mQuotePageNumber)
																	.append("#")
																	.append(mQuoteFloorNumber)
																	.append("\n\n\n[right]此消息由[url=10.110.19.123/mycc98/intro.html][color=red]MyCC98[/color][/url]发送[/right]")
																	.toString());
										} catch (ClientProtocolException e) {
											e.printStackTrace();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}.start();

								if (mReplyTask != null
										&& mReplyTask.getStatus() == GenericTask.Status.RUNNING) {
									return;
								} else {
									if (!TextHelper.isEmpty(contentString)) {
										mReplyTask = new PushReplyTask();
										mReplyTask.setListener(mReplyListener);

										TaskParams params = new TaskParams();
										params.put("postLink", postLink
												+ "&page=");
										params.put("content", contentString);
										params.put("title", titleString);
										params.put("faceExpression",
												faceGroupChooseString);
										mReplyTask.execute(params);
									}
								}

							}
						});

				builder.setNegativeButton("不",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (mReplyTask != null
										&& mReplyTask.getStatus() == GenericTask.Status.RUNNING) {
									return;
								} else {
									if (!TextHelper.isEmpty(contentString)) {
										mReplyTask = new PushReplyTask();
										mReplyTask.setListener(mReplyListener);

										TaskParams params = new TaskParams();
										params.put("postLink", postLink
												+ "&page=");
										params.put("content", contentString);
										params.put("title", titleString);
										params.put("faceExpression",
												faceGroupChooseString);
										mReplyTask.execute(params);
									}
								}

							}
						});
				builder.show();
			}
		});
 
		replyTitleEditText.addTextChangedListener(new TextWatcher() {

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
					Toast.makeText(EditActivity.this,
							R.string.title_length_overflow, Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

		replyContent.addTextChangedListener(new TextWatcher() {

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
					Toast.makeText(EditActivity.this,
							R.string.post_content_length_overflow,
							Toast.LENGTH_SHORT).show();
				}
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

	/**
     * 
     */
	private void findViews() {
		replyContent = (EditText) findViewById(R.id.reply_content);
		replyTitleEditText = (EditText) findViewById(R.id.reply_title_edit);
		insertFaceExpression = findViewById(R.id.insert_expression_button);
		previewButton = findViewById(R.id.preview_reply);
		faceRadioGroup = (RadioGroup) findViewById(R.id.face_choose_radio_group);
		upLoadButton = findViewById(R.id.upload_image_button);
		quickEmotGroupOne = (RadioGroup) findViewById(R.id.quick_emot_group_one);
		quickEmotGroupTwo = (RadioGroup) findViewById(R.id.quick_emot_group_two);
		mHeaderView = (HeaderView) findViewById(R.id.main_header);

	}

	private void onReplyFailure(String string) {

		dialog.dismiss();
		Toast.makeText(EditActivity.this, "回复失败，请检查！", Toast.LENGTH_SHORT)
				.show();
	}

	protected void onReplySuccess() {

		dialog.dismiss();
		Toast.makeText(EditActivity.this, "回复成功！", Toast.LENGTH_SHORT).show();
		setResult(Activity.RESULT_OK);
		this.finish();
		overridePendingTransition(R.anim.backward_activity_move_in,
				R.anim.backward_activity_move_out);
	}

	private void doPickPhotoAction() {
		final Context context = EditActivity.this;

		// Wrap our context to inflate list items using correct theme
		final Context dialogContext = new ContextThemeWrapper(context,
				android.R.style.Theme_Light);
		String[] choices;
		choices = new String[4];
		choices[0] = "拍照"; // 拍照
		choices[1] = "本地上传"; // 从相册中选择
		choices[2] = "涂鸦"; // from sketch
		choices[3] = "取消";
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
							if (Environment.getExternalStorageState().equals(
									Environment.MEDIA_MOUNTED)) {
								uploadSketch();
							} else {
								Toast.makeText(context, "您的设备没有SD卡！",
										Toast.LENGTH_SHORT).show();
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

	private void uploadSketch() {
		startActivityForResult(new Intent(this, SketchActivity.class),
				GET_SKETCH);
		overridePendingTransition(R.anim.forward_activity_move_in,
				R.anim.forward_activity_move_out);
	}

	/**
	 * 拍照获取图片
	 * 
	 */
	protected void doTakePhoto() {
		try {
			PHOTO_DIR.mkdirs();// 创建照片的存储目录
			Log.d(TAG, "doTakePhoto");
			mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
			mCurrentPhotoFile.createNewFile();
			final Intent intent = getTakePickIntent();
			startActivityForResult(intent, CAMERA_WITH_DATA);
			overridePendingTransition(R.anim.forward_activity_move_in,
					R.anim.forward_activity_move_out);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "未找到目标！", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Intent getTakePickIntent() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(mCurrentPhotoFile));
		return intent;
	}

	/**
	 * 用当前时间给取得的图片命名
	 * 
	 */
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyy-MM-dd HH-mm-ss");
		return dateFormat.format(date) + ".jpg";
		// return "aaaa.jpg";
	}

	// 请求Gallery程序
	protected void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			final Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
			overridePendingTransition(R.anim.forward_activity_move_in,
					R.anim.forward_activity_move_out);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, " ", Toast.LENGTH_LONG).show();
		}
	}

	// 封装请求Gallery的intent
	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		return intent;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			Log.d(TAG, resultCode + "");
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
				if (mCurrentPhotoFile.length() > MAX_IMAGE_SIZE_IN_BYTE) {
					doCompressPhoto();
				}
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
				int cursor = replyContent.getSelectionStart();
				replyContent.getText().insert(cursor, emotChooseString);
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
				if (picLink.equals("")) {
					mResult = TaskResult.FAILED;
				} else {
					mResult = TaskResult.OK;
				}

			} catch (PatternSyntaxException e) {
				mResult = TaskResult.FAILED;
				e.printStackTrace();
			} catch (MalformedURLException e) {
				mResult = TaskResult.FAILED;
				e.printStackTrace();
			} catch (IOException e) {
				mResult = TaskResult.FAILED;
				e.printStackTrace();
			}
			return mResult;

		}

	}

	private class PushReplyTask extends GenericTask {

		String postLink;

		String rootID;

		String boardID;

		String content;

		String title;

		String faceExpression;

		List<NameValuePair> nvpsList;

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {

			TaskParams param = params[0];

			try {

				postLink = param.getString("postLink");
				content = param.getString("content")
						+ (SettingsActivity.addTail ? TAIL : "");
				title = param.getString("title");
				faceExpression = param.getString("faceExpression");
				configReplyParams();
				if (CC98Client.submitReply(nvpsList, boardID, rootID)) {
					return TaskResult.OK;
				} else {
					return TaskResult.FAILED;
				}
			} catch (Exception e) {

				return TaskResult.FAILED;
			}
		}

		private void configReplyParams() {
			Log.d("dd", postLink);
			postLink = postLink.toLowerCase();
			int indexbegin = postLink.indexOf("&id");
			int indexend = postLink.indexOf("&page");
			rootID = postLink.substring(indexbegin + 4, indexend);
			Log.d("rootID", rootID);
			indexbegin = postLink.indexOf("boardid=");
			indexend = postLink.indexOf("&id");
			boardID = postLink.substring(indexbegin + 8, indexend);

			Log.d("boardID", boardID + " " + indexbegin + " " + indexend);
			nvpsList = new ArrayList<NameValuePair>();
			nvpsList.add(new BasicNameValuePair("upfilername", ""));
			nvpsList.add(new BasicNameValuePair("followup", rootID));
			nvpsList.add(new BasicNameValuePair("rootID", rootID));
			nvpsList.add(new BasicNameValuePair("star", ""));
			nvpsList.add(new BasicNameValuePair("TotalUseTable", "bbs5"));
			nvpsList.add(new BasicNameValuePair("subject", title));
			nvpsList.add(new BasicNameValuePair("Expression", faceExpression));
			nvpsList.add(new BasicNameValuePair("Content", content));
			nvpsList.add(new BasicNameValuePair("signflag", "yes"));

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

	public void sendPm(String touser, String title, String message)
			throws ClientProtocolException, IOException {
		int flag = CC98Client.sendPm(touser, title, message
				+ (SettingsActivity.addTail ? TAIL : ""));
		if (flag == CC98Client.PM_SEND_SUCC) {
			Toast.makeText(this, "发送短信息成功", Toast.LENGTH_SHORT).show();
			this.finish();
		} else if (flag == CC98Client.PM_SEND_FAIL) {
			Toast.makeText(this, "发送短信息失败", Toast.LENGTH_SHORT).show();
		}
	}

}
