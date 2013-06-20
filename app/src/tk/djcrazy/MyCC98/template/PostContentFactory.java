package tk.djcrazy.MyCC98.template;

import java.io.IOException;
import java.io.InputStream;
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

	public static final String DEFAULT_UBB_SCRIPT = "file:///android_asset/clientubb.js";
	public static final String LIFETOY_UBB_SCRIPT = "file:///android_asset/clientubb_proxy.js";
	public static final String DEFAULT_TEMPLATE = "template/default_post_content.mustache";
	public static final String SIMPLE_TEMPLATE = "template/simple_post_content.mustache";
	public static final String CLASSICAL_STYLE = "file:///android_asset/classical.css";

	List<Post> posts = new ArrayList<Post>();
	List<Script> scripts = new ArrayList<Script>();
	List<Style> styles = new ArrayList<Style>();
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

	public void addScript(String scriptPath) {
		scripts.add(new Script(scriptPath));
	}

	public void addStyle(String stylePath) {
		styles.add(new Style(stylePath));
	}

	public PostContentFactory(List<PostContentEntity> list, int page) {
		this.list = list;
		this.page = page;
	}

	public String genContent(InputStream templateIn) {
		for (int i = 1; i < list.size(); ++i) {
			PostContentEntity entity = list.get(i);
			posts.add(new Post(entity.getPostTitle(), entity
					.getUserAvatarLink(), entity.getUserName(),
					((page - 1) * 10 + i) + " 楼", DateFormatUtil
							.convertDateToString(entity.getPostTime(), true),
					entity.getGender().getName(), entity.getPostFace(), entity
							.getPostContent(), i));
		}
		MustacheFactory mFactory = new DefaultMustacheFactory();
		Mustache mustache = mFactory.compile(new InputStreamReader(templateIn),
				"posts");
		StringWriter stringWriter = (StringWriter) mustache.execute(
				new StringWriter(), this);
		stringWriter.flush();
		return stringWriter.toString();
	}
}
