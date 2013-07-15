package tk.djcrazy.libCC98;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
import ch.boye.httpclientandroidlib.ParseException;
import ch.boye.httpclientandroidlib.auth.AuthenticationException;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.HttpClient;

public interface ICC98Service {

	public boolean isUseProxy();

	public void doLogin(String userName, String pwd32, String pw16, String proxyName,
			String proxyPwd, LoginType type) throws ClientProtocolException, IOException,
			IllegalAccessException, ParseException, ParseContentException, NetworkErrorException;

	public void logOut();

	public void clearProxy();

	public void addFriend(String friendName) throws ParseException, NoUserFoundException,
			IOException;

	public String getCurrentUserName();

	public List<Bitmap> getUserAvatars();

	public Bitmap getCurrentUserAvatar();

	public String uploadFile(File file) throws PatternSyntaxException, MalformedURLException,
			IOException, ParseContentException;

	public void pushNewPost(String boardId, String title, String faceString, String content)
			throws IOException;

	public void reply(String boardId, String postId, String title, String faceString, String content)
			throws Exception;

	public List<SearchResultEntity> searchPost(String keyword, String boardId, String sType,
			int page) throws ParseException, IOException, ParseContentException,
			java.text.ParseException;

	public void sendPm(String toUser, String title, String content) throws ClientProtocolException,
			IOException, CC98Exception;

	public List<HotTopicEntity> getHotTopicList() throws ClientProtocolException, ParseException,
			IOException, ParseContentException;

	public String getMsgContent(int pmId) throws ClientProtocolException, ParseException,
			IOException;

	public List<SearchResultEntity> getNewPostList(int currentPageNumber)
			throws ClientProtocolException, ParseException, IOException, ParseContentException,
			java.text.ParseException;

	public List<BoardEntity> getPersonalBoardList() throws ClientProtocolException, ParseException,
			IOException, ParseContentException, java.text.ParseException;

	public List<PmInfo> getPmData(int pageNum, InboxInfo inboxInfo, int type)
			throws ClientProtocolException, ParseException, IOException;

	public List<PostContentEntity> getPostContentList(String boardId, String postId, int pageNum)
			throws ClientProtocolException, ParseException, ParseContentException,
			java.text.ParseException, IOException;

	public List<PostEntity> getPostList(String boardId, int pageNum)
			throws ClientProtocolException, ParseException, IOException, ParseContentException,
			java.text.ParseException;

	public List<BoardStatus> getTodayBoardList() throws ClientProtocolException, ParseException,
			IOException, ParseContentException;

	public List<UserStatueEntity> getFriendList() throws ClientProtocolException, ParseException,
			IOException, ParseContentException;

	public UserProfileEntity getUserProfile(String userName) throws ParseException,
			NoUserFoundException, IOException, ParseContentException;

	public String getUserImgUrl(String userName) throws ParseContentException,
			ClientProtocolException, ParseException, IOException;

	public String getDomain();

	public Bitmap getBitmapFromUrl(String url) throws IOException;

	public ICC98Client getCC98Client();

	public UsersInfo getusersInfo();

	public void switchToUser(int index);

	public void deleteUserInfo(int pos);

	public List<BoardEntity> getBoardList(String boardId)
			throws org.apache.http.client.ClientProtocolException, org.apache.http.ParseException,
			IOException, ParseContentException, java.text.ParseException;

}
