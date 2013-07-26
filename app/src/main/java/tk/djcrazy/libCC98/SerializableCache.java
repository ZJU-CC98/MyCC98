package tk.djcrazy.libCC98;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;

import com.github.droidfu.cachefu.AbstractCache;
import com.github.droidfu.cachefu.CacheHelper;

public class SerializableCache extends AbstractCache<String, Serializable> {

	private static SerializableCache cache = null;
	private static final int CACHE_INIT_CAP = 64;
	private static final int CACHE_EXPIRATION_MINUTES = 7;
	private static final int MAX_CONCURRENT_THREADS = 4;

	public static SerializableCache getInstance(Context context) {
		if (cache == null) {
			synchronized (SerializableCache.class) {
				if (cache == null) {
					cache = new SerializableCache(CACHE_INIT_CAP,
							CACHE_EXPIRATION_MINUTES, MAX_CONCURRENT_THREADS);
					cache.enableDiskCache(context,
							AbstractCache.DISK_CACHE_INTERNAL);
				}
			}

		}
		return cache;
	}

	private SerializableCache(int initialCapacity, long expirationInMinutes,
			int maxConcurrentThreads) {
		super("SerializableCache", initialCapacity, expirationInMinutes,
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
	protected Serializable readValueFromDisk(File file) throws IOException {
		ObjectInputStream istream = new ObjectInputStream(
				new FileInputStream(file));
		Serializable serializable = null;
		try {
			serializable = (Serializable)istream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		istream.close();
		return serializable;
	}

	@Override
	protected void writeValueToDisk(File file, Serializable value) throws IOException {
		ObjectOutputStream outputStream = new ObjectOutputStream(
				new FileOutputStream(file));
		outputStream.writeObject(value);
		outputStream.close();

	}
}
