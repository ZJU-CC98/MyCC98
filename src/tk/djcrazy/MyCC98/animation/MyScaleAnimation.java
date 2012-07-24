package tk.djcrazy.MyCC98.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;

public class MyScaleAnimation extends ScaleAnimation {

 
	public MyScaleAnimation(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
 	}
	

	@Override
	public boolean willChangeBounds() {
		return true;
	}

}
