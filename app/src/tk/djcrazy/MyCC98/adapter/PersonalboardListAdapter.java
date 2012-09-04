package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import com.google.inject.Inject;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.PostListActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.CC98ClientImpl;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.BoardEntity;
import tk.djcrazy.libCC98.util.DateFormatUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PersonalboardListAdapter extends BaseItemListAdapter<BoardEntity> {

	private final class ListItemView {
		public TextView boardName;
		public TextView lastReplyTopicName;
		public TextView lastReplyAuthor;
		public TextView lastReplyTime;
		public TextView postNumberToday;
		public View boardNameClickable;
		public View lastReplyNameClickable;
		public View lastReplyTimeClickable;
	}

	public PersonalboardListAdapter(Activity context, List<BoardEntity> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = inflater.inflate(
					R.layout.personal_board_list_item_view, null);
			findViews(convertView, listItemView);
			convertView.setTag(listItemView);

		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		listItemView.boardName.setText(items.get(position).getBoardName());
		listItemView.lastReplyTopicName.setText(items.get(position)
				.getLastReplyTopicName());
		listItemView.lastReplyAuthor.setText(items.get(position)
				.getLastReplyAuthor());
		listItemView.lastReplyTime.setText(DateFormatUtil.convertDateToString(
				items.get(position).getLastReplyTime(), true));
		listItemView.postNumberToday.setText(String.valueOf(+items
				.get(position).getPostNumberToday()));
		setListeners(position, listItemView);
		return convertView;
	}

	/**
	 * @param convertView
	 * @param listItemView
	 */
	private void findViews(View convertView, ListItemView listItemView) {
		// 获取控件对象
		listItemView.boardName = (TextView) convertView
				.findViewById(R.id.board_name);
		listItemView.lastReplyTopicName = (TextView) convertView
				.findViewById(R.id.last_reply_topic_name);
		listItemView.lastReplyAuthor = (TextView) convertView
				.findViewById(R.id.last_reply_author);
		listItemView.lastReplyTime = (TextView) convertView
				.findViewById(R.id.last_reply_time);
		listItemView.postNumberToday = (TextView) convertView
				.findViewById(R.id.topic_number_today);
		listItemView.boardNameClickable = convertView
				.findViewById(R.id.board_name);
		listItemView.lastReplyNameClickable = convertView
				.findViewById(R.id.last_reply_topic_name);
		listItemView.lastReplyTimeClickable = convertView
				.findViewById(R.id.last_reply_time_clickable);
	}

	/**
	 * @param clickPosition
	 * @param listItemView
	 */
	private void setListeners(final int clickPosition, ListItemView listItemView) {
		// 注册相应版面时事件处理
		listItemView.boardNameClickable
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,
								PostListActivity.class);
						intent.putExtra(PostListActivity.BOARD_ID,
								items.get(clickPosition).getBoardID());
						intent.putExtra(PostListActivity.BOARD_NAME,
								items.get(clickPosition).getBoardName());
						intent.putExtra(PostListActivity.PAGE_NUMBER, 1);
						context.startActivity(intent);
						context.overridePendingTransition(
								R.anim.forward_activity_move_in,
								R.anim.forward_activity_move_out);
					}
				});

		listItemView.lastReplyNameClickable
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,
								PostContentsJSActivity.class);
						intent.putExtra(PostContentsJSActivity.POST_ID, items
								.get(clickPosition).getLastReplyTopicID());
						intent.putExtra(PostContentsJSActivity.POST_NAME, items
								.get(clickPosition).getLastReplyTopicName());
						intent.putExtra(PostContentsJSActivity.PAGE_NUMBER, 1);
						intent.putExtra(PostContentsJSActivity.BOARD_ID, items
								.get(clickPosition).getBoardID());
						intent.putExtra(PostContentsJSActivity.BOARD_NAME,
								items.get(clickPosition).getBoardName());
						context.startActivity(intent);
						context.overridePendingTransition(
								R.anim.forward_activity_move_in,
								R.anim.forward_activity_move_out);
					}
				});

		listItemView.lastReplyTimeClickable
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,
								PostContentsJSActivity.class);
						intent.putExtra(PostContentsJSActivity.POST_ID, items
								.get(clickPosition).getLastReplyTopicID());
						intent.putExtra(PostContentsJSActivity.POST_NAME, items
								.get(clickPosition).getLastReplyTopicName());
						intent.putExtra(PostContentsJSActivity.PAGE_NUMBER,
								32767);
						intent.putExtra(PostContentsJSActivity.BOARD_ID, items
								.get(clickPosition).getBoardID());
						intent.putExtra(PostContentsJSActivity.BOARD_NAME,
								items.get(clickPosition).getBoardName());
						context.startActivity(intent);
						context.overridePendingTransition(
								R.anim.forward_activity_move_in,
								R.anim.forward_activity_move_out);
					}
				});
	}
}
