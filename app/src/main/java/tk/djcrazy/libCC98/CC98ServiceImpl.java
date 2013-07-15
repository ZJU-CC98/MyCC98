package tk.djcrazy.libCC98;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import tk.djcrazy.MyCC98.application.MyApplication.UsersInfo;
import tk.djcrazy.libCC98.data.BoardEntity;
import tk.djcrazy.libCC98.data.BoardStatus;
import tk.djcrazy.libCC98.data.HotTopicEntity;
import tk.djcrazy.libCC98.data.InboxInfo;
import tk.djcrazy.libCC98.data.LoginType;
import tk.djcrazy.libCC98.data.PmInfo;
import tk.djcrazy.libCC98.data.PostContentEntity;
import tk.djcrazy.libCC98.data.PostEntity;
import tk.djcrazy.libCC98.data.SearchResultEntity;
import tk.djcrazy.libCC98.data.UserProfileEntity;
import tk.djcrazy.libCC98.data.UserStatueEntity;
import tk.djcrazy.libCC98.exception.CC98Exception;
import tk.djcrazy.libCC98.exception.NoUserFoundException;
import tk.djcrazy.libCC98.exception.ParseContentException;
import android.accounts.NetworkErrorException;
import android.graphics.Bitmap;
import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.ParseException;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CC98ServiceImpl implements ICC98Service {
	@Inject
	private ICC98Client cc98Client;
	@Inject
	private ICC98Parser cc98Parser;

	@Override
	public void doLogin(String userName, String pwd32, String pwd16, String proxyName,
			String proxyPwd, LoginType type) throws ClientProtocolException, IOException,
			IllegalAccessException, ParseException, ParseContentException, NetworkErrorException {
		cc98Client.doLogin(userName, pwd32, pwd16, proxyName, proxyPwd, type);
	}

	@Override
	public void logOut() {
		cc98Client.clearLoginInfo();
	}

	@Override
	public void clearProxy() {
		cc98Client.clearLoginInfo();
	}

	@Override
	public void addFriend(String friendName) throws ParseException, NoUserFoundException,
			IOException {
		cc98Client.addFriend(friendName);
	}

	@Override
	public String uploadFile(File file) throws PatternSyntaxException, MalformedURLException,
			IOException, ParseContentException {
		return cc98Client.uploadPicture(file);
	}

	@Override
	public void pushNewPost(String boardId, String title, String faceString, String content)
			throws ClientProtocolException, IOException {
		List<NameValuePair> nvpsList = new ArrayList<NameValuePair>();
		nvpsList.add(new BasicNameValuePair("upfilername", ""));
		nvpsList.add(new BasicNameValuePair("subject", title));
		nvpsList.add(new BasicNameValuePair("Expression", faceString));
		nvpsList.add(new BasicNameValuePair("Content", content));
		nvpsList.add(new BasicNameValuePair("signflag", "yes"));
		nvpsList.add(new BasicNameValuePair("Submit", "发 表"));
		cc98Client.pushNewPost(nvpsList, boardId);
	}

	@Override
	public void reply(String boardId, String rootId, String title, String faceString, String content)
			throws Exception {
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
		cc98Client.submitReply(nvpsList, boardId, rootId);
	}

	@Override
	public List<SearchResultEntity> searchPost(String keyword, String boardid, String sType,
			int page) throws ParseException, IOException, ParseContentException,
			java.text.ParseException {
		return cc98Parser.searchPost(keyword, boardid, sType, page);
	}

	@Override
	public void sendPm(String toUser, String title, String content) throws ClientProtocolException,
			IOException, CC98Exception {
		cc98Client.sendPm(toUser, title, content);
	}

	@Override
	public List<HotTopicEntity> getHotTopicList() throws ClientProtocolException, ParseException,
			IOException, ParseContentException {
		return cc98Parser.getHotTopicList();
	}

	@Override
	public String getMsgContent(int pmId) throws ClientProtocolException, ParseException,
			IOException {
		return cc98Parser.getMsgContent(pmId);
	}

	@Override
	public List<SearchResultEntity> getNewPostList(int pageNum) throws ClientProtocolException,
			ParseException, IOException, ParseContentException, java.text.ParseException {
		return cc98Parser.getNewPostList(pageNum);
	}

	@Override
	public List<BoardEntity> getPersonalBoardList() throws ClientProtocolException, ParseException,
			IOException, ParseContentException, java.text.ParseException {
		return cc98Parser.getPersonalBoardList();
	}

	@Override
	public List<BoardEntity> getBoardList(String boardId)
			throws org.apache.http.client.ClientProtocolException, org.apache.http.ParseException,
			IOException, ParseContentException, java.text.ParseException {
		return cc98Parser.getBoardList(boardId);
	}

	@Override
	public List<PmInfo> getPmData(int pageNum, InboxInfo inboxInfo, int type)
			throws ClientProtocolException, ParseException, IOException {
		return cc98Parser.getPmData(pageNum, inboxInfo, type);
	}

	@Override
	public List<PostContentEntity> getPostContentList(String boardId, String postId, int pageNum)
			throws ClientProtocolException, ParseException, ParseContentException,
			java.text.ParseException, IOException {
		return cc98Parser.getPostContentList(boardId, postId, pageNum);
	}

	@Override
	public List<PostEntity> getPostList(String boardId, int pageNum)
			throws ClientProtocolException, ParseException, IOException, ParseContentException,
			java.text.ParseException {
		return cc98Parser.getPostList(boardId, pageNum);
	}

	@Override
	public List<BoardStatus> getTodayBoardList() throws ClientProtocolException, ParseException,
			IOException, ParseContentException {
		return cc98Parser.getTodayBoardList();
	}

	@Override
	public List<UserStatueEntity> getFriendList() throws ClientProtocolException, ParseException,
			IOException, ParseContentException {
		return cc98Parser.getUserFriendList();
	}

	@Override
	public UserProfileEntity getUserProfile(String userName) throws ParseException,
			NoUserFoundException, IOException, ParseContentException {
		return cc98Parser.getUserProfile(userName);
	}

	@Override
	public String getUserImgUrl(String userName) throws ClientProtocolException, ParseException,
			IOException, ParseContentException {
		cc98Client.getUserImgUrl(userName);
		return null;
	}

	@Override
	public String getDomain() {
		return cc98Client.getDomain();
	}

	@Override
	public Bitmap getBitmapFromUrl(String url) throws IOException {
		return cc98Client.getBitmapFromUrl(url);
	}

	@Override
	public boolean isUseProxy() {
		return false;
	}

	@Override
	public String getCurrentUserName() {
		return cc98Client.getCurrentUserData().getUserName();
	}

	@Override
	public List<Bitmap> getUserAvatars() {
		return cc98Client.getUserAvatars();
	}

	@Override
	public ICC98Client getCC98Client() {
		return cc98Client;
	}

	@Override
	public UsersInfo getusersInfo() {
		return cc98Client.getUsersInfo();
	}

	@Override
	public Bitmap getCurrentUserAvatar() {
		return cc98Client.getCurrentUserAvatar();
	}

	@Override
	public void switchToUser(int index) {
		cc98Client.switchToUser(index);
	}

	@Override
	public void deleteUserInfo(int pos) {
		cc98Client.deleteUserInfo(pos);
	}

}
