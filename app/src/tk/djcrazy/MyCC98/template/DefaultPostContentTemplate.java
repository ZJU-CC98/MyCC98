package tk.djcrazy.MyCC98.template;

import java.io.IOException;
import java.util.List;

import tk.djcrazy.MyCC98.util.StreamUtils;
import tk.djcrazy.libCC98.data.PostContentEntity;
import tk.djcrazy.libCC98.util.DateFormatUtil;
import android.content.Context;
import android.util.Log;

public class DefaultPostContentTemplate implements PostcontentTemplate {
	
	private static PostcontentTemplate template = new DefaultPostContentTemplate();
	private static String mContent;
	private static String start;
	private static String middle ;
	private static String last ;
	public static PostcontentTemplate getInstance() {
		return template;
	}
	
	@Override
	public String genContent(Context context, List<PostContentEntity> list ,int page) {
		prepareContent(context);
		StringBuilder builder= new StringBuilder();
		builder.append(start);
		for (int i = 1; i < list.size(); i++) {
			PostContentEntity entity = list.get(i);
			builder.append(middle);
 			replaceVariable("${title}", builder, entity.getPostTitle());
			replaceVariable("${avatar}", builder,  entity.getUserAvatarLink());
			replaceVariable("${author}", builder,  entity.getUserName());
			replaceVariable("${floor}", builder, ((page - 1) * 10 + i)+" 楼");
			replaceVariable("${gender}", builder, entity.getGender().getName());
			replaceVariable("${time}", builder,DateFormatUtil.convertDateToString(entity.getPostTime(), true));
			replaceVariable("${face}", builder, entity.getPostFace());
			replaceVariable("${content}", builder, entity.getPostContent());
			replaceVariable("${i}", builder,  String.valueOf(i));
			replaceVariable("${i}", builder,  String.valueOf(i));
			replaceVariable("${i}", builder,  String.valueOf(i));
			replaceVariable("${i}", builder,  String.valueOf(i));
		}
		builder.append(last);
		Log.d("DefaultPostContentTemplate", builder.toString());
		return builder.toString();
	}

 	private void replaceVariable(String var, StringBuilder builder, String newContent) {
		int offset = builder.indexOf(var);
		builder.replace(offset, offset+var.length(), newContent);
	}
	
 

	/**
	 * @param context
	 */
	private void prepareContent(Context context) {
		if (mContent==null) {
			try {
				mContent = StreamUtils.Stream2String(context.getAssets().open("template/default_post_content_template.html"));
				start = mContent.substring(0, mContent.indexOf("$foreach$"));
				middle = mContent.substring(mContent.indexOf("$foreach$")+9, mContent.indexOf("$endeach$"));
				last = mContent.substring(mContent.indexOf("$endeach$")+9);
			} catch (IOException e) {
				e.printStackTrace();
	 			throw new RuntimeException(e.getMessage());
			}
			
		}
	}
}
