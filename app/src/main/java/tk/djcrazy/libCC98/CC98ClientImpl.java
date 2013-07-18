package tk.djcrazy.libCC98;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownServiceException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpVersion;
import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.ParseException;
import ch.boye.httpclientandroidlib.auth.AuthScope;
import ch.boye.httpclientandroidlib.auth.UsernamePasswordCredentials;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.conn.ClientConnectionManager;
import ch.boye.httpclientandroidlib.conn.scheme.PlainSocketFactory;
import ch.boye.httpclientandroidlib.conn.scheme.Scheme;
import ch.boye.httpclientandroidlib.conn.scheme.SchemeRegistry;
import ch.boye.httpclientandroidlib.conn.ssl.SSLSocketFactory;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntity;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.entity.mime.content.StringBody;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.impl.conn.tsccm.ThreadSafeClientConnManager;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;
import ch.boye.httpclientandroidlib.params.BasicHttpParams;
import ch.boye.httpclientandroidlib.params.CoreConnectionPNames;
import ch.boye.httpclientandroidlib.params.HttpParams;
import ch.boye.httpclientandroidlib.params.HttpProtocolParams;
import ch.boye.httpclientandroidlib.util.EntityUtils;
import tk.djcrazy.MyCC98.application.MyApplication;
import tk.djcrazy.MyCC98.application.MyApplication.UsersInfo;
import tk.djcrazy.libCC98.data.LoginType;
import tk.djcrazy.libCC98.data.UserData;
import tk.djcrazy.libCC98.exception.CC98Exception;
import tk.djcrazy.libCC98.exception.NoUserFoundException;
import tk.djcrazy.libCC98.exception.ParseContentException;
import tk.djcrazy.libCC98.util.RegexUtil;

/**
 * This class take care of the connection with CC98, keep the cookies and the
 * user information.
 */
@Singleton
public class CC98ClientImpl implements ICC98Client {

    public static final String TAG = CC98ClientImpl.class.getSimpleName();
    @Inject
    private ICC98UrlManager manager;
    @Inject
    private Application application;
    private DefaultHttpClient client;

    @Override
    public UserData getCurrentUserData() {
        UserData userData = ((MyApplication) application).getCurrentUserData();
        return userData;
    }

    @Override
    public Bitmap getCurrentUserAvatar() {
        return ((MyApplication) application).getCurrentUserAvatar();
    }

    private DefaultHttpClient getHttpClient() {
        getHttpClient(false);
        return client;
    }

    public DefaultHttpClient getHttpClient(boolean forceRefresh) {
        if (client == null || forceRefresh) {
            genHttpClient();
            if (getCurrentUserData().getCookieStore() != null) {
                client.setCookieStore(getCurrentUserData().getCookieStore());
            }
            if (getCurrentUserData().getLoginType() == LoginType.USER_DEFINED
                    && (getCurrentUserData().getProxyUserName() != null)) {
                addHttpBasicAuthorization(
                        getCurrentUserData().getProxyUserName(),
                        getCurrentUserData().getProxyPassword());
            }
        }
        return client;
    }

    @SuppressWarnings("deprecation")
    private void genHttpClient() {
        try {
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "UTF-8");
            HttpProtocolParams.setUseExpectContinue(params, true);
            X509TrustManager xtm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{xtm}, null);
            SSLSocketFactory sf = new SSLSocketFactory(ctx,
                    SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            schReg.register(new Scheme("https", 443, sf));
            ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                    params, schReg);
            client = new DefaultHttpClient(conMgr, params);
            client.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                    30000);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Initial http params failed");
        }
    }

    @Override
    public void clearLoginInfo() {
        getHttpClient().getCookieStore().clear();
        getHttpClient().getCredentialsProvider().clear();
    }

    public DefaultHttpClient getNoCurrentUserHttpClient() {
        genHttpClient();
        return client;
    }

    @Override
    public void doLogin(String id, String pw32, String pw16, String proxyName,
                        String proxyPwd, LoginType loginType) {
        getNoCurrentUserHttpClient();
        if (loginType == LoginType.USER_DEFINED) {
            addHttpBasicAuthorization(proxyName, proxyPwd);
        } else if (loginType == LoginType.RVPN) {
            authenticateForRVPN(proxyName, proxyPwd);
        }
        storeUserData(id, pw32, pw16, proxyName, proxyPwd, loginType);
    }

    private void storeUserData(String id, String pw32, String pw16, String proxyName, String proxyPwd, LoginType loginType) {
        UserData newUserData = new UserData();
        doInternalLogin(id, pw32, loginType);
        newUserData.setUserName(id);
        newUserData.setPassword16(pw16);
        newUserData.setPassword32(pw32);
        newUserData.setLoginType(loginType);
        newUserData.setProxyUserName(proxyName);
        newUserData.setProxyPassword(proxyPwd);
        newUserData.setCookieStore(getHttpClient().getCookieStore());
        Bitmap bitmap = getLoginUserImgAndGender(newUserData, loginType);
        ((MyApplication) application).addNewUser(newUserData, bitmap, true);
        ((MyApplication) application).storeUsersInfo();
    }

    private void doInternalLogin(String id, String pw32, LoginType loginType) {
        try {
            HttpPost post = new HttpPost(manager.getLoginUrl(loginType));
            List<NameValuePair> keys = new ArrayList<NameValuePair>();
            keys.add(new BasicNameValuePair("a", "i"));
            keys.add(new BasicNameValuePair("u", id));
            keys.add(new BasicNameValuePair("p", pw32));
            keys.add(new BasicNameValuePair("userhidden", "2"));
            post.setEntity(new UrlEncodedFormEntity(keys, "UTF-8"));
            HttpResponse response = getHttpClient().execute(post);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new CC98Exception("服务器或网络错误");
            }
            String sysMsg = EntityUtils.toString(response.getEntity());
            if (!sysMsg.contains("9898")) {
                throw new CC98Exception("用户名或密码错误");
            }
        } catch (Exception e) {
            throw new CC98Exception(e.getMessage());
        }
    }

    private void authenticateForRVPN(String proxyName, String proxyPwd) {
        try {
            String initUrl = "https://61.175.193.50//por/login_auth.csp?dev=android-phone&language=zh_CN";
            String initPage = getPage(initUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("svpn_name", proxyName));
            nvps.add(new BasicNameValuePair("svpn_rand_code", ""));
            nvps.add(new BasicNameValuePair("svpn_password", proxyPwd));
            HttpPost post = new HttpPost(
                    "https://61.175.193.50//por/login_psw.csp?type=cs&dev=android-phone&dev=android-phone&language=zh_CN");
            post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse response = client.execute(post);
            String result = EntityUtils.toString(response.getEntity());
            System.err.println("authenticateForRVPN result：" + result);
            if (!result.contains("<Result>1</Result>")) {
                throw new IllegalArgumentException("RVPN 用户名或密码不正确!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("do rvpn authenticate failed");
        }
    }

    @Override
    public void pushNewPost(List<NameValuePair> pairList, String boardID) {
        try {
            HttpPost httpPost = new HttpPost(manager.getPushNewPostUrl(boardID));
            httpPost.addHeader("Referer", manager.getPushNewPostReferer(boardID));
            pairList.add(new BasicNameValuePair("username", getCurrentUserData()
                    .getUserName()));
            pairList.add(new BasicNameValuePair("passwd", getCurrentUserData()
                    .getPassword16()));
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, "UTF-8"));
            HttpResponse response = getHttpClient().execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());
            if (StringUtils.isEmpty(responseString) || !responseString.contains("状态：发表帖子成功")) {
                throw new CC98Exception("发帖失败");
            }
        } catch (Exception e) {
            throw new CC98Exception(e.getMessage());
        }
    }

    @Override
    public void submitReply(List<NameValuePair> nvpsList, String boardID, String rootID) {
        HttpPost httpPost = new HttpPost(manager.getSubmitReplyUrl(boardID));
        httpPost.addHeader("Referer",
                manager.getSubmitReplyReferer(boardID, rootID));
        nvpsList.add(new BasicNameValuePair("username", getCurrentUserData()
                .getUserName()));
        nvpsList.add(new BasicNameValuePair("passwd", getCurrentUserData()
                .getPassword16()));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvpsList, "UTF-8"));
            HttpResponse response = getHttpClient().execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());
            if (StringUtils.isEmpty(responseString)||!responseString.contains("状态：回复帖子成功")) {
                throw new CC98Exception("回复失败");
            }
        } catch (Exception e) {
            throw new CC98Exception(e.getMessage());
        }
    }

    @Override
    public String queryPosts(String keyWord, String sType, String searchDate,
                             int boardArea, String boardID) throws ParseException, IOException {
        /*
         * action: queryresult.asp name=keyword name=sType, value=2: 主题关键字搜索,
		 * value=1: 主题作者搜索 name=SearchDate, value=ALL: 所有日期, value=1: 昨天以来 ,
		 * value=5: 5天以来, value=10: 10天以来, value=30: 30天以来 name=boardarea,
		 * value=0: 全站搜索 name=boardid, name=serType, value=1: 所有帖子, value=2:
		 * 精华贴, value=3: 保存贴
		 */
        List<NameValuePair> nvpsList = new ArrayList<NameValuePair>();
        HttpEntity entity;
        HttpPost httpPost = new HttpPost(manager.getQueryUrl());
        HttpResponse response = null;
        String html = null;
        httpPost.addHeader("Referer", manager.getQueryReferer());
        nvpsList.add(new BasicNameValuePair("keyword", keyWord));
        nvpsList.add(new BasicNameValuePair("sType", sType));
        nvpsList.add(new BasicNameValuePair("SearchDate", searchDate));
        nvpsList.add(new BasicNameValuePair("boardarea", String
                .valueOf(boardArea)));
        nvpsList.add(new BasicNameValuePair("serType", String.valueOf(boardID)));

        httpPost.setEntity(new UrlEncodedFormEntity(nvpsList, "UTF-8"));
        response = getHttpClient().execute(httpPost);
        entity = response.getEntity();

        if (entity != null) {
            html = EntityUtils.toString(entity);
            if (html.contains("搜索主题共查询到")) {
                return html;
            } else if (html.contains("没有找到您要查询的内容")) {
                System.err.println("没有找到您要查询的内容!");
                return "";
            }
        }

        return null;
    }

    @Override
    public String getPage(String link) {
        try {
            HttpResponse  rsp = getHttpClient().execute( new HttpGet(link));
            if (rsp.getStatusLine().getStatusCode() != 200) {
                throw new CC98Exception("服务器有误！");
            }
            return EntityUtils.toString(rsp.getEntity());
        } catch (Exception e) {
            throw new CC98Exception(e.getMessage());
        }
    }

    @Override
    public String uploadPicture(File picFile)
            throws PatternSyntaxException, MalformedURLException, IOException,
            ParseContentException {
        HttpPost post = new HttpPost(manager.getUploadPictureUrl());
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("act", new StringBody("upload"));
        reqEntity.addPart("fname", new StringBody(picFile.getName()));
        reqEntity.addPart("file1", new FileBody(picFile));
        post.setEntity(reqEntity);
        HttpResponse response = getHttpClient().execute(post);
        String sTotalString = EntityUtils.toString(response.getEntity());
        if (response.getStatusLine().getStatusCode() != 200) {
            Log.e(TAG, sTotalString);
            throw new IllegalStateException("upload error");
        }
        return RegexUtil.getMatchedString(
                CC98ParseRepository.UPLOAD_PIC_ADDRESS_REGEX, sTotalString)
                .replace(",1", "");
    }

    @Override
    public String getOutboxHtml(int pageNumber) throws ClientProtocolException,
            ParseException, IOException {
        return getPage(manager.getOutboxUrl(pageNumber));
    }

    @Override
    public String getUserImgUrl(String userName)
            throws ClientProtocolException, ParseException, IOException,
            ParseContentException {

        String html = getPage(manager.getUserProfileUrl(userName));
        String url = RegexUtil.getMatchedString(
                CC98ParseRepository.USER_PROFILE_AVATAR_REGEX, html);
        if (!url.startsWith("http") && !url.startsWith("ftp")) {
            url = getDomain() + url;
        }
        return url;
    }

    @Override
    public void sendPm(String touser, String title, String message)
            throws ClientProtocolException, IOException, CC98Exception {
        HttpPost post = new HttpPost(manager.getSendPmUrl());
        List<NameValuePair> msgInfo = new ArrayList<NameValuePair>();
        msgInfo.add(new BasicNameValuePair("touser", touser));
        msgInfo.add(new BasicNameValuePair("title", title));
        msgInfo.add(new BasicNameValuePair("message", message));
        post.setEntity(new UrlEncodedFormEntity(msgInfo, "UTF-8"));
        HttpResponse response = getHttpClient().execute(post);

        HttpEntity entity = response.getEntity();
        if (entity == null
                || !EntityUtils.toString(response.getEntity()).contains(
                "发送短信息成功")) {
            Log.d(TAG, EntityUtils.toString(response.getEntity()));
            throw new CC98Exception("send pm failed, reply content is: "
                    + EntityUtils.toString(response.getEntity()));
        }
    }

    @Override
    public void addFriend(String userId) throws ParseException,
            NoUserFoundException, IOException {
        if (userId == null) {
            throw new IllegalArgumentException("Null argument!");
        }
        HttpPost httpPost = new HttpPost(manager.getAddFriendUrl());
        httpPost.addHeader("Referer", manager.getAddFriendUrlReferrer());
        List<NameValuePair> nvpsList = new ArrayList<NameValuePair>();
        nvpsList.add(new BasicNameValuePair("todo", "saveF"));
        nvpsList.add(new BasicNameValuePair("touser", userId));
        nvpsList.add(new BasicNameValuePair("Submit", "保存"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvpsList, "UTF-8"));
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

    @Override
    public Bitmap getBitmapFromUrl(String url) throws CC98Exception {
        try {
            HttpGet get = new HttpGet(url);
            HttpResponse response = getHttpClient().execute(get);
            return BitmapFactory.decodeStream(response.getEntity().getContent());
        } catch (Exception e) {
            throw new CC98Exception(e.getMessage());
        }
    }

    @Override
    public Bitmap getUserImg(String userName) throws ClientProtocolException,
            ParseException, IOException, ParseContentException {
        return getBitmapFromUrl(getUserImgUrl(userName));
    }

    private Bitmap getLoginUserImgAndGender(UserData userData2, LoginType type) {
        try {
            String html = getPage(manager.getUserProfileUrl(type,
                    userData2.getUserName()));
            String url = RegexUtil.getMatchedString(
                    CC98ParseRepository.USER_PROFILE_AVATAR_REGEX, html);
            if (!url.startsWith("http") && !url.startsWith("ftp")) {
                url = manager.getClientUrl(type) + url;
            }
            return getBitmapFromUrl(url);
        } catch (Exception e) {
            return BitmapFactory
                    .decodeFile("file:///android_asset/pic/no_avatar.jpg");
        }
    }

    public void addHttpBasicAuthorization(String authName,
                                          String authPassword) {
        try {
            URI uri = new URI(getDomain(LoginType.USER_DEFINED));
            getHttpClient().getCredentialsProvider().setCredentials(
                    new AuthScope(uri.getHost(), uri.getPort(),
                            AuthScope.ANY_SCHEME),
                    new UsernamePasswordCredentials(authName, authPassword));
        } catch (URISyntaxException e) {
            throw new CC98Exception("Invalid Uri problem");
        }
    }

    @Override
    public String getDomain() {
        return manager.getClientUrl();
    }

    @Override
    public String getDomain(LoginType type) {
        return manager.getClientUrl(type);
    }

    @Override
    public List<Bitmap> getUserAvatars() {
        return ((MyApplication) application).getUserAvatars();
    }

    @Override
    public UsersInfo getUsersInfo() {
        return ((MyApplication) application).getUsersInfo();
    }

    @Override
    public void switchToUser(int index) {
        getUsersInfo().currentUserIndex = index;
        ((MyApplication) application).storeUsersInfo();
        getHttpClient(true);
    }

    @Override
    public void deleteUserInfo(int pos) {
        if (pos != getUsersInfo().currentUserIndex) {
            Log.d(TAG, "to delete:" + pos);
            UsersInfo info = getUsersInfo();
            List<Bitmap> avatars = getUserAvatars();
            UserData currentUser = getCurrentUserData();
            info.users.remove(pos);
            avatars.remove(pos);
            int newPos = info.users.indexOf(currentUser);
            info.currentUserIndex = newPos;
            ((MyApplication) application).storeUsersInfo();
        }
    }
}
