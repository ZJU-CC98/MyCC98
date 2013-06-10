package tk.djcrazy.MyCC98.helper;

public class SerializableCacheHelper {
	private static final String POST = "post://";
	private static final String PM = "pm://";

	public static String postPageKey(String boardId, String postId, int pageNum) {
		StringBuilder builder = new StringBuilder(POST);
		return builder.append(boardId).append('/').append(postId).append('/')
				.append(pageNum).toString();
	}
	
	public static String pmKey(int pmID) {
		return PM + String.valueOf(pmID);
	}
}
