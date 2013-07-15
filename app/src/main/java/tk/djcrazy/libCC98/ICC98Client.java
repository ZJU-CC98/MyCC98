package tk.djcrazy.libCC98;

import android.accounts.NetworkErrorException;
import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.ParseException;
import tk.djcrazy.MyCC98.application.MyApplication.UsersInfo;
import tk.djcrazy.libCC98.data.LoginType;
import tk.djcrazy.libCC98.data.UserData;
import tk.djcrazy.libCC98.exception.CC98Exception;
import tk.djcrazy.libCC98.exception.NoUserFoundException;
import tk.djcrazy.libCC98.exception.ParseContentException;

public interface ICC98Client {
    public UserData getCurrentUserData();

    public void doLogin(String id, String pw32, String pw16, String proxyName, String proxyPwd,
                        LoginType type) throws
            IOException, IllegalAccessException, ParseException,
            ParseContentException, NetworkErrorException;

    public boolean pushNewPost(List<NameValuePair> nvpsList, String boardID)
            throws IOException;

    public boolean editPost(List<NameValuePair> nvpsList, String link)
            throws IOException;

    public void submitReply(List<NameValuePair> nvpsList, String boardID,
                            String rootID) throws Exception;

    public String queryPosts(String keyWord, String sType, String searchDate,
                             int boardArea, String boardID) throws ParseException, IOException;

    public String getPage(String link) throws
            IOException, ParseException;

    public String uploadPicture(File picFile)
            throws PatternSyntaxException, IOException,
            ParseContentException;

    public String getInboxHtml(int pageNumber) throws
            ParseException, IOException;

    public String getOutboxHtml(int pageNumber) throws
            ParseException, IOException;

    public String getUserImgUrl(String userName)
            throws ParseException, IOException,
            ParseContentException;

    public void sendPm(String toUser, String title, String message)
            throws IOException, CC98Exception;

    public void addFriend(String userId) throws ParseException,
            NoUserFoundException, IOException;

    public String getUserProfileHtml(String userName)
            throws NoUserFoundException, IOException;

    public Bitmap getBitmapFromUrl(String url) throws IOException;

    public Bitmap getUserImg(String userName) throws
            ParseException, IOException, ParseContentException;

    public void addHttpBasicAuthorization(UserData userData2, String authName, String authPassword);

    void clearLoginInfo();

    public String getDomain();

    public Bitmap getCurrentUserAvatar();

    public List<Bitmap> getUserAvatars();

    public UsersInfo getUsersInfo();

    public String getDomain(LoginType type);

    public void switchToUser(int index);

    public void deleteUserInfo(int pos);
}