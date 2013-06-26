package tk.djcrazy.libCC98;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.PatternSyntaxException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.application.MyApplication;
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
import tk.djcrazy.libCC98.util.SerializableCacheUtil;
import android.accounts.NetworkErrorException;
import android.graphics.Bitmap;
import android.view.FocusFinder;
import ch.boye.httpclientandroidlib.ParseException;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;

@Singleton
public class CachedCC98Service {
	@Inject
	private ICC98Service service;
	private SerializableCache cache;

	@Inject
	private CachedCC98Service() {
		cache = SerializableCache.getInstance(MyApplication.getAppContext());
	}

	public boolean isUseProxy() {
		return service.isUseProxy();
	}

	abstract class ServiceCallable<T> {
		@SuppressWarnings("unchecked")
		public T call(boolean forceRefresh) throws Exception {
			T ret = null;
			String keyString = getKeyString();
			beforeLoad();
			Object object = cache.get(keyString);
			if (forceRefresh || object == null) {
				ret = getContent();
				if (ret != null) {
					cache.put(keyString, (Serializable) ret);
				}
			} else {
				ret = (T) object;
			}
			return ret;
		}

		protected void beforeLoad() {

		}

		abstract protected T getContent() throws Exception;

		abstract protected String getKeyString();
	}

	public void doLogin(String userName, String pwd32, String pw16,
			String proxyName, String proxyPwd, LoginType type)
			throws ClientProtocolException, IOException,
			IllegalAccessException, ParseException, ParseContentException,
			NetworkErrorException {
		service.doLogin(userName, pwd32, pw16, proxyName, proxyPwd, type);
	}

	public void logOut() {
		service.logOut();
	}

	public void clearProxy() {
		service.clearProxy();
	}

	public void addFriend(String friendName) throws ParseException,
			NoUserFoundException, IOException {
		service.addFriend(friendName);
	}

	public String getCurrentUserName() {
		return service.getCurrentUserName();
	}

	public List<Bitmap> getUserAvatars() {
		return service.getUserAvatars();
	}

	public Bitmap getCurrentUserAvatar() {
		return service.getCurrentUserAvatar();
	}

	public String uploadFile(File file) throws PatternSyntaxException,
			MalformedURLException, IOException, ParseContentException {
		return service.uploadFile(file);
	}

	public void pushNewPost(String boardId, String title, String faceString,
			String content) throws ClientProtocolException, IOException {
		service.pushNewPost(boardId, title, faceString, content);
	}

	public void reply(String boardId, String postId, String title,
			String faceString, String content) throws ClientProtocolException,
			IOException, Exception {
		service.reply(boardId, postId, title, faceString, content);
	}

	public List<SearchResultEntity> searchPost(String keyword, String boardid,
			String sType, int page) throws ParseException, IOException,
			ParseContentException, java.text.ParseException {
		return service.searchPost(keyword, boardid, sType, page);
	}

	public void sendPm(String toUser, String title, String content)
			throws ClientProtocolException, IOException, CC98Exception {
		service.sendPm(toUser, title, content);
	}

	public List<HotTopicEntity> getHotTopicList(boolean forceRefresh)
			throws Exception {
		return (new ServiceCallable<List<HotTopicEntity>>() {
			protected List<HotTopicEntity> getContent()
					throws ClientProtocolException, ParseException,
					IOException, ParseContentException {
				return service.getHotTopicList();
			}

			protected String getKeyString() {
				return SerializableCacheUtil.hottopicKey();
			}
		}).call(forceRefresh);
	}

	public String getMsgContent(final int pmId, boolean forceRefresh)
			throws Exception {
		return (new ServiceCallable<String>() {
			protected String getContent() throws ClientProtocolException,
					ParseException, IOException {
				return service.getMsgContent(pmId);
			}

			protected String getKeyString() {
				return SerializableCacheUtil.msgKey(pmId);
			}
		}).call(forceRefresh);
	}

	public List<SearchResultEntity> getNewPostList(final int currentPageNumber,
			final boolean forceRefresh) throws Exception {
		return (new ServiceCallable<List<SearchResultEntity>>() {
			protected List<SearchResultEntity> getContent()
					throws ClientProtocolException, ParseException,
					IOException, ParseContentException,
					java.text.ParseException {
				return service.getNewPostList(currentPageNumber);
			}

			protected String getKeyString() {
				return SerializableCacheUtil.newTopicKey(currentPageNumber);
			}

			protected void beforeLoad() {
				if (forceRefresh) {
					cache.removeAllWithPrefix(SerializableCacheUtil
							.newTopicKey());
				}
			}
		}).call(forceRefresh);
	}

	public List<BoardEntity> getPersonalBoardList(boolean forceRefresh)
			throws Exception {
		return (new ServiceCallable<List<BoardEntity>>() {
			protected List<BoardEntity> getContent()
					throws ClientProtocolException, ParseException,
					IOException, ParseContentException,
					java.text.ParseException {
				return service.getPersonalBoardList();
			}

			protected String getKeyString() {
				return SerializableCacheUtil.personalBoardKey(service
						.getCurrentUserName());
			}
		}).call(forceRefresh);
	}

	public List<BoardEntity> getBoardList(final String boardId,
			boolean forceRefresh) throws Exception {
		return (new ServiceCallable<List<BoardEntity>>() {
			protected List<BoardEntity> getContent()
					throws org.apache.http.client.ClientProtocolException,
					org.apache.http.ParseException, IOException,
					ParseContentException, java.text.ParseException {
				return service.getBoardList(boardId);
			}

			protected String getKeyString() {
				return SerializableCacheUtil.boardsKey(boardId);
			}
		}).call(forceRefresh);
	}

	public List<PmInfo> getPmData(int pageNum, InboxInfo inboxInfo, int type)
			throws ClientProtocolException, ParseException, IOException {
		return service.getPmData(pageNum, inboxInfo, type);
	}

	public List<PostContentEntity> getPostContentList(final String boardId,
			final String postId, final int pageNum, boolean forceRefresh)
			throws Exception {
		return (new ServiceCallable<List<PostContentEntity>>() {
			protected List<PostContentEntity> getContent()
					throws ClientProtocolException, ParseException,
					ParseContentException, java.text.ParseException,
					IOException {
				return service.getPostContentList(boardId, postId, pageNum);
			}

			protected String getKeyString() {
				return SerializableCacheUtil.postPageKey(boardId, postId,
						pageNum);
			}

			protected void beforeLoad() {
				if (pageNum == PostContentsJSActivity.LAST_PAGE) {
					cache.remove(SerializableCacheUtil.postPageKey(boardId,
							postId, pageNum));
				}
			}
		}).call(forceRefresh);
	}

	public List<PostEntity> getPostList(final String boardId,
			final int pageNum, final boolean forceRefresh) throws Exception {
		return (new ServiceCallable<List<PostEntity>>() {
			protected List<PostEntity> getContent()
					throws ClientProtocolException, ParseException,
					IOException, ParseContentException,
					java.text.ParseException {
				return service.getPostList(boardId, pageNum);
			}

			protected String getKeyString() {
				return SerializableCacheUtil.postListKey(boardId, pageNum);
			}

			protected void beforeLoad() {
				if (forceRefresh) {
					cache.removeAllWithPrefix(SerializableCacheUtil
							.postListKey(boardId));
				}
			}
		}).call(forceRefresh);
	}

	public List<BoardStatus> getTodayBoardList()
			throws ClientProtocolException, ParseException, IOException,
			ParseContentException {
		return service.getTodayBoardList();
	}

	public List<UserStatueEntity> getFriendList()
			throws ClientProtocolException, ParseException, IOException,
			ParseContentException {
		return service.getFriendList();
	}

	public UserProfileEntity getUserProfile(String userName)
			throws ParseException, NoUserFoundException, IOException,
			ParseContentException {
		return service.getUserProfile(userName);
	}

	public String getUserImgUrl(String userName) throws ParseContentException,
			ClientProtocolException, ParseException, IOException {
		return service.getUserImgUrl(userName);
	}

	public String getDomain() {
		return service.getDomain();
	}

	public Bitmap getBitmapFromUrl(String url) throws IOException {
		return service.getBitmapFromUrl(url);
	}

	public ICC98Client getCC98Client() {
		return service.getCC98Client();
	}

	public UsersInfo getusersInfo() {
		return service.getusersInfo();
	}

	public void switchToUser(int index) {
		service.switchToUser(index);
	}

	public void deleteUserInfo(int pos) {
		service.deleteUserInfo(pos);
	}
}
