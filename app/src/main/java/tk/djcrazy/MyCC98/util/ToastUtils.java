package tk.djcrazy.MyCC98.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


/**
 * Utilities for displaying toast notifications
 */
public class ToastUtils {


    public static void info(Activity activity, String msg) {
        Crouton.makeText(activity,msg, Style.INFO).show();
    }

    public static void alert(Activity activity, String msg) {
        Crouton.makeText(activity,msg, Style.ALERT).show();
    }

    public static void confirm(Activity activity, String msg) {
        Crouton.makeText(activity,msg, Style.CONFIRM).show();
    }

    public static void info(Activity activity, int msgResId) {
        Crouton.makeText(activity, msgResId, Style.INFO).show();
    }

    public static void alert(Activity activity, int msgResId) {
        Crouton.makeText(activity, msgResId, Style.ALERT).show();
    }

    public static void confirm(Activity activity, int msgResId) {
        Crouton.makeText(activity, msgResId, Style.CONFIRM).show();
    }

    public static void info(Activity activity, int msgResId, ViewGroup customView) {
        Crouton.makeText(activity, msgResId, Style.INFO, customView).show();
    }

    public static void alert(Activity activity, int msgResId, ViewGroup customView) {
        Crouton.makeText(activity, msgResId, Style.ALERT, customView).show();
    }

    public static void confirm(Activity activity, int msgResId, ViewGroup customView) {
        Crouton.makeText(activity, msgResId, Style.CONFIRM, customView).show();
    }
    public static void info(Activity activity, String msg, ViewGroup customView) {
        Crouton.makeText(activity,msg, Style.INFO, customView).show();
    }

    public static void alert(Activity activity, String msg, ViewGroup customView) {
        Crouton.makeText(activity,msg, Style.ALERT, customView).show();
    }

    public static void confirm(Activity activity, String msg, ViewGroup customView) {
        Crouton.makeText(activity,msg, Style.CONFIRM, customView).show();
    }

}
