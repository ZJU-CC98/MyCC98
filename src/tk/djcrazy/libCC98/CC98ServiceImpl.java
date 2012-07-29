package tk.djcrazy.libCC98;

import java.io.File;
import java.io.IOException;
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

	@Override
	public void doProxyAuthorization(String userName, String pwd) {
		
	}

	@Override
	public void setUseProxy(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isUseProxy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void doLogin(String userName, String pwd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logOut() {
		// TODO Auto-generated method stub

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
