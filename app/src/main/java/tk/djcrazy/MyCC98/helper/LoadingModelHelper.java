package tk.djcrazy.MyCC98.helper;

import android.view.View;
import android.view.animation.AnimationUtils;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.util.ViewUtils;

/**
 * Created by Ding on 13-8-17.
 */
public class LoadingModelHelper {

     private View contentView;
    private View loadingView;
    private View emptyView;

    public LoadingModelHelper(View view, final OnReloadListener listener) {
         contentView = view.findViewById(R.id.loading_content);
        emptyView = view.findViewById(android.R.id.empty);
        loadingView = view.findViewById(R.id.pb_loading);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
                listener.onReload();
            }
        });
    }

    public LoadingModelHelper(View content, View empty, View loading, final OnReloadListener listener) {
        contentView = content;
        emptyView = empty;
        loadingView = loading;
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
                listener.onReload();
            }
        });
    }

    public void loading() {
            hide(contentView).hide(emptyView).show(loadingView, false);
    }

    public void content() {
        if (!contentView.isShown()) {
            hide(loadingView).hide(emptyView).show(contentView);
        }
    }
    public void content(boolean animate) {
        if (!contentView.isShown()) {
            hide(loadingView).hide(emptyView).show(contentView, false);
        }
    }

    public void empty(){
        if (!emptyView.isShown()) {
            hide(contentView).hide(loadingView).show(emptyView);
        }
    }

    private LoadingModelHelper hide(View view) {
        ViewUtils.setGone(view, true);
        return this;
    }
    private LoadingModelHelper show(View view) {
        ViewUtils.setGone(view, false);
        fadeIn(view);
        return this;
    }
    private LoadingModelHelper show(View view, boolean animate) {
        ViewUtils.setGone(view, false);
        if (animate){
            fadeIn(view);
        }
        return this;
    }

    private LoadingModelHelper fadeIn(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(contentView.getContext(),
                R.anim.activity_open_enter));
        return this;
    }

    public static interface OnReloadListener {
        public void onReload();
    }
}
