package tk.djcrazy.libCC98;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.cookie.Cookie;

import tk.djcrazy.libCC98.exception.NoUserFoundException;
import tk.djcrazy.libCC98.exception.ParseContentException;
import android.graphics.Bitmap;

public interface ICC98Client {

	public Bitmap getLoginUserImg();

	public void setLoginUserImg(Bitmap bitmap);

	/**
	 * Login method
	 * 
	 * @param id
	 * @param password
	 * @return is success
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws IllegalAccessException
	 * @throws ParseContentException 
	 * @throws ParseException 
	 */
	public void doLogin(String id, String pw) throws ClientProtocolException,
			IOException, IllegalAccessException, ParseException,
			ParseContentException;

	/**
	 * @author zsy
	 * @param ip
	 * @param port
	 */
	public void setProxy(String ip, int port);

	/**
	 * @author zsy
	 */
	public void rmProxy();
 
	/**
	 * Reply ?
	 * 
	 * @author DJ
	 * @param nvpsList
	 * @param boardID
	 * @param rootID
	 * @return ?
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public boolean pushNewPost(List<NameValuePair> nvpsList, String boardID)
			throws ClientProtocolException, IOException;

	/**
	 * Edit post
	 * 
	 * @author zsy
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public boolean editPost(List<NameValuePair> nvpsList, String link)
			throws ClientProtocolException, IOException;

	/**
	 * Reply ?
	 * 
	 * @author DJ
	 * @param nvpsList
	 * @param boardID
	 * @param rootID
	 * @return ?
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public boolean submitReply(List<NameValuePair> nvpsList, String boardID,
			String rootID) throws ClientProtocolException, IOException;

	/**
	 * @author zsy
	 * @param keyWord
	 * @param sType
	 * @param searchDate
	 * @param boardArea
	 * @param boardID
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public String queryPosts(String keyWord, String sType, String searchDate,
			int boardArea, int boardID) throws ParseException, IOException;

	/**
	 * get the HTML code of the link address
	 * 
	 * @param link
	 * @return HTML of the link address
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws ParseException
	 */
	public String getPage(String link) throws ClientProtocolException,
			IOException, ParseException;

	/**
	 * 
	 * @param picFile
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws PatternSyntaxException
	 * @throws ParseContentException
	 */
	public String uploadPictureToCC98(File picFile)
			throws PatternSyntaxException, MalformedURLException, IOException,
			ParseContentException;

	/**
	 * get the HTML of the PM inbox
	 * 
	 * @author zsy
	 * 
	 * @param pageNumber
	 *            The Page number of the inbox
	 * @return A String contains the HTML of inbox
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 */
	public String getInboxHtml(int pageNumber) throws ClientProtocolException,
			ParseException, IOException;

	/**
	 * get the HTML of the PM outbox
	 * 
	 * @author zsy
	 * 
	 * @param pageNumber
	 *            The Page number of the outbox
	 * @return A String contains the HTML of outbox
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 */
	public String getOutboxHtml(int pageNumber) throws ClientProtocolException,
			ParseException, IOException;

	/**
	 * Get user's avatar URL using user's id.
	 * 
	 * @author zsy
	 * 
	 * @param userName
	 * @return User's avatar URL.
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @throws ParseContentException
	 */
	public String getUserImgUrl(String userName)
			throws ClientProtocolException, ParseException, IOException,
			ParseContentException;

	public String getPasswd();

	public String getUserName();

	public void setPassword(String pwd);

	public void setUserName(String name);

	/**
	 * @author zsy
	 * @param touser
	 *            User's name
	 * @param title
	 *            Message title
	 * @param message
	 *            The message
	 * @return PM_SEND_SUCC on success PM_SEND_FAIL on failure
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public int sendPm(String touser, String title, String message)
			throws ClientProtocolException, IOException;

	/**
	 * add one user to friend list
	 * 
	 * @param userId
	 * @throws ParseException
	 * @throws NoUserFoundException
	 * @throws IOException
	 */
	public void addFriend(String userId) throws ParseException,
			NoUserFoundException, IOException;

	public String getUserProfileHtml(String userName)
			throws NoUserFoundException, IOException;

	/**
	 * Get bitmap using the url given
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */

	public Bitmap getBitmapFromUrl(String url) throws IOException;

	public Bitmap getUserImg(String userName) throws ClientProtocolException,
			ParseException, IOException, ParseContentException;

	public boolean doHttpBasicAuthorization(String authName, String authPassword)
			throws ClientProtocolException, IOException, URISyntaxException;

}