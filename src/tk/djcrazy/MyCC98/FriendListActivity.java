/**
 * 
 */
package tk.djcrazy.MyCC98;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.MyCC98.adapter.FriendListViewAdapter;
import tk.djcrazy.libCC98.CC98Client;
import tk.djcrazy.libCC98.CC98Parser;
import tk.djcrazy.libCC98.data.UserStatueEntity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author DJ
 *
 */
public class FriendListActivity extends Activity {
	public static final String ID = "FriendListActivity";
	private static final int LOAD_FRIEND_LIST_FAILURE = 0;
	private static final int LOAD_FRIEND_LIST_SUCCESS = 1;
	@SuppressWarnings("unused")
	private static final int LOAD_FRIEND_LIST_SUCCESS_REFREASH = 2;
	private FriendListViewAdapter mFriendListViewAdapter;
    private ProgressDialog dialog;
    private ListView mFriendList;
    private Handler mGetFriendListHandler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case LOAD_FRIEND_LIST_SUCCESS:
				dialog.dismiss();
				@SuppressWarnings("unchecked")
				List<UserStatueEntity> mUserStatueEntities = (List<UserStatueEntity>) msg.obj;
				mFriendListViewAdapter = new FriendListViewAdapter(getApplicationContext(), mUserStatueEntities);
				mFriendList.setAdapter(mFriendListViewAdapter);
    			Log.d("FriendListActivity", "mGetFriendListHandler");
				break;
			case LOAD_FRIEND_LIST_FAILURE:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "加载好友列表失败", Toast.LENGTH_SHORT);
				break;
			default:
				break;
			}
    	}
    };
    Thread getFriendListThread = new Thread() {
    	@Override
    	public void run() {
    		try {
    			Log.d("FriendListActivity", "getFriendListThread");	
    			List<UserStatueEntity> mUserStatueEntities = CC98Parser.getUserFriendList();
    			Message msg = new Message();

     			for (UserStatueEntity userStatueEntity : mUserStatueEntities) {
					userStatueEntity.setUserAvartar(CC98Client.getUserImg(userStatueEntity.getUserName()));
				}	
     			msg.what  = LOAD_FRIEND_LIST_SUCCESS;
				msg.obj = mUserStatueEntities;
				mGetFriendListHandler.sendMessage(msg);
 			} catch (ClientProtocolException e) {
				mGetFriendListHandler.sendEmptyMessage(LOAD_FRIEND_LIST_FAILURE);
				e.printStackTrace();
			} catch (ParseException e) {
				mGetFriendListHandler.sendEmptyMessage(LOAD_FRIEND_LIST_FAILURE);
				e.printStackTrace();
			} catch (IOException e) {
				mGetFriendListHandler.sendEmptyMessage(LOAD_FRIEND_LIST_FAILURE);
				e.printStackTrace();
			}
    	}
    };
	@Override
	public void onCreate(Bundle savedIntanceState) {
		super.onCreate(savedIntanceState);
		setContentView(R.layout.friend_list);
		setTitle("好友列表");
		findViews();
		dialog = ProgressDialog.show(this, "请稍等", "正在加载好友列表...");
		dialog.setCancelable(true);
		dialog.show();
		getFriendListThread.start();
	}
	private void findViews() {
		mFriendList = (ListView) findViewById(R.id.friend_list_view);
	}
}
