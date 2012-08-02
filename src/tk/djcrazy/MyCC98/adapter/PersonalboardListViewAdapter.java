package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import com.google.inject.Inject;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.PostListActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.CC98ClientImpl;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.BoardEntity;
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

public class PersonalboardListViewAdapter extends BaseAdapter {

	private Activity context;
	private Bundle bundle = new Bundle();
	private Intent intent = new Intent();

	private List<BoardEntity> listItem;

	private LayoutInflater listInflater;
	private Bitmap userImage;
	
	@Inject
	private ICC98Service service;

	/**
	 * @return the userImage
	 */
	public Bitmap getUserImage() {
		return userImage;
	}

	/**
	 * @param userImage the userImage to set
	 */
	public void setUserImage(Bitmap userImage) {
		this.userImage = userImage;
	}


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

	public PersonalboardListViewAdapter(Activity context, List<BoardEntity> list) {
		this.context = context;
		listInflater = LayoutInflater.from(context);
		this.listItem = list;
	}

	@Override
	public int getCount() {
		return listItem.size();
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
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取布局文件视图
			convertView = listInflater.inflate(
					R.layout.personal_board_list_item_view, null);

			findViews(convertView, listItemView);
			convertView.setTag(listItemView);

		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		listItemView.boardName.setText(listItem.get(position).getBoardName());
		listItemView.lastReplyTopicName.setText(listItem.get(position)
				.getLastReplyTopicName());
		listItemView.lastReplyAuthor.setText(listItem.get(position)
				.getLastReplyAuthor());
		listItemView.lastReplyTime.setText(listItem.get(position)
				.getLastReplyTime());
		listItemView.postNumberToday.setText(""
				+ listItem.get(position).getPostNumberToday());

		setListeners(position, listItemView);
		return convertView;
	}
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	  if (observer != null) {
	    super.unregisterDataSetObserver(observer);
	  }
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
						String boardlink = service.getDomain()
								+ listItem.get(clickPosition).getBoardLink();

						intent.setClass(context, PostListActivity.class);
						bundle.putString(PostListActivity.BOARD_LINK, boardlink);
						bundle.putString(PostListActivity.BOARD_NAME, listItem
								.get(clickPosition).getBoardName());
						bundle.putInt(PostListActivity.PAGE_NUMBER, 1);
						bundle.putParcelable(PostListActivity.USER_IMAGE, userImage);
 						intent.putExtra(PostListActivity.BOARD_LIST, bundle);
						context.startActivity(intent);
						context.overridePendingTransition(R.anim.forward_activity_move_in, R.anim.forward_activity_move_out);

					}
				});

		listItemView.lastReplyNameClickable
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String postlink = service.getDomain()
								+ listItem.get(clickPosition)
										.getLastReplyTopicLink();

						intent.setClass(context, PostContentsJSActivity.class);
						bundle.putString(PostContentsJSActivity.POST_LINK,
								postlink);
						bundle.putString(PostContentsJSActivity.POST_NAME,
								listItem.get(clickPosition)
										.getLastReplyTopicName());
						bundle.putInt(PostContentsJSActivity.PAGE_NUMBER, 1);
						bundle.putParcelable(PostContentsJSActivity.USER_IMAGE, userImage);
						intent.putExtra(PostContentsJSActivity.POST, bundle);
						context.startActivity(intent);
						context.overridePendingTransition(R.anim.forward_activity_move_in, R.anim.forward_activity_move_out);
 					}
				});

		listItemView.lastReplyTimeClickable
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String postlink = service.getDomain()
								+ listItem.get(clickPosition)
										.getLastReplyTopicLink();

						intent.setClass(context, PostContentsJSActivity.class);
						bundle.putString(PostContentsJSActivity.POST_LINK,
								postlink);
						bundle.putString(PostContentsJSActivity.POST_NAME,
								listItem.get(clickPosition)
										.getLastReplyTopicName());
						bundle.putInt(PostContentsJSActivity.PAGE_NUMBER, 32767);
						bundle.putParcelable(PostContentsJSActivity.USER_IMAGE, userImage);
						intent.putExtra(PostContentsJSActivity.POST, bundle);
						context.startActivity(intent);
						context.overridePendingTransition(R.anim.forward_activity_move_in, R.anim.forward_activity_move_out);
					}
				});
	}
}
