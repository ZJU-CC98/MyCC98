package tk.djcrazy.libCC98;

import tk.djcrazy.libCC98.data.LoginType;

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

	public String getSearchUrl(String keyword, String boardid, String sType,
			int page);

	public String getInboxUrl(int pageNum);

	public String getOutboxUrl(int pageNum);

	public String getMessagePageUrl(int pmId);

	public String getPersonalBoardUrl();

	public String getClientUrl();
	
	public String getClientUrl(LoginType type);

	public String getBoardUrl(String boardId, int pageNum);

	public String getBoardUrl(String boardId);

	public String getPostUrl(String boardId, String postId, int pageNum);
	
	public String getCC98PostUrl(String boardId, String postId, int pageNum);

    public String getClientUrl(LoginType type, String proxyHost);

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
	public String getNewPostUrl(int pageNum);

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

	public String getSendPmUrl();

 	public String getPushNewPostReferer(String boardID);

 	public String getPushNewPostUrl(String boardId);

 	public String getSubmitReplyUrl(String boardID);

 	public String getSubmitReplyReferer(String boardID, String rootID);

    public String getUserProfileUrl(LoginType type, String userName);

    public String getUserProfileUrl(LoginType type, String proxyHost, String userName);

 	public String getLoginUrl(LoginType type);

    public String getLoginUrl(LoginType loginType, String proxyHost);
}