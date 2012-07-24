/**
 * 
 */
package tk.djcrazy.MyCC98.view;

import tk.djcrazy.MyCC98.HomeActivity;
import tk.djcrazy.MyCC98.R;
import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Common footer
 * 
 * @author zsy
 * 
 */
public class FooterView extends LinearLayout implements ChildView {
	private static final String TAG="FooterView";
	private ImageView ivHome;
	private ImageView ivBoardList;
	private ImageView ivHot;
	private ImageView ivNew;
	private ImageView ivPM;
	private View vStat;
 	private ParentView pv;
 	private int xPos=0;
 	private int whichView=0;
 	private static final int ANI_DURATION = 300;
 	
	public FooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.footer, this, true);
		findViews();
 	}
 	private void doChangeFooterStateAnimation(int x, int which) {
 		whichView = which;
  		TranslateAnimation animation = new TranslateAnimation(
 				Animation.ABSOLUTE, xPos ,
 				Animation.ABSOLUTE, x,
				Animation.RELATIVE_TO_SELF, 0f,  
                Animation.RELATIVE_TO_SELF, 0f);  
		animation.setDuration(ANI_DURATION);
		animation.setInterpolator(AnimationUtils.loadInterpolator(getContext(),
                android.R.anim.accelerate_decelerate_interpolator));
		animation.setFillAfter(true);
		vStat.startAnimation(animation);
		xPos=x;
	}
	
 	public void resetFooterState(int width) {
 		int x =  width/5*whichView;
  		TranslateAnimation animation = new TranslateAnimation(
 				Animation.ABSOLUTE, xPos ,
 				Animation.ABSOLUTE, x,
				Animation.RELATIVE_TO_SELF, 0f,  
                Animation.RELATIVE_TO_SELF, 0f);  
		animation.setDuration(ANI_DURATION);
	
		animation.setInterpolator(AnimationUtils.loadInterpolator(getContext(),
                android.R.anim.accelerate_decelerate_interpolator));
		animation.setFillAfter(true);
		animation.cancel();
		vStat.startAnimation(animation);
		xPos=x;
	}
 	
	public void setListeners(final HomeActivity group) {
		ivHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pv.switchToView(HomeActivity.V_PERSONAL_BOARD);
				int[] location = new int[2];
				ivHome.getLocationOnScreen(location);
				doChangeFooterStateAnimation(location[0], 0);
			}
		});

		ivBoardList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pv.switchToView(HomeActivity.V_BOARD_SEARCH);
				int[] location = new int[2];
				ivBoardList.getLocationOnScreen(location);
				doChangeFooterStateAnimation(location[0],1 );
			}
		});
		ivPM.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) { 
				pv.switchToView(HomeActivity.V_FRIEND);
				int[] location = new int[2];
				ivPM.getLocationOnScreen(location);
				doChangeFooterStateAnimation(location[0],2 );

			}
		});

		ivHot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pv.switchToView(HomeActivity.V_HOT);
				int[] location = new int[2];
				ivHot.getLocationOnScreen(location);
				doChangeFooterStateAnimation(location[0],3 );

			}
		});

		ivNew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pv.switchToView(HomeActivity.V_NEW);
				int[] location = new int[2];
				ivNew.getLocationOnScreen(location);
				doChangeFooterStateAnimation(location[0],4 );

			}
		});
	}

	private void findViews() {
		ivHome = (ImageView) findViewById(R.id.iv_footer_home);
		ivBoardList = (ImageView) findViewById(R.id.iv_footer_boardlist);
		ivHot = (ImageView) findViewById(R.id.iv_footer_hot);
		ivNew = (ImageView) findViewById(R.id.iv_footer_new);
		ivPM = (ImageView) findViewById(R.id.iv_footer_pm);
		vStat  = findViewById(R.id.v_stat);
  	}

	@Override
	public void setParentView(ParentView pv) {
		this.pv = pv;
	}

	@Override
	public void onSwitch() {

	}

}
