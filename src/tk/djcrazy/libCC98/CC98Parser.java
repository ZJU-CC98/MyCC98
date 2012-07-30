package tk.djcrazy.libCC98;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import tk.djcrazy.MyCC98.PmActivity;
import tk.djcrazy.libCC98.data.BoardEntity;
import tk.djcrazy.libCC98.data.Gender;
import tk.djcrazy.libCC98.data.HotTopicEntity;
import tk.djcrazy.libCC98.data.InboxInfo;
import tk.djcrazy.libCC98.data.PmInfo;
import tk.djcrazy.libCC98.data.PostContentEntity;
import tk.djcrazy.libCC98.data.PostEntity;
import tk.djcrazy.libCC98.data.UserProfileEntity;
import tk.djcrazy.libCC98.data.UserStatue;
import tk.djcrazy.libCC98.data.UserStatueEntity;
import tk.djcrazy.libCC98.exception.NoUserFoundException;
import tk.djcrazy.libCC98.exception.ParseContentException;
import static tk.djcrazy.libCC98.util.StringUtil.*;
import static tk.djcrazy.libCC98.util.RegexUtil.*;
import static tk.djcrazy.libCC98.CC98ParseRepository.*;
import static tk.djcrazy.libCC98.util.DateFormatUtil.*;

public class CC98Parser {

	private CC98Client cc98Client;
	private CC98UrlManager cc98UrlManager;

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
	 * 
	 * @see #parsePostList(String)
	 */
	public List<PostEntity> getPostList(int boardId, int pageNum)
			throws ClientProtocolException, ParseException, IOException,
			ParseContentException {

		return parsePostList(cc98Client.getPage(cc98UrlManager.getBoardUrl(
				boardId, pageNum)));
	}

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
			throws ClientProtocolException, ParseException, IOException, ParseContentException {
		return parseHotTopicList(cc98Client.getPage(cc98UrlManager
				.getHotTopicUrl()));
	}

	/**
	 * Get a List of BoardEntity of board info from your personal board list.
	 * 
	 * @author DJ
	 * @return List board info.
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @throws ParseContentException 
	 * 
	 * @see #parsePersonalBoardList(String)
	 */
	public List<BoardEntity> getPersonalBoardList()
			throws ClientProtocolException, ParseException, IOException, ParseContentException {
		return parsePersonalBoardList(cc98Client.getPage(cc98UrlManager
				.getPersonalBoardUrl()));
	}

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
	public List<PostContentEntity> getPostContentList(int boardId, int postId,
			int pageNum) throws ClientProtocolException, ParseException,
			ParseContentException, java.text.ParseException, IOException {
		return parsePostContentList(cc98Client.getPage(cc98UrlManager
				.getPostUrl(boardId, postId, pageNum)));
	}

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
			throws NoUserFoundException, IOException, ParseException, ParseContentException {
		return parseUserProfile(cc98Client.getPage(cc98UrlManager
				.getUserProfileUrl(userName)));
	}

	/**
	 * @author DJ
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 */
	public List<Map<String, Object>> getNewPostList()
			throws ClientProtocolException, ParseException, IOException {
		return parseQueryResult(cc98Client.getPage(cc98UrlManager
				.getNewPostUrl()));
	}

	/**
	 * get user friend list
	 * 
	 * @param html
	 * @return list of user
	 * @throws ClientProtocolException
	 * @throws ParseException
	 * @throws IOException
	 */
	public List<UserStatueEntity> getUserFriendList()
			throws ClientProtocolException, ParseException, IOException {
		return parseUserFriendList(cc98Client.getPage(cc98UrlManager
				.getUserManagerUrl()));
	}
	/**
	 * Get the html of the pm using the given pm id.
	 * 
	 * @author zsy
	 * 
	 * @param pmId
	 *            The id which CC98 assigned to each pm.
	 * @return The html of the pm page.
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 */
	private String getMsgPageHtml(int pmId) throws ClientProtocolException,
			ParseException, IOException {
		return cc98Client.getPage(cc98UrlManager.getMessagePageUrl(pmId));
	}


	/**
	 * Parse the HTML, and put posts and their poster id in a list of
	 * NameValuePair
	 * 
	 * @param html
	 *            String (must not be null)
	 * @return A list of the pair of poster id and their posts
	 * @throws ParseContentException
	 * @throws java.text.ParseException
	 */
	private List<PostContentEntity> parsePostContentList(String html)
			throws ParseContentException, java.text.ParseException {
		List<PostContentEntity> list = new ArrayList<PostContentEntity>();
		// get some information of the topic
		List<String> postInfoList = getMatchedStringList(
				POST_CONTENT_INFO_REGEX, html, 3);
		PostContentEntity postInfoEntity = new PostContentEntity();
		postInfoEntity.setPostTopic(filterHtmlDecode(postInfoList.get(0)));
		postInfoEntity.setBoardName(postInfoList.get(1));
		postInfoEntity.setTotalPage(Integer.parseInt(postInfoList.get(2)));
		list.add(postInfoEntity);
		// get each reply info
		List<String> contentHtml = getMatchedStringList(
				POST_CONTENT_WHOLE_REGEX, html, -1);
		for (String reply : contentHtml) {
			PostContentEntity entity = new PostContentEntity();
			entity.setUserName(getMatchedString(POST_CONTENT_USERNAME_REGEX,
					reply));
			entity.setPostContent(getMatchedString(
					POST_CONTENT_POST_CONTENT_REGEX, reply));
			entity.setPostTitle(getMatchedString(POST_CONTENT_POST_TITLE_REGEX,
					reply));
			entity.setPostFace(Integer.parseInt(getMatchedString(
					POST_CONTENT_POST_FACE_REGEX, reply)));
			entity.setPostTime(convertStringToDateInPostContent(getMatchedString(
					POST_CONTENT_POST_TIME_REGEX, reply)));
			{
				String avatarLink = getMatchedString(
						POST_CONTENT_USER_AVATAR_LINK_REGEX, reply);
				if (!avatarLink.contains("http://")) {
					avatarLink = cc98UrlManager.getClientUrl() + avatarLink;
				}
				entity.setUserAvatarLink(avatarLink);
			}
			{
				String sex = getMatchedString(POST_CONTENT_GENDER_REGEX, reply);
				if ("Male".equals(sex)) {
					entity.setGender(Gender.MALE);
				} else if ("FeMale".equals(sex)) {
					entity.setGender(Gender.FEMALE);
				}
			}
			list.add(entity);
		}
		return list;
	}

	/**
	 * Parse the HTML to obtain posts names and their URL.
	 * 
	 * @param html
	 * @return
	 * @throws ParseContentException
	 */
	private List<PostEntity> parsePostList(String html)
			throws ParseContentException {

		List<PostEntity> list = new ArrayList<PostEntity>();
		List<String> contentList = getMatchedStringList(
				POST_LIST_POST_ENTITY_REGEX, html, -1);
		for (String post : contentList) {
			PostEntity entity = new PostEntity();
			entity.setPostName(filterHtmlDecode(getMatchedString(
					POST_LIST_POST_NAME_REGEX, post)));
			entity.setPostPageNumber(Integer.parseInt(getMatchedString(
					POST_LIST_POST_PAGE_NUMBER_REGEX, post)));
			entity.setPostType(getMatchedString(POST_LIST_POST_TYPE_REGEX, post));
			entity.setReplyNumber(getMatchedString(POST_LIST_REPLY_NUM_REGEX,
					post));
			entity.setPostAuthorName(getMatchedString(
					POST_LIST_POST_AUTHOR_NAME_REGEX, post));
			entity.setLastReplyAuthor(getMatchedString(
					POST_LIST_LAST_REPLY_AUTHOR_REGEX, post));
			entity.setLastReplyLink(getMatchedString(
					POST_LIST_LAST_REPLY_LINK_REGEX, post));
			entity.setLastReplyTime(getMatchedString(
					POST_LIST_LAST_REPLY_TIME_REGEX, post));
			{
				String tempLink = getMatchedString(POST_LIST_POST_LINK_REGEX,
						post);
				int idx = tempLink.indexOf("&page=");
				idx = idx == -1 ? tempLink.length() : idx;
				entity.setPostLink(tempLink.substring(0, idx));
			}
			list.add(entity);
		}
		return list;
	}

	/**
	 * 
	 * @param html
	 * @return
	 * @throws ParseContentException
	 */
	private List<BoardEntity> parsePersonalBoardList(String html)
			throws ParseContentException {
		List<BoardEntity> nList = new ArrayList<BoardEntity>();
		String boardinfo = getMatchedString(P_BOARD_OUTER_WRAAPER_REGEX, html);
		List<String> board = getMatchedStringList(
				P_BOARD_SINGLE_BOARD_WRAPPER_REGEX, boardinfo, 0);
		for (String string : board) {
			BoardEntity entity = new BoardEntity();
			entity.setBoardName(getMatchedString(P_BOARD_NAME_REGEX, string));
			entity.setBoardIntro(getMatchedString(P_BOARD_INTRO_REGEX, string));
			entity.setBoardLink(getMatchedString(P_BOARD_LINK_REGEX, string));
			entity.setBoardMaster(getMatchedString(P_BOARD_BOARD_MASTER_REGEX,
					string));
			entity.setLastReplyAuthor(getMatchedString(
					P_BOARD_LAST_REPLY_AUTHOR_REGEX, string));
			entity.setLastReplyTime(getMatchedString(
					P_BOARD_LAST_REPLY_TIME_REGEX, string));
			entity.setLastReplyTopicLink(getMatchedString(
					P_BOARD_LAST_REPLY_TOPIC_LINK_REGEX, string));
			entity.setLastReplyTopicName(filterHtmlDecode(getMatchedString(
					P_BOARD_LAST_REPLY_TOPIC_NAME_REGEX, string)));
			entity.setPostNumberToday(Integer.parseInt(getMatchedString(
					P_BOARD_POST_NUMBER_TODAY, string)));
			nList.add(entity);
		}
		return nList;
	}

	/**
	 * @author DJ
	 * @param html
	 * @return
	 * @throws ParseContentException
	 */
	private UserProfileEntity parseUserProfile(String html)
			throws ParseContentException {
		UserProfileEntity entity = new UserProfileEntity();
 		// avatar link
		{
			String url = getMatchedString(USER_PROFILE_AVATAR_REGEX, html);
			if (!url.startsWith("http") && !url.startsWith("ftp")) {
				url = cc98UrlManager.getClientUrl() + url;
			}
			entity.setUserAvatarLink(url);
		}
		// general profile
		{
			String info = getMatchedString(USER_PROFILE_GENERAL_PROFILE_REGEX,
					html);
			String[] details = info.split("<br>");
			entity.setUserNickName(details[0]);
			entity.setUserLevel(details[1]);
			entity.setUserGroup(details[2]);
			entity.setGoodPosts(details[3]);
			entity.setTotalPosts(details[4]);
			entity.setUserPrestige(details[5]);
			entity.setRegisterTime(details[6]);
			entity.setLoginTimes(details[7]);
			entity.setDeletedPosts(details[8]);
			entity.setDeletedRatio(details[9]);
			entity.setLastLoginTime(details[10]);
		}
		// personal profile
		{
			String info = getMatchedString(USER_PROFILE_PERSON_PROFILE_REGEX,
					html);
			String[] details = info.split("<br>");

			details[1] = details[1].replaceAll("<.*?>", "");
			details[2] = details[2].replaceAll("<.*?>", "");
			details[3] = details[3].replaceAll("<.*?>", "");
			details[4] = details[4].replaceAll("<.*?>", "");
			details[5] = details[5].replaceAll("&nbsp;", " ");
			details[5] = details[5].replaceAll("<.*?>", "");
			details[6] = details[6].replaceAll("<.*?>", "");

			Pattern pattern = Pattern.compile("(?<=alt=).*?座", Pattern.DOTALL);
			Matcher matcher = pattern.matcher(details[2]);
			if (matcher.find()) {
				details[2] = matcher.group();
			}
			entity.setUserGender(details[0]);
			entity.setUserBirthday(details[1]);
			entity.setUserConstellation(details[2]);
			entity.setUserEmail(details[3]);
			entity.setUserQQ(details[4]);
			entity.setUserMSN(details[5]);
			entity.setUserPage(details[6]);

		}
		// bbs master info
		{
			String string = getMatchedString(USER_PROFILE_AVATAR_REGEX, html);
			string = string.replaceAll("\t|\n|\r|<br>|&nbsp;|<.*?>| ", "");
 			entity.setBbsMasterInfo(string);
		}
		//online status
		entity.setOnlineTime(getMatchedString(USER_PROFILE_ONLINE_INFO_REGEX,
				html));
		return entity;
	}

 	private List<HotTopicEntity> parseHotTopicList(String page) throws ParseContentException {
		List<HotTopicEntity> list = new ArrayList<HotTopicEntity>();
		List<String> topicList = getMatchedStringList(HOT_TOPIC_WRAPPER, page, -1);
		for (String topic : topicList) {
			HotTopicEntity entity = new HotTopicEntity();
			entity.setTopicName(filterHtmlDecode(getMatchedString(HOT_TOPIC_NAME_REGEX, topic)));
			entity.setPostLink(getMatchedString(HOT_TOPIC_LINK_REGEX, topic));
			entity.setPostTime(getMatchedString(HOT_TOPIC_POST_TIME_REGEX, topic));
			//click number
			{
				List<String> numList = getMatchedStringList(HOT_TOPIC_CLICK_REGEX, topic, 3);
				entity.setFocusNumber(Integer.parseInt(numList.get(0)));
				entity.setReplyNumber(Integer.parseInt(numList.get(1)));
				entity.setClickNumber(Integer.parseInt(numList.get(2)));
			}
			//board name, author
			{
				List<String> bList = getMatchedStringList(HOT_TOPIC_BOARD_NAME_WITH_AUTHOR_REGEX, topic, 2);
				entity.setBoardName(bList.get(0));
				entity.setPostAuthor(bList.get(1));
			}
			list.add(entity);
		}
		return list;
 	}

	/**
	 * Store information of the msgs in a list
	 * 
	 * @author zsy
	 * @param html
	 *            The html of the inbox page
	 * @return A list of PmInfo
	 */
	protected List<PmInfo> parsePmList(String html, InboxInfo inboxInfo) {
		List<PmInfo> pmList = new ArrayList<PmInfo>();
		String regexString = "(?<=<img src=pic/m_)\\w+(?=\\.gif>)|(?<=target=_blank>)[^:]+(?=</a>)|(?<=\\s>).*?(?=</a></td>)|(?<=<a href=\"messanger.asp\\?action=(read|outread)&id=)\\d+?(?=&sender)|(?<=target=_blank>).*?(?=</a></td>)";
		Pattern p1 = Pattern.compile(regexString);
		Matcher m1 = p1.matcher(html);
		getInboxList(pmList, m1);
		// Get total page number
		Pattern p2 = Pattern.compile("(?<=/<b>)\\d+(?=</b>页)");
		Matcher m2 = p2.matcher(html);
		if (m2.find()) {
			// Get the total page number of the pm inbox.
			inboxInfo.setTotalInPage(Integer.parseInt(m2.group()));
		}
		// Get total pm count
		Pattern p3 = Pattern.compile("(?<=总数<b>)\\d+(?=</b></td>)");
		Matcher m3 = p3.matcher(html);
		if (m3.find()) {
			inboxInfo.setTotalPmIn(Integer.parseInt(m3.group()));
		}
		return pmList;
	}

	/**
	 * Get a list of PmInfo.
	 * 
	 * @author zsy
	 * 
	 * @param pmList
	 * @param m1
	 */
	private void getInboxList(List<PmInfo> pmList, Matcher m1) {
		while (m1.find()) {
			String isNewString = m1.group();
			boolean isNew = isNewString.equals("olds")
					|| isNewString.equals("issend_1") ? false : true;
			m1.find();
			String sender = m1.group();
			m1.find();
			String topic = m1.group();
			m1.find();
			int pmId = Integer.parseInt(m1.group());
			m1.find();
			String time = m1.group();
			pmList.add(new PmInfo.Builder(pmId).fromWho(sender)
					.topicTitle(topic).sendTime(time).newTopic(isNew)
					.userAvatar("")
					.build());
		}
	}

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
			ParseException, IOException {
		String html = getMsgPageHtml(pmId);
		Pattern p = Pattern
				.compile("(?<=<span id=\"ubbcode1\" >).*?(?=</span>)");
		Matcher m = p.matcher(html);
		if (!m.find()) {
			throw new IllegalStateException("can not get msg content");
		}
		return m.group();
	}

	/**
	 * 
	 * @param page_num
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 */

	public List<PmInfo> getPmData(int page_num, InboxInfo inboxInfo, int type)
			throws ClientProtocolException, ParseException, IOException {
		if (type == 0) {
			return parsePmList(
					cc98Client.getPage(cc98UrlManager.getInboxUrl(page_num)),
					inboxInfo);
		} else if (type == 1) {
			return parsePmList(cc98Client.getOutboxHtml(page_num), inboxInfo);
		}
		return new ArrayList<PmInfo>();
	}

	public List<Map<String, Object>> searchPost(String keyword, int boardid,
			String sType, int page) throws ParseException, IOException {
		/*
		 * http://www.cc98.org/queryresult.asp?page=2&stype=2&pSearch=1&nSearch=&
		 * keyword=t&SearchDate=1000&boardid=0&sertype=1
		 */
		keyword = URLEncoder.encode(keyword);
		return parseQueryResult(cc98Client.getPage(cc98UrlManager.getSearchUrl(
				keyword, boardid, sType, page)));
	}

	public List<Map<String, Object>> query(String keyWord, String sType,
			String searchDate, int boardArea, int boardID)
			throws ParseException, IOException {
		return parseQueryResult(cc98Client.queryPosts(keyWord, sType,
				searchDate, boardArea, boardID));
	}

	/**
	 * @author DJ
	 * @author zsy (bug fix)
	 * @param html
	 * @return
	 */
	public List<Map<String, Object>> parseQueryResult(String html) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> mmap = new HashMap<String, Object>();

		Pattern pattern = Pattern
				.compile(
						"(?<=tablebody1 width=\\*>).*?(?=<!--<font color=\"#FF0000\">)",
						Pattern.DOTALL);

		Pattern postFacePattern = Pattern.compile(
				"(?<=<img src='face/face).*?(?=\\.gif)", Pattern.DOTALL);
		Pattern postLinkPattern = Pattern.compile(
				"dispbbs.asp\\?boardID.*?(?=')", Pattern.DOTALL);
		Pattern postTitlePattern = Pattern.compile("(?<=blank>).*?(?=</a>)",
				Pattern.DOTALL);
		Pattern authorPattern = Pattern.compile(
				"(?<=target=_blank>).{0,10}?(?=</a>)", Pattern.DOTALL);
		Pattern postTimePattern = Pattern.compile("(?<=195>).*&nbsp;",
				Pattern.DOTALL);

		/* get total post number */
		Pattern totalPostPattern = Pattern.compile("(?<=查询到).*?(?=</font>)",
				Pattern.DOTALL);
		Matcher matcher = totalPostPattern.matcher(html);
		if (matcher.find()) {
			String string = matcher.group();
			string = string.replaceAll("(<.*?>)|([\n\r])", "");
			System.err.println("totalPost:" + string);
			mmap.put("totalPost", string);
		}

		list.add(mmap);
		matcher = pattern.matcher(html);

		Matcher sMatcher;
		while (matcher.find()) {
			Map<String, Object> map = new HashMap<String, Object>();
			String sPost = matcher.group();
			// System.err.println(sPost);
			sMatcher = postTitlePattern.matcher(sPost);
			if (sMatcher.find()) {
				sMatcher.find();
				String string = sMatcher.group();
				string = string.replaceAll("\n|\t", "");
				string = string.replaceAll("&nbsp;", " ");
				string = string.replaceAll("&lt;", "<");
				string = string.replaceAll("&gt;", ">");

				// System.err.println("postTitle:" + string);
				map.put("postTitle", string);
			} else {
				map.put("postTitle", "");
			}

			sMatcher = authorPattern.matcher(sPost);
			if (sMatcher.find()) {
				String string = sMatcher.group();
				// System.err.println("author:" + string);
				map.put("author", string);
			} else {
				map.put("author", "");

			}

			sMatcher = postFacePattern.matcher(sPost);
			if (sMatcher.find()) {
				String string = sMatcher.group();
				// System.err.println("postFace:" + string);
				map.put("postFace", string);
			} else {
				map.put("postFace", "7");

			}

			sMatcher = postLinkPattern.matcher(sPost);
			if (sMatcher.find()) {
				String string = sMatcher.group();
				// System.err.println("postLink:" + string);

				map.put("postLink", cc98UrlManager.getClientUrl() + string);
			} else {
				map.put("postLink", "");

			}

			sMatcher = postTimePattern.matcher(sPost);
			if (sMatcher.find()) {
				String string = sMatcher.group();
				string = string.replaceAll("&nbsp;", " ");
				string = string.replaceAll("\t|\n| ", "");
				// System.err.println("postTime:" + string);

				map.put("postTime", string);
			} else {
				map.put("postTime", "");

			}
			list.add(map);
		}
		return list;
	}

	private List<UserStatueEntity> parseUserFriendList(String html)
			throws ClientProtocolException, ParseException, IOException {
		if (html == null) {
			throw new IllegalArgumentException("Null pointer!");
		}
		List<UserStatueEntity> list = new ArrayList<UserStatueEntity>();
		Pattern userRegexPattern = Pattern.compile(
				"&nbsp;<a href=dispuser\\.asp\\?name=.*?<br>", Pattern.DOTALL);
		Pattern userNamePattern = Pattern.compile("(?<= >).*?(?=</a>)",
				Pattern.DOTALL);
		Pattern userStatuePattern = Pattern.compile("(?<=\\[).*?(?=\\])",
				Pattern.DOTALL);
		Matcher matcher = userRegexPattern.matcher(html);
		while (matcher.find()) {
			String mString = matcher.group();
			UserStatueEntity mEntity = new UserStatueEntity();
			Matcher mMatcher = userNamePattern.matcher(mString);
			if (mMatcher.find()) {
				mEntity.setUserName(mMatcher.group());
			}
			mMatcher = userStatuePattern.matcher(mString);
			if (mMatcher.find()) {
				String string = mMatcher.group().replaceAll("<.*?>", "");
				if (string.contains("离线")) {
					mEntity.setStatue(UserStatue.OFF_LINE);
				} else {
					mEntity.setStatue(UserStatue.ON_LINE);
					mEntity.setOnlineTime(string);
				}
			}
			mEntity.setUserAvartar(cc98Client.getUserImg(mEntity.getUserName()));
			list.add(mEntity);
		}
		return list;
	}

	/**
	 * @author zsy
	 * @return
	 * @throws ClientProtocolException
	 * @throws ParseException
	 * @throws IOException
	 */
	public List<NameValuePair> getTodayBoardList()
			throws ClientProtocolException, ParseException, IOException {
		String content = cc98Client.getPage(cc98UrlManager.getTodayBoardList());
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		Pattern linkAndName = Pattern.compile(
				"(?<=<a href=\")list.*?(?=</a></td><td )", Pattern.DOTALL);
		Matcher matcher = linkAndName.matcher(content);
		while (matcher.find()) {
			String mString = matcher.group();
			int idx = mString.indexOf("\">");
			if (idx == -1 || idx + 2 >= mString.length()) {
				throw new IllegalStateException("parse error.");
			}
			String boardLink = mString.substring(0, idx);
			String boardName = mString.substring(idx + 2);
			list.add(new BasicNameValuePair(boardName, boardLink));
		}
		return list;
	}

	/**
	 * @return the cc98Client
	 */
	public CC98Client getCC98Client() {
		return cc98Client;
	}

	/**
	 * @param cc98Client
	 *            the cc98Client to set
	 */
	public void setCC98Client(CC98Client cc98Client) {
		this.cc98Client = cc98Client;
	}

	/**
	 * @return the cc98UrlManager
	 */
	public CC98UrlManager getCC98UrlManager() {
		return cc98UrlManager;
	}

	/**
	 * @param cc98UrlManager
	 *            the cc98UrlManager to set
	 */
	public void setCC98UrlManager(CC98UrlManager cc98UrlManager) {
		this.cc98UrlManager = cc98UrlManager;
	}

}
