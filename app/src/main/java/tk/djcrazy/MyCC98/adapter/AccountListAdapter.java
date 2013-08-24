/**
 * 
 */
package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.application.MyApplication.UsersInfo;
import tk.djcrazy.MyCC98.util.ViewUtils;
import tk.djcrazy.libCC98.data.LoginType;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author DJ
 * 
 */
public class AccountListAdapter extends BaseAdapter {

 	LayoutInflater mInflater;
 	private UsersInfo mUsersInfo;
	private List<Bitmap> mUserBitmaps;
	
	public final class ViewHolder {

		public ImageView image;
		public TextView text;
 		private TextView  useProxy;
	}

	public AccountListAdapter(Context context, UsersInfo usersInfo, List<Bitmap> list) {
 		mInflater = LayoutInflater.from(context);
 		mUsersInfo = usersInfo;
 		mUserBitmaps = list;
	}

	@Override
	public int getCount() {
		return mUserBitmaps.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder listItemView = null;
		if (convertView == null) {
			listItemView = new ViewHolder();
			convertView = mInflater.inflate(R.layout.account_list_item,
					null);
			listItemView.image = (ImageView) convertView
					.findViewById(R.id.action_item_image);
			listItemView.text = (TextView) convertView
					.findViewById(R.id.action_item_text);
 			listItemView.useProxy = (TextView) convertView.findViewById(R.id.use_proxy);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ViewHolder) convertView.getTag();
		}
 		listItemView.text.setText(mUsersInfo.users.get(position).getUserName());
		listItemView.image.setImageBitmap(mUserBitmaps.get(position)); 
		if (mUsersInfo.currentUserIndex==position) {
             convertView.setBackgroundResource(R.drawable.current_account_bg);
		} else {
             convertView.setBackgroundResource(R.color.transparent);
		}
		
		if (mUsersInfo.users.get(position).getLoginType()==LoginType.USER_DEFINED) {
			ViewUtils.setGone(listItemView.useProxy, false);
			listItemView.useProxy.setText("使用代理");
		} else if (mUsersInfo.users.get(position).getLoginType()==LoginType.RVPN) {
			ViewUtils.setGone(listItemView.useProxy, false);
			listItemView.useProxy.setText("使用RVPN");
		} else {
			ViewUtils.setGone(listItemView.useProxy, true);
		}
 		return convertView;
	}
}
