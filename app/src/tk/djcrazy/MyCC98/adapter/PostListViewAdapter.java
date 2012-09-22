package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.PostEntity;
import tk.djcrazy.libCC98.data.PostType;
import tk.djcrazy.libCC98.util.DateFormatUtil;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.inject.Inject;

public class PostListViewAdapter extends BaseItemListAdapter<PostEntity> {

 	private String mBoardId;
	private String mBoardName;
 
	private final class ListItemView {
		public TextView postName;
		public TextView postAuthor;
		public TextView lastReplyAuthor;
		public TextView lastReplyTime;
		public View postLastReplyClickable;
		public TextView replyNum;
	}

	public PostListViewAdapter(Activity context, List<PostEntity> postList,
			String boardId, String boardName) {
		super(context, postList);
 		mBoardId = boardId;
		mBoardName = boardName;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PostEntity entity = items.get(position);
		final int clickPosition = position;
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取布局文件视图
			convertView = inflater.inflate(
					R.layout.post_list_item, null);
			getPostListView(convertView, listItemView);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		listItemView.postName.setText(entity.getPostName());
		listItemView.postAuthor.setText(entity.getPostAuthorName());
		listItemView.lastReplyAuthor.setText(entity.getLastReplyAuthor());
		listItemView.lastReplyTime.setText(DateFormatUtil.convertDateToString(
				entity.getLastReplyTime(), true));
		listItemView.replyNum.setText(entity.getReplyNumber());
		setListItemViewListener(clickPosition, listItemView);

		String postType = items.get(position).getPostType();
		Log.d("postType", postType);
		// 给不同类型的帖子给予不同的颜色的标题
		if (postType.equals(PostType.Z_TOP)) {
			// 总固顶，红色
			listItemView.postName.setTextColor(Color.rgb(220, 20, 60));
		} else if (postType.equals(PostType.TOP_B)) {
			// 普通固顶，橙色
			listItemView.postName.setTextColor(Color.rgb(255, 140, 0));
		} else if (postType.equals(PostType.TOP)) {
			// 区固顶，暗橙色
			listItemView.postName.setTextColor(Color.rgb(222, 184, 135));
		} else if (postType.equals(PostType.FOLDER)) {
			// 普通贴， 
			listItemView.postName.setTextColor(Color.rgb(117, 137, 158));
		} else if (postType.equals(PostType.CLOSED_B)) {
			// 投票贴
			listItemView.postName.setTextColor(Color.rgb(100, 149, 237));
		} else if (postType.equals(PostType.FOLDER_RED)) {
			// 你发布的帖子
			listItemView.postName.setTextColor(Color.rgb(255, 105, 180));
		} else if (postType.equals(PostType.FOLDER_SAVE)) {
			// 保存贴
			listItemView.postName.setTextColor(Color.rgb(0, 0, 139));
		} else if (postType.equals(PostType.IS_BEST)) {
			// 精华帖
			listItemView.postName.setTextColor(Color.rgb(148, 20, 211));
		} else if (postType.equals(PostType.LOCK_FOLDER)) {
			// 锁定
			listItemView.postName.setTextColor(Color.rgb(128, 128, 128));
		} else if (postType.equals(PostType.HOT_FOLDER)) {
			// 热门贴，大红色
			listItemView.postName.setTextColor(Color.rgb(255, 0, 0));
		}
		return convertView;
	}

	private void setListItemViewListener(final int clickPosition,
			final ListItemView listItemView) {
		listItemView.postName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						PostContentsJSActivity.class);
				intent.putExtra(PostContentsJSActivity.BOARD_ID, items.get(clickPosition).getBoardId());
				intent.putExtra(PostContentsJSActivity.BOARD_NAME, mBoardName);
				intent.putExtra(PostContentsJSActivity.POST_ID,
						items.get(clickPosition).getPostId());
				intent.putExtra(PostContentsJSActivity.POST_NAME, items
						.get(clickPosition).getPostName());
				intent.putExtra(PostContentsJSActivity.PAGE_NUMBER, 1);
				context.startActivity(intent);
				 
			}
		});

		listItemView.postLastReplyClickable
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(context,
								PostContentsJSActivity.class);
						intent.putExtra(PostContentsJSActivity.BOARD_ID,
								mBoardId);
						intent.putExtra(PostContentsJSActivity.BOARD_NAME,
								mBoardName);
						intent.putExtra(PostContentsJSActivity.POST_ID,
								items.get(clickPosition).getPostId());
						intent.putExtra(PostContentsJSActivity.POST_NAME,
								items.get(clickPosition).getPostName());
						intent.putExtra(PostContentsJSActivity.PAGE_NUMBER,
								32767);
						context.startActivity(intent);
						 
					}
				});
	}

	/**
	 * @param convertView
	 * @param listItemView
	 */
	private void getPostListView(View convertView, ListItemView listItemView) {
		listItemView.postName = (TextView) convertView
				.findViewById(R.id.post_title);
		listItemView.postAuthor = (TextView) convertView
				.findViewById(R.id.post_author);
		listItemView.lastReplyAuthor = (TextView) convertView
				.findViewById(R.id.post_last_reply_author);
		listItemView.lastReplyTime = (TextView) convertView
				.findViewById(R.id.post_last_reply_time);
		listItemView.postLastReplyClickable = convertView
				.findViewById(R.id.post_last_reply_clickable);
		listItemView.replyNum = (TextView) convertView
				.findViewById(R.id.post_list_reply_num);
	}
}
