package tk.djcrazy.MyCC98.adapter;

import tk.djcrazy.MyCC98.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class EmotionGridViewAdapter extends BaseAdapter {

	LayoutInflater inflater;

	public EmotionGridViewAdapter(Activity context) {
		super();
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return emotion.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return emotion[position];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.emotion_view, null);
			imageView = (ImageView) convertView.findViewById(R.id.grid_image);
			convertView.setTag(imageView);
		} else {
			imageView = (ImageView) convertView.getTag();
		}
		imageView.setImageResource(emotion[position]);
		return convertView;
	}

 	private int[] emotion = { R.drawable.em00, R.drawable.em01,
			R.drawable.em02, R.drawable.em03, R.drawable.em04, R.drawable.em05,
			R.drawable.em06, R.drawable.em07, R.drawable.em08, R.drawable.em09,
			R.drawable.em10, R.drawable.em11, R.drawable.em12, R.drawable.em13,
			R.drawable.em14, R.drawable.em15, R.drawable.em16, R.drawable.em17,
			R.drawable.em18, R.drawable.em19, R.drawable.em20, R.drawable.em21,
			R.drawable.em22, R.drawable.em23, R.drawable.em24, R.drawable.em25,
			R.drawable.em26, R.drawable.em27, R.drawable.em28, R.drawable.em29,
			R.drawable.em30, R.drawable.em31, R.drawable.em32, R.drawable.em33,
			R.drawable.em34, R.drawable.em35, R.drawable.em36, R.drawable.em37,
			R.drawable.em38, R.drawable.em39, R.drawable.em40, R.drawable.em41,
			R.drawable.em42, R.drawable.em43, R.drawable.em44, R.drawable.em45,
			R.drawable.em46, R.drawable.em47, R.drawable.em48, R.drawable.em49,
			R.drawable.em50, R.drawable.em51, R.drawable.em52, R.drawable.em53,
			R.drawable.em54, R.drawable.em55, R.drawable.em56, R.drawable.em57,
			R.drawable.em58, R.drawable.em59, R.drawable.em60, R.drawable.em61,
			R.drawable.em62, R.drawable.em63, R.drawable.em64, R.drawable.em65,
			R.drawable.em66, R.drawable.em67, R.drawable.em68, R.drawable.em69,
			R.drawable.em70, R.drawable.em71, R.drawable.em72, R.drawable.em73,
			R.drawable.em74, R.drawable.em75, R.drawable.em76, R.drawable.em77,
			R.drawable.em78, R.drawable.em79, R.drawable.em80, R.drawable.em81,
			R.drawable.em82, R.drawable.em83, R.drawable.em84, R.drawable.em85,
			R.drawable.em86, R.drawable.em87, R.drawable.em88, R.drawable.em89,
			R.drawable.em90, R.drawable.em91 };
}
