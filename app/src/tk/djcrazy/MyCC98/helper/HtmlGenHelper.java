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
	public static final String ITEM_OPEN = "<div class=\"post\"><div class=\"post-content-wrapper\">";
	public static final String ITEM_CLOSE = "</div>";

	public final String PAGE_OPEN = "<!DOCTYPE html><html>" // min-width-320px
															// min-width-480px
			+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"
			+ "<link rel=\"stylesheet\" href=\"file:///android_asset/custom.css\" />"
			+ "<link rel=\"stylesheet\" href=\"file:///android_asset/bootstrap.css\" />"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/mootools.core.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/tools.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/clientubb.js\"></script>"
			+ "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\" />"
			+ "</head><body>" + "<div  class=\"bg-wrapper\">";

	public final String PAGE_PROXY_OPEN = "<!DOCTYPE html><html>" // min-width-320px
			+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"
			+ "<link rel=\"stylesheet\" href=\"file:///android_asset/custom.css\" />"
			+ "<link rel=\"stylesheet\" href=\"file:///android_asset/bootstrap.css\" />"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/mootools.core.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/tools.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/clientubb_proxy.js\"></script>"
			+ "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\" />"
			+ "</head><body>" + "<div  class=\"bg-wrapper\">";
	
	public final String PAGE_RVPN_OPEN = "<!DOCTYPE html><html>" // min-width-320px
			+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"
			+ "<link rel=\"stylesheet\" href=\"file:///android_asset/custom.css\" />"
			+ "<link rel=\"stylesheet\" href=\"file:///android_asset/bootstrap.css\" />"
			+ "<script language=\"JavaScript\" src=\"file:///android_asset/svpn_websvc_functions.js\" charset=\"utf-8\" sf_script=\"1\"></script>"
			+ "<script type=\"text/vbscript\" src=\"file:///android_asset/svpn_websvc_functions.vbs\" charset=\"utf-8\" sf_script=\"1\"></script><script sf_script=\"1\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/mootools.core.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/tools.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/clientubb_rvpn.js\"></script>"
			+ "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\" />"
			+ "</head><body>" + "<div  class=\"bg-wrapper\">";

	public final String PAGE_CLOSE = "</div> </body></html>";

	private final String TAG = "HtmlGenHelper";

	public static void addPostInfo(StringBuilder out, String readTopic,
			String senderAvatarUrl, String sender, String gender, int floorNum,
			String sendTime, int index) {
		out.append("<div class=\"post-info\">");
		addTopic(out, readTopic);
		addSenderAvatar(out, senderAvatarUrl, index);
		addSenderName(out, sender);
		addFloorNum(out, floorNum);
		addGender(out, gender);
		addSendTime(out, sendTime);
		out.append("</div>");
	}

	public static void postInfo(StringBuilder out, String readTopic,
			String senderAvatarUrl, String sender, String gender, int floorNum,
			String sendTime, int index) {
		addPostInfo(out, readTopic, senderAvatarUrl, sender, gender, floorNum,
				sendTime, index);
	}

	private static void addFloorNum(StringBuilder out, int floorNum) {
		out.append("<div class=\"floor-num\">").append(floorNum)
				.append(" 楼</div>");
	}

	private static void addGender(StringBuilder out, String gender) {
		out.append("<img class=\"img-gender\" src=\"file:///android_asset/pic/")
				.append(gender).append(".gif\"/>");
	}

	public static void addSenderAvatar(StringBuilder out,
			String senderAvatarUrl, int index) {
		out.append("<img class=\"img-avatar\" src=\"").append(senderAvatarUrl)
				.append("\" height=64/>");
	}

	public static void addTopic(StringBuilder out, String readTopic) {
		out.append("<div class=\"topic\" ><big><strong>").append(readTopic)
				.append("</strong></big></div><br />");
	}

	public static void addSenderName(StringBuilder out, String sender) {
		out.append("<div class=\"name\">").append(sender)
				.append("</div><br />");
	}

	public static void addSendTime(StringBuilder out, String sendTime) {
		out.append("<div class=\"time\"><small>").append(sendTime)
				.append("</small></div><br />");
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

			pageLink = tmp.replaceAll(
					"(\\&star=\\d+.*)|(\\[url\\])|(\\[/url\\])", "");
			if (!pageLink.startsWith("http")) {
				if (pageLink.startsWith("/")) {
					pageLink = service.getDomain() + pageLink.substring(1);
				} else {
					pageLink = service.getDomain() + pageLink;
				}
			}
			matcher.appendReplacement(
					stringBuffer,
					"[noubb]<a style=\"color:blue;\" href=\"javascript:"
							+ jsInterface + ".open('" + pageLink + "',"
							+ pageNum + ");\">"
							+ tmp.replaceAll("(\\[url\\])|(\\[/url\\])", "")
							+ "</a>[/noubb]");
		}
		matcher.appendTail(stringBuffer);
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
