package tk.djcrazy.libCC98;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import tk.djcrazy.libCC98.exception.NoUserFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
 
/**
 * 
 * This class take care of the connection with CC98, keep the cookies and the
 * user information.
 */
public class CC98Client {

	public  final int PM_SEND_FAIL = -1;
	public  final int PM_SEND_SUCC = 0;
	/**
	 * Store the URL of CC98
	 */
	private  String CC98 = "http://www.cc98.org/";

	public  String HOT_TOPIC_LINK = CC98 + "hottopic.asp";
	public  String USER_PROFILE = CC98 + "dispuser.asp?name=";
	public  String NEW_POST_LINK = CC98 + "queryresult.asp?stype=3";
	public  String USER_CONTROL_LINK = CC98 + "usermanager.asp";
	public  String ADD_FRIEND_LINK = CC98 + "usersms.asp?action=friend";
	public  String ADD_FRIEND_REFERRER = CC98
			+ "usersms.asp?action=friend&todo=addF";
	public  String LOGIN_LINK = CC98 + "login.asp?action=chk";
	private  Bitmap userImg;
	/**
	 * Store the id
	 */
	private  String username;

	/*
	 * Store cookies
	 */
	private  List<Cookie> cookies;

	/**
	 * Don't using this, to avoid null pointer, use getHttpClient instead
	 */
	protected  DefaultHttpClient client = getHttpClient();

	public  Bitmap getLoginUserImg() {
		return userImg;
	}
	public  void setLoginUserImg(Bitmap bitmap) {
		  userImg = bitmap;
	}

	public  DefaultHttpClient getHttpClient() {
		if (client == null) {
			System.err.println("Create HttpClient...");

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params,
					HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			client = new DefaultHttpClient(conMgr, params);
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					10000);

		}

		return client;
	}

	private  String passwd;
	public  final String ID_PASSWD_ERROR_MSG = "用户名/密码错误";
	public  final String SERVER_ERROR = "CC98服务器异常！";

	/**
	 * Login method
	 * 
	 * @param id
	 * @param password
	 * @return is success
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws IllegalAccessException
	 */
	 public void doLogin(String id, String pw)
			throws ClientProtocolException, IOException, IllegalAccessException {

		HttpResponse response;
		HttpPost httpost = new HttpPost(CC98 + "login.asp?action=chk");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", id));
		nvps.add(new BasicNameValuePair("password", pw));
		nvps.add(new BasicNameValuePair("CookieDate", "3"));
		nvps.add(new BasicNameValuePair("loginaction", "login"));
		nvps.add(new BasicNameValuePair("userhidden", "2"));
		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		response = getHttpClient().execute(httpost);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new IllegalAccessException(SERVER_ERROR);
		}
//		System.err.println("Login form get: " + response.getStatusLine());
		String sysMsg = EntityUtils.toString(response.getEntity());
//		System.err.println("Login form get: " + sysMsg);
		if (sysMsg.contains("密码错误") || sysMsg.contains("论坛错误信息")) {
			throw new IllegalAccessException(ID_PASSWD_ERROR_MSG);
		}
		cookies = getHttpClient().getCookieStore().getCookies();
		userImg = getUserImg(id);
	}

	/**
	 * @author zsy
	 * @param ip
	 * @param port
	 */
	public  void setProxy(String ip, int port) {
		System.err.println("Proxy: " + ip + ":" + port);
		HttpHost proxy = new HttpHost(ip, port);
		getHttpClient().getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
				proxy);
	}

	/**
	 * @author zsy
	 */
	public  void rmProxy() {
		getHttpClient().getParams().removeParameter(
				ConnRouteParams.DEFAULT_PROXY);
	}

	public  List<Cookie> getCookies() {
		return cookies;
	}

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
	 public boolean pushNewPost(List<NameValuePair> nvpsList,
			String boardID) throws ClientProtocolException, IOException {
		System.out.println("" + boardID);
		HttpEntity entity;
		HttpPost httpPost = new HttpPost(getCC98Domain()
				+ "SaveAnnounce.asp?boardID=" + boardID);
		HttpResponse response = null;
		String html = null;
		httpPost.addHeader("Referer", getCC98Domain() + "announce.asp?boardid="
				+ boardID);
		nvpsList.add(new BasicNameValuePair("username", username));
		nvpsList.add(new BasicNameValuePair("passwd", passwd));
//		System.err.println("User Info:" + username + " " + passwd);
		httpPost.setEntity(new UrlEncodedFormEntity(nvpsList, HTTP.UTF_8));
		response = getHttpClient().execute(httpPost);
		entity = response.getEntity();
		if (entity != null) {
			html = EntityUtils.toString(entity);
			// System.err.println("Reply Html:" + html.toLowerCase());
			if (html.contains("状态：发表帖子成功")) {
//				System.err.println("new post OK!");
				return true;
			}
		}
		return false;
	}

	/**
	 * Edit post
	 * 
	 * @author zsy
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	 public boolean editPost(List<NameValuePair> nvpsList, String link)
			throws ClientProtocolException, IOException {
		HttpEntity entity;
		HttpPost httpPost = new HttpPost(link);
		httpPost.setEntity(new UrlEncodedFormEntity(nvpsList, HTTP.UTF_8));
		HttpResponse response = getHttpClient().execute(httpPost);
		entity = response.getEntity();
		if (entity != null) {
			String html = EntityUtils.toString(entity);
//			System.err.println("Reply Html:" + link + html.toLowerCase());

			if (html.contains("编辑帖子成功")) {
				return true;
			}
		}
		return false;
	}

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
	 public boolean submitReply(List<NameValuePair> nvpsList,
			String boardID, String rootID) throws ClientProtocolException,
			IOException {
		HttpEntity entity;
		HttpPost httpPost = new HttpPost(getCC98Domain()
				+ "SaveReAnnounce.asp?method=Topic&boardID=" + boardID);
		HttpResponse response = null;
		String html = null;

		httpPost.addHeader("Referer", getCC98Domain()
				+ "reannounce.asp?BoardID=" + boardID + "&id=" + rootID
				+ "&star=1");
		nvpsList.add(new BasicNameValuePair("username", username));
		nvpsList.add(new BasicNameValuePair("passwd", passwd));
		System.err.println("User Info:" + username + " " + passwd);

		httpPost.setEntity(new UrlEncodedFormEntity(nvpsList, HTTP.UTF_8));
		response = getHttpClient().execute(httpPost);
		entity = response.getEntity();

		if (entity != null) {
			html = EntityUtils.toString(entity);
			// System.err.println("Reply Html:" + html.toLowerCase());

			if (html.contains("状态：回复帖子成功")) {

				System.err.println("Reply OK!");
				return true;
			}
		}
		return false;
	}

	public  final String SEARCH_TYPE_TITLE = "2";
	public  final String SEARCH_TYPE_AUTHOR = "1";

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
	 public String queryPosts(String keyWord, String sType,
			String searchDate, int boardArea, int boardID)
			throws ParseException, IOException {
		/*
		 * action: queryresult.asp name=keyword name=sType, value=2: 主题关键字搜索,
		 * value=1: 主题作者搜索 name=SearchDate, value=ALL: 所有日期, value=1: 昨天以来 ,
		 * value=5: 5天以来, value=10: 10天以来, value=30: 30天以来 name=boardarea,
		 * value=0: 全站搜索 name=boardid, name=serType, value=1: 所有帖子, value=2:
		 * 精华贴, value=3: 保存贴
		 */
		List<NameValuePair> nvpsList = new ArrayList<NameValuePair>();
		HttpEntity entity;
		HttpPost httpPost = new HttpPost(getCC98Domain() + "queryresult.asp");
		HttpResponse response = null;
		String html = null;
		httpPost.addHeader("Referer", getCC98Domain() + "query.asp?boardid=0");
		nvpsList.add(new BasicNameValuePair("keyword", keyWord));
		nvpsList.add(new BasicNameValuePair("sType", sType));
		nvpsList.add(new BasicNameValuePair("SearchDate", searchDate));
		nvpsList.add(new BasicNameValuePair("boardarea", String
				.valueOf(boardArea)));
		nvpsList.add(new BasicNameValuePair("serType", String.valueOf(boardID)));

		httpPost.setEntity(new UrlEncodedFormEntity(nvpsList, HTTP.UTF_8));
		response = getHttpClient().execute(httpPost);
		entity = response.getEntity();

		if (entity != null) {
			html = EntityUtils.toString(entity);
			// System.err.println("Reply Html:" + html.toLowerCase());
			if (html.contains("搜索主题共查询到")) {
				System.err.println("Search OK!");
				return html;
			} else if (html.contains("没有找到您要查询的内容")) {
				System.err.println("没有找到您要查询的内容!");
				return null;
			}
		}

		return null;
	}

	/**
	 * get the HTML code of the link address
	 * 
	 * @param link
	 * @return HTML of the link address
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws ParseException
	 */
	public  String getPage(String link) throws ClientProtocolException,
			IOException, ParseException {
		System.err.println("URL:" + link);
		String content = "";
		HttpGet get = new HttpGet(link);
		HttpResponse rsp = null;
		for (int i = 0; i < 3; ++i) {
			try {
				// Why null pointer here ?
				rsp = getHttpClient().execute(get);
				System.err.println("Try get the " + i + "th time");
				break;
			} catch (NullPointerException e) {

			}
		}
		int mCode = rsp.getStatusLine().getStatusCode();
		if (mCode != 200) {
			throw new IOException("服务器有误！");
		}
		HttpEntity entity = rsp.getEntity();

		if (entity != null) {
			content = EntityUtils.toString(entity);
		}

		return content;
	}

	/**
	 * 
	 * @param picFile
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws PatternSyntaxException
	 */
	public  String uploadPictureToCC98(File picFile)
			throws PatternSyntaxException, MalformedURLException, IOException {

		URL url = new URL(getCC98Domain() + "saveannouce_upfile.asp?boardid=10");
		HttpURLConnection uc = (HttpURLConnection) url.openConnection();
		// 上传图片的一些参数设置
		String cookieString = "";
		for (Cookie cookie : getCookies()) {
			cookieString += cookie.getName() + "=" + cookie.getValue() + "; ";
		}
		uc.setRequestProperty("Cookie", cookieString);
		uc.setRequestProperty("User-Agent",
				"Apache-HttpClient/4.1.2 (java 1.5)");
		uc.setRequestProperty("Connection", "Keep-Alive");
		uc.setRequestProperty("Host", "www.cc98.org");
		uc.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=iZhqsAXWbyxTJ01lIrUiXj_AGU2675rDPk");
		uc.addRequestProperty("Cookie2", "$Version=1");
		uc.setDoOutput(true);
		uc.setUseCaches(true);
		// 读取文件流
		int size = (int) picFile.length();
		byte[] data = new byte[size];
		FileInputStream fis = new FileInputStream(picFile);
		OutputStream out = uc.getOutputStream();
		fis.read(data, 0, size);
		String temp = "--iZhqsAXWbyxTJ01lIrUiXj_AGU2675rDPk\r\n";
		out.write(temp.getBytes());
		temp = "Content-Disposition: form-data; name=\"file\"; "
				+ "filename=\"" + picFile.getName() + "\"\r\n";
		out.write(temp.getBytes());
		temp = "Content-Type: application/octet-stream\r\nContent-Transfer-Encoding: binary\r\n\r\n";
		out.write(temp.getBytes());
		// 写入图片流
		out.write(data);
		temp = "\n--iZhqsAXWbyxTJ01lIrUiXj_AGU2675rDPk\r\nContent-Disposition: form-data; name=\"descript\"\r\nContent-Type: text/plain; charset=US-ASCII\r\nContent-Transfer-Encoding: 8bit\r\n";
		out.write(temp.getBytes());
		temp = "0431.la\r\n--iZhqsAXWbyxTJ01lIrUiXj_AGU2675rDPk--\r\n";
		out.write(temp.getBytes());
		out.flush();
		out.close();
		fis.close();
		// 读取响应数据
		int code = uc.getResponseCode();
		String sCurrentLine = "";
		// 存放响应结果
		String sTotalString = "";
		if (code == 200) {
			InputStream is = uc.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			while ((sCurrentLine = reader.readLine()) != null)
				if (sCurrentLine.length() > 0)
					sTotalString = sTotalString + sCurrentLine.trim();
		} else {
			sTotalString = "远程服务器连接失败,错误代码:" + code;
		}
		Pattern pattern = Pattern.compile(
				"\\[upload.*?\\]http://file.cc98.org.*?\\[/upload\\]",
				Pattern.DOTALL);
		Matcher matcher = pattern.matcher(sTotalString);
		if (matcher.find()) {
			return matcher.group().replace(",1", "");
		} else {
			return "";
		}
	}

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
	public  String getInboxHtml(int page_number)
			throws ClientProtocolException, ParseException, IOException {
		return getPage(getCC98Domain() + "usersms.asp?action=inbox&page="
				+ page_number);
	}

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
	public  String getOutboxHtml(int page_number)
			throws ClientProtocolException, ParseException, IOException {
		return getPage(getCC98Domain() + "usersms.asp?action=issend&page="
				+ page_number);
	}

	/**
	 * Get user's avatar URL using user's id.
	 * 
	 * @author zsy
	 * 
	 * @param username
	 * @return User's avatar URL.
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 */
	public  String getUserImgUrl(String username)
			throws ClientProtocolException, ParseException, IOException {

		String html = getPage(getCC98Domain() + "dispuser.asp?name="
				+ URLEncoder.encode(username));
		// System.err.println("HTML:" + html);
		Pattern p = Pattern.compile("(?<=&nbsp;<img src=).*?(?= )");
		Matcher m = p.matcher(html);
		if (!m.find()) {
			System.err.println("Can't get " + username + "'s image url");
			return null;
		}
		String url = m.group();
		if (!url.startsWith("http") && !url.startsWith("ftp")) {
			url = getCC98Domain() + url;
		}
		System.err.println("URL:" + url);
		return url;
	}

	public  String getPasswd() {
		return passwd;
	}

	public  String getUserName() {
		return username;
	}

	 public void setPassword(String pwd) {
		passwd = pwd;
	}

	 public void setUserName(String name) {
		username = name;
	}

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
			throws ClientProtocolException, IOException {
		String url = getCC98Domain() + "messanger.asp?action=send";
		HttpPost post = new HttpPost(url);
		List<NameValuePair> msgInfo = new ArrayList<NameValuePair>();
		msgInfo.add(new BasicNameValuePair("touser", touser));
		msgInfo.add(new BasicNameValuePair("title", title));
		msgInfo.add(new BasicNameValuePair("message", message));
		HttpResponse response = null;
		int flag = PM_SEND_FAIL;

		post.setEntity(new UrlEncodedFormEntity(msgInfo, HTTP.UTF_8));
		response = getHttpClient().execute(post);

		HttpEntity entity = response.getEntity();

		if (entity != null
				&& EntityUtils.toString(response.getEntity()).contains("论坛成功")) {
			flag = PM_SEND_SUCC;
		}

		return flag;
	}

	/**
	 * @return the cC98
	 */
	public  String getCC98Domain() {
		return CC98;
	}

	public  void setCC98Domain(String cC98) {
		CC98 = cC98;
 		HOT_TOPIC_LINK = CC98 + "hottopic.asp";
		USER_PROFILE = CC98 + "dispuser.asp?name=";
		NEW_POST_LINK = CC98 + "queryresult.asp?stype=3";
		USER_CONTROL_LINK = CC98 + "usermanager.asp";
		ADD_FRIEND_LINK = CC98 + "usersms.asp?action=friend";
		ADD_FRIEND_REFERRER = CC98 + "usersms.asp?action=friend&todo=addF";
		LOGIN_LINK = CC98 + "login.asp?action=chk";
	}

	/**
	 * add one user to friend list
	 * 
	 * @param userId
	 * @throws ParseException
	 * @throws NoUserFoundException
	 * @throws IOException
	 */
	public  void addFriend(String userId) throws ParseException,
			NoUserFoundException, IOException {
		if (userId == null) {
			throw new IllegalArgumentException("Null argument!");
		}
		HttpPost httpPost = new HttpPost(ADD_FRIEND_LINK);
		httpPost.addHeader("Referer", ADD_FRIEND_REFERRER);
		List<NameValuePair> nvpsList = new ArrayList<NameValuePair>();
		nvpsList.add(new BasicNameValuePair("todo", "saveF"));
		nvpsList.add(new BasicNameValuePair("touser", userId));
		nvpsList.add(new BasicNameValuePair("Submit", "保存"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvpsList, HTTP.UTF_8));
		HttpResponse response = getHttpClient().execute(httpPost);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new ConnectException("服务器返回错误！");
		}
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String html = EntityUtils.toString(entity);
			if (html.contains("好友添加成功")) {
				return;
			} else if (html.contains("论坛没有这个用户，操作未成功")) {
				throw new NoUserFoundException("不存在此用户！");
			} else {
				throw new UnknownServiceException("未知错误！");
			}
		} else {
			throw new ConnectException("服务器返回错误！");
		}
	}

	public  String getUserProfileHtml(String userName)
			throws NoUserFoundException, IOException {
		String mString = getPage(USER_PROFILE + URLEncoder.encode(userName));
		if (mString.contains("您查询的名字不存在")) {
			throw new NoUserFoundException("您查询的名字不存在");
		} else if (mString.contains("用户头衔")) {
			return mString;
		} else {
			throw new IOException("网络连接有误");
		}
	}

	/**
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws ParseException
	 * @throws IOException
	 */
	public  String getNewPostListHtml() throws ClientProtocolException,
			ParseException, IOException {
		return getPage(NEW_POST_LINK);
	}

	/**
	 * Get bitmap using the url given
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */

	public  Bitmap getBitmapFromUrl(String url) throws IOException {
		if (url == null) {
			throw new IllegalArgumentException();
		}
		Log.d("IMG", url);
		BufferedInputStream bis = null;
		URL img_url = new URL(url);
		URLConnection conn = img_url.openConnection();
		conn.setConnectTimeout(5000);
		conn.connect();
		InputStream in = conn.getInputStream();
		bis = new BufferedInputStream(in);
		Log.d("IMG", bis.toString());
		return BitmapFactory.decodeStream(bis);

	}

	public  Bitmap getUserImg(String user_name)
			throws ClientProtocolException, ParseException, IOException {
		return getBitmapFromUrl(getUserImgUrl(user_name));
	}

	public  boolean doHttpBasicAuthorization(String authName,
			String authPassword) throws ClientProtocolException, IOException,
			URISyntaxException {
		// Set url
		URI uri = null;
		uri = new URI("http://hz.cc98.lifetoy.org/index.asp");
		getHttpClient().getCredentialsProvider().setCredentials(
				new AuthScope(uri.getHost(), uri.getPort(),
						AuthScope.ANY_SCHEME),
				new UsernamePasswordCredentials(authName, authPassword));

		// // Set timeout
		// client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
		// 8000);
		HttpGet request = new HttpGet(uri);

		HttpResponse response = getHttpClient().execute(request);
	//	Log.d("auth", EntityUtils.toString(response.getEntity()));

		if (response.getStatusLine().getStatusCode() == 200) {
			return true;
		} else {
			return false;
		}
	}
}
