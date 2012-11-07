package tk.djcrazy.libCC98;

import java.net.URLEncoder;

import tk.djcrazy.MyCC98.application.MyApplication;

import android.app.Application;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CC98UrlManagerImpl implements ICC98UrlManager {
	private final String CC98 = "http://www.cc98.org/";
	private final String LIFETOY = "http://hz.cc98.lifetoy.org/";

	@Inject
	private Application application;
 
	private String getClient() {
		if (isProxyVersion()) {
			return LIFETOY;
		} else {
			return CC98;
		}
	}

	private boolean isProxyVersion() {
		return ((MyApplication) application).getUserData().isProxyVersion();
	}

	@Override
	public String getGetOutboxUrl(String pageNum) {
		return getClient() + "usersms.asp?action=issend&page=" + pageNum;
	}

	@Override
	public String getGetInboxUrl(String pageNum) {
		return getClient() + "usersms.asp?action=inbox&page=" + pageNum;
	}

	@Override
	public String getQueryUrl() {
		return getClient() + "queryresult.asp";
	}

	@Override
	public String getQueryReferer() {
		return getClient() + "query.asp?boardid=0";
	}

	@Override
	public String getUploadPictureUrl() {
		return getClient() + "saveannouce_upfile.asp?boardid=10";
	}

	@Override
	public String getTodayBoardList() {
		return getClient() + "boardstat.asp?boardid=0";
	}

	@Override
	public String getSearchUrl(String keyword, String boardid, String sType,
			int page) {
		StringBuilder sBuilder = new StringBuilder(getClient());
		sBuilder.append("queryresult.asp?page=").append(page).append("&stype=")
				.append(sType).append("&pSearch=1&nSearch=&keyword=")
				.append(keyword).append("&SearchDate=1000&boardid=")
				.append(boardid).append("&sertype=1");
		return sBuilder.toString();
	}

	@Override
	public String getInboxUrl(int pageNum) {
		return getClient() + "usersms.asp?action=inbox&page=" + pageNum;
	}

	@Override
	public String getOutboxUrl(int pageNum) {
		return getClient() + "usersms.asp?action=issend&page=" + pageNum;
	}

	@Override
	public String getMessagePageUrl(int pmId) {
		return getClient() + "messanger.asp?action=read&id=" + pmId;
	}

	@Override
	public String getPersonalBoardUrl() {
		return getClient();
	}

	@Override
	public String getClientUrl() {
		return getClient();
	}

	@Override
	public String getBoardUrl(String boardId, int pageNum) {
		return getClient() + "list.asp?boardid=" + boardId + "&page=" + pageNum;
	}

	@Override
	public String getBoardUrl(String boardId) {
		return getBoardUrl(boardId, 1);
	}

	@Override
	public String getPostUrl(String boardId, String postId, int pageNum) {
		return getClient() + "dispbbs.asp?boardID=" + boardId + "&ID=" + postId
				+ "&star=" + pageNum;
	}

	@Override
	public String getHotTopicUrl() {
		return getClient() + "hottopic.asp";
	}

	@Override
	public String getUserProfileUrl(String userName) {
		return getClient() + "dispuser.asp?name=" + URLEncoder.encode(userName);
	}

	@Override
	public String getNewPostUrl(int pageNum) {
		return getClient() + "queryresult.asp?stype=3" + "&page=" + pageNum;
	}

	@Override
	public String getUserManagerUrl() {
		return getClient() + "usermanager.asp";
	}

	@Override
	public String getAddFriendUrl() {
		return getClient() + "usersms.asp?action=friend";
	}

	@Override
	public String getAddFriendUrlReferrer() {
		return getClient() + "usersms.asp?action=friend&todo=addF";
	}

	@Override
	public String getLoginUrl() {
		//return getClient() + "login.asp?action=chk";
		return getClient() + "sign.asp";
	}

  
	@Override
	public String getSendPmUrl() {
		return getClient() + "messanger.asp?action=send";
	}

	@Override
	public String getPushNewPostReferer(String boardID) {
		return getClient() + "announce.asp?boardid=" + boardID;
	}

	@Override
	public String getPushNewPostUrl(String boardId) {
		return getClient() + "SaveAnnounce.asp?boardID=" + boardId;
	}

	@Override
	public String getSubmitReplyUrl(String boardID) {
		return getClient() + "SaveReAnnounce.asp?method=Topic&boardID="
				+ boardID;
	}

	@Override
	public String getSubmitReplyReferer(String boardID, String rootID) {
		return getClient() + "reannounce.asp?BoardID=" + boardID + "&id="
				+ rootID + "&star=1";
	}

 }
