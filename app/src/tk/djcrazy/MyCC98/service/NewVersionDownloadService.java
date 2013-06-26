package tk.djcrazy.MyCC98.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import roboguice.service.RoboIntentService;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.util.Intents;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NewVersionDownloadService extends RoboIntentService {

	public NewVersionDownloadService() {
		super("Download");
	}

	public static Intent createIntent(Activity activity, String url) {
		return new Intents.Builder(activity, NewVersionDownloadService.class)
				.downloadLink(url).toIntent();
	}

	private static final int NODIFICATION_ID = 345738745;
	private File saveFile;
	NotificationCompat.Builder mBuilder;
 	private String link;
	NotificationManager mNotificationManager;

	private void createNotification() {
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(this).setSmallIcon(
				R.drawable.ic_launcher).setContentTitle("正在下载更新").setTicker("正在下载更新...");
 		// mId allows you to update the notification later on.
 		File storeDir = new File(Environment.getExternalStorageDirectory(), "MyCC98");
		if (!storeDir.exists()) {
			storeDir.mkdir();
		}
		saveFile = new File(storeDir, "MyCC98.apk");
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		link = arg0.getStringExtra(Intents.EXTRA_DOWNLOAD_LINK);
		createNotification();
		new DownloadTask(this).execute();
	}
	
	private class DownloadTask extends RoboAsyncTask<String> {

		protected DownloadTask(Context context) {
			super(context);
		}
		
		@Override
		public String call() throws Exception {
			return null;
		}
		
		
		@Override
		protected void onException(Exception e) throws RuntimeException {
			super.onException(e);
			mNotificationManager.cancel(NODIFICATION_ID);
		}
		
		@Override
		protected void onSuccess(String t) throws Exception {
 			super.onSuccess(t);
	 		Uri uri = Uri.fromFile(saveFile); 
			Intent intent = new Intent(Intent.ACTION_VIEW); 
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(uri, "application/vnd.android.package-archive"); 
			startActivity(intent);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext()).setSmallIcon(
					R.drawable.ic_launcher).setTicker("下载成功");
			mNotificationManager.notify(NODIFICATION_ID, builder.build());
			mNotificationManager.cancel(NODIFICATION_ID);
		} 
	}
}
