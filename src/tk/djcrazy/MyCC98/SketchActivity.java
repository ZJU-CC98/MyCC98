package tk.djcrazy.MyCC98;

import java.io.File;

import tk.djcrazy.MyCC98.view.DrawView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * 
 * @author zsy
 * 
 */
public class SketchActivity extends Activity {
	public static final String FILE_PATH = "file_path";

	private RadioGroup butGroup;
	private RadioButton butDraw;
	private RadioButton butErase;
	private DrawView drawView;
	private View butClear;
	private View butSave;
	private EditText edtName;
	private static final String TAG = "Sketch";
	private static final String SKTECH_PATH = "/MyCC98/";
	private static final int SELECT_IMG = 1;
	private String filePath;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sketch);
		setTitle("98涂鸦");
		findViews();
		setupButtons();
		setViews();
	}

	private void setViews() {
		drawView.setSize(this);
	}

	private void findViews() {
		drawView = (DrawView) findViewById(R.id.draw_view);
		butDraw = (RadioButton) findViewById(R.id.but_draw);
		butErase = (RadioButton) findViewById(R.id.but_erase);
		butGroup = (RadioGroup) findViewById(R.id.radio_group);
		butClear = findViewById(R.id.but_clear);
		butSave = findViewById(R.id.but_save);
	}

	private void saveDialog() {
		edtName = new EditText(this);
		edtName.requestFocus();
		edtName.setFocusableInTouchMode(true);
		new AlertDialog.Builder(this)
				.setTitle("保存为...")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(edtName)
				.setPositiveButton("保存并上传",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String name = edtName.getText().toString();
								if (save(name)) {
									Toast.makeText(SketchActivity.this,
											"Success!", Toast.LENGTH_SHORT)
											.show();
									setResult(RESULT_OK, (new Intent())
											.putExtra(FILE_PATH, filePath
													+ name + ".jpg"));
									finish();
								} else {
									Toast.makeText(SketchActivity.this,
											"Failed!", Toast.LENGTH_SHORT)
											.show();
								}
							}
						}).setNegativeButton("Cancel", null).show();
	}

	// private void open() {
	// Intent intent = new Intent();
	// intent.setType("image/*");
	// intent.setAction(Intent.ACTION_GET_CONTENT);
	// startActivityForResult(Intent.createChooser(intent, "Select Picture"),
	// SELECT_IMG);
	// }

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (requestCode == SELECT_IMG) {
	// if (resultCode == RESULT_OK) {
	// Uri uri = data.getData();
	// ContentResolver cr = this.getContentResolver();
	// drawView.open(uri, cr);
	// }
	// }
	// };

	private boolean save(String name) {
		filePath = Environment.getExternalStorageDirectory() + SKTECH_PATH;
		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		return drawView.save(filePath + name);
	}

	private void setupButtons() {
		butDraw.setText("画笔");
		butErase.setText("橡皮");
		butGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.but_draw) {
					drawView.setMod(DrawView.DRAW);
					Log.d(TAG, "draw mod");
				} else if (checkedId == R.id.but_erase) {
					drawView.setMod(DrawView.ERASE);
					Log.d(TAG, "erase mod");
				}
			}
		});
		butClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				drawView.clear();
			}
		});
		butSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveDialog();
			}
		});
	}
}
