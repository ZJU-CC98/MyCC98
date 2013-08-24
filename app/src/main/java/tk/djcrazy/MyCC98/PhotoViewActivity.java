package tk.djcrazy.MyCC98;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.ActionBar;
import com.google.inject.Inject;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.util.Intents;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.MyCC98.util.ViewUtils;
import tk.djcrazy.libCC98.NewCC98Service;
import tk.djcrazy.libCC98.util.RequestResultListener;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

@ContentView(R.layout.activity_photo_view)
public class
        PhotoViewActivity extends BaseActivity implements RequestResultListener<Bitmap> {

    @InjectExtra(value = Intents.EXTRA_DOWNLOAD_LINK, optional = true)
    private String mPhotoUrl;
    @InjectView(R.id.photo_load_progress)
    private ProgressBar mProgressBar;
    @InjectView(R.id.photo_view)
    private ImageView mImageView;
    @Inject
    private NewCC98Service service;

    public static Intent createIntent(String url) {
        Log.i("PhotoViewActivity", "Load url:" + url);
        Intent intent = new Intents.Builder("photo.VIEW").downloadLink(url).toIntent();
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureActionBar();
        service.submitBitmapRequest(this.getClass(),mPhotoUrl, this);
     }

    private void configureActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setLogo(new BitmapDrawable(getResources(), service.getCurrentUserAvatar()));
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

    @Override
    public void onRequestComplete(Bitmap result) {
        ViewUtils.setGone(mProgressBar, true);
        if (Build.VERSION.SDK_INT >= 11) {
            changeRenderTypeIfNessary(result);
        }
        mImageView.setImageBitmap(result);
        ViewUtils.setGone(mImageView, false);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setMaxScale(Math.max(result.getHeight(), result.getWidth()) / 100);
        mAttacher.setOnViewTapListener(new OnViewTapListener() {
            @SuppressLint("NewApi")
            @Override
            public void onViewTap(View view, float x, float y) {
                if (getActionBar().isShowing()) {
                    getActionBar().hide();
                } else {
                    getActionBar().show();
                }
            }
        });
        getSupportActionBar().hide();
    }


    @Override
    protected void onStop() {
        super.onStop();
        service.cancelRequest(this.getClass());
    }

    @Override
    public void onRequestError(String msg) {
        ToastUtils.alert(this, msg);
    }

    /**
     * OpenGL max bitmap supoorts 2048*2048, see http://stackoverflow.com/questions/10271020/bitmap-too-large-to-be-uploaded-into-a-texture
     */
    @TargetApi(11)
    private void changeRenderTypeIfNessary(Bitmap t) {
        if (t.getHeight() > 2048 || t.getWidth() > 2048) {
            mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            ToastUtils.info(PhotoViewActivity.this, "图片过大，关闭硬件加速");
        }
    }
}
