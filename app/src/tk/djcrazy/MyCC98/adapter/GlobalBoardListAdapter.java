package tk.djcrazy.MyCC98.adapter;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.HomeActionListAdapter.ViewHolder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GlobalBoardListAdapter extends BaseAdapter {
	private String[] boardNames ;
 	LayoutInflater inflater;
	
	
	public GlobalBoardListAdapter (Context context) {
 		boardNames = context.getResources().getStringArray(R.array.global_board_name);
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
 		return boardNames.length;
	}

	@Override
	public Object getItem(int position) {
 		return boardNames[position];
	}

	@Override
	public long getItemId(int position) {
 		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView;
 		if (convertView == null) {
 			convertView = inflater.inflate(R.layout.global_board_item,
					null);
			textView = (TextView) convertView
					.findViewById(R.id.global_board_name);
 			convertView.setTag(textView);
		} else {
			textView = (TextView) convertView.getTag();
		}
 		textView.setText(boardNames[position]);
 		return convertView;
	}

}
