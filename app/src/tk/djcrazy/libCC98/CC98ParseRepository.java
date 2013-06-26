package tk.djcrazy.libCC98;

/**
 * @author Ding
 */
public class CC98ParseRepository {
	
	//post content regex string
	public static final String POST_CONTENT_INFO_REGEX = "(?<=<title>).*?(?=&raquo;)|" +
			"(?<=&page=\\d{1,5}>).+?(?=</a>)|" +
			"(?<=本主题贴数 <b>).{0,10}?(?=</b>)";
	public static final String POST_CONTENT_USERNAME_REGEX = "(?<=<span style=\"color: #.{3,8};\"><b>).*?(?=</b></span>)";
	public static final String POST_CONTENT_WHOLE_REGEX="(?<=<table cellpadding=).*?(?=align=absmiddle></a>)";
	public static final String POST_CONTENT_POST_CONTENT_REGEX = "(?<=</b><br>).*?</script>|<hr noshade=.*?(?=</td>)";
	public static final String POST_CONTENT_USER_AVATAR_LINK_REGEX = "(?<=\"><img src=\").*?(?=\" )|(?<=&nbsp;<img src=\").*?(?=\" style=\"border-style)";
	public static final String POST_CONTENT_POST_TITLE_REGEX = "(?<=title=\"发贴心情\">&nbsp;<b>).*?(?=</b><br>)";
	public static final String POST_CONTENT_POST_FACE_REGEX = "(?<=face/)face.*?.gif";
	public static final String POST_CONTENT_POST_TIME_REGEX = "(?<=\"></a>).{10,25}?(PM|AM)";
	public static final String POST_CONTENT_GENDER_REGEX = "(?<=pic/).{0,5}?Male(?=\\.gif)";
 	public static final String POST_CONTENT_REPLY_ID_REGEX = "";
 	
 	
 	//post list regex string
 	public static final String POST_LIST_POST_TYPE_REGEX= "(?<=alt=\").{1,20}?(?=\" />)|(?<=alt=\").{1,20}?(?=\">)";
 	public static final String POST_LIST_POST_NAME_REGEX = "(?<=最后跟贴：\">).*?(?=</a>)";
 	public static final String POST_LIST_POST_ID_REGEX = "(?<=&ID=)\\d{1,10}?(?=&page=)";
 	public static final String POST_LIST_POST_BOARD_ID_REGEX = "(?<=dispbbs.asp\\?boardID=)\\d{1,10}?(?=&ID=)";
 	
 	public static final String POST_LIST_POST_PAGE_NUMBER_REGEX = "(?<=<font color=#FF0000>).{1,6}?(?=</font></a>.?</b>\\])";
 	public static final String POST_LIST_POST_AUTHOR_NAME_REGEX = "(?<=target=\"_blank\">).{1,15}(?=</a>)|(?<=auto;\" class=\"tablebody2\">).{1,15}?(?=</td>)";
 	public static final String POST_LIST_REPLY_NUM_REGEX= "(?<=auto;\" class=\"tablebody1\">).*?(?=</td>)";
 	public static final String POST_LIST_LAST_REPLY_TIME_REGEX = "(?<=#bottom\">).*?(?=</a>)";
  	public static final String POST_LIST_LAST_REPLY_AUTHOR_REGEX= "(?<=usr\":\").*?(?=\")";
 	public static final String POST_LIST_POST_ENTITY_REGEX  = "(?<=<tr style=\"vertical-align: middle;\">).*?(?=</script>)";

 	//personal board list
  	public static final String P_BOARD_SINGLE_BOARD_WRAPPER_REGEX = "<!--版面名字-->.*?</table>";
 	public static final String P_BOARD_NAME_REGEX = "(?<=<font color=\"#000066\">).*?(?=</font></a>)";
 	public static final String P_IS_PARENT_BOARD_REGEX = "(?<=有 )\\d{1,3}?(?= 个下属论坛)";
 	public static final String P_BOARD_ID_REGEX = "(?<=<a href=\"list.asp\\?boardid=)[0-9]+(?=\">)";
  	public static final String P_BOARD_LAST_REPLY_TOPIC_ID_REGEX= "(?<=&ID=)\\d*?(?=&star=1\">)";
 	public static final String P_BOARD_LAST_REPLY_TOPIC_NAME_REGEX = "(?<=&star=1\">).*?(?=</a><br />)";
 	public static final String P_BOARD_LAST_REPLY_AUTHOR_REGEX = "(?<=target=\"_blank\">).*?(?=</a><br />)|(?<=作者：)匿名(?=<br />)";
 	public static final String P_BOARD_LAST_REPLY_TIME_REGEX = "(?<=bottom\">).*?(?=&nbsp;</a>)";
 	public static final String P_BOARD_LAST_REPLY_BOARDID_REGEX = "(?<=dispbbs\\.asp\\?Boardid=)\\d{1,5}?(?=&)";
  	public static final String P_BOARD_POST_NUMBER_TODAY = "(?<=<font color=\"#FF0000\">).*?(?=</font></td>)";
  	
 	// board list
  	public static final String LIST_BOARD_SINGLE_BOARD_WRAPPER_REGEX = "<!--版面名字-->.*?</table>";
 	public static final String LIST_BOARD_NAME_REGEX = "(?<=<span style=\"color: #000066;\">).*?(?=</span></a>)";
 	public static final String LIST_IS_PARENT_BOARD_REGEX = "(?<=有 )\\d{1,3}?(?= 个下属论坛)";
 	public static final String LIST_BOARD_ID_REGEX = "(?<=<a href=\"list.asp\\?boardid=)[0-9]+(?=\">)";
  	public static final String LIST_BOARD_LAST_REPLY_TOPIC_ID_REGEX= "(?<=&ID=)\\d*?(?=&replyID)";
 	public static final String LIST_BOARD_LAST_REPLY_TOPIC_NAME_REGEX = "(?<=主题：).+?(?=</a><br />)";
 	public static final String LIST_BOARD_LAST_REPLY_AUTHOR_REGEX = "(?<=target=\"_blank\">).*?(?=</a><br>)|(?<=作者：)匿名(?=<br />)";
 	public static final String LIST_BOARD_LAST_REPLY_TIME_REGEX = "(?<=bottom\">).*?(?=</a>)";
  	public static final String LIST_BOARD_POST_NUMBER_TODAY = "(?<=#FF0000;\">).*?(?=</span></td>)";
 	public static final String LIST_BOARD_LAST_REPLY_BOARDID_REGEX = "(?<=dispbbs\\.asp\\?Boardid=)\\d{1,5}?(?=&)";

 	//user profile
 	public static final String USER_PROFILE_AVATAR_REGEX = "(?<=&nbsp;<img src=).*?(?= )";
 	public static final String USER_PROFILE_GENERAL_PROFILE_REGEX = "用户头衔：.*?最后登录：.*?<br>";
 	public static final String USER_PROFILE_PERSON_PROFILE_REGEX = "性 别.*?主 页.*?</font>";
 	public static final String USER_PROFILE_BBS_MASTER_INFO_REGEX = "(?<=<font align=left>)论坛职务：</font><br>.*?(?=<td  class=tablebody1)";
 	public static final String USER_PROFILE_ONLINE_INFO_REGEX = "(?<=&nbsp;&nbsp;状态：).*?\\]";
  	
 	//hot topic
 	public static final String HOT_TOPIC_WRAPPER = "&nbsp;<a href=\".*?(</td></tr><TR><TD align=middle|</td></tr><!--data)";
 	public static final String HOT_TOPIC_NAME_REGEX = "(?<=\\<font color=#000066>).*?(?=\\</font>)";
 	public static final String HOT_TOPIC_ID_REGEX = "(?<=&id=).*?(?=\" )";
 	public static final String HOT_TOPIC_BOARD_ID_REGEX = "(?<=boardid=)\\d{0,5}?(?=&id=)";
 	public static final String HOT_TOPIC_BOARD_NAME_WITH_AUTHOR_REGEX = "(?<=target=\"_blank\">).{0,30}?(?=</a></td><td height=20)";
 	public static final String HOT_TOPIC_POST_TIME_REGEX = "(?<=\">).{5,18}?(?=</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;)";
 	public static final String HOT_TOPIC_CLICK_REGEX = "(?<=align=middle class=tablebody\\d>).*?(?=</td>)";
 	
 	public static final String UPLOAD_PIC_ADDRESS_REGEX = "\\[upload.*?\\]http://file.cc98.org.*?\\[/upload\\]";
 	
 	public static final String TODAY_POST_NUMBER_REGEX = "(?<=今日帖数：)\\d*?(?= )";
 	public static final String TODAY_BOARD_ENTITY_REGEX = "list\\.asp\\?boardid=.*?</td></tr>";
 	public static final String TODAY_BOARD_ID_REGEX = "(?<=boardid=).*?(?=\">)";
 	public static final String TODAY_BOARD_NAME_REGEX  = "(?<=\">).*?(?=</a></td><td)";
 	public static final String TODAY_BOARD_TOPIC_NUM_REGEX = "(?<=align=center>)\\d{0,10}?(?=</td>)";

 	public static final String NEW_TOPIC_WRAPPER_REGEX = "(?<=<img src=').*?(?=<!--<font color=\"#FF0000\">)";
 	public static final String NEW_TOPIC_TITLE_REGEX = "(?<=blank\">).*?(?=</a>)";
 	public static final String NEW_TOPIC_FACE_REGEX = "\\d{0,3}?(?=\\.gif)";
 	public static final String NEW_TOPIC_AUTHOR_REGEX = "(?<=\" target=\"_blank\">).{1,12}?(?=</a>)";
 	public static final String NEW_TOPIC_ID_REGEX = "(?<=&ID=)\\d{0,10}?(?=' )";
 	public static final String NEW_TOPIC_BOARD_ID = "(?<=boardID=)\\d{0,10}?(?=&ID)";	
 	public static final String NEW_TOPIC_TOTAL_POST = "(?<=<font color=\"#FF0000\">)\\d{0,10}?(?=</font>)";
 	public static final String NEW_TOPIC_TIME = "(?<=195\">).*&nbsp;";
}	
