package tk.djcrazy.libCC98;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ch.boye.httpclientandroidlib.client.cache.HttpCacheEntry;

import com.github.droidfu.cachefu.AbstractCache;
import com.github.droidfu.cachefu.CacheHelper;

public class HttpCache extends AbstractCache<String, HttpCacheEntry> {

	public HttpCache(int initialCapacity, long expirationInMinutes,
			int maxConcurrentThreads) {
		super("HttpCache", initialCapacity, expirationInMinutes,
				maxConcurrentThreads);
	}
	
	public synchronized void removeAllWithPrefix(String urlPrefix) {
        CacheHelper.removeAllWithStringPrefix(this, urlPrefix);
    }

	@Override
	public String getFileNameForKey(String key) {
		return CacheHelper.getFileNameFromUrl(key);
	}

	@Override
	protected HttpCacheEntry readValueFromDisk(File file) throws IOException {
		ObjectInputStream iStream = new ObjectInputStream(new FileInputStream(
				file));
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			iStream.close();
			throw new IOException("Cannot read files larger than "
					+ Integer.MAX_VALUE + " bytes");
		}
		HttpCacheEntry httpCacheEntry = null;
		try {
			httpCacheEntry = (HttpCacheEntry) iStream.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iStream.close();
		return httpCacheEntry;
	}

	@Override
	protected void writeValueToDisk(File file, HttpCacheEntry value)
			throws IOException {
		ObjectOutputStream outputStream = new ObjectOutputStream(
				new FileOutputStream(file));
		outputStream.writeObject(value);
		outputStream.close();

	}
}
