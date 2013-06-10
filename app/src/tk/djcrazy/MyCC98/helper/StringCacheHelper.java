package tk.djcrazy.MyCC98.helper;

public class StringCacheHelper {
	private static final String POST = "post://";

	public static String postPageToString(String boardId, String postId, int pageNum) {
		StringBuilder builder = new StringBuilder(POST);
		return builder.append(boardId).append('/').append(postId).append('/')
				.append(pageNum).toString();
	}
}
