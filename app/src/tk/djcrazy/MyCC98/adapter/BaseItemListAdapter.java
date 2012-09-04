package tk.djcrazy.MyCC98.adapter;

import java.util.List;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class BaseItemListAdapter<E> extends BaseAdapter {

	protected Activity context;
	
	protected List<E> items;

	protected LayoutInflater inflater;

	public BaseItemListAdapter(Activity context, List<E> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.items = list;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public List<E> getItems() {
		return items;
	}

	public void setItems(List<E> items) {
		this.items = items;
		notifyDataSetChanged();
	}
	
	public void setItem(int index, E item) {
		items.set(index, item);
	}
	
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	  if (observer != null) {
	    super.unregisterDataSetObserver(observer);
	  }
	} 
}
