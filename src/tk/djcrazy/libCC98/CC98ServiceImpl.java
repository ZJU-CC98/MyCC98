package tk.djcrazy.libCC98;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

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
import android.graphics.Bitmap;

public class CC98ServiceImpl implements ICC98Service {
	private enum Status{
		INIT, PROXYED, LOGINED, PROXY_LOGINED;
	}
	private Status status = Status.INIT;
	private CC98Client cc98Client;
	private CC98Parser cc98Parser;
	private boolean useProxy=false;
	
	@Override
	public void doProxyAuthorization(String userName, String pwd) throws ClientProtocolException, IOException, URISyntaxException {
		if (useProxy==true||status==Status.INIT) {
			cc98Client.doHttpBasicAuthorization(userName, pwd);
			status = Status.PROXYED;
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
	public void doLogin(String userName, String pwd) throws ClientProtocolException, IOException, IllegalAccessException {
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
		
		
	}

	@Override
	public void clearProxy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addFriend(String friendName) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bitmap getUserAvatar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String uploadFile(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pushNewPost(int boardId, String title, int faceId,
			String content) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reply(int boardId, int postId, String title, int faceId,
			String content) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<SearchResultEntity> searchPost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendPm(String toUser, String title, String content) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<HotTopicEntity> getHotTopicList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMsgContent(int pmId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SearchResultEntity> getNewPostList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BoardEntity> getPersonalBoardList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PmInfo> getPmData(int pageNum, InboxInfo inboxInfo, int type)
			throws ClientProtocolException, ParseException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PostContentEntity> getPostContentList(int boardId, int postId,
			int pageNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PostEntity> getPostList(int boardId, int pageNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BoardStatue> getTodayBoardList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserStatueEntity> getFriendList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserProfileEntity getUserProfile(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

}
