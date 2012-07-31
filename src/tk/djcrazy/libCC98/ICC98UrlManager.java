package tk.djcrazy.libCC98;

public interface ICC98UrlManager {

	/**
	 * @return the getOutboxUrl
	 */
	public String getGetOutboxUrl(String pageNum);

	/**
	 * @return the getInboxUrl
	 */
	public String getGetInboxUrl(String pageNum);

	/**
	 * @return the queryUrl
	 */
	public String getQueryUrl();

	/**
	 * @return the queryReferer
	 */
	public String getQueryReferer();

	/**
	 * @return the uploadPictureUrl
	 */
	public String getUploadPictureUrl();

	/**
	 * @return the todayBoardList
	 */
	public String getTodayBoardList();

	public String getSearchUrl(String keyword, int boardid, String sType,
			int page);

	public String getInboxUrl(int pageNum);

	public String getOutboxUrl(int pageNum);

	public String getMessagePageUrl(int pmId);

	public String getPersonalBoardUrl();

	public String getClientUrl();

	public String getBoardUrl(int boardId, int pageNum);

	public String getBoardUrl(int boardId);

	public String getPostUrl(int boardId, int postId, int pageNum);

	/**
	 * @return the hotTopicUrl
	 */
	public String getHotTopicUrl();

	/**
	 * @return the userProfileUrl
	 */
	public String getUserProfileUrl(String userName);

	/**
	 * @return the newPostUrl
	 */
	public String getNewPostUrl();

	/**
	 * @return the userManagerUrl
	 */
	public String getUserManagerUrl();

	/**
	 * @return the addFriendUrl
	 */
	public String getAddFriendUrl();

	/**
	 * @return the addFriendUrlReferrer
	 */
	public String getAddFriendUrlReferrer();

	/**
	 * @return the loginUrl
	 */
	public String getLoginUrl();

	public void setLifetoyVersion(boolean b);

	public boolean isLifetoyVersion();

	public String getSendPmUrl();

	/**
	 * @return the pushNewPostReferer
	 */
	public String getPushNewPostReferer(String boardID);

	/**
	 * @return the pushNewPostUrl
	 */
	public String getPushNewPostUrl(String boardId);

	/**
	 * @return the submitReplyUrl
	 */
	public String getSubmitReplyUrl(String boardID);

	/**
	 * @return the submitReplyReferer
	 */
	public String getSubmitReplyReferer(String boardID, String rootID);

}