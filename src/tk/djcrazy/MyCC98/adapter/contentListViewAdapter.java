package tk.djcrazy.MyCC98.adapter;

import java.util.List;
import java.util.Map;

import tk.djcrazy.MyCC98.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class contentListViewAdapter extends BaseAdapter {
    Context context;
    List<Map<String, Object>> listItem;
    LayoutInflater listInflater;

    public final class ListItemView {
        public ImageView image; 
        public TextView replyID;
        public TextView reply_time;
        public TextView reply_content;               
    }

    public contentListViewAdapter(Context context,
            List<Map<String, Object>> listItem) {
        this.context = context;
        listInflater = LayoutInflater.from(context);
        this.listItem = listItem;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
     
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            // 获取布局文件视图
            convertView = listInflater.inflate( 
                    R.layout.post_content_list_item_view, null);

            // 获取控件对象

            listItemView.image = (ImageView) convertView
                    .findViewById(R.id.user_image);
            listItemView.reply_content = (TextView) convertView
                    .findViewById(R.id.reply_content);
            listItemView.reply_time = (TextView) convertView
                    .findViewById(R.id.reply_time);
            listItemView.replyID = (TextView) convertView
                    .findViewById(R.id.replyID);

            convertView.setTag(listItemView); 
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }

        // 设置文字和图片
        listItemView.replyID.setText((CharSequence) listItem.get(position).get(
                "replyID"));
        listItemView.reply_content.setText((CharSequence) listItem
                .get(position).get("reply_content"));
        listItemView.reply_time.setText((CharSequence) listItem.get(position)
                .get("reply_time"));
        // listItemView.image.setBackgroundResource((Integer)
        // listItem.get(position).get("image"));
        listItemView.image.setBackgroundResource(R.drawable.icon);

        // 注册点击头像时事件处理
        listItemView.image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

        return convertView;
    }
}
