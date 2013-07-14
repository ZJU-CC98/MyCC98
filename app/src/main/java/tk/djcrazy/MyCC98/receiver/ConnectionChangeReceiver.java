/**
 * 
 */
package tk.djcrazy.MyCC98.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * @author DJ
 * 
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		NetworkInfo mobNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (activeNetInfo != null) {
			Toast.makeText(context,
					"Active Network Type : " + activeNetInfo.getTypeName(),
					Toast.LENGTH_SHORT).show();
		}
		if (mobNetInfo != null) {
			Toast.makeText(context,
					"Mobile Network Type : " + mobNetInfo.getTypeName(),
					Toast.LENGTH_SHORT).show();
		}
	}

 }
