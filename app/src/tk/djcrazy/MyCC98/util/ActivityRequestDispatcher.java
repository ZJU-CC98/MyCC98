package tk.djcrazy.MyCC98.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tk.djcrazy.libCC98.exception.CC98Exception;
import android.content.Intent;


// TODO 发现没法区分是否是父版面，无限期搁置
public class ActivityRequestDispatcher {

	public static Intent dispatchCC98URL(String url) throws CC98Exception {
		if (url.contains("list.asp")) {
			// board list
			Matcher matcher = Pattern.compile("(?<=/list\\.asp\\?boardid=)\\d{1,4}?", Pattern.DOTALL).matcher(url.toLowerCase());
			if (matcher.find()) {
				String boardId = matcher.group();
			}
 		}  
		throw new CC98Exception("Not illegal CC98 Url:"+url);
		 
	}
}
