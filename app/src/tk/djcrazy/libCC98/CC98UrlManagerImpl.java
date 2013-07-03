package tk.djcrazy.libCC98;

import java.net.URLEncoder;

import tk.djcrazy.MyCC98.application.MyApplication;
import tk.djcrazy.libCC98.data.LoginType;

import android.app.Application;

import com.google.inject.Inject;
import com.google.inject.Singleton;
/**
 * Thank flankerhqd for the rvpn access.
 *
 */
@Singleton
public class CC98UrlManagerImpl implements ICC98UrlManager {
	private final String CC98 = "http://www.cc98.org/";
	private final String PROXY = "http://hz.cc98.lifetoy.org/";
	private final String ZJU_RVPN_HOST = "https://61.175.193.50/";
	private final String ZJU_RVPN_HOST_PREFIX = "/web/1/http/0/www.cc98.org/";
	
	@Inject 
	private Application application;
 
	private String getClientPrefix() {
		if (getProxyType()==LoginType.USER_DEFINED) {
			return PROXY;
		} else if (getProxyType()==LoginType.RVPN) {
			return ZJU_RVPN_HOST + ZJU_RVPN_HOST_PREFIX;
		} else {
			return CC98;
		}
	}
	
	private String getClientPrefix(LoginType proxy) { 
		if (proxy==LoginType.USER_DEFINED) {
			return PROXY;
		} else if (proxy==LoginType.RVPN) {
			return ZJU_RVPN_HOST+ZJU_RVPN_HOST_PREFIX;
		} else {
			return CC98;
		}
	}

	private LoginType getProxyType() {
		return ((MyApplication) application).getCurrentUserData().getLoginType();
	}

	@Override
	public String getGetOutboxUrl(String pageNum) {
		return getClientPrefix() + "usersms.asp?action=issend&page=" + pageNum;
	}

	@Override
	public String getGetInboxUrl(String pageNum) {
		return getClientPrefix() + "usersms.asp?action=inbox&page=" + pageNum;
	}

	@Override
	public String getQueryUrl() {
		return getClientPrefix() + "queryresult.asp";
	}

	@Override
	public String getQueryReferer() {
		return getClientPrefix() + "query.asp?boardid=0";
	}

	@Override
	public String getUploadPictureUrl() {
		return getClientPrefix() + "saveannouce_upfile.asp?boardid=10";
	}

	@Override
	public String getTodayBoardList() {
		return getClientPrefix() + "boardstat.asp?boardid=0";
	}

	@Override
	public String getSearchUrl(String keyword, String boardid, String sType,
			int page) {
		StringBuilder sBuilder = new StringBuilder(getClientPrefix());
		sBuilder.append("queryresult.asp?page=").append(page).append("&stype=")
				.append(sType).append("&pSearch=1&nSearch=&keyword=")
				.append(keyword).append("&SearchDate=1000&boardid=")
				.append(boardid).append("&sertype=1");
		return sBuilder.toString();
	}

	@Override
	public String getInboxUrl(int pageNum) {
		return getClientPrefix() + "usersms.asp?action=inbox&page=" + pageNum;
	}

	@Override
	public String getOutboxUrl(int pageNum) {
		return getClientPrefix() + "usersms.asp?action=issend&page=" + pageNum;
	}

	@Override
	public String getMessagePageUrl(int pmId) {
		return getClientPrefix() + "messanger.asp?action=read&id=" + pmId;
	}

	@Override
	public String getPersonalBoardUrl() {
		return getClientPrefix();
	}

	@Override
	public String getClientUrl() {
		if (getProxyType()==LoginType.USER_DEFINED) {
			return PROXY;
		} else if (getProxyType()==LoginType.RVPN) {
			return ZJU_RVPN_HOST;
		} else {
			return CC98;
		}
	}

	@Override
	public String getBoardUrl(String boardId, int pageNum) {
		return getClientPrefix() + "list.asp?boardid=" + boardId + "&page=" + pageNum;
	}

	@Override
	public String getBoardUrl(String boardId) {
		return getBoardUrl(boardId, 1);
	}

	@Override
	public String getPostUrl(String boardId, String postId, int pageNum) {
		return getClientPrefix() + "dispbbs.asp?boardID=" + boardId + "&ID=" + postId
				+ "&star=" + pageNum;
	}
	
	@Override
	public String getCC98PostUrl(String boardId, String postId, int pageNum) {
		return CC98 + "dispbbs.asp?boardID=" + boardId + "&ID=" + postId
				+ "&star=" + pageNum;
	}
 
	@Override
	public String getHotTopicUrl() {
		return getClientPrefix() + "hottopic.asp";
	}

	@Override
	public String getUserProfileUrl(String userName) {
		return getClientPrefix() + "dispuser.asp?name=" + URLEncoder.encode(userName);
	}

	@Override
	public String getUserProfileUrl(LoginType type, String userName) {
		return getClientPrefix(type) + "dispuser.asp?name=" + URLEncoder.encode(userName);
	}

	@Override
	public String getNewPostUrl(int pageNum) {
		return getClientPrefix() + "queryresult.asp?stype=3" + "&page=" + pageNum;
	}

	@Override
	public String getUserManagerUrl() {
		return getClientPrefix() + "usermanager.asp";
	}

	@Override
	public String getAddFriendUrl() {
		return getClientPrefix() + "usersms.asp?action=friend";
	}

	@Override
	public String getAddFriendUrlReferrer() {
		return getClientPrefix() + "usersms.asp?action=friend&todo=addF";
	}

	@Override
	public String getLoginUrl() {
		return getClientPrefix() + "sign.asp";
	}

	@Override
	public String getLoginUrl(LoginType loginType) {
		return getClientPrefix(loginType) + "sign.asp";
	}
  
	@Override
	public String getSendPmUrl() {
		return getClientPrefix() + "messanger.asp?action=send";
	}

	@Override
	public String getPushNewPostReferer(String boardID) {
		return getClientPrefix() + "announce.asp?boardid=" + boardID;
	}

	@Override
	public String getPushNewPostUrl(String boardId) {
		return getClientPrefix() + "SaveAnnounce.asp?boardID=" + boardId;
	}

	@Override
	public String getSubmitReplyUrl(String boardID) {
		return getClientPrefix() + "SaveReAnnounce.asp?method=Topic&boardID="
				+ boardID;
	}

	@Override
	public String getSubmitReplyReferer(String boardID, String rootID) {
		return getClientPrefix() + "reannounce.asp?BoardID=" + boardID + "&id="
				+ rootID + "&star=1";
	}

	@Override
	public String getClientUrl(LoginType type) {
		if (type==LoginType.USER_DEFINED) {
			return PROXY;
		} else if(type==LoginType.NORMAL){
			return CC98;
		} else {
			return ZJU_RVPN_HOST;
		}
	}
}
