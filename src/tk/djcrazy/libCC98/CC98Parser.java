package tk.djcrazy.libCC98;

import static tk.djcrazy.libCC98.CC98Client.HOT_TOPIC_LINK;
import static tk.djcrazy.libCC98.CC98Client.getCC98Domain;
import static tk.djcrazy.libCC98.CC98Client.getNewPostListHtml;
import static tk.djcrazy.libCC98.CC98Client.getPage;
import static tk.djcrazy.libCC98.CC98Client.getUserProfileHtml;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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
import tk.djcrazy.libCC98.data.HotTopicEntity;
import tk.djcrazy.libCC98.data.InboxInfo;
import tk.djcrazy.libCC98.data.PmInfo;
import tk.djcrazy.libCC98.data.PostContentEntity;
import tk.djcrazy.libCC98.data.PostEntity;
import tk.djcrazy.libCC98.data.UserProfileEntity;
import tk.djcrazy.libCC98.data.UserStatue;
import tk.djcrazy.libCC98.data.UserStatueEntity;
import tk.djcrazy.libCC98.exception.NoUserFoundException;
import android.util.Log;

public class CC98Parser {

	/**
	 * Get a List of Maps of posts info.
	 * 
	 * @author DJ
	 * @param boardLink, url String
	 * @return A List that contains each post's basic information,
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * 
	 * @see #parsePostList(String)
	 */
	static public List<PostEntity> getPostList(String boardLink)
			throws ClientProtocolException, ParseException, IOException {

		return parsePostList(getPage(boardLink));
	}

	/**
	 * Get a List of HashMaps of hot Topic
	 * 
	 * @author DJ
	 * @return List of hot topic
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * 
	 */
	static public List<HotTopicEntity> getHotTopicList()
			throws ClientProtocolException, ParseException, IOException {
		return parseHotTopicList(getPage(HOT_TOPIC_LINK));
	}

	/**
	 * Get a List of BoardEntity of board info from your personal board list.
	 * 
	 * @author DJ
	 * @return List board info.
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * 
	 * @see #parsePersonalBoardList(String)
	 */
	static public List<BoardEntity> getPersonalBoardList()
			throws ClientProtocolException, ParseException, IOException {
		return parsePersonalBoardList(getPage(getCC98Domain()));
	}

	/**
	 * Get a list of Maps of each reply post's info.
	 * 
	 * @author DJ
	 * @param postLink
	 * @return A list of Maps of each reply post's info.
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @see #parsePostContentList(String)
	 */
	static public List<PostContentEntity> getPostContentList(String postLink)
			throws ClientProtocolException, ParseException, IOException {
		//System.err.println("Post Link:" + postLink);
		return parsePostContentList(getPage(postLink));
	}

	/**
	 * @author DJ
	 * @param userName
	 * @return Map of user profile info
	 * 
	 *         //TODO the keys in the map are ...not finished, to be added
	 * @throws IOException
	 * @throws NoUserFoundException
	 */
	static public UserProfileEntity getUserProfile(String userName)
			throws NoUserFoundException, IOException {
		if (userName == null) {
			throw new IllegalArgumentException("Null pointer!");
		}
		//System.err.println("Id: " + userName);
		return parseUserProfile(getUserProfileHtml(userName));
	}

	/**
	 * @author DJ
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 */
	static public List<Map<String, Object>> getNewPostList()
			throws ClientProtocolException, ParseException, IOException {
		return parseQueryResult(getNewPostListHtml());
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
	public static List<UserStatueEntity> getUserFriendList()
			throws ClientProtocolException, ParseException, IOException {
		return parseUserFriendList(getPage(CC98Client.USER_CONTROL_LINK));
	}

	/**
	 * @author DJ
	 * @param keyWord
	 * @param sType
	 * @param searchDate
	 * @param boardArea
	 * @param boardID
	 * @return List<Map<String, Object>>, A list of Maps of each post's info.
	 * 
	 *         Maps in the list contain keys:
	 * 
	 *         postTitle => String author => String postTime => String
	 * @throws IOException
	 * @throws ParseException
	 * 
	 */
	public static List<Map<String, Object>> queryPosts(String keyWord,
			String sType, String searchDate, int boardArea, int boardID)
			throws ParseException, IOException {
		String html = CC98Client.queryPosts(keyWord, sType, searchDate,
				boardArea, boardID);
		if (html != null) {
			parseQueryResult(html);
		} else {
			throw new IllegalArgumentException("Null html String!");
		}
		return null;
	}

	/**
	 * Parse the HTML, and put posts and their poster id in a list of
	 * NameValuePair
	 * 
	 * @param html
	 *            String (must not be null)
	 * @return A list of the pair of poster id and their posts
	 */
	static private List<PostContentEntity> parsePostContentList(String html) {
		if (html == null)
			throw new IllegalArgumentException("Null html String!");
		List<PostContentEntity> list = new ArrayList<PostContentEntity>();
		Matcher matcher;
		// get some information of the topic
		Pattern postInfoPattern = Pattern
				.compile(
						"(?<=<title>).*?(?=&raquo;)|(?<=&page=\\d{1,5}>).+?(?=</a>)|(?<=>\\[).{0,10}?(?=\\]</font></span></td>)|(?<=>\\[).{0,10}?(?=\\]</a></span></td>)",
						Pattern.DOTALL);
		Matcher postInfoMatcher = postInfoPattern.matcher(html);
		PostContentEntity postInfoEntity = new PostContentEntity();
		if (postInfoMatcher.find()) {
			String string = postInfoMatcher.group();
			string = string.replaceAll("&gt;|&lt;|\n|&nbsp;", "");
			string = string.replaceAll("&nbsp;", " ");
			string = string.replaceAll("&lt;", "<");
			string = string.replaceAll("&gt;", ">");
			postInfoEntity.setPostTopic(string);
		}
		if (postInfoMatcher.find()) {
			postInfoEntity.setBoardName(postInfoMatcher.group());
		}
		if (postInfoMatcher.find()) {
			postInfoEntity.setTotalPage(Integer.parseInt(postInfoMatcher
					.group()));
		}
		list.add(postInfoEntity);

		Pattern userNamePattern = Pattern.compile(
				"(?<=<font color=#.{6}(\\s|)><.>).*?(?=</.></font>)",
				Pattern.DOTALL);
		Pattern postContentPattern = Pattern.compile(
				"(?<=</b><br>)<span id=.*?</script>", Pattern.DOTALL);
		Pattern userAvatarLinkPattern = Pattern
				.compile(
						"(?<= ><img src=).*?(?= )|(?<=&nbsp;<img src=\").*?(?=\" border=0 ><br>)",
						Pattern.DOTALL);
		Pattern postTitlePattern = Pattern
				.compile("(?<=alt=\"发贴心情\">&nbsp;<b>).*?(?=</b><br><span id)");

		Pattern postFacePattern = Pattern
				.compile("(?<=<img src=face/).*?gif(?= border=0)");
		Pattern postTimePattern = Pattern.compile(
				"<img align=absmiddle border=0 width=13 height=15.*?(?=</td>)",
				Pattern.DOTALL);
		Pattern genderPattern = Pattern.compile(
				"(?<=<img src=pic/).*?Male.gif", Pattern.DOTALL);
		Pattern quoteLinkPattern = Pattern.compile(
				"(?<=<a href=\")reannounce.asp\\?.*?(?=\">)", Pattern.DOTALL);
		Pattern trackUserLinkPattern = Pattern.compile(
				"(?<=<a href=\")dispbbslz.asp.*?(?=\"><img)", Pattern.DOTALL);
		Pattern editPostLinkPattern = Pattern.compile(
				"(?<=<a href=)editannounce.asp.*?(?=><img)", Pattern.DOTALL);
		String regexString = "(?<=valign=middle style=).*?(?=align=absmiddle></a>)";
		Pattern pattern = Pattern.compile(regexString, Pattern.DOTALL);
		Matcher ma = pattern.matcher(html);
		while (ma.find()) {
			PostContentEntity entity = new PostContentEntity();
			String postInfo = ma.group();
			matcher = userNamePattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setUserName(matcher.group());
			}
			matcher = postContentPattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setPostContent(matcher.group());
			}
			matcher = userAvatarLinkPattern.matcher(postInfo);
			if (matcher.find()) {
				String string = matcher.group();
				if (!string.contains("http://")) {
					string = CC98Client.getCC98Domain() + string;
				}
				entity.setUserAvatarLink(string);
			}
			matcher = postTitlePattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setPostTitle(matcher.group());
			}
			matcher = postFacePattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setPostFace(matcher.group());
			}
			matcher = postTimePattern.matcher(postInfo);
			if (matcher.find()) {
				String string = matcher.group();
				string = string.replaceAll("<.*?>", "");
				string = string.replaceAll("\n|\t", "");
				entity.setPostTime(string);
			}
			matcher = genderPattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setGender("file:///android_asset/pic/" + matcher.group());
			}
			matcher = quoteLinkPattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setQuoteLink(CC98Client.getCC98Domain()
						+ matcher.group());
			}
			matcher = trackUserLinkPattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setTrackUserLink(CC98Client.getCC98Domain()
						+ matcher.group());
			}
			matcher = editPostLinkPattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setEditPostLink(CC98Client.getCC98Domain()
						+ matcher.group());
			}
			list.add(entity);
		}
		return list;
	}

	/**
	 * Parse the HTML to obtain posts names and their URL.
	 * 
	 * @param html
	 * @return A List of the NameValuePair of posts names and their URL
	 */
	static private List<PostEntity> parsePostList(String html) {

		if (html == null)
			throw new IllegalArgumentException("Null html String!");
		List<PostEntity> list = new ArrayList<PostEntity>();

		Pattern postTypePattern = Pattern.compile("(?<=alt=).*?(?=></TD>)",
				Pattern.DOTALL);
		Pattern postNamePattern = Pattern.compile("(?<=最后跟贴：\">).*?(?=</a>)",
				Pattern.DOTALL);
		Pattern postLinkPattern = Pattern
				.compile(
						"(?<=<a id=\"topic_\\d{1,10}\" href=\")dispbbs\\.asp\\?.*?(?=\" title=\")",
						Pattern.DOTALL);
		Pattern postPageNumberPattern = Pattern
				.compile("(?<=<font color=#FF0000>).{1,6}?(?=</font></a>.?</b>\\])");
		Pattern postAuthorNamePattern = Pattern.compile(
				"(?<=target=_blank>).{1,10}(?=</a></a>)", Pattern.DOTALL);
		Pattern replyNumberPattern = Pattern.compile(
				"(?<=<td width=\\* nowrap class=tablebody1>).*?(?=</td>)",
				Pattern.DOTALL);
		Pattern lastReplyTimePattern = Pattern.compile(
				"(?<=#bottom\">).*?(?=</a>)", Pattern.DOTALL);
		Pattern lastReplyLinkPattern = Pattern.compile(
				"(?<=&nbsp;<a href=).{5,70}?(?=#bottom)", Pattern.DOTALL);
		Pattern lastReplyAuthorPattern = Pattern.compile(
				"(?<=usr\":\").*?(?=\")", Pattern.DOTALL);
		String regexString = "(?<=<tr align=middle><td).*?(?=;</script>)";
		Pattern pattern = Pattern.compile(regexString, Pattern.DOTALL);
		Matcher ma = pattern.matcher(html);
		while (ma.find()) {
			PostEntity entity = new PostEntity();
			String postInfo = ma.group();
			Matcher matcher = postTypePattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setPostType(matcher.group());
//				System.err.println("PostType:" + entity.getPostType());
			}
			matcher = postNamePattern.matcher(postInfo);
			if (matcher.find()) {
				String string = matcher.group();
				string = string.replaceAll("\n", "");
				string = string.replaceAll("&nbsp;", " ");
				string = string.replaceAll("&lt;", "<");
				string = string.replaceAll("&gt;", ">");
				entity.setPostName(string);
//				System.err.println("PostName:" + entity.getPostName());
			}
			matcher = postLinkPattern.matcher(postInfo);
			if (matcher.find()) {
				String tmp = matcher.group();
				int idx = tmp.indexOf("&page=");
				idx = idx == -1 ? tmp.length() : idx;
				entity.setPostLink(tmp.substring(0, idx));
//				System.err.println("PostLink:" + entity.getPostLink());
			}
			matcher = postPageNumberPattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setPostPageNumber(Integer.parseInt(matcher.group()));
				//System.err.println("PostPageNumber:"
				//		+ entity.getPostPageNumber());
			}
			matcher = postAuthorNamePattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setPostAuthorName(matcher.group());
				//System.err.println("PostAuthorName:"
				//		+ entity.getPostAuthorName());
			}
			matcher = replyNumberPattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setReplyNumber(matcher.group());
				//System.err.println("ReplyNumber:" + entity.getReplyNumber());
			}
			matcher = lastReplyTimePattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setLastReplyTime(matcher.group());
				//System.err
				//		.println("LastReplyTime:" + entity.getLastReplyTime());
			}
			matcher = lastReplyLinkPattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setLastReplyLink(matcher.group());
				//System.err
				//		.println("LastReplyLink:" + entity.getLastReplyLink());
			}
			matcher = lastReplyAuthorPattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setLastReplyAuthor(matcher.group());
				//System.err.println("LastReplyAuthor:"
				//		+ entity.getLastReplyAuthor());
			}
			list.add(entity);
		}
		return list;
	}

	/**
	 * Parse the HTML page to get my board list
	 * 
	 * @author DJ
	 * @param html
	 * @return List of NameValuePair of board links and board names
	 */
	static private List<BoardEntity> parsePersonalBoardList(String html) {
		if (html == null)
			throw new IllegalArgumentException("Null html String!");
		List<BoardEntity> nList = new ArrayList<BoardEntity>();

		String regexString;
		String boardinfo = null;
		String singleBoardInfo;
		regexString = "var customboards_disp = new Array.*?document.write";
		Pattern pa = Pattern.compile(regexString, Pattern.DOTALL);
		Matcher ma = pa.matcher(html);
		Matcher matcher;
		if (ma.find()) {
			boardinfo = ma.group();
		} else {
			Log.d("CC98Parser",CC98Client.getCC98Domain()+"\n"+ html);
			throw new IllegalStateException("preParse Error!");
		}
		Pattern namePattern = Pattern.compile(
				"(?<=<font color=#000066>).*?(?=</font>)", Pattern.DOTALL);
		Pattern linkPattern = Pattern.compile(
				"(?<=<a href=\")list.asp\\?boardid=[0-9]+(?=\">)",
				Pattern.DOTALL);
		Pattern introPattern = Pattern
				.compile(
						"(?<=<img src=pic/Forum_readme.gif align=middle>).*?(?=</FONT></TD></TR>)",
						Pattern.DOTALL);
		Pattern lastReplyTopicLinkPattern = Pattern.compile(
				"(?<=主题：<a href=\").*?(?=\">)", Pattern.DOTALL);
		Pattern lastReplyTopicNamePattern = Pattern.compile(
				"(?<=ID=\\d{0,10}\">).*?(?=</a><BR>作者)", Pattern.DOTALL);
		Pattern lastReplyAuthorPattern = Pattern.compile(
				"(?<=blank>).*?(?=</a><BR>)", Pattern.DOTALL);
		Pattern lastReplyTimePattern = Pattern.compile(
				"(?<=bottom\">).*?(?=</a>)", Pattern.DOTALL);
		Pattern lastReplyLinkPattern = Pattern.compile(
				"(?<=日期：<a href=\").*?(?=#bottom\")", Pattern.DOTALL);
		Pattern postNumberTodayPattern = Pattern.compile(
				"(?<=<font color=#FF0000>).*?(?=</font></td>)", Pattern.DOTALL);
		Pattern boardMasterPattern = Pattern.compile(
				"版主：.*?(?=</a>&nbsp;</TD>)", Pattern.DOTALL);
		regexString = "</a>-->.*?(?=</td></tr></table></TD>)";
		pa = Pattern.compile(regexString, Pattern.DOTALL);
		ma = pa.matcher(boardinfo);

		while (ma.find()) {
			BoardEntity mEntity = new BoardEntity();
			singleBoardInfo = ma.group();
			matcher = namePattern.matcher(singleBoardInfo);
			if (matcher.find()) {
				mEntity.setBoardName(matcher.group());
			}
			matcher = linkPattern.matcher(singleBoardInfo);
			if (matcher.find()) {
				mEntity.setBoardLink(matcher.group());
			}
			matcher = introPattern.matcher(singleBoardInfo);
			if (matcher.find()) {
				String string = matcher.group();
				string = string.replaceAll("<.*?>", "");
				mEntity.setBoardIntro(string);
			}
			matcher = lastReplyTopicLinkPattern.matcher(singleBoardInfo);
			if (matcher.find()) {
				mEntity.setLastReplyTopicLink(matcher.group());
			}
			matcher = lastReplyTopicNamePattern.matcher(singleBoardInfo);
			if (matcher.find()) {
				String string = matcher.group();
				string = string.replaceAll("\n", "");
				string = string.replaceAll("&nbsp;", " ");
				string = string.replaceAll("&lt;", "<");
				string = string.replaceAll("&gt;", ">");
				mEntity.setLastReplyTopicName(string);
			}
			matcher = lastReplyAuthorPattern.matcher(singleBoardInfo);
			if (matcher.find()) {
				mEntity.setLastReplyAuthor(matcher.group());
			}
			matcher = lastReplyTimePattern.matcher(singleBoardInfo);
			if (matcher.find()) {
				mEntity.setLastReplyTime(matcher.group());
			}
			matcher = lastReplyLinkPattern.matcher(singleBoardInfo);
			if (matcher.find()) {
				mEntity.setLastReplyTopicLink(matcher.group());
			}
			matcher = postNumberTodayPattern.matcher(singleBoardInfo);
			if (matcher.find()) {
				mEntity.setPostNumberToday(Integer.parseInt(matcher.group()));
			}
			matcher = boardMasterPattern.matcher(singleBoardInfo);
			if (matcher.find()) {
				String string = matcher.group();
				string = string.replaceAll("<.*?>", "");
				string = string.replaceAll("&nbsp;", " ");
				mEntity.setBoardMaster(string);
			}
			nList.add(mEntity);
		}
		return nList;
	}

	/**
	 * @author DJ
	 * @param html
	 * @return
	 */
	private static UserProfileEntity parseUserProfile(String html) {
		if (html == null)
			throw new IllegalArgumentException("Null html String!");
		UserProfileEntity entity = new UserProfileEntity();
		Pattern userAvatarLinkPattern = Pattern.compile(
				"(?<=&nbsp;<img src=).*?(?= )", Pattern.DOTALL);
		Pattern generalProfilePattern = Pattern.compile("用户头衔：.*?最后登录：.*?<br>",
				Pattern.DOTALL);
		Pattern personalProfilePattern = Pattern.compile("性 别.*?主 页.*?</font>",
				Pattern.DOTALL);
		Pattern bbsMasterInfoPattern = Pattern
				.compile(
						"(?<=<font align=left>)论坛职务：</font><br>.*?(?=<td  class=tablebody1)",
						Pattern.DOTALL);
		Pattern onLineInfoPattern = Pattern.compile(
				"(?<=&nbsp;&nbsp;状态：).*?\\]", Pattern.DOTALL);

		Matcher matcher = userAvatarLinkPattern.matcher(html);
		if (matcher.find()) {
			String url = matcher.group();
			if (!url.startsWith("http") && !url.startsWith("ftp")) {
				url = CC98Client.getCC98Domain() + url;
			}
			entity.setUserAvatarLink(url);
		}

		matcher = generalProfilePattern.matcher(html);
		if (matcher.find()) {
			String info = matcher.group();
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
//			System.err.println("userNickName" + details[0]);
//			System.err.println("userLevel" + details[1]);
//			System.err.println("userGroup" + details[2]);
//			System.err.println("goodPosts" + details[3]);
//			System.err.println("totalPosts" + details[4]);
//			System.err.println("userPrestige" + details[5]);
//			System.err.println("registerTime" + details[6]);
//			System.err.println("loginTimes" + details[7]);
//			System.err.println("deletedPosts" + details[8]);
//			System.err.println("deletedRatio" + details[9]);
//			System.err.println("lastLoginTime" + details[10]);
		}

		matcher = personalProfilePattern.matcher(html);
		if (matcher.find()) {
			String info = matcher.group();
			String[] details = info.split("<br>");

			details[1] = details[1].replaceAll("<.*?>", "");
			details[2] = details[2].replaceAll("<.*?>", "");
			details[3] = details[3].replaceAll("<.*?>", "");
			details[4] = details[4].replaceAll("<.*?>", "");
			details[5] = details[5].replaceAll("&nbsp;", " ");
			details[5] = details[5].replaceAll("<.*?>", "");
			details[6] = details[6].replaceAll("<.*?>", "");

			Pattern pattern = Pattern.compile("(?<=alt=).*?座", Pattern.DOTALL);
			matcher = pattern.matcher(details[2]);
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
//			System.err.println("userGender" + details[0]);
//			System.err.println("userBirthday" + details[1]);
//			System.err.println("userConstellation" + details[2]);
//			System.err.println("userEmail" + details[3]);
//			System.err.println("userQQ" + details[4]);
//			System.err.println("userMSN" + details[5]);
//			System.err.println("userPage" + details[6]);
		}
		matcher = bbsMasterInfoPattern.matcher(html);
		if (matcher.find()) {
			String string = matcher.group();
			string = string.replaceAll("\t|\n|\r", "");
			string = string.replaceAll("<br>", "");
			string = string.replaceAll("&nbsp;", "");
			string = string.replaceAll("<.*?>", "");
			string = string.replaceAll(" ", "");

			entity.setBbsMasterInfo(string);
			// System.err.println("bbsMasterInfo"+string);
		}
		matcher = onLineInfoPattern.matcher(html);
		if (matcher.find()) {
			entity.setOnlineTime(matcher.group());
			// System.err.println("onlineTime"+
		}

		return entity;
	}

	/**
	 * @author DJ
	 * @param page
	 * @return
	 */
	private static List<HotTopicEntity> parseHotTopicList(String page) {
		if (page == null)
			throw new IllegalArgumentException("Null html String!");

		List<HotTopicEntity> list = new ArrayList<HotTopicEntity>();

		Pattern topicNamePattern = Pattern.compile(
				"(?<=<font color=#000066>).*?(?=</font>)", Pattern.DOTALL);
		Pattern postLinkPattern = Pattern.compile(
				"(?<=&nbsp;<a href=\").*?(?=\" target=)", Pattern.DOTALL);
		Pattern boardNameWithAuthorPattern = Pattern.compile(
				"(?<=target=\"_blank\">).{0,30}?(?=</a></td><td height=20)",
				Pattern.DOTALL);
		Pattern postTimePattern = Pattern.compile(
				"(?<=\">).{5,18}?(?=</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;)",
				Pattern.DOTALL);
		Pattern focusWithRplyWitClieckhNumberPattern = Pattern.compile(
				"(?<=align=middle class=tablebody\\d>).*?(?=</td>)",
				Pattern.DOTALL);

		String regexString = "&nbsp;<a href=\".*?(</td></tr><TR><TD align=middle|</td></tr><!--data)";
		Pattern pattern = Pattern.compile(regexString, Pattern.DOTALL);
		Matcher ma = pattern.matcher(page);
		while (ma.find()) {
			HotTopicEntity entity = new HotTopicEntity();
			String postInfo = ma.group();
			Matcher matcher = topicNamePattern.matcher(postInfo);
			if (matcher.find()) {
				String string = matcher.group();
				string = string.replaceAll("\n", "").replaceAll("&nbsp;", " ")
						.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
				entity.setTopicName(string);
//				System.err.println("topicName" + string);
			}
			matcher = postLinkPattern.matcher(postInfo);
			if (matcher.find()) {
				String string = matcher.group();
				entity.setPostLink(string);
//				System.err.println("postLink" + string);
			}
			matcher = boardNameWithAuthorPattern.matcher(postInfo);
			if (matcher.find()) {
				String string = matcher.group();
				entity.setBoardName(string);
//				System.err.println("boardName" + string);
			}
			if (matcher.find()) {
				String string = matcher.group();
				entity.setPostAuthor(string);
//				System.err.println("postAuthor" + string);
			}
			matcher = postTimePattern.matcher(postInfo);
			if (matcher.find()) {
				String string = matcher.group();
				entity.setPostTime(string);
//				System.err.println("postTime:" + string);
			}
			matcher = focusWithRplyWitClieckhNumberPattern.matcher(postInfo);
			if (matcher.find()) {
				entity.setFocusNumber(Integer.parseInt(matcher.group()));
//				System.err
//						.println("focusNumber" + entity.getFocusNumber() + "");
			}
			if (matcher.find()) {
				entity.setReplyNumber(Integer.parseInt(matcher.group()));
//				System.err.println("replyNumber" + entity.getReplyNumber());
			}
			if (matcher.find()) {
				entity.setClickNumber(Integer.parseInt(matcher.group()));
//				System.err.println("clickNumber:" + entity.getClickNumber());
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
	protected static List<PmInfo> parsePmList(String html, InboxInfo inboxInfo) {
		if (html == null)
			throw new IllegalArgumentException("Null html String!");

		// Parse
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
	private static String getMsgPageHtml(int pmId)
			throws ClientProtocolException, ParseException, IOException {
		return CC98Client.getPage(CC98Client.getCC98Domain()
				+ "messanger.asp?action=read&id=" + pmId);
	}

	/**
	 * Get a list of PmInfo.
	 * 
	 * @author zsy
	 * 
	 * @param pmList
	 * @param m1
	 */
	private static void getInboxList(List<PmInfo> pmList, Matcher m1) {
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
					// .content(TextParseHelper.getMsgContent(pmId))
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
	public static String getMsgContent(int pmId)
			throws ClientProtocolException, ParseException, IOException {
		String html = getMsgPageHtml(pmId);
		Pattern p = Pattern
				.compile("(?<=<span id=\"ubbcode1\" >).*?(?=</span>)");
		Matcher m = p.matcher(html);
		if (!m.find()) {
			System.err.println("Can't get pm content, PM id=" + pmId);
			return "";
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

	public static List<PmInfo> getPmData(int page_num, InboxInfo inboxInfo,
			int type) throws ClientProtocolException, ParseException,
			IOException {
		if (type == PmActivity.INBOX) {
			return parsePmList(CC98Client.getInboxHtml(page_num), inboxInfo);
		} else if (type == PmActivity.OUTBOX) {
			return parsePmList(CC98Client.getOutboxHtml(page_num), inboxInfo);
		}
		return new ArrayList<PmInfo>();
	}

	public static List<Map<String, Object>> searchPost(String keyword,
			int boardid, String sType, int page) throws ParseException,
			IOException {
		/*
		 * http://www.cc98.org/queryresult.asp?page=2&stype=2&pSearch=1&nSearch=&
		 * keyword=t&SearchDate=1000&boardid=0&sertype=1
		 */
		keyword = URLEncoder.encode(keyword);
		StringBuilder sBuilder = new StringBuilder(CC98Client.getCC98Domain());
		sBuilder.append("queryresult.asp?page=").append(page).append("&stype=")
				.append(sType).append("&pSearch=1&nSearch=&keyword=")
				.append(keyword).append("&SearchDate=1000&boardid=")
				.append(boardid).append("&sertype=1");
//		System.err.println(sBuilder.toString());
		return parseQueryResult(getPage(sBuilder.toString()));
	}

	public static List<Map<String, Object>> query(String keyWord, String sType,
			String searchDate, int boardArea, int boardID)
			throws ParseException, IOException {
		return parseQueryResult(CC98Client.queryPosts(keyWord, sType,
				searchDate, boardArea, boardID));
	}

	/**
	 * @author DJ
	 * @author zsy (bug fix)
	 * @param html
	 * @return
	 */
	public static List<Map<String, Object>> parseQueryResult(String html) {
		if (html == null)
			throw new IllegalArgumentException("Null html String!");

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

//				System.err.println("postTitle:" + string);
				map.put("postTitle", string);
			} else {
				map.put("postTitle", "");
			}

			sMatcher = authorPattern.matcher(sPost);
			if (sMatcher.find()) {
				String string = sMatcher.group();
//				 System.err.println("author:" + string);
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

				map.put("postLink", CC98Client.getCC98Domain() + string);
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

	private static List<UserStatueEntity> parseUserFriendList(String html)
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
			mEntity.setUserAvartar(CC98Client.getUserImg(mEntity.getUserName()));
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
	public static List<NameValuePair> getTodayBoardList()
			throws ClientProtocolException, ParseException, IOException {
		String content = CC98Client.getPage(CC98Client.getCC98Domain()
				+ "boardstat.asp?boardid=0");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		Pattern linkAndName = Pattern.compile(
				"(?<=<a href=\")list.*?(?=</a></td><td )", Pattern.DOTALL);
		Matcher matcher = linkAndName.matcher(content);
		while (matcher.find()) {
			String mString = matcher.group();
			int idx = mString.indexOf("\">");
			if (idx == -1 || idx + 2 >= mString.length()) {
//				System.err.println("Error result: " + mString);
				return null;
			}
			String boardLink = mString.substring(0, idx);
			String boardName = mString.substring(idx + 2);
			list.add(new BasicNameValuePair(boardName, boardLink));
		}
		return list;
	}

}
