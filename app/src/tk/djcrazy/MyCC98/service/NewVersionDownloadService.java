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
				R.drawable.icon).setContentTitle("正在下载更新").setTicker("正在下载更新...");
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
			FileOutputStream fileOutputStream = null;
			try {
				HttpURLConnection connection = (HttpURLConnection) new URL(link)
						.openConnection();
				fileOutputStream = new FileOutputStream(saveFile);
				int length = Integer.parseInt(connection.getHeaderField("Content-Length"));
	 			mBuilder.setContentText("软件包大小："+(length/1024)+"KB");
				mBuilder.setProgress(length, 0, false);
				InputStream inputStream = connection.getInputStream();
				int temp = 0;
				int count = 0;
				byte[] data = new byte[10240];
				while ((temp = inputStream.read(data)) != -1) {
					fileOutputStream.write(data, 0, temp);
					count+=temp;
					mBuilder.setProgress(length, count, false);
					Notification notification = mBuilder.build();
					notification.flags = Notification.FLAG_NO_CLEAR;
					mNotificationManager.notify(NODIFICATION_ID, mBuilder.build());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
					R.drawable.icon).setTicker("下载成功");
			mNotificationManager.notify(NODIFICATION_ID, builder.build());
			mNotificationManager.cancel(NODIFICATION_ID);
		} 
	}
}
