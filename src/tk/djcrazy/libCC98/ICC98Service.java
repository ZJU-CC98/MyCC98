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

public interface ICC98Service {
	public void doProxyAuthorization(String userName, String pwd) throws ClientProtocolException, IOException, URISyntaxException;
	public void setUseProxy(boolean b);
	public boolean isUseProxy();
	public void doLogin(String userName, String pwd) throws ClientProtocolException, IOException, IllegalAccessException;
	public void logOut();
	public void clearProxy();
	public void addFriend(String friendName);
	public String getUserName();
	public Bitmap getUserAvatar();
	public String uploadFile(File file);
	public void pushNewPost(int boardId, String title, int faceId, String content);
	
	public void reply(int boardId, int postId, String title, int faceId, String content);
	
	public List<SearchResultEntity> searchPost();
	
	public void sendPm(String toUser, String title, String content);
	
	public List<HotTopicEntity> getHotTopicList();

	public String getMsgContent(int pmId);
	
	public List<SearchResultEntity> getNewPostList();
	
	public List<BoardEntity> getPersonalBoardList();
	
	public   List<PmInfo> getPmData(int pageNum, InboxInfo inboxInfo,
			int type) throws ClientProtocolException, ParseException,
			IOException;
	
	public List<PostContentEntity> getPostContentList(int boardId, int postId, int pageNum);
	
	public List<PostEntity> getPostList(int boardId, int pageNum);
	
	public List<BoardStatue> getTodayBoardList();
	
	public List<UserStatueEntity> getFriendList();
	
	public UserProfileEntity getUserProfile(String userName);
	
}
