package tk.djcrazy.libCC98.util;

public class StringUtil {
	public static String filterHtmlDecode(String html) {
		return html.replaceAll("&amp;", "&").replaceAll("&lt;", "<")
				.replaceAll("&gt;", ">").replaceAll("&apos;", "\'")
				.replaceAll("&quot;", "\"").replaceAll("&nbsp;", " ")
				.replaceAll("&copy;", "@").replaceAll("&reg;", "?")
				.replaceAll("\n", "");
	}
	public static String filterHtmlEncode(String content) {
		return content.replaceAll("&amp;", "&").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;").replaceAll("\'", "&apos;")
				.replaceAll("&quot;", "\"").replaceAll(" ", "&nbsp;")
				.replaceAll( "@","&copy;").replaceAll("\\?", "&reg;");
	}
}
