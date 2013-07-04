package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.data.PmInfo;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PmListViewAdapter extends BaseItemListAdapter<PmInfo> {

	public PmListViewAdapter(Activity context, List<PmInfo> list) {
		super(context, list);
	}

	public final class ListItemView {
 		public TextView senderName;
		public TextView topic;
		public TextView time;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ListItemView itemView = null;
		if (convertView == null) {
			itemView = new ListItemView();
			convertView = inflater.inflate(R.layout.pm_item, null);
			findViews(convertView, itemView);
			convertView.setTag(itemView);
		} else {
			itemView = (ListItemView) convertView.getTag();
		}

		fillDataIntoView(position, itemView);
  		return convertView;
	}

 	/**
	 * find views in the pm list item view
	 * 
	 * @param convertView
	 * @param itemView
	 */
	private void findViews(View convertView, ListItemView itemView) {
 		itemView.senderName = (TextView) convertView
				.findViewById(R.id.pm_sender_name);
		itemView.topic = (TextView) convertView.findViewById(R.id.pm_topic);
		itemView.time = (TextView) convertView.findViewById(R.id.pm_time);
	}

	/**
	 * 
	 * @param position
	 * @param itemView
	 */
	private void fillDataIntoView(int position, ListItemView itemView) {
		// fill data into itemView
		itemView.senderName.setText(StringEscapeUtils.unescapeHtml4(items.get(
				position).getSender()));
		itemView.topic.setText(StringEscapeUtils.unescapeHtml4(items.get(
				position).getTopic()));
		itemView.time.setText(items.get(position).getSendTime());
		if (items.get(position).isNew()) {
			itemView.topic.setTextColor(context.getResources().getColorStateList(R.color.pm_title_unread));
		} else {
			itemView.topic.setTextColor(context.getResources().getColorStateList(R.color.post_text));
		}
	}
}
