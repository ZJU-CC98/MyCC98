package tk.djcrazy.MyCC98.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class StreamUtils {

	public static String Stream2String(InputStream stream) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
			StringBuilder builder = new StringBuilder();
			String line= null;
			while ((line = reader.readLine())!=null) {
				builder.append(line);
			}
			return builder.toString();
		} catch (UnsupportedEncodingException e) {
 			e.printStackTrace();
 			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
 			e.printStackTrace();
 			throw new RuntimeException(e.getMessage());
		}
	}
}
