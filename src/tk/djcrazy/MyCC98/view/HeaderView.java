package tk.djcrazy.MyCC98.view;

import tk.djcrazy.MyCC98.HomeActivity;
import tk.djcrazy.MyCC98.PostSearchActivity;
import tk.djcrazy.MyCC98.R;
import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class HeaderView extends LinearLayout implements ChildView {
	public static enum Mods {
		HOT, NEW
	}

 	private ImageView ivUserImg;
	private ImageView ivSearch;
	private TextView tvTitle;
	private ParentView pv;
	private Bitmap userImage;

	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.header, this, true);
		findViews();
	}

	public void setUserImg(Bitmap bitmap) {
		ivUserImg.setImageBitmap(bitmap);
		userImage = bitmap;
	}

		public ImageView getButton() {
 			return ivSearch;
		}
	public void setTitle(String s) {
		tvTitle.setText(s);
	}

	public void setTitleTextSize(float f) {
		tvTitle.setTextSize(f);
	}

	public void setTitleOnclickListener(View.OnClickListener listener) {
		tvTitle.setOnClickListener(listener);
	}

	public void setUserImageOnclickListener(View.OnClickListener listener) {
		ivUserImg.setOnClickListener(listener);
	}

	public void setButtonImageResource(int resId) {
		ivSearch.setImageResource(resId);
	}
	
	public void setButtonBackgroundResource(int resId) {
		ivSearch.setBackgroundResource(resId);
	}
	
	public Drawable getButtomDrawable() {
		return ivSearch.getDrawable();
	}
	
	public void resetButton() {
		ivSearch.setImageResource(R.drawable.search_icon);
		setListeners(null);
	}

	public void setButtonPadding(int a, int b, int c, int d) {
		ivSearch.setPadding(a, b, c, d);
	}

	public void setButtonOnclickListener(View.OnClickListener listener) {
		ivSearch.setOnClickListener(listener);
	}

	public void setListeners(final HomeActivity group) {
 
		ivSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getContext().startActivity(
						(new Intent().setClass(getContext(),
								PostSearchActivity.class).putExtra(
								PostSearchActivity.USER_IMAGE, userImage)));
			}
		});
	}

	private void findViews() {
		ivUserImg = (ImageView) findViewById(R.id.iv_header_userimg);
		ivSearch = (ImageView) findViewById(R.id.iv_header_search);
		tvTitle = (TextView) findViewById(R.id.tv_header_title);
	}

	@Override
	public void setParentView(ParentView pv) {
		this.pv = pv;
	}

	@Override
	public void onSwitch() {

	}

}
