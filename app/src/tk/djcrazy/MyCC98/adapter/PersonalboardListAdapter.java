package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import com.google.inject.Inject;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.PostListActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.util.ViewUtils;
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
					R.layout.personal_board_list_item, null);
			findViews(convertView, listItemView);
			convertView.setTag(listItemView);

		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		listItemView.boardName.setText(items.get(position).getBoardName());
		listItemView.postNumberToday.setText(String.valueOf(+items
				.get(position).getPostNumberToday()));
		if (items.get(position).getLastReplyTime() == null) {
			ViewUtils.setGone(listItemView.lastReplyAuthor, true);
			ViewUtils.setGone(listItemView.lastReplyTime, true);
			listItemView.lastReplyTopicName.setText("认证论坛，请认证用户进入浏览");
			setListeners(position, listItemView);
			listItemView.lastReplyTopicName.setClickable(false);
			listItemView.lastReplyTimeClickable.setClickable(false);
		} else {
			listItemView.lastReplyTopicName.setClickable(true);
			listItemView.lastReplyTimeClickable.setClickable(true);
			ViewUtils.setGone(listItemView.lastReplyAuthor, false);
			ViewUtils.setGone(listItemView.lastReplyTime, false);
			listItemView.lastReplyTopicName.setText(items.get(position)
					.getLastReplyTopicName());
			listItemView.lastReplyAuthor.setText(items.get(position)
					.getLastReplyAuthor());
			listItemView.lastReplyTime.setText(DateFormatUtil
					.convertDateToString(
							items.get(position).getLastReplyTime(), true));
			setListeners(position, listItemView);
		}
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
						context.startActivity(PostListActivity.createIntent(
								items.get(clickPosition).getBoardName(), items
										.get(clickPosition).getBoardID()));
					}
				});

		listItemView.lastReplyNameClickable
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						context.startActivity(PostContentsJSActivity
								.createIntent(items.get(clickPosition)
										.getBoardID(), items.get(clickPosition)
										.getLastReplyTopicID(), 1));
					}
				});

		listItemView.lastReplyTimeClickable
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						context.startActivity(PostContentsJSActivity
								.createIntent(items.get(clickPosition)
										.getBoardID(), items.get(clickPosition)
										.getLastReplyTopicID(), 32767));
					}
				});
	}
}
