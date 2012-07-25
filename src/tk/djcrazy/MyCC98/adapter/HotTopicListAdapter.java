/**
 * 
 */
package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.libCC98.CC98Client;
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
    
	Activity context ;
    Bundle bundle = new Bundle();
    Intent intent = new Intent();
     
    final List<HotTopicEntity> listItem;

    LayoutInflater listInflater;
	private Bitmap userImage;

    public final class ListItemView {

        public TextView boardName;

        public TextView topicName;

        public TextView author;

        public TextView postTime;

        public View postClickable;
    }

    public HotTopicListAdapter(Activity context, List<HotTopicEntity> topicList) {
        this.context = context;
        listInflater = LayoutInflater.from(context);
        this.listItem = topicList;
    }

    @Override
    public int getCount() {

        return listItem.size();
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

        final int clickPosition = position;
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            // 获取布局文件视图
            convertView = listInflater.inflate(R.layout.hot_topic_item, null);

            // 获取控件对象
            listItemView.boardName = (TextView) convertView.findViewById(R.id.hot_topic_board_name);
            listItemView.topicName = (TextView) convertView.findViewById(R.id.hot_topic_name);
            
            listItemView.author	   = (TextView) convertView.findViewById(R.id.hot_topic_author);
            listItemView.postTime  = (TextView) convertView.findViewById(R.id.hot_topic_time);
            listItemView.postClickable = convertView.findViewById(R.id.hot_topic_clickable);
            
            convertView.setTag(listItemView);

        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        listItemView.boardName.setText(""+listItem.get(position).getBoardName());
        listItemView.topicName.setText(listItem.get(position).getTopicName());
        listItemView.author.setText("By: "+listItem.get(position).getPostAuthor());
        listItemView.postTime.setText(listItem.get(position).getPostTime());
        
        // 注册相应版面时事件处理
        listItemView.postClickable.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                 
                intent.setClass(context, PostContentsJSActivity.class);
                Bundle jumpbBundle = new Bundle();
                jumpbBundle.putString(PostContentsJSActivity.POST_LINK, CC98Client.getCC98Domain() + listItem.get(clickPosition).getPostLink()+"&page=");
                jumpbBundle.putString(PostContentsJSActivity.POST_NAME, listItem.get(clickPosition).getTopicName());
                jumpbBundle.putInt(PostContentsJSActivity.PAGE_NUMBER, 1);
                jumpbBundle.putParcelable(PostContentsJSActivity.USER_IMAGE, userImage);
                intent.putExtra(PostContentsJSActivity.POST, jumpbBundle);
                context.startActivity(intent);
        		context.overridePendingTransition(R.anim.forward_activity_move_in, R.anim.forward_activity_move_out);

            }
        });

        return convertView;
    }

	public void setUserImage(Bitmap userImage) {
		this.userImage = userImage;
	}


}
