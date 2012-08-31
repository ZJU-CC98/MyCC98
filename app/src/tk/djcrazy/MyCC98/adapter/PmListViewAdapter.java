package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import tk.djcrazy.MyCC98.PmViewActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.data.PmInfo;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class PmListViewAdapter extends BaseAdapter implements ListAdapter {

	private Activity context;
	private List<PmInfo> listItem;
	private LayoutInflater listInflater;

	public final class ListItemView {
		public View pmItem;
		public TextView senderName;
		public TextView topic;
		public TextView time;
	}

	public PmListViewAdapter(Activity context, List<PmInfo> listItem) {
		this.context = context;
		listInflater = LayoutInflater.from(context);
		this.listItem = listItem;
	}

	@Override
	public int getCount() {
		if (listItem == null)
			return 0;
		return listItem.size();
	}

	@Override
	public Object getItem(int position) {

		return listItem.get(position);
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ListItemView itemView = null;
		if (convertView == null) {
			itemView = new ListItemView();
			convertView = listInflater.inflate(R.layout.pm_item, null);

			findViews(convertView, itemView);

			convertView.setTag(itemView);
		} else {
			itemView = (ListItemView) convertView.getTag();
		}

		fillDataIntoView(position, itemView);
		final int fpos = position;
		itemView.pmItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				PmInfo pmInfo = listItem.get(fpos);
				reply_pm(pmInfo.getPmId(), pmInfo.getSender(),
						pmInfo.getSendTime(), pmInfo.getTopic());
			}
		});

		return convertView;
	}

	/**
	 * Reply to the pm with the pm id. Start the PmReply Activity.
	 * 
	 * @param pmId
	 */
	public void reply_pm(int pmId, String sender, String sendTime, String topic) {
		Intent intent = new Intent(context, PmViewActivity.class);
		intent.putExtra("PmId", pmId);
		intent.putExtra("Sender", sender);
		intent.putExtra("SendTime", sendTime);
		intent.putExtra("Topic", topic);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.forward_activity_move_in, R.anim.forward_activity_move_out);

	}

	/**
	 * find views in the pm list item view
	 * 
	 * @param convertView
	 * @param itemView
	 */
	private void findViews(View convertView, ListItemView itemView) {
		itemView.pmItem = convertView.findViewById(R.id.pm_item_view);
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
		itemView.senderName.setText((CharSequence) StringEscapeUtils
				.unescapeHtml4(listItem.get(position).getSender()));
		itemView.topic.setText((CharSequence) StringEscapeUtils
				.unescapeHtml4(listItem.get(position).getTopic()));
		itemView.time.setText((CharSequence) listItem.get(position)
				.getSendTime());
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

}
