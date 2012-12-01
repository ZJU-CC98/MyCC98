package tk.djcrazy.MyCC98.view;

import com.actionbarsherlock.app.ActionBar;

import tk.djcrazy.MyCC98.BaseActivity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyEditText extends EditText {
	

	public MyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyEditText(Context context) {
		super(context);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		ActionBar actionBar = ((BaseActivity)getContext()).getSupportActionBar();
		if (h<actionBar.getHeight()) {
			actionBar.hide();
		} else if (h>2*actionBar.getHeight()) {
			actionBar.show();
		} 
	}

}
