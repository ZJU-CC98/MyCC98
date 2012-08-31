package tk.djcrazy.libCC98;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.libCC98.data.BoardEntity;
import tk.djcrazy.libCC98.data.BoardStatus;
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

public interface ICC98Parser {

	/**
	 * Get a List of Maps of posts info.
	 * 
	 * @author DJ
	 * @param boardLink
	 *            , url String
	 * @return A List that contains each post's basic information,
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @throws ParseContentException
	 * @throws java.text.ParseException 
	 * 
	 * @see #parsePostList(String)
	 */
	public List<PostEntity> getPostList(String boardId, int pageNum)
			throws ClientProtocolException, ParseException, IOException,
			ParseContentException, java.text.ParseException;

	/**
	 * Get a List of HashMaps of hot Topic
	 * 
	 * @author DJ
	 * @return List of hot topic
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @throws ParseContentException 
	 * 
	 */
	public List<HotTopicEntity> getHotTopicList()
			throws ClientProtocolException, ParseException, IOException,
			ParseContentException;

	/**
	 * Get a List of BoardEntity of board info from your personal board list.
	 * 
	 * @author DJ
	 * @return List board info.
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @throws ParseContentException 
	 * @throws java.text.ParseException 
	 * 
	 * @see #parsePersonalBoardList(String)
	 */
	public List<BoardEntity> getPersonalBoardList()
			throws ClientProtocolException, ParseException, IOException,
			ParseContentException, java.text.ParseException;

	/**
	 * Get a list of Maps of each reply post's info.
	 * 
	 * @author DJ
	 * @param postLink
	 * @return A list of Maps of each reply post's info.
	 * @throws java.text.ParseException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @throws ParseContentException
	 * @see #parsePostContentList(String)
	 */
	public List<PostContentEntity> getPostContentList(String boardId, String postId,
			int pageNum) throws ClientProtocolException, ParseException,
			ParseContentException, java.text.ParseException, IOException;

	/**
	 * @author DJ
	 * @param userName
	 * @return Map of user profile info
	 * 
	 *         //TODO the keys in the map are ...not finished, to be added
	 * @throws IOException
	 * @throws NoUserFoundException
	 * @throws ParseContentException 
	 * @throws ParseException 
	 */
	public UserProfileEntity getUserProfile(String userName)
			throws NoUserFoundException, IOException, ParseException,
			ParseContentException;

	/**
	 * @author DJ
	 * @param pageNum 
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @throws java.text.ParseException 
	 * @throws ParseContentException 
	 */
	public List<SearchResultEntity> getNewPostList(int pageNum)
			throws ClientProtocolException, ParseException, IOException, ParseContentException, java.text.ParseException;

	/**
	 * get user friend list
	 * 
	 * @param html
	 * @return list of user
	 * @throws ClientProtocolException
	 * @throws ParseException
	 * @throws IOException
	 * @throws ParseContentException 
	 */
	public List<UserStatueEntity> getUserFriendList()
			throws ClientProtocolException, ParseException, IOException,
			ParseContentException;

	/**
	 * Get the content of the pm using the given pm id.
	 * 
	 * @author zsy
	 * 
	 * @param pmId
	 * @return A String of pm content.
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 */
	public String getMsgContent(int pmId) throws ClientProtocolException,
			ParseException, IOException;

	/**
	 * 
	 * @param page_num
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 */

	public List<PmInfo> getPmData(int page_num, InboxInfo inboxInfo, int type)
			throws ClientProtocolException, ParseException, IOException;

	public List<SearchResultEntity> searchPost(String keyword, String boardid,
			String sType, int page) throws ParseException, IOException, ParseContentException, java.text.ParseException;

	public List<SearchResultEntity> query(String keyWord, String sType,
			String searchDate, int boardArea, String boardID)
			throws ParseException, IOException, ParseContentException, java.text.ParseException;

	/**
	 * @author DJ
	 * @author zsy (bug fix)
	 * @param html
	 * @return
	 * @throws java.text.ParseException 
	 * @throws ParseContentException 
	 */
	public List<SearchResultEntity> parseQueryResult(String html) throws ParseContentException, java.text.ParseException;

	/**
	 * @author zsy
	 * @return
	 * @throws ClientProtocolException
	 * @throws ParseException
	 * @throws IOException
	 * @throws ParseContentException 
	 */
	public List<BoardStatus> getTodayBoardList()
			throws ClientProtocolException, ParseException, IOException, ParseContentException;

	/**
	 * @return the cc98Client
	 */
	public ICC98Client getCC98Client();

	/**
	 * @param cc98Client
	 *            the cc98Client to set
	 */
	public void setCC98Client(ICC98Client cc98Client);

	/**
	 * @return the cc98UrlManager
	 */
	public ICC98UrlManager getCC98UrlManager();

	/**
	 * @param cc98UrlManager
	 *            the cc98UrlManager to set
	 */
	public void setCC98UrlManager(ICC98UrlManager cc98UrlManager);

}