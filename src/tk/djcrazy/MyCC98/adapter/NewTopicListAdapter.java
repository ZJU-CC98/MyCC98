/**
 * 
 */
package tk.djcrazy.MyCC98.adapter;

import java.util.List;
import java.util.Map;

import tk.djcrazy.MyCC98.PostContentsJSActivity;
import tk.djcrazy.MyCC98.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author DJ
 *
 */
public class NewTopicListAdapter extends BaseAdapter {
    private static final String TAG = "NewTopicListAdapter";
	private Activity context ;
    private Intent intent = new Intent();
     
    private final List<Map<String, Object>> listItem;

    private LayoutInflater listInflater;
	private Bitmap userImage;

    public final class ListItemView {

 
    	public TextView topicName;

        public TextView author;

        public TextView postTime;

        public View postClickable;
        
        public ImageView postType;
    }

    public NewTopicListAdapter(Activity context, List<Map<String, Object>> listItem) {
        this.context = context;
        listInflater = LayoutInflater.from(context);
        this.listItem = listItem;
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
            convertView = listInflater.inflate(R.layout.new_topic_item, null);

            // 获取控件对象
            listItemView.topicName = (TextView) convertView.findViewById(R.id.new_topic_name);
            
            listItemView.author	   = (TextView) convertView.findViewById(R.id.new_topic_author);
            listItemView.postTime  = (TextView) convertView.findViewById(R.id.new_topic_time);
            listItemView.postClickable = convertView.findViewById(R.id.new_topic_clickable);
            listItemView.postType = (ImageView) convertView.findViewById(R.id.new_topic_post_type);
            convertView.setTag(listItemView);

        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
 
        if (listItem.get(position).get("postTitle")==null) {
			Log.d("NewTopicListAdapter", "null!!!");
		}
        listItemView.topicName.setText(listItem.get(position).get("postTitle").toString());
        listItemView.author.setText("作者："+listItem.get(position).get("author").toString());
        listItemView.postTime.setText(listItem.get(position).get("postTime").toString());
        listItemView.postType.setImageResource(getPostTypeResource(
        		listItem.get(position).get("postFace").toString()));
        // 注册相应版面时事件处理
        listItemView.postClickable.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intent.setClass(context, PostContentsJSActivity.class);
                Bundle jumpbBundle = new Bundle();
                jumpbBundle.putString(PostContentsJSActivity.POST_LINK, listItem.get(clickPosition).get("postLink").toString()+"&page=");
                jumpbBundle.putString(PostContentsJSActivity.POST_NAME, listItem.get(clickPosition).get("postTitle").toString());
                jumpbBundle.putInt(PostContentsJSActivity.PAGE_NUMBER, 1);
                jumpbBundle.putParcelable(PostContentsJSActivity.USER_IMAGE, userImage);
                intent.putExtra(PostContentsJSActivity.POST, jumpbBundle);
                context.startActivity(intent);
        		context.overridePendingTransition(R.anim.forward_activity_move_in, R.anim.forward_activity_move_out);

            }
        });

        return convertView;
    }
    private int getPostTypeResource(String num) {
    	int i = Integer.parseInt(num);
    	Log.d(TAG, num);
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

	public void setUserImage(Bitmap userImage) {
		this.userImage = userImage;
		
	}

}
