package tk.djcrazy.MyCC98.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

import tk.djcrazy.MyCC98.application.MyApplication;
import android.os.Environment;
import android.util.Log;

public class ThemeHelper {
	private static final String CSS_PATH = "css";
	private static final String POST_TEMPLATE = "post.mustache";
	private static final String TAG = "ThemeHelper";

	public static String getThemeRootPath() {
		String state = Environment.getExternalStorageState();
		String ret = "";
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			ret = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/Android/data/"
					+ MyApplication.getAppContext().getPackageName()
					+ "/files/themes";
		}
		return ret;
	}

	public static String getThemePath(String themeName) {
		String themeRootPath = getThemeRootPath();
		return themeRootPath + '/' + themeName;
	}

	public static String[] getStyleSheets(String themeName) {
		String cssPath = getThemePath(themeName) + '/' + CSS_PATH;
		File cssDir = new File(cssPath);
		if (cssDir.exists() && cssDir.isDirectory()) {
			String[] styles = cssDir.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					return filename.toLowerCase().endsWith(".css");
				}
			});
			for (int i = 0; i < styles.length; ++i) {
				styles[i] = "file://" + cssPath + '/' + styles[i];
			}
			return styles;
		}
		return null;
	}

	public static String getPostTemplate(String themeName) {
		return getThemePath(themeName) + '/' + POST_TEMPLATE;
	}

	public static InputStream getTemplateStream(String themeName)
			throws FileNotFoundException {
		File template = new File(getPostTemplate(themeName));
		if (template.exists()) {
			return new FileInputStream(template);
		}
		return null;
	}
}
