package tk.djcrazy.libCC98;

import java.io.IOException;

import tk.djcrazy.MyCC98.application.MyApplication;

import com.github.droidfu.cachefu.AbstractCache;

import android.R.string;
import android.content.Context;
import android.util.Log;

import ch.boye.httpclientandroidlib.client.cache.HttpCacheEntry;
import ch.boye.httpclientandroidlib.client.cache.HttpCacheStorage;
import ch.boye.httpclientandroidlib.client.cache.HttpCacheUpdateCallback;
import ch.boye.httpclientandroidlib.client.cache.HttpCacheUpdateException;

public class MultiLevelHttpCacheStorage implements HttpCacheStorage {
	private HttpCache httpCache = null;
	private final String TAG = "MultiLevelHttpCacheStorage";

	public MultiLevelHttpCacheStorage(int initialCapacity,
			long expirationInMinutes, int maxConcurrentThreads) {
		httpCache = new HttpCache(initialCapacity, expirationInMinutes,
				maxConcurrentThreads);
		httpCache.enableDiskCache(MyApplication.getAppContext(),
				AbstractCache.DISK_CACHE_INTERNAL);
	}

	@Override
	public HttpCacheEntry getEntry(String key) throws IOException {
		Log.d(TAG, "get " + key);
		return httpCache.get(key);
	}

	@Override
	public void putEntry(String key, HttpCacheEntry value) throws IOException {
		Log.d(TAG, "put " + key);
		httpCache.put(key, value);
	}

	@Override
	public void removeEntry(String key) throws IOException {
		Log.d(TAG, "remove " + key);
		httpCache.remove(key);
	}

	@Override
	public void updateEntry(String key, HttpCacheUpdateCallback updateCallback)
			throws IOException, HttpCacheUpdateException {
		Log.d(TAG, "update " + key);
		HttpCacheEntry oldeCacheEntry = httpCache.get(key);
		httpCache.put(key, updateCallback.update(oldeCacheEntry));
	}

}
