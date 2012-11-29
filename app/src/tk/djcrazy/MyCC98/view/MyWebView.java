package tk.djcrazy.MyCC98.view;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class MyWebView extends WebView {

	public MyWebView(Context context) {
		super(context);
 	}

    /**
     * Constructs a new WebView with layout parameters.
     *
     * @param context a Context object used to access application assets
     * @param attrs an AttributeSet passed to our parent
     */
    public MyWebView(Context context, AttributeSet attrs) {
    	super(context, attrs);
    }

     public MyWebView(Context context, AttributeSet attrs, int defStyle) {
    	 super(context, attrs, defStyle);
     }
     public MyWebView(Context context, AttributeSet attrs, int defStyle,
            boolean privateBrowsing) {
    	 super(context, attrs, defStyle, privateBrowsing);
     }

     @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
    	// TODO Auto-generated method stub
    	super.onScrollChanged(l, t, oldl, oldt);
    }
 
}
