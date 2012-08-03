package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import com.google.inject.Inject;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.CC98ClientImpl;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.PostEntity;
import tk.djcrazy.libCC98.data.PostType;
import tk.djcrazy.libCC98.util.DateFormatUtil;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostListViewAdapter extends BaseAdapter {

	private Activity context;

	private Bundle bundle = new Bundle();

	private Intent intent = new Intent();

	private final List<PostEntity> listItem;

	private LayoutInflater listInflater;

	private Bitmap userImage;
	
	@Inject
	private ICC98Service service;
	/**
	 * @param userImage the userImage to set
	 */
	public void setUserImage(Bitmap userImage) {
		this.userImage = userImage;
	}

	public final class ListItemView {

		public ImageView postTypeImageView;

		public TextView postName;

		public TextView postAuthor;

		public TextView lastReplyAuthor;

		public TextView lastReplyTime;

		public View postLastReplyClickable;

	}

	public PostListViewAdapter(Activity context, List<PostEntity> postList) {
		this.context = context;
		listInflater = LayoutInflater.from(context);
		this.listItem = postList;
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

		final int clickPosition = position;
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取布局文件视图
			convertView = listInflater.inflate(R.layout.post_list_item, null);
			getPostListView(convertView, listItemView);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		listItemView.postName.setText((CharSequence) listItem.get(position)
				.getPostName());
		listItemView.postAuthor.setText((CharSequence) listItem.get(position)
				.getPostAuthorName());
		listItemView.lastReplyAuthor.setText((CharSequence) listItem.get(
				position).getLastReplyAuthor());
		listItemView.lastReplyTime.setText(DateFormatUtil.convertDateToString(listItem
				.get(position).getLastReplyTime(), true));

		// controlPageNumberListView(pageNumber, listItemView);

		setListItemViewListener(clickPosition, listItemView);

		String postType = listItem.get(position).getPostType();
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

	/**
	 * @param clickPosition
	 * @param listItemView
	 */
	private void setListItemViewListener(final int clickPosition,
			final ListItemView listItemView) {
		listItemView.postName.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String postLink = service.getDomain()
						+ listItem.get(clickPosition).getPostLink();

				intent.setClass(context, PostContentsJSActivity.class);
				bundle.putString(PostContentsJSActivity.POST_LINK, postLink);
				bundle.putString(PostContentsJSActivity.POST_NAME, listItem.get(clickPosition).getPostName());
				bundle.putInt(PostContentsJSActivity.PAGE_NUMBER, 1);
				bundle.putParcelable(PostContentsJSActivity.USER_IMAGE, userImage);
				intent.putExtra(PostContentsJSActivity.POST, bundle);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.forward_activity_move_in, R.anim.forward_activity_move_out);
			}
		});

		listItemView.postLastReplyClickable
				.setOnClickListener(new View.OnClickListener() {
 
					@Override
					public void onClick(View v) {

						String postLink = service.getDomain()
								+ listItem.get(clickPosition).getPostLink();

						intent.setClass(context, PostContentsJSActivity.class);
						bundle.putString(PostContentsJSActivity.POST_LINK,
								postLink);
						bundle.putString(PostContentsJSActivity.POST_NAME,
								listItem.get(clickPosition).getPostName());
						bundle.putInt(PostContentsJSActivity.PAGE_NUMBER,
								listItem.get(clickPosition).getPostPageNumber());
						bundle.putParcelable(PostContentsJSActivity.USER_IMAGE, userImage);
						intent.putExtra(PostContentsJSActivity.POST, bundle);
						context.startActivity(intent);
						context.overridePendingTransition(R.anim.forward_activity_move_in, R.anim.forward_activity_move_out);

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
