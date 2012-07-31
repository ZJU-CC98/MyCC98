package tk.djcrazy.libCC98;

import java.net.URLEncoder;

public class CC98UrlManager {
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
	
	private String messagePageUrl;
	
	private String todayBoardList;
	
	/**
	 * @return the todayBoardList
	 */
	public String getTodayBoardList() {
		return todayBoardList;
	}

	public String getSearchUrl(String keyword,
			int boardid, String sType, int page) {
		StringBuilder sBuilder = new StringBuilder(client);
		sBuilder.append("queryresult.asp?page=").append(page).append("&stype=")
				.append(sType).append("&pSearch=1&nSearch=&keyword=")
				.append(keyword).append("&SearchDate=1000&boardid=")
				.append(boardid).append("&sertype=1");
		return sBuilder.toString();
	}
	
	public String getInboxUrl(int pageNum) {
		return inboxUrl +pageNum;
	}
	public String getOutboxUrl(int pageNum) {
		return outboxUrl +pageNum;
	}
 	public String getMessagePageUrl(int pmId) {
		return messagePageUrl +pmId;
	}

	public String getPersonalBoardUrl() {
		return client;
	}
	
	public String getClientUrl() {
		return client;
	}

	public String getBoardUrl(int boardId, int pageNum) {
		return client + "list.asp?boardid="+boardId + "&page="+pageNum;
	}

	public String getBoardUrl(int boardId) {
		return getBoardUrl(boardId, 1);
	}

	
	public String getPostUrl(int boardId, int postId, int pageNum) {
		return client +"dispbbs.asp?boardID="+boardId+"&ID="+postId+"&page="+pageNum;
	}
	/**
	 * @return the hotTopicUrl
	 */
	public String getHotTopicUrl() {
		return hotTopicUrl;
	}

	/**
	 * @return the userProfileUrl
	 */
	public String getUserProfileUrl(String userName) {
		return userProfileUrl + URLEncoder.encode(userName);
	}

	/**
	 * @return the newPostUrl
	 */
	public String getNewPostUrl() {
		return newPostUrl;
	}

	/**
	 * @return the userManagerUrl
	 */
	public String getUserManagerUrl() {
		return userManagerUrl;
	}

	/**
	 * @return the addFriendUrl
	 */
	public String getAddFriendUrl() {
		return addFriendUrl;
	}

	/**
	 * @return the addFriendUrlReferrer
	 */
	public String getAddFriendUrlReferrer() {
		return addFriendUrlReferrer;
	}

	/**
	 * @return the loginUrl
	 */
	public String getLoginUrl() {
		return loginUrl;
	}

	public CC98UrlManager() {

	}

	public void setLifetoyVersion(boolean b) {
		updateLink(b);
	}

	public boolean isLifetoyVersion() {
		return lifetoyVersion;
	}

	public CC98UrlManager(boolean lifetoyVersion) {
		updateLink(lifetoyVersion);
	}

	public String getSendPmUrl() {
		return sendPmUrl;
	}

	private void updateLink(boolean lifetoyVersion) {
		if (this.lifetoyVersion == lifetoyVersion) {
			return;
		}
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
		messagePageUrl = client+"messanger.asp?action=read&id=";
		inboxUrl  = client +"usersms.asp?action=inbox&page=";
		outboxUrl = client +"usersms.asp?action=issend&page=";
		todayBoardList = client + "boardstat.asp?boardid=0";
		sendPmUrl = client + "messanger.asp?action=send";
	}
}
