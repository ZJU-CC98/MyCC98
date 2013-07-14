package tk.djcrazy.MyCC98.template;

import java.util.List;

import tk.djcrazy.libCC98.data.PostContentEntity;
import android.content.Context;

public interface PostcontentTemplate {

	public String genContent(Context context, List<PostContentEntity> list, int page);

}