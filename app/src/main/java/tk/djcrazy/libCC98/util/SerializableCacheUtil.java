package tk.djcrazy.libCC98.util;

public class SerializableCacheUtil {
	private static final String POST = "post://";
	private static final String PM = "pm://";
	private static final String POST_LIST = "posts://";
	private static final String PERSONAL_BOARD = "personalboard://";
	private static final String HOT_TOPIC = "hottopics";
	private static final String NEW_TOPIC = "newtopic://";
	private static final String AVATAR = "avatar://";
	private static final String MSG = "msg://";
	private static final String BOARDS = "boards://";
	
	public static String boardsKey(String boardId) {
		return BOARDS + boardId;
	}
	
	public static String msgKey(int pmid) {
		return MSG + String.valueOf(pmid);
	}
	
	public static String avatarKey(String username) {
		return AVATAR + username;
	}

	public static String postPageKey(String boardId, String postId, int pageNum) {
		StringBuilder builder = new StringBuilder(POST);
		return builder.append(boardId).append('/').append(postId).append('/')
				.append(pageNum).toString();
	}

	public static String pmKey(int pmID) {
		return PM + String.valueOf(pmID);
	}

	public static String postListKey(String boardId, int pagenum) {
		return postListKey(boardId) + pagenum;
	}
	
	public static String postListKey(String boardId) {
		return POST_LIST + boardId + '/';
	}
	
	public static String personalBoardKey(String username) {
		return PERSONAL_BOARD + username;
	}
	
	public static String newTopicKey() {
		return NEW_TOPIC;
	}
	
	public static String newTopicKey(int pagenum) {
		return NEW_TOPIC + String.valueOf(pagenum);
	}
	
	public static String hottopicKey() {
		return HOT_TOPIC;
	}
}
