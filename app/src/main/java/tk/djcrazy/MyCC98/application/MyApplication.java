package tk.djcrazy.MyCC98.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import ch.boye.httpclientandroidlib.HttpVersion;
import ch.boye.httpclientandroidlib.auth.AuthScope;
import ch.boye.httpclientandroidlib.auth.UsernamePasswordCredentials;
import ch.boye.httpclientandroidlib.conn.scheme.PlainSocketFactory;
import ch.boye.httpclientandroidlib.conn.scheme.Scheme;
import ch.boye.httpclientandroidlib.conn.scheme.SchemeRegistry;
import ch.boye.httpclientandroidlib.conn.ssl.SSLSocketFactory;
import ch.boye.httpclientandroidlib.cookie.Cookie;
import ch.boye.httpclientandroidlib.impl.client.BasicCookieStore;
import ch.boye.httpclientandroidlib.impl.client.ContentEncodingHttpClient;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.impl.conn.PoolingClientConnectionManager;
import ch.boye.httpclientandroidlib.impl.cookie.BasicClientCookie2;
import ch.boye.httpclientandroidlib.params.BasicHttpParams;
import ch.boye.httpclientandroidlib.params.CoreConnectionPNames;
import ch.boye.httpclientandroidlib.params.HttpParams;
import ch.boye.httpclientandroidlib.params.HttpProtocolParams;
import tk.djcrazy.MyCC98.util.BitmapLruCache;
import tk.djcrazy.libCC98.data.LoginType;
import tk.djcrazy.libCC98.data.UserData;
import tk.djcrazy.libCC98.exception.CC98Exception;

public class MyApplication extends Application {
    public static final String USERS_STRING_INFO = "userStringData";
    public static final String USER_AVATAR_PREFIX = "userAvatar.png";
    private static Context mContext;
    public RequestQueue mRequestQueue;
    public ImageLoader mImageLoader;
    public DefaultHttpClient mHttpClient;
    private UsersInfo usersInfo;
    private List<Bitmap> userAvatars = new ArrayList<Bitmap>();
    private Gson mGson = new GsonBuilder().registerTypeAdapter(BasicClientCookie2.class, new InstanceCreator<BasicClientCookie2>() {
        @Override
        public BasicClientCookie2 createInstance(Type type) {
            return new BasicClientCookie2("", "");
        }
    }).create();

    public static Context getAppContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHttpClient = genHttpClient();
        initVolley();
        initUsersInfo();
        syncUserDataAndHttpClient();
        mContext = getApplicationContext();
    }

    private void initVolley() {
        HttpClientStack stack = new HttpClientStack(mHttpClient);
        mRequestQueue = Volley.newRequestQueue(this, stack);
        mRequestQueue.start();
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache());
    }

    /**
     *
     */
    private void initUsersInfo() {
        try {
            SharedPreferences preferences = getSharedPreferences(USERS_STRING_INFO, MODE_PRIVATE);
            String data = preferences.getString(USERS_STRING_INFO, "{}");
            usersInfo = mGson.fromJson(data, UsersInfo.class);
            for (int i = 0; i < usersInfo.users.size(); i++) {
                userAvatars.add(BitmapFactory.decodeStream(openFileInput(USER_AVATAR_PREFIX + i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void storeUsersInfo() {
        ObjectOutputStream oos = null;
        try {
            getSharedPreferences(USERS_STRING_INFO, MODE_PRIVATE).edit()
                    .putString(USERS_STRING_INFO, mGson.toJson(usersInfo)).commit();
            System.out.println(mGson.toJson(usersInfo));
            for (int i = 0; i < userAvatars.size(); i++) {
                userAvatars.get(i).compress(Bitmap.CompressFormat.PNG, 70,
                        openFileOutput(USER_AVATAR_PREFIX + i, MODE_PRIVATE));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error(e);
        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (IOException e) {
                throw new Error(e);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private DefaultHttpClient genHttpClient() {
        HttpParams params = new BasicHttpParams();
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", 80, PlainSocketFactory
                .getSocketFactory()));
        schReg.register(new Scheme("https", 443,  SSLSocketFactory.getSocketFactory()));

        DefaultHttpClient client = new ContentEncodingHttpClient(new PoolingClientConnectionManager(schReg), params);
        client.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                30000);
        return client;
    }

    public void syncUserDataAndHttpClient() {
        try {
            UserData data = getCurrentUserData();
            BasicCookieStore cookieStore = new BasicCookieStore();
            for (Cookie cookie : data.getCookies()) {
                cookieStore.addCookie(cookie);
            }
            if (data.getLoginType() == LoginType.USER_DEFINED
                    && (StringUtils.isNotBlank(data.getProxyUserName()))) {
                addHttpBasicAuthorization(data.getProxyHost(), data.getProxyUserName(), data.getProxyPassword());
            }
            mHttpClient.setCookieStore(cookieStore);
        } catch (NoCurrentUserException e) {
            //do nothing here
        }

    }

    public void syncUserDataAndHttpClient(UserData data) {
        try {
            BasicCookieStore cookieStore = new BasicCookieStore();
            for (Cookie cookie : data.getCookies()) {
                cookieStore.addCookie(cookie);
            }
            if (data.getLoginType() == LoginType.USER_DEFINED
                    && (StringUtils.isNotBlank(data.getProxyUserName()))) {
                addHttpBasicAuthorization(data.getProxyHost(), data.getProxyUserName(), data.getProxyPassword());
            }
            mHttpClient.setCookieStore(cookieStore);
        } catch (NoCurrentUserException e) {
            //do nothing here
        }

    }

    public void addHttpBasicAuthorization(String host, String authName,
                                          String authPassword) {
        try {
            URI uri = new URI(host);
            mHttpClient.getCredentialsProvider().setCredentials(
                    new AuthScope(uri.getHost(), uri.getPort(),
                            AuthScope.ANY_SCHEME),
                    new UsernamePasswordCredentials(authName, authPassword));
        } catch (URISyntaxException e) {
            throw new CC98Exception("Invalid Uri problem");
        }
    }

    /**
     * @return the userData
     */
    public UserData getCurrentUserData() throws NoCurrentUserException {
        return usersInfo.getCurrentUserData();
    }

    public List<UserData> getAllUserDatas() {
        return usersInfo.users;
    }

    public void addNewUser(UserData userData, Bitmap avatar, boolean isCurrentUser) {
        if (!usersInfo.users.contains(userData)) {
            usersInfo.users.add(userData);
            userAvatars.add(avatar);
        } else {
            usersInfo.users.set(usersInfo.users.indexOf(userData), userData);
            userAvatars.set(usersInfo.users.indexOf(userData), avatar);
        }
        if (isCurrentUser) {
            usersInfo.currentUserIndex = usersInfo.users.indexOf(userData);
        }
        storeUsersInfo();
    }

    /**
     * @return the userAvatar
     */
    public Bitmap getCurrentUserAvatar() {
        if (usersInfo.users.size() > 0) {
            return userAvatars.get(usersInfo.currentUserIndex);
        } else {
            throw new IllegalArgumentException("No user in current");
        }
    }

    public UsersInfo getUsersInfo() {
        return usersInfo;
    }

    public List<Bitmap> getUserAvatars() {
        return userAvatars;
    }

    public static class UsersInfo implements Serializable {

        private static final long serialVersionUID = 1161679319055452529L;
        public int currentUserIndex;
        public ArrayList<UserData> users = new ArrayList<UserData>();

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

        public UserData getCurrentUserData() throws NoCurrentUserException {
            if (users.size() > 0) {
                return users.get(currentUserIndex);
            } else {
                throw new NoCurrentUserException("No user in current!");
            }
        }
    }

    public static class NoCurrentUserException extends RuntimeException {
        public NoCurrentUserException() {
            super();
        }

        public NoCurrentUserException(String msg) {
            super(msg);
        }
    }
}
