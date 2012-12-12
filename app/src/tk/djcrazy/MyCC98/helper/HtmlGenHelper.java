package tk.djcrazy.MyCC98.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;

import tk.djcrazy.libCC98.CC98ClientImpl;
import tk.djcrazy.libCC98.ICC98Service;
import android.util.Log;

public class HtmlGenHelper {
	@Inject
	private ICC98Service service;

	public final String PAGE_OPEN = "<!DOCTYPE html><html class=\"ui-mobile\">" // min-width-320px
																				// min-width-480px
			+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"
			+ "<link rel=\"stylesheet\" href=\"file:///android_asset/custom.css\" />"
			+ "<link rel=\"stylesheet\" href=\"file:///android_asset/bootstrap.css\" />"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/mootools.core.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/tools.js\"></script>"
			// +
			// "<script type=\"text/javascript\" src=\"file:///android_asset/mootools.more.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/clientubb.js\"></script>"
			+ "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\" />"
			+ "</head><body>" + "<div  class=\"bg-wrapper\">";
	public final String PAGE_CLOSE = "</div> </body></html>";

	private final String TAG = "HtmlGenHelper";

	public static String addPostInfo(String readTopic, String senderAvatarUrl,
			String sender, String gender, int floorNum, String sendTime,
			int index) {
		return "<div class=\"post-info\">" + addTopic(readTopic)
				+ addSenderAvatar(senderAvatarUrl, index)
				+ addSenderName(sender) + addFloorNum(floorNum)
				+ addGender(gender) + addSendTime(sendTime) + "</div>";
	}

	public static void postInfo(StringBuilder out, String readTopic,
			String senderAvatarUrl, String sender, String gender, int floorNum,
			String sendTime, int index) {
		out.append(addPostInfo(readTopic, senderAvatarUrl, sender, gender,
				floorNum, sendTime, index));
	}

	private static String addFloorNum(int floorNum) {
		if (floorNum == -1) {
			return "";
		} else {
			return "<div class=\"floor-num\">" + floorNum + " 楼</div>";
		}
	}

	private static String addGender(String gender) {
		if (gender == "") {
			return "";
		}
		return "<img class=\"img-gender\" src=\"file:///android_asset/pic/"
				+ gender + ".gif\"/>";
	}

	public static String addSenderAvatar(String senderAvatarUrl, int index) {
		return "<img class=\"img-avatar\" src=\"" + senderAvatarUrl
				+ "\" height=64/>";
	}

	public static String addTopic(String readTopic) {
		return "<div class=\"topic\" ><big><strong>" + readTopic
				+ "</strong></big></div><br />";
	}

	public static String addSenderName(String sender) {
		return "<div class=\"name\">" + sender + "</div><br />";
	}

	public static String addSendTime(String sendTime) {
		return "<div class=\"time\"><small>" + sendTime
				+ "</small></div><br />";
	}

	public String parseInnerLink(String content, String jsInterface) {
		final String regString = "(\\[url\\]|http://www\\.cc98\\.org|\\[url\\]http://www\\.cc98\\.org)(/|)dispbbs\\.asp\\?boardID=\\d+?&ID=\\d+?(&star=\\d+|)(\\[/url\\]|).*?(?=(<br>|[;；#,.!?，。！？]|$))";
		Pattern pattern = Pattern.compile(regString, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(content);
		StringBuffer stringBuffer = new StringBuffer();
		String tmp = null, pageLink = null;
		while (matcher.find() && !content.contains("10.10.98.98")) {
			tmp = matcher.group();
			int beg = tmp.indexOf("star");
			int end = tmp.indexOf("&page");
			if (end == -1) {
				end = tmp.indexOf("[/");
				end = end == -1 ? tmp.length() : end;
			}

			Log.d(TAG + " url", tmp);
			int pageNum = 1;
			String ttmpString = "";
			if (beg == -1) {
				pageNum = 1;
			} else {
				try {
					ttmpString = tmp.substring(beg + 5, end);
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					pageNum = Integer.parseInt(ttmpString);
				} catch (Exception e) {
					pageNum = 1;
				}
			}

			Log.d(TAG + " url", "tmp:" + ttmpString);

			Log.d(TAG, "pn:" + pageNum);

			pageLink = tmp.replaceAll(
					"(\\&star=\\d+.*)|(\\[url\\])|(\\[/url\\])", "");
			if (!pageLink.startsWith("http")) {
				if (pageLink.startsWith("/")) {
					pageLink = service.getDomain() + pageLink.substring(1);
				} else {
					pageLink = service.getDomain() + pageLink;
				}
			}
			Log.d(TAG, " link" + pageLink);
			matcher.appendReplacement(
					stringBuffer,
					"[noubb]<a style=\"color:blue;\" href=\"javascript:"
							+ jsInterface + ".open('" + pageLink + "',"
							+ pageNum + ");\">"
							+ tmp.replaceAll("(\\[url\\])|(\\[/url\\])", "")
							+ "</a>[/noubb]");
		}
		matcher.appendTail(stringBuffer);
		Log.d(TAG, "final:" + stringBuffer.toString());
		return stringBuffer.toString();
	}

	public static void jsBtn(StringBuilder out, String text, String funcName,
			String... args) {
		out.append("<a class=\"btn\" onclick=\"" + funcName + "("
				+ StringUtils.join(args, ',') + ");\">" + text + "</a>");
	}

	public static void postContent(StringBuilder out, String postFace,
			String content, int index) {
		out.append("<img class=\"post-face\" src=\"file:///android_asset/pic/")
				.append(postFace).append("\" /><br />")
				.append("<div class=\"post-content\">")
				.append("<span id=\"ubbcode").append(index).append("\">")
				.append(content).append("</span><script>searchubb('ubbcode")
				.append(index).append("',1,'tablebody2');</script></div>")
				.append("</div>");
	}

	public static void btnsBegin(StringBuilder out) {
		out.append("<div class=\"btn-group\">");
	}

	public static void btnsEnd(StringBuilder out) {
		out.append("</div>");
	}
}
