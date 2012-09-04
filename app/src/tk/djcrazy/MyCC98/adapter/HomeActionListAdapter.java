/**
 * 
 */
package tk.djcrazy.MyCC98.adapter;

import tk.djcrazy.MyCC98.R;
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
public class HomeActionListAdapter extends BaseAdapter {

	private Context mContext;
	private String mUserName;
	private Bitmap mUserAvatar;
	LayoutInflater mInflater;
	private String[] mItemName = {"设置", "个人信息","反馈","关于",};
	private int[]  mItemImage = {R.drawable.setting_btn,R.drawable.personal_profile_icon,R.drawable.feedback_icon,R.drawable.about_icon};

	public final class ViewHolder {

		public ImageView image;
		public TextView text;
	}

	public HomeActionListAdapter(Context context, String userName,
			Bitmap userAvatar) {
		this.mContext = context;
		mUserName = userName;
		mUserAvatar = userAvatar;	
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder listItemView = null;
		if (convertView == null) {
			listItemView = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.home_action_list_item, null);
			listItemView.image = (ImageView) convertView.findViewById(R.id.action_item_image);
			listItemView.text = (TextView) convertView.findViewById(R.id.action_item_text);
  			convertView.setTag(listItemView);
		} else {
			listItemView = (ViewHolder) convertView.getTag();
		}
		if (position==0) {
			listItemView.image.setImageBitmap(mUserAvatar);
			listItemView.text.setText(mUserName);
		} else {
			listItemView.text.setText(mItemName[position-1]);
			listItemView.image.setImageResource(mItemImage[position-1]);
		}
 		return convertView;
	}
}
