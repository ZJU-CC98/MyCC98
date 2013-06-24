package tk.djcrazy.MyCC98.template;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import tk.djcrazy.MyCC98.application.MyApplication;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class PmContentFactory {
	static class Msg {
		String title;
		String avatar;
		String author;
		String time;
		String content;

		public Msg(String title, String avatar, String author, String time,
				String content) {
			this.title = title;
			this.avatar = avatar;
			this.author = author;
			this.time = time;
			this.content = content;
		}
	}

	private Msg msg = null;

	public PmContentFactory(String title, String avatar, String author,
			String time, String content) {
		msg = new Msg(title, avatar, author, time, content);
	}

	public String getConent() throws IOException {
		MustacheFactory mFactory = new DefaultMustacheFactory();
		Mustache mustache = mFactory
				.compile(
						new InputStreamReader(
								MyApplication
										.getAppContext()
										.getAssets()
										.open("template/default_pm_content.mustache")),
						"pm");
		StringWriter stringWriter = (StringWriter) mustache.execute(
				new StringWriter(), msg);
		stringWriter.flush();
		return stringWriter.toString();
	}
}
