package tk.djcrazy.MyCC98.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import tk.djcrazy.libCC98.NewCC98Service;

/**
 * Created by Ding on 13-8-15.
 */
public class ProgressDialogBuilder {

    public static ProgressDialog buildNew(final NewCC98Service service,final Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载...");
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                service.cancelRequest(context.getClass());
            }
        });
        return dialog;
    }
}
