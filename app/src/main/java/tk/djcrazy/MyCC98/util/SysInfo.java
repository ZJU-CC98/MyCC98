package tk.djcrazy.MyCC98.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.Formatter;


/**
 * Created by zsy on 8/14/13.
 */
public class SysInfo {
    public static String getSystemSummary(Context context, String username, String userProfileURL) {
        StringBuilder builder = new StringBuilder("System Info");
        builder.append("\n-------------------------");
        Formatter formatter = new Formatter(builder);
        formatter.format("\n* App Version: %d", getVersionCode(context));
        formatter.format("\n* OS Version: %s (%s)", System.getProperty("os.version"), Build.VERSION.INCREMENTAL);
        formatter.format("\n* OS API Level: %d", Build.VERSION.SDK_INT);
        formatter.format("\n* Device: %s", Build.DEVICE);
        formatter.format("\n* Model: %s (%s)", Build.MODEL, Build.PRODUCT);
        formatter.format("\n* Reporter: [%s](%s)", username, userProfileURL);
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
