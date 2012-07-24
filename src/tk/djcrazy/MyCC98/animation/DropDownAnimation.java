package tk.djcrazy.MyCC98.animation;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class DropDownAnimation extends Animation {
	private int targetHeight;
	private int initHeight;
	private View view;
	private boolean down;
	int newHeight;

	public DropDownAnimation(View view, int targetHeight, boolean down) {
		this.view = view;
		this.targetHeight = targetHeight;
		this.initHeight = view.getLayoutParams().height;
		this.down = down;
		setDuration(1000);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {

		newHeight = initHeight
				+ (int) ((targetHeight - initHeight) * interpolatedTime);
		view.getLayoutParams().height = newHeight;
		view.requestLayout();
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
	}

	@Override
	public boolean willChangeBounds() {
		return true;
	}
}
