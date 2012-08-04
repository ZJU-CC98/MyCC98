/**
 * 
 */
package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.BoardEntity;
import tk.djcrazy.libCC98.data.BoardStatus;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.inject.Inject;

/**
 * @author DJ
 * @author zsy
 */
public class SearchResultListAdapter extends BaseAdapter {

	private List<BoardStatus> mBoardList;
	private Activity context;
	private int dispWidth = 480;
	@Inject
	private ICC98Service service;

	public final class ListItemView {
		public TextView boardName;
		public TextView postNum;
		public ImageView postNumLine;
	}

	public SearchResultListAdapter(Activity context,
			List<BoardStatus> currentResult) {
		this.context = context;
		this.mBoardList = currentResult;
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		dispWidth = metric.widthPixels;
	}

	public void setBoardList(List<BoardStatus> list) {
		mBoardList = list;
	}
	@Override
	public int getCount() {
		if (mBoardList == null) {
			return 0;
		}
		return mBoardList.size();
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
		BoardStatus entity = mBoardList.get(position);
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.search_result_item, null);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		// 获取控件对象
		listItemView.boardName = (TextView) convertView
				.findViewById(R.id.search_result_board_name);
		listItemView.postNum = (TextView) convertView
				.findViewById(R.id.search_result_post_number_today);
		listItemView.postNumLine = (ImageView) convertView
				.findViewById(R.id.post_num_line);
		listItemView.boardName.setText(entity.getBoardName());
		listItemView.postNum
				.setText(String.valueOf(entity.getPostNumberToday()));
		listItemView.postNumLine.getLayoutParams().width = (int) (entity
				.getPostNumberPercentage() * dispWidth);
		/**
		 * 日帖数达到1000帖我们给予红色条，300-999给予黄色条，100-299给予绿色条，<100给予蓝色条，
		 * 条的长度代表发帖量占全站的发帖量比例
		 */
		if (entity.getPostNumberToday() > 999) {
			listItemView.postNumLine
					.setImageResource(R.drawable.status_line_red);
		} else if(entity.getPostNumberToday() > 299) {
			listItemView.postNumLine
			.setImageResource(R.drawable.status_line_yellow);
		} else if (entity.getPostNumberToday()>99) {
			listItemView.postNumLine
			.setImageResource(R.drawable.status_line_green);
		} else {
			listItemView.postNumLine
			.setImageResource(R.drawable.status_line_blue);
		}
		convertView.setTag(listItemView);
		return convertView;
	}
}
