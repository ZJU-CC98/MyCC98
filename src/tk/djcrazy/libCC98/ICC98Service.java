package tk.djcrazy.libCC98;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.apache.http.NameValuePair;
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
import tk.djcrazy.libCC98.exception.NoUserFoundException;
import tk.djcrazy.libCC98.exception.ParseContentException;
import android.accounts.NetworkErrorException;
import android.graphics.Bitmap;

public interface ICC98Service {
	public boolean doProxyAuthorization(String userName, String pwd)
			throws ClientProtocolException, IOException, URISyntaxException;

	public void setUseProxy(boolean b);

	public boolean isUseProxy();

	public void doLogin(String userName, String pwd)
			throws ClientProtocolException, IOException,
			IllegalAccessException, ParseException, ParseContentException,
			NetworkErrorException;

	public void logOut();

	public void clearProxy();

	public void addFriend(String friendName) throws ParseException,
			NoUserFoundException, IOException;

	public String getUserName();

	public Bitmap getUserAvatar();

	public String uploadFile(File file) throws PatternSyntaxException,
			MalformedURLException, IOException, ParseContentException;

	public void pushNewPost(String boardId, String title, String faceString,
			String content) throws ClientProtocolException, IOException;

	public void reply(String boardId, String postId, String title,
			String faceString, String content) throws ClientProtocolException,
			IOException;

	public List<Map<String, Object>> searchPost(String keyword, int boardid,
			String sType, int page) throws ParseException, IOException;

	public void sendPm(String toUser, String title, String content)
			throws ClientProtocolException, IOException;

	public List<HotTopicEntity> getHotTopicList()
			throws ClientProtocolException, ParseException, IOException,
			ParseContentException;

	public String getMsgContent(int pmId) throws ClientProtocolException,
			ParseException, IOException;

	public List<Map<String, Object>> getNewPostList()
			throws ClientProtocolException, ParseException, IOException;

	public List<BoardEntity> getPersonalBoardList()
			throws ClientProtocolException, ParseException, IOException,
			ParseContentException;

	public List<PmInfo> getPmData(int pageNum, InboxInfo inboxInfo, int type)
			throws ClientProtocolException, ParseException, IOException;

	public List<PostContentEntity> getPostContentList(int boardId, int postId,
			int pageNum) throws ClientProtocolException, ParseException,
			ParseContentException, java.text.ParseException, IOException;

	public List<PostEntity> getPostList(int boardId, int pageNum)
			throws ClientProtocolException, ParseException, IOException,
			ParseContentException;

	public List<NameValuePair> getTodayBoardList()
			throws ClientProtocolException, ParseException, IOException;

	public List<UserStatueEntity> getFriendList()
			throws ClientProtocolException, ParseException, IOException,
			ParseContentException;

	public UserProfileEntity getUserProfile(String userName)
			throws ParseException, NoUserFoundException, IOException,
			ParseContentException;
	
	public String getUserImgUrl(String userName) throws ParseContentException, ClientProtocolException, ParseException, IOException;
 
	public String getDomain();

	public Bitmap getBitmapFromUrl(String url) throws IOException;
}
