package tk.djcrazy.libCC98;

import java.net.URLEncoder;

import com.google.inject.Singleton;

@Singleton
public class CC98UrlManagerImpl implements ICC98UrlManager {
	private final String CC98 = "http://www.cc98.org/";
	private final String LIFETOY = "http://hz.cc98.lifetoy.org/";

	private boolean lifetoyVersion = false;

	private String client;
	private String hotTopicUrl;
	private String userProfileUrl;
	private String newPostUrl;
	private String userManagerUrl;
	private String addFriendUrl;
	private String addFriendUrlReferrer;
	private String loginUrl;
	private String inboxUrl;
	private String outboxUrl;
	private String sendPmUrl;
	private String pushNewPostUrl;
	private String pushNewPostReferer;
	private String messagePageUrl;
	private String submitReplyUrl;
	private String submitReplyReferer;
	private String todayBoardList;
	private String uploadPictureUrl;
	private String queryUrl;
	private String queryReferer;
	private String getInboxUrl;
	private String getOutboxUrl;
	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getGetOutboxUrl(java.lang.String)
	 */
	@Override
	public String getGetOutboxUrl(String pageNum) {
		return getOutboxUrl +pageNum;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getGetInboxUrl(java.lang.String)
	 */
	@Override
	public String getGetInboxUrl(String pageNum) {
		return getInboxUrl+pageNum;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getQueryUrl()
	 */
	@Override
	public String getQueryUrl() {
		return queryUrl;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getQueryReferer()
	 */
	@Override
	public String getQueryReferer() {
		return queryReferer;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getUploadPictureUrl()
	 */
	@Override
	public String getUploadPictureUrl() {
		return uploadPictureUrl;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getTodayBoardList()
	 */
	@Override
	public String getTodayBoardList() {
		return todayBoardList;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getSearchUrl(java.lang.String, int, java.lang.String, int)
	 */
	@Override
	public String getSearchUrl(String keyword, int boardid, String sType,
			int page) {
		StringBuilder sBuilder = new StringBuilder(client);
		sBuilder.append("queryresult.asp?page=").append(page).append("&stype=")
				.append(sType).append("&pSearch=1&nSearch=&keyword=")
				.append(keyword).append("&SearchDate=1000&boardid=")
				.append(boardid).append("&sertype=1");
		return sBuilder.toString();
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getInboxUrl(int)
	 */
	@Override
	public String getInboxUrl(int pageNum) {
		return inboxUrl + pageNum;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getOutboxUrl(int)
	 */
	@Override
	public String getOutboxUrl(int pageNum) {
		return outboxUrl + pageNum;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getMessagePageUrl(int)
	 */
	@Override
	public String getMessagePageUrl(int pmId) {
		return messagePageUrl + pmId;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getPersonalBoardUrl()
	 */
	@Override
	public String getPersonalBoardUrl() {
		return client;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getClientUrl()
	 */
	@Override
	public String getClientUrl() {
		return client;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getBoardUrl(int, int)
	 */
	@Override
	public String getBoardUrl(int boardId, int pageNum) {
		return client + "list.asp?boardid=" + boardId + "&page=" + pageNum;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getBoardUrl(int)
	 */
	@Override
	public String getBoardUrl(int boardId) {
		return getBoardUrl(boardId, 1);
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getPostUrl(int, int, int)
	 */
	@Override
	public String getPostUrl(int boardId, int postId, int pageNum) {
		return client + "dispbbs.asp?boardID=" + boardId + "&ID=" + postId
				+ "&page=" + pageNum;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getHotTopicUrl()
	 */
	@Override
	public String getHotTopicUrl() {
		return hotTopicUrl;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getUserProfileUrl(java.lang.String)
	 */
	@Override
	public String getUserProfileUrl(String userName) {
		return userProfileUrl + URLEncoder.encode(userName);
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getNewPostUrl()
	 */
	@Override
	public String getNewPostUrl() {
		return newPostUrl;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getUserManagerUrl()
	 */
	@Override
	public String getUserManagerUrl() {
		return userManagerUrl;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getAddFriendUrl()
	 */
	@Override
	public String getAddFriendUrl() {
		return addFriendUrl;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getAddFriendUrlReferrer()
	 */
	@Override
	public String getAddFriendUrlReferrer() {
		return addFriendUrlReferrer;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getLoginUrl()
	 */
	@Override
	public String getLoginUrl() {
		return loginUrl;
	}

	public CC98UrlManagerImpl() {
		updateLink(false);
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#setLifetoyVersion(boolean)
	 */
	@Override
	public void setLifetoyVersion(boolean b) {
		updateLink(b);
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#isLifetoyVersion()
	 */
	@Override
	public boolean isLifetoyVersion() {
		return lifetoyVersion;
	}

	public CC98UrlManagerImpl(boolean lifetoyVersion) {
		updateLink(lifetoyVersion);
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getSendPmUrl()
	 */
	@Override
	public String getSendPmUrl() {
		return sendPmUrl;
	}

	private void updateLink(boolean lifetoyVersion) {
 		if (lifetoyVersion) {
			client = LIFETOY;
		} else {
			client = CC98;
		}
		hotTopicUrl = client + "hottopic.asp";
		userProfileUrl = client + "dispuser.asp?name=";
		newPostUrl = client + "queryresult.asp?stype=3";
		userManagerUrl = client + "usermanager.asp";
		addFriendUrl = client + "usersms.asp?action=friend";
		addFriendUrlReferrer = client + "usersms.asp?action=friend&todo=addF";
		messagePageUrl = client + "messanger.asp?action=read&id=";
		inboxUrl = client + "usersms.asp?action=inbox&page=";
		outboxUrl = client + "usersms.asp?action=issend&page=";
		todayBoardList = client + "boardstat.asp?boardid=0";
		sendPmUrl = client + "messanger.asp?action=send";
		pushNewPostUrl = client + "SaveAnnounce.asp?boardID=";
		pushNewPostReferer = client + "announce.asp?boardid=";
		submitReplyUrl = client + "SaveReAnnounce.asp?method=Topic&boardID=";
		submitReplyReferer = client + "reannounce.asp?BoardID=";
		uploadPictureUrl = client + "saveannouce_upfile.asp?boardid=10";
		queryUrl = client + "queryresult.asp";
		queryReferer = client + "query.asp?boardid=0";
		getInboxUrl = client + "usersms.asp?action=inbox&page=";
		getOutboxUrl = client +"usersms.asp?action=issend&page=";
		loginUrl  = client + "login.asp?action=chk";
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getPushNewPostReferer(java.lang.String)
	 */
	@Override
	public String getPushNewPostReferer(String boardID) {
		return pushNewPostReferer + boardID;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getPushNewPostUrl(java.lang.String)
	 */
	@Override
	public String getPushNewPostUrl(String boardId) {
		return pushNewPostUrl + boardId;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getSubmitReplyUrl(java.lang.String)
	 */
	@Override
	public String getSubmitReplyUrl(String boardID) {
		return submitReplyUrl + boardID;
	}

	/* (non-Javadoc)
	 * @see tk.djcrazy.libCC98.ICC98UrlMaanager#getSubmitReplyReferer(java.lang.String, java.lang.String)
	 */
	@Override
	public String getSubmitReplyReferer(String boardID, String rootID) {
		return submitReplyReferer + boardID + "&id=" + rootID + "&star=1";
	}
}
