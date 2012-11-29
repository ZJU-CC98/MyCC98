package tk.djcrazy.MyCC98.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author DJ
 * 
 */
public class Md5 {
	public static final int T32 = 0;
	public static final int T16 = 1;

	public static String MyMD5(final String password, int type) {
		MessageDigest mDigest = null;
		try {
			mDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		mDigest.update(password.getBytes());
		byte[] binhashedPassword = mDigest.digest();
		String hashedPassword = byte2hex(binhashedPassword, type);
		return hashedPassword;
	}

	public static String byte2hex(byte[] b, int type) // 二进制转十六进制
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		if (type == T16) {
			hs = hs.substring(8, 24);
		}
		return hs.toLowerCase();
	}
}