package tk.djcrazy.MyCC98.task;

import roboguice.util.RoboAsyncTask;
import android.app.Activity;
import android.app.ProgressDialog;

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
	protected void onException(Exception e) throws RuntimeException {
		dialog.dismiss();
	}
	@Override
	protected void onSuccess(ResultT t) throws Exception {
		dialog.dismiss();
	};
	@Override
	protected void onFinally() throws RuntimeException {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		super.onFinally();
	}

}
