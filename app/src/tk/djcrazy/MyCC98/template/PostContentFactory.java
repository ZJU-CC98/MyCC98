package tk.djcrazy.MyCC98.template;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import tk.djcrazy.MyCC98.util.StreamUtils;
import tk.djcrazy.libCC98.data.PostContentEntity;
import tk.djcrazy.libCC98.util.DateFormatUtil;
import android.content.Context;
import android.util.Log;

public class PostContentFactory {

	List<Post> posts = new ArrayList<Post>();
	private Context context;
	private List<PostContentEntity> list;
	private int page;

	private static final String TAG = "PostContentMustache";
	static class Post {
		String title;
		String avatar;
		String author;
		String floor;
		String time;
		String gender;
		String face;
		String content;
		int index;

		Post(String title, String avatar, String author, String floor,
				String time, String gender, String face, String content,
				int index) {
			this.title = title;
			this.avatar = avatar;
			this.author = author;
			this.floor = floor;
			this.time = time;
			this.gender = gender;
			this.face = face;
			this.content = content;
			this.index = index;
		}
	}

	static class Script {
		String script;

		Script(String script) {
			this.script = script;
		}
	}

	static class Style {
		String stylesheet;

		Style(String stylesheet) {
			this.stylesheet = stylesheet;
		}
	}

	

	List<Script> scripts() {
		return Arrays.asList(new Script("file:///android_asset/clientubb.js"));
	}

	List<Style> styles() {
		return Arrays.asList(new Style("file:///android_asset/classical.css"));
	}

	public PostContentFactory(Context context, List<PostContentEntity> list,
			int page) {
		this.context = context;
		this.list = list;
		this.page = page;
	}

	public String genContent() {
		for (int i = 1; i < list.size(); ++i) {
			PostContentEntity entity = list.get(i);
			posts.add(new Post(entity.getPostTitle(), entity
					.getUserAvatarLink(), entity.getUserName(),
					((page - 1) * 10 + i) + " 楼",
					DateFormatUtil.convertDateToString(
							entity.getPostTime(), true), entity.getGender()
							.getName(), entity.getPostFace(), entity
							.getPostContent(), i));
		}
		try {
			MustacheFactory mFactory = new DefaultMustacheFactory();
			Mustache mustache = mFactory.compile(
					new InputStreamReader(context.getAssets().open(
							"template/base_post_content.mustache")),
					"posts");
			StringWriter stringWriter = (StringWriter) mustache.execute(
					new StringWriter(), this);
			stringWriter.flush();
			return stringWriter.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
