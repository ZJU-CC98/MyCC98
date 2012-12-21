package tk.djcrazy.libCC98.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtil {
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("M/d/yyyy h:mm:ss a", Locale.ENGLISH);
	public static Date convertStringToDateInPostContent(String dateString)
			throws ParseException {
 		return sdf1.parse(dateString);
	}
	public static Date convertStringToDateInPostList(String dateString)
			throws ParseException {
 		return sdf1.parse(dateString);
	}
	public static Date convertStringToDateInQueryResult(String dateString)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyyHH:mm", Locale.ENGLISH);
		return sdf.parse(dateString);
	}
	
	public static Date convertStrToDateInPBoard(String dateString)
			throws ParseException {
 		return sdf1.parse(dateString);
	}

	public static String convertDateToString(Date date, boolean useFriendlyTime) {
		if (useFriendlyTime) {
			return useFriendlyTime(date);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
			return sdf.format(date);
		}
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param time
	 * @return
	 */
	private static String useFriendlyTime(Date time) {
		// 获取time距离当前的秒数
		int ct = (int) ((System.currentTimeMillis() - time.getTime()) / 1000);
		if (ct <= 0) {
			return "刚刚";
		}
		if (ct > 0 && ct < 60) {
			return ct + "秒前";
		}
		if (ct >= 60 && ct < 3600) {
			return Math.max(ct / 60, 1) + "分钟前";
		}
		if (ct >= 3600 && ct < 86400)
			return ct / 3600 + "小时前";
		if (ct >= 86400 && ct < 2592000) { // 86400 * 30
			int day = ct / 86400;
			return day + "天前";
		}
		if (ct >= 2592000 && ct < 31104000) { // 86400 * 30
			return ct / 2592000 + "个月前";
		}
		return ct / 31104000 + "年前";
	}
}
