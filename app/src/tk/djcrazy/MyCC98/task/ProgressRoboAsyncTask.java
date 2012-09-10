package tk.djcrazy.MyCC98.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import roboguice.util.RoboAsyncTask;

public abstract class ProgressRoboAsyncTask<ResultT> extends RoboAsyncTask<ResultT> {

	protected ProgressDialog dialog;
	protected Activity context;
	
	protected ProgressRoboAsyncTask(Activity context) {
		super(context);
		this.context = context;
		dialog = new ProgressDialog(context);
	}
	@Override
	protected void onPreExecute() throws Exception {
		dialog.show();
		super.onPreExecute();
	}
	@Override
	protected void onFinally() throws RuntimeException {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		super.onFinally();
	}

}
