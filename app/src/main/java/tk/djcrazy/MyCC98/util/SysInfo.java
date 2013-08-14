package tk.djcrazy.MyCC98.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import tk.djcrazy.MyCC98.HomeActivity;
import tk.djcrazy.MyCC98.application.MyApplication;

/**
 * Created by zsy on 8/14/13.
 */
public class SysInfo {
    public static String getSystemSummary(Context context) {
        StringBuilder builder = new StringBuilder("System Info");
        builder.append("\n-------------------------");
        builder.append("\n* App Version: ").append(getVersionCode(context));
        builder.append("\n* OS Version: ").append(System.getProperty("os.version")).append(" (").append(Build.VERSION.INCREMENTAL).append(")");
        builder.append("\n* OS API Level: ").append(Build.VERSION.SDK_INT);
        builder.append("\n* Device: ").append(Build.DEVICE);
        builder.append("\n* Model: ").append(Build.MODEL).append(" (").append(Build.PRODUCT).append(")");
        return builder.toString();
    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo packInfo =  context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return Integer.MAX_VALUE;
        }
    }
}
