/*
 * 现在这个文件暂时没用了，本来提供密码的md5加密，若果以后想采用加密连接的话可以使用
 */


package tk.djcrazy.MyCC98.security;

import java.security.*;
/**
 * @author DJ
 *
 */
public class Md5{
	public static String MyMD5(final String password) {
		MessageDigest mDigest = null;
		try {
			mDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		mDigest.update(password.getBytes());
		byte [] binhashedPassword = mDigest.digest();
		String hashedPassword = byte2hex(binhashedPassword);
		return hashedPassword;
	}
	
	public static String byte2hex(byte[] b) //二进制转十六进制
    {
     String hs="";
     String stmp="";
     for (int n=0;n<b.length;n++)
      {
       stmp=(java.lang.Integer.toHexString(b[n] & 0XFF));
       if (stmp.length()==1) hs=hs+"0"+stmp;
       else hs=hs+stmp;
      }
     hs=hs.substring(8, 24);
     return hs.toLowerCase();
    }
}