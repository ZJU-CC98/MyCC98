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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.inject.Inject;

public class PostListViewAdapter extends BaseAdapter {

	private Activity mContext;
	private String mBoardId;
	private String mBoardName;

	private final List<PostEntity> mListItem;

	@Inject
	private ICC98Service service;

	private final class ListItemView {

		public ImageView postTypeImageView;

		public TextView postName;

		public TextView postAuthor;

		public TextView lastReplyAuthor;

		public TextView lastReplyTime;

		public View postLastReplyClickable;

	}

	public PostListViewAdapter(Activity context, List<PostEntity> postList,
			String boardId, String boardName) {
		mContext = context;
		mListItem = postList;
		mBoardId = boardId;
		mBoardName = boardName;
	}

	@Override
	public int getCount() {

		return mListItem.size();
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

		final int clickPosition = position;
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取布局文件视图
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.post_list_item, null);
			getPostListView(convertView, listItemView);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		listItemView.postName.setText((CharSequence) mListItem.get(position)
				.getPostName());
		listItemView.postAuthor.setText((CharSequence) mListItem.get(position)
				.getPostAuthorName());
		listItemView.lastReplyAuthor.setText((CharSequence) mListItem.get(
				position).getLastReplyAuthor());
		listItemView.lastReplyTime.setText(DateFormatUtil.convertDateToString(
				mListItem.get(position).getLastReplyTime(), true));

		setListItemViewListener(clickPosition, listItemView);

		String postType = mListItem.get(position).getPostType();
		Log.d("postType", postType);
		if (postType.equals(PostType.Z_TOP)) {
			listItemView.postTypeImageView.setImageResource(R.drawable.ztop);
		} else if (postType.equals(PostType.TOP_B)) {
			listItemView.postTypeImageView.setImageResource(R.drawable.topb);
		} else if (postType.equals(PostType.TOP)) {
			listItemView.postTypeImageView.setImageResource(R.drawable.top);
		} else if (postType.equals(PostType.FOLDER)) {
			listItemView.postTypeImageView.setImageResource(R.drawable.folder);
		} else if (postType.equals(PostType.CLOSED_B)) {
			listItemView.postTypeImageView.setImageResource(R.drawable.closedb);
		} else if (postType.equals(PostType.FOLDER_RED)) {
			listItemView.postTypeImageView
					.setImageResource(R.drawable.folder_red);
		} else if (postType.equals(PostType.FOLDER_SAVE)) {
			listItemView.postTypeImageView
					.setImageResource(R.drawable.foldersave);
		} else if (postType.equals(PostType.IS_BEST)) {
			listItemView.postTypeImageView.setImageResource(R.drawable.isbest);
		} else if (postType.equals(PostType.LOCK_FOLDER)) {
			listItemView.postTypeImageView
					.setImageResource(R.drawable.lockfolder);
		} else if (postType.equals(PostType.HOT_FOLDER)) {
			listItemView.postTypeImageView
					.setImageResource(R.drawable.hotfolder);
		}
		return convertView;
	}

	private void setListItemViewListener(final int clickPosition,
			final ListItemView listItemView) {
		listItemView.postName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(mContext,
						PostContentsJSActivity.class);
				intent.putExtra(PostContentsJSActivity.BOARD_ID, mBoardId);
				intent.putExtra(PostContentsJSActivity.BOARD_NAME, mBoardName);
				intent.putExtra(PostContentsJSActivity.POST_ID,
						mListItem.get(clickPosition).getPostId());
				intent.putExtra(PostContentsJSActivity.POST_NAME, mListItem
						.get(clickPosition).getPostName());
				intent.putExtra(PostContentsJSActivity.PAGE_NUMBER, 1);
				mContext.startActivity(intent);
				mContext.overridePendingTransition(
						R.anim.forward_activity_move_in,
						R.anim.forward_activity_move_out);
			}
		});

		listItemView.postLastReplyClickable
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(mContext,
								PostContentsJSActivity.class);
						intent.putExtra(PostContentsJSActivity.BOARD_ID, mBoardId);
						intent.putExtra(PostContentsJSActivity.BOARD_NAME, mBoardName);
						intent.putExtra(PostContentsJSActivity.POST_ID,
								mListItem.get(clickPosition).getPostId());
						intent.putExtra(PostContentsJSActivity.POST_NAME, mListItem
								.get(clickPosition).getPostName());
						intent.putExtra(PostContentsJSActivity.PAGE_NUMBER, 32767);
						mContext.startActivity(intent);
						mContext.overridePendingTransition(
								R.anim.forward_activity_move_in,
								R.anim.forward_activity_move_out);
					}
				});
	}

	/**
	 * @param convertView
	 * @param listItemView
	 */
	private void getPostListView(View convertView, ListItemView listItemView) {
		listItemView.postTypeImageView = (ImageView) convertView
				.findViewById(R.id.post_type);
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
	}
}
