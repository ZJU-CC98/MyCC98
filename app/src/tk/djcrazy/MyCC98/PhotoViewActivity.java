package tk.djcrazy.MyCC98;

import com.actionbarsherlock.app.ActionBar;
import com.google.inject.Inject;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.util.Intents;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.MyCC98.util.ViewUtils;
import tk.djcrazy.libCC98.ICC98Service;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

@ContentView(R.layout.activity_photo_view)
public class PhotoViewActivity extends BaseActivity {
	
	@InjectExtra(value=Intents.EXTRA_DOWNLOAD_LINK,optional=true)
	private String mPhotoUrl;
 	
	@InjectView(R.id.photo_load_progress)
	private ProgressBar mProgressBar;
	@InjectView(R.id.photo_view)
	private ImageView mImageView;
	PhotoViewAttacher mAttacher;
	@Inject
	private ICC98Service  service;
	
	public static Intent createIntent(String url) {
		Log.i("PhotoViewActivity", "Load url:"+url);
		Intent intent = new Intents.Builder("photo.VIEW").downloadLink(url).toIntent();
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		configureActionBar();
		new DownloadPhotoTask(this, mPhotoUrl).execute();
 	}
	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setLogo(new BitmapDrawable(service.getCurrentUserAvatar()));
		actionBar.setTitle("查看图片");
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
 		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class DownloadPhotoTask extends RoboAsyncTask<Bitmap> {
		private String aUrl;
		
		protected DownloadPhotoTask(Context context, String url) {
			super(context);
			aUrl = url;
		}

		@Override
		public Bitmap call() throws Exception {
			return service.getBitmapFromUrl(aUrl);
		}
		
		@Override
		protected void onSuccess(Bitmap t) throws Exception {
			super.onSuccess(t);
			ViewUtils.setGone(mProgressBar, true);
			if(Build.VERSION.SDK_INT >= 11){
 				changeRenderTypeIfNessary(t); 
	        }
			mImageView.setImageBitmap(t);
			ViewUtils.setGone(mImageView, false);
		    mAttacher = new PhotoViewAttacher(mImageView);
 		    mAttacher.setMaxScale(Math.max(t.getHeight(), t.getWidth())/100);
 		    mAttacher.setOnViewTapListener(new OnViewTapListener() {
				@Override
				public void onViewTap(View view, float x, float y) {
					if (getActionBar().isShowing()) {
						getActionBar().hide();
					} else {
						getActionBar().show();
					}
				}
			});
			getActionBar().hide();
		}

		/**
		 * OpenGL max bitmap supoorts 2048*2048, see http://stackoverflow.com/questions/10271020/bitmap-too-large-to-be-uploaded-into-a-texture
		 */
		@TargetApi(11)
		private void changeRenderTypeIfNessary(Bitmap t) {
			if (t.getHeight()>2048||t.getWidth()>2048) {
			    mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			    ToastUtils.show(PhotoViewActivity.this, "图片过大，关闭硬件加速");
			}
		}
		
		@Override
		protected void onException(Exception e) throws RuntimeException {
			super.onException(e);
			Log.e("DownloadPhotoTask", "DownloadPhotoTask failed", e);
			ToastUtils.show(PhotoViewActivity.this, "图片下载失败，请重试");
		}
	}
}
