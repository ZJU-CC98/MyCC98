package tk.djcrazy.libCC98;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

 
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tk.djcrazy.libCC98.data.BoardEntity;
import tk.djcrazy.libCC98.data.BoardStatue;
import tk.djcrazy.libCC98.data.HotTopicEntity;
import tk.djcrazy.libCC98.data.InboxInfo;
import tk.djcrazy.libCC98.data.PmInfo;
import tk.djcrazy.libCC98.data.PostContentEntity;
import tk.djcrazy.libCC98.data.PostEntity;
import tk.djcrazy.libCC98.data.SearchResultEntity;
import tk.djcrazy.libCC98.data.UserProfileEntity;
import tk.djcrazy.libCC98.data.UserStatueEntity;
import tk.djcrazy.libCC98.exception.NoUserFoundException;
import tk.djcrazy.libCC98.exception.ParseContentException;
import android.accounts.NetworkErrorException;
import android.graphics.Bitmap;

@Singleton
public class CC98ServiceImpl implements ICC98Service {
	private enum Status{
		INIT, PROXYED, LOGINED, PROXY_LOGINED;
	}
	private Status status = Status.INIT;
	@Inject
	private ICC98Client cc98Client;
	@Inject
	private ICC98Parser cc98Parser;
	
	private boolean useProxy=false;
	
	@Override
	public boolean doProxyAuthorization(String userName, String pwd) throws ClientProtocolException, IOException, URISyntaxException {
		if (useProxy==true||status==Status.INIT) {
			boolean res = cc98Client.doHttpBasicAuthorization(userName, pwd);
			status = Status.PROXYED;
			return res;
		} else {
			throw new IllegalStateException("You cannot do this before you set proxy, or have done login or do proxy.");
		}
	}

	@Override
	public void setUseProxy(boolean b) {
		if (status==Status.INIT) {
			useProxy = true;
		} else {
			throw new IllegalStateException("You cannnot do this when you hava proxyed or logined");
		}
	}

	@Override
	public boolean isUseProxy() {
		return useProxy;
	}

	@Override
	public void doLogin(String userName, String pwd) throws ClientProtocolException, IOException, IllegalAccessException, ParseException, ParseContentException, NetworkErrorException {
		if (useProxy==false&&status==Status.INIT) {
			cc98Client.doLogin(userName, pwd);
			status = Status.LOGINED;
		} else if (useProxy==true&&status==Status.INIT) {
			cc98Client.doLogin(userName, pwd);
			status = Status.PROXY_LOGINED;
		} else {
			throw new IllegalStateException("You cannot do login due to illegal state.");
		}
	}

	@Override
	public void logOut() {
		if (status==Status.LOGINED||status==Status.PROXY_LOGINED) {
			cc98Client.clearLoginInfo();
		} else {
			throw new IllegalStateException("You cannot do logout due to illegal state.");
		}
	}

	@Override
	public void clearProxy() {
		if (status==Status.PROXYED) {
			cc98Client.clearLoginInfo();
		} else {
			throw new IllegalStateException("You cannot do clear proxy due to illegal state.");
		}
	}

	@Override
	public void addFriend(String friendName) throws ParseException, NoUserFoundException, IOException {
		if (status==Status.LOGINED||status==Status.PROXY_LOGINED) {
			cc98Client.addFriend(friendName);
		} else {
			throw new IllegalStateException("You cannot do this due without login.");
		}
	}

	@Override
	public String getUserName() {
 		return cc98Client.getUserName();
	}

	@Override
	public Bitmap getUserAvatar() {
		return cc98Client.getLoginUserImg();
	}

	@Override
	public String uploadFile(File file) throws PatternSyntaxException, MalformedURLException, IOException, ParseContentException {
		if (status==Status.LOGINED||status==Status.PROXY_LOGINED) {
			return cc98Client.uploadPictureToCC98(file);
		} else {
			throw new IllegalStateException("You cannot do this due without login.");
		}
	}

	@Override
	public void pushNewPost(String boardId, String title, String faceString,
			String content) throws ClientProtocolException, IOException {
		if (status==Status.LOGINED||status==Status.PROXY_LOGINED) {
			List<NameValuePair> nvpsList = new ArrayList<NameValuePair>();
			nvpsList.add(new BasicNameValuePair("upfilername", ""));
			nvpsList.add(new BasicNameValuePair("subject", title));
			nvpsList.add(new BasicNameValuePair("Expression", faceString));
			nvpsList.add(new BasicNameValuePair("Content", content));
			nvpsList.add(new BasicNameValuePair("signflag", "yes"));
			nvpsList.add(new BasicNameValuePair("Submit", "发 表"));
			cc98Client.pushNewPost(nvpsList, boardId);
		} else {
			throw new IllegalStateException("You cannot do this due without login.");
		}
	}

	@Override
	public void reply(String boardId, String rootId, String title, String faceString,
			String content) throws ClientProtocolException, IOException {
		List<NameValuePair> nvpsList = new ArrayList<NameValuePair>();
		nvpsList.add(new BasicNameValuePair("upfilername", ""));
		nvpsList.add(new BasicNameValuePair("followup", rootId));
		nvpsList.add(new BasicNameValuePair("rootID", rootId));
		nvpsList.add(new BasicNameValuePair("star", ""));
		nvpsList.add(new BasicNameValuePair("TotalUseTable", "bbs5"));
		nvpsList.add(new BasicNameValuePair("subject", title));
		nvpsList.add(new BasicNameValuePair("Expression", faceString));
		nvpsList.add(new BasicNameValuePair("Content", content));
		nvpsList.add(new BasicNameValuePair("signflag", "yes"));
		cc98Client.submitReply(nvpsList,  boardId, rootId);
	}

	@Override
	public List<Map<String, Object>> searchPost(String keyword, int boardid,
			String sType, int page) throws ParseException, IOException {
			return cc98Parser.searchPost(keyword, boardid, sType, page);
	}

	@Override
	public void sendPm(String toUser, String title, String content) throws ClientProtocolException, IOException {
		cc98Client.sendPm(toUser, title, content);
	}

	@Override
	public List<HotTopicEntity> getHotTopicList() throws ClientProtocolException, ParseException, IOException, ParseContentException {
		return cc98Parser.getHotTopicList();
	}

	@Override
	public String getMsgContent(int pmId) throws ClientProtocolException, ParseException, IOException {
		return cc98Parser.getMsgContent(pmId);
	}

	@Override
	public List<Map<String, Object>> getNewPostList() throws ClientProtocolException, ParseException, IOException {
		return cc98Parser.getNewPostList();
	}

	@Override
	public List<BoardEntity> getPersonalBoardList() throws ClientProtocolException, ParseException, IOException, ParseContentException {
		return cc98Parser.getPersonalBoardList();
	}

	@Override
	public List<PmInfo> getPmData(int pageNum, InboxInfo inboxInfo, int type)
			throws ClientProtocolException, ParseException, IOException {
		return cc98Parser.getPmData(pageNum, inboxInfo, type);
	}

	@Override
	public List<PostContentEntity> getPostContentList(int boardId, int postId,
			int pageNum) throws ClientProtocolException, ParseException, ParseContentException, java.text.ParseException, IOException {
		return cc98Parser.getPostContentList(boardId, postId, pageNum);
	}

	@Override
	public List<PostEntity> getPostList(int boardId, int pageNum) throws ClientProtocolException, ParseException, IOException, ParseContentException {
		return cc98Parser.getPostList(boardId, pageNum);
	}

	@Override
	public List<NameValuePair> getTodayBoardList() throws ClientProtocolException, ParseException, IOException {
		return cc98Parser.getTodayBoardList();
	}

	@Override
	public List<UserStatueEntity> getFriendList() throws ClientProtocolException, ParseException, IOException, ParseContentException {
		return cc98Parser.getUserFriendList();
	}

	@Override
	public UserProfileEntity getUserProfile(String userName) throws ParseException, NoUserFoundException, IOException, ParseContentException {
		return cc98Parser.getUserProfile(userName);
	}

	@Override
	public String getUserImgUrl(String userName) throws ClientProtocolException, ParseException, IOException, ParseContentException {
		cc98Client.getUserImgUrl(userName);
		return null;
	}

	@Override
	public String getDomain() {
		return cc98Client.getDomain();
	}

}
