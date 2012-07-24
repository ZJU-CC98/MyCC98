package tk.djcrazy.MyCC98.util;

import android.content.Context;
import android.util.Log;

public class DisplayUtil {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		int res = (int) (dpValue * scale + 0.5f);
		Log.d("DisplayUtil", "dip2px:"+res);
		return res;
	}
 
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
