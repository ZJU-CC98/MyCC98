/**
 * 
 */
package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import com.google.inject.Inject;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.CC98ClientImpl;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.HotTopicEntity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author DJ
 *
 */
public class HotTopicListAdapter extends BaseAdapter {
    
	Activity mContext ;
     
    @Inject
    private ICC98Service service;
     
    final List<HotTopicEntity> mListItem;
  
    public final class ListItemView {

        public TextView boardName;
        public TextView topicName;
        public TextView author;
        public TextView postTime;
     }

    public HotTopicListAdapter(Activity context, List<HotTopicEntity> topicList) {
        this.mContext = context;
        this.mListItem = topicList;
    }

    @Override
    public int getCount() {
        return mListItem.size();
    }
    
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
      if (observer != null) {
        super.unregisterDataSetObserver(observer);
      }
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.hot_topic_item, null);
            listItemView.boardName = (TextView) convertView.findViewById(R.id.hot_topic_board_name);
            listItemView.topicName = (TextView) convertView.findViewById(R.id.hot_topic_name);
            listItemView.author	   = (TextView) convertView.findViewById(R.id.hot_topic_author);
            listItemView.postTime  = (TextView) convertView.findViewById(R.id.hot_topic_time);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        listItemView.boardName.setText(mListItem.get(position).getBoardName());
        listItemView.topicName.setText(mListItem.get(position).getTopicName());
        listItemView.author.setText(mListItem.get(position).getPostAuthor());
        listItemView.postTime.setText(mListItem.get(position).getPostTime());
        return convertView;
    }
}
