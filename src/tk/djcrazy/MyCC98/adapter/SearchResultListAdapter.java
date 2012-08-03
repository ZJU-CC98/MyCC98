/**
 * 
 */
package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import org.apache.http.NameValuePair;

import com.google.inject.Inject;

import tk.djcrazy.MyCC98.PostListActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.CC98ClientImpl;
import tk.djcrazy.libCC98.ICC98Service;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author DJ
 * @author zsy
 */
public class SearchResultListAdapter extends BaseAdapter {

	private final List<NameValuePair> mBoardList;
	private Activity context;

	private LayoutInflater listInflater;
 
	@Inject 
	private ICC98Service service;
	
	public final class ListItemView {
		public TextView boardName;
	}

	public SearchResultListAdapter(Activity context,
			List<NameValuePair> boardList) {
		this.context = context;
		listInflater = LayoutInflater.from(context);
		this.mBoardList = boardList;
	}

	@Override
	public int getCount() {
		if (mBoardList==null) {
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
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取布局文件视图
			convertView = listInflater.inflate(R.layout.search_result_item,
					null);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		// 获取控件对象
		listItemView.boardName = (TextView) convertView
				.findViewById(R.id.search_result_board_name);
		listItemView.boardName.setText(mBoardList.get(position).getName());
		convertView.setTag(listItemView);
		final int clkpos = position;
		listItemView.boardName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString(PostListActivity.BOARD_ID,
						service.getDomain()
								+ mBoardList.get(clkpos).getValue());
				bundle.putString(PostListActivity.BOARD_NAME, mBoardList
						.get(clkpos).getName());
				bundle.putInt(PostListActivity.PAGE_NUMBER, 1);

				Intent intent = new Intent(context, PostListActivity.class);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.forward_activity_move_in, R.anim.forward_activity_move_out);
			}
		});
		return convertView;
	}

}
