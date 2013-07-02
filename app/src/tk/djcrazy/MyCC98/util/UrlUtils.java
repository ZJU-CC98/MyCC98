package tk.djcrazy.MyCC98.util;

import junit.framework.Assert;
import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.libCC98.exception.ParseContentException;
import tk.djcrazy.libCC98.util.RegexUtil;
import android.content.Intent;

public class UrlUtils {

	public static boolean isPostContentLink(String url) {
		String inUrl = url.toLowerCase();
		if (inUrl.contains("dispbbs.asp?boardid=") && inUrl.contains("&id=")) {
			// is legal cc98 post content url
			return true;
		} else {
			return false;
		}
	}

	public static Intent getPostContentIntent(String url) {
		Assert.assertTrue(isPostContentLink(url));
		String inUrl = url.toLowerCase();
		try {
			String boardId = RegexUtil.getMatchedString("(?<=boardid=)\\d*", inUrl);
			String postId = RegexUtil.getMatchedString("(?<=&id=)\\d*", inUrl);
			if (inUrl.contains("star=")) {
				String pageNumber = RegexUtil.getMatchedString("(?<=star=)\\d*", inUrl);
				return PostContentsJSActivity.createIntent(boardId, postId, Integer.parseInt(pageNumber), true);
			} else {
				return PostContentsJSActivity.createIntent(boardId, postId);
			}	
		} catch (ParseContentException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
