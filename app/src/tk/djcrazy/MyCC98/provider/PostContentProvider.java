package tk.djcrazy.MyCC98.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Get and reassemble the post pages, then put them in cache.
 * ALSO do prefetch works.
 * @author zsy
 *
 */
public class PostContentProvider {

	private Map<Integer, String> pageCache = new ConcurrentHashMap<Integer, String>();

	public PostContentProvider() {

	}

	public String getPage(int pageNum) {
		
		String content = null;
		
		if (pageCache.containsKey(pageNum)) {
			content = pageCache.get(pageNum);
		} else {
			content = doGetPage(pageNum);
		}
		
		return content;
	}

	private String doGetPage(int pageNum) {
		
		return null;
	}
}
