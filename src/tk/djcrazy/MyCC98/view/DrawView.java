package tk.djcrazy.MyCC98.view;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class DrawView extends View {
	public static final int DRAW = 0;
	public static final int ERASE = 1;
	public static final int CLEAR = -1;
	private static final int STROKE_WIDTH = 8;
	private int mod = DRAW;
	private Paint pen;
	private Paint eraser;
	private Bitmap bitmap;
	private Canvas mCanvas;
	private Paint bitmapPaint;
	private Path currPath;
	private float preX = -1;
	private float preY = -1;
	private static final String TAG = "DrawView";
	private int screenWidth = 480;

	public DrawView(Context context) {
		super(context);
	}
	
	public void setSize(Activity ac) {
		WindowManager windowManager = ac.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		screenWidth = display.getWidth();
		init(getContext());
	}

	public boolean save(String path) {
		boolean flag = false;
		File sketchImgFile = new File(path + ".jpg");
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(sketchImgFile));
			//bitmap = Bitmap.createScaledBitmap(bitmap, 480, 480, false);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public void setMod(int mod) {
		this.mod = mod;
	}

	public void clear() {
		bitmap.eraseColor(Color.argb(255, 255, 255, 255));
		invalidate();
	}

	private void init(Context context) {
		setupPaint(context);
		bitmap = Bitmap.createBitmap(screenWidth, screenWidth, Bitmap.Config.ARGB_8888);
		bitmap.eraseColor(Color.argb(255, 255, 255, 255));
		mCanvas = new Canvas(bitmap);
		currPath = new Path();
	}

	public DrawView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	private void setupPaint(Context context) {
		pen = new Paint();
		pen.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | pen.getFlags());
		pen.setStyle(Paint.Style.STROKE);
		pen.setStrokeJoin(Paint.Join.ROUND);
		pen.setStrokeCap(Paint.Cap.ROUND);
		pen.setColor(Color.argb(255, 255, 0, 0));
		pen.setStrokeWidth(STROKE_WIDTH);

		eraser = new Paint();
		eraser.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG
				| eraser.getFlags());
		eraser.setStyle(Paint.Style.STROKE);
		eraser.setStrokeJoin(Paint.Join.ROUND);
		eraser.setStrokeCap(Paint.Cap.SQUARE);
		eraser.setColor(Color.argb(255, 255, 255, 255));
		eraser.setStrokeWidth(STROKE_WIDTH*2);

		bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//setMeasuredDimension(bitmap.getWidth(), bitmap.getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.argb(255, 255, 255, 255));
		switch (mod) {
		case DRAW:
			mCanvas.drawPath(currPath, pen);
			break;
		case ERASE:
			mCanvas.drawPath(currPath, eraser);
			break;
		default:
			break;
		}

		canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
		// draw(mCanvas);
		super.onDraw(canvas);
	}

	private void startDraw(float x, float y) {
		preX = x;
		preY = y;
		currPath.moveTo(preX, preY);
		invalidate();
	}

	private void contDraw(float x, float y) {
		if (preX == -1 || preY == -1) {
			return;
		}
		currPath.quadTo(preX, preY, (preX + x) / 2, (preY + y) / 2);
		preX = x;
		preY = y;
		invalidate();
	}

	private void compDraw(float x, float y) {
		currPath.quadTo(preX, preY, (preX + x) / 2, (preY + y) / 2);
		currPath.reset();
		preX = -1;
		preY = -1;
		Log.d(TAG, "ACTION UP");
		// path.addPath(tPath);
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startDraw(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_MOVE:
			contDraw(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_UP:
			compDraw(event.getX(), event.getY());
			break;
		default:
			break;
		}
		return true;
	}
}