package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import org.apache.http.ParseException;

import tk.djcrazy.MyCC98.EditActivity;
import tk.djcrazy.MyCC98.ProfileActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.data.UserStatue;
import tk.djcrazy.libCC98.data.UserStatueEntity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendListViewAdapter extends BaseItemListAdapter<UserStatueEntity> {

	public FriendListViewAdapter(Activity context, List<UserStatueEntity> list) {
		super(context, list);
	}
	private final class ListItemView {
		public ImageView avartar;
		public TextView userName;
		public TextView userState;
	}

  	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int clickPosition = position;
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取布局文件视图
			convertView = inflater.inflate(R.layout.friend_list_item, null);

			// 获取控件对象

			listItemView.avartar = (ImageView) convertView
					.findViewById(R.id.friend_list_item_avartar);
			listItemView.userName = (TextView) convertView
					.findViewById(R.id.friend_list_item_user_name);
			listItemView.userState = (TextView) convertView
					.findViewById(R.id.friend_list_item_user_state);

			convertView.setTag(listItemView);

		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		try {
			listItemView.avartar.setImageBitmap(items.get(position).getUserAvartar());
			listItemView.userName.setText(items.get(position).getUserName());
			if (items.get(position).getStatue() == UserStatue.ON_LINE) {
				listItemView.userState.setText("在线");
				listItemView.userState
						.setTextColor(Color.RED);
			} else {
				listItemView.userState.setText("离线");
				listItemView.userState
						.setTextColor(Color.GRAY);
			}
		} catch (ParseException e) {
			Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

		listItemView.avartar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ProfileActivity.class);
				intent.putExtra("userName", items.get(clickPosition)
						.getUserName());
 				context.startActivity(intent);
				context.overridePendingTransition(R.anim.forward_activity_move_in, R.anim.forward_activity_move_out);
			}
		});
		
		listItemView.userName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendPm(items.get(clickPosition).getUserName());
			} 
		});
		return convertView;
	}
	private void sendPm(String target) {
		Intent intent = new Intent(context, EditActivity.class);
 		intent.putExtra(EditActivity.MOD, EditActivity.MOD_PM);
		intent.putExtra(EditActivity.PM_TO_USER, target);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.forward_activity_move_in, R.anim.forward_activity_move_out);
	}
}
