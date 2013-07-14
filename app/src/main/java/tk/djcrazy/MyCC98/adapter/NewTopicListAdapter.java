/**
 * 
 */
package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.data.SearchResultEntity;
import tk.djcrazy.libCC98.util.DateFormatUtil;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author DJ
 * 
 */
public class NewTopicListAdapter extends
		BaseItemListAdapter<SearchResultEntity> {
	public NewTopicListAdapter(Activity context, List<SearchResultEntity> list) {
		super(context, list);
	}

	private static final String TAG = "NewTopicListAdapter";

	public final class ListItemView {
		public TextView topicName;
		public TextView author;
		public TextView postTime;
		public ImageView postType;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SearchResultEntity entity = items.get(position);
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.new_topic_item, null);
			listItemView.topicName = (TextView) convertView
					.findViewById(R.id.new_topic_name);
			listItemView.author = (TextView) convertView
					.findViewById(R.id.new_topic_author);
			listItemView.postTime = (TextView) convertView
					.findViewById(R.id.new_topic_time);
			listItemView.postType = (ImageView) convertView
					.findViewById(R.id.new_topic_post_type);
			convertView.setTag(listItemView);

		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		listItemView.topicName.setText(entity.getTitle());
		listItemView.author.setText(entity.getAuthorName());
		listItemView.postTime.setText(DateFormatUtil.convertDateToString(
				entity.getPostTime(), true));
		listItemView.postType.setImageResource(getPostTypeResource(entity
				.getFaceId()));
		return convertView;
	}

	private int getPostTypeResource(String num) {
		int i = Integer.parseInt(num);
		switch (i) {
		case 1:
			return R.drawable.face1;
		case 2:
			return R.drawable.face2;
		case 3:
			return R.drawable.face3;
		case 4:
			return R.drawable.face4;
		case 5:
			return R.drawable.face5;
		case 6:
			return R.drawable.face6;
		case 7:
			return R.drawable.face7;
		case 8:
			return R.drawable.face8;
		case 9:
			return R.drawable.face9;
		case 10:
			return R.drawable.face10;
		case 11:
			return R.drawable.face11;
		case 12:
			return R.drawable.face12;
		case 13:
			return R.drawable.face13;
		case 14:
			return R.drawable.face14;
		case 15:
			return R.drawable.face15;
		case 16:
			return R.drawable.face16;
		case 17:
			return R.drawable.face17;
		case 18:
			return R.drawable.face18;
		case 19:
			return R.drawable.face19;
		case 20:
			return R.drawable.face20;
		case 21:
			return R.drawable.face21;
		case 22:
			return R.drawable.face22;

		default:
			return R.drawable.face7;
		}
	}

}
