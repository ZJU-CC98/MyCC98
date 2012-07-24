package tk.djcrazy.MyCC98.view;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.PersonalboardListViewAdapter;
import tk.djcrazy.MyCC98.view.PullToRefreshListView.OnRefreshListener;
import tk.djcrazy.libCC98.CC98Parser;
import tk.djcrazy.libCC98.data.BoardEntity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 
 * @author DJ
 * @author zsy
 * 
 */

public class PersonalBoardView extends LinearLayout implements ChildView, OnRefreshListener{

	private static final String TAG = "PersonalBoardView";
	private List<BoardEntity> boardList;
	private PersonalboardListViewAdapter boardListViewAdapter;
	private PullToRefreshListView listView;
	private ProgressDialog dialog;
	private Bitmap userImage;
	private ParentView pv;
	private static final int GET_LIST_SUCCESS = 1;
	private static final int GET_LIST_FAILED = 0;
	private static final int SEND_FEEDBACK_SUCCESS = 10;
	private static final int SEND_FEEDBACK_FAILED = 11;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_LIST_SUCCESS:
				listView.setAdapter(boardListViewAdapter);
				boardListViewAdapter.notifyDataSetChanged();
				pv.refresh();
				listView.onRefreshComplete();
				listView.invalidate();
				// IMPORTANT! put the dismiss at last to force the refresh
				dialog.dismiss();
				break;
			case GET_LIST_FAILED:
				dialog.dismiss();
				Toast.makeText(getContext(), "网络或解析出错！", Toast.LENGTH_SHORT);
				break;
			case SEND_FEEDBACK_FAILED:
				Toast.makeText(getContext(), "无法连接到服务器，请稍候再试",
						Toast.LENGTH_SHORT).show();
				break;
			case SEND_FEEDBACK_SUCCESS:
				Toast.makeText(getContext(), "发送成功！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};

	public PersonalBoardView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.personal_board, this,
				true);
		listView = (PullToRefreshListView) findViewById(R.id.personal_board_list);
		listView.setOnRefreshListener(this);
		dialog = ProgressDialog.show(getContext(), "", "Loading...", true);
		dialog.setCancelable(false);

	}

	public void  setUserImage(Bitmap bitmap) {
		userImage = bitmap;
	}
	public void fetchContent(){
		new Thread() {
			// child thread
			@Override
			public void run() {
				try {
					boardList = CC98Parser.getPersonalBoardList();
					boardListViewAdapter = new PersonalboardListViewAdapter(getContext(), boardList);
					boardListViewAdapter.setUserImage(userImage);
					handler.sendEmptyMessage(GET_LIST_SUCCESS);
				} catch (ClientProtocolException e) {
					handler.sendEmptyMessage(GET_LIST_FAILED);
					e.printStackTrace();
				} catch (ParseException e) {
					handler.sendEmptyMessage(GET_LIST_FAILED);
					e.printStackTrace();
				} catch (IOException e) {
					handler.sendEmptyMessage(GET_LIST_FAILED);
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void setParentView(ParentView pv) {
		this.pv = pv;
	}
	public void scrollListTo(int x, int y) { 
		Log.d(VIEW_LOG_TAG, "listView.scrollTo(x, y);");
		listView.scrollTo(x, y);
 
	}

	@Override
	public void onSwitch() {
		if(boardList==null){
			dialog.show();
			fetchContent();
		}
		pv.setTitle("定制区");
	}

	@Override
	public void onRefresh() {
		fetchContent();
	}
}
