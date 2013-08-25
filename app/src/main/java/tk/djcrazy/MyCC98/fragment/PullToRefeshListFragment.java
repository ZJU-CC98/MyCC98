/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tk.djcrazy.MyCC98.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.WrapperListAdapter;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.helper.LoadingModelHelper;
import tk.djcrazy.libCC98.util.RequestResultListener;

public abstract class PullToRefeshListFragment<E> extends RoboSherlockFragment implements LoadingModelHelper.OnReloadListener,
         OnRefreshListener<ListView>, RequestResultListener<List<E>> {

 	protected PullToRefreshListView listView;

    protected List<E> items = new ArrayList<E>();

    protected  LoadingModelHelper helper;
   	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
 	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.paged_item_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        helper = new LoadingModelHelper(view,this);
 		listView = (PullToRefreshListView) view.findViewById(R.id.loading_content);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onListItemClick((ListView) parent, view, position, id);
			}
		});
		listView.setOnRefreshListener(this);
        listView.getRefreshableView().setFastScrollEnabled(true);
        if (Build.VERSION.SDK_INT>=11) {
            fastScrollBarAlwaysVisible();
        }
        listView.getRefreshableView().setVerticalScrollBarEnabled(true);
        shouldConfigureListViewBeforeSetAdapter(listView);
        listView.setAdapter(createAdapter(items));
        if (items.size()>0) {
            helper.content(false);
        } else {
            helper.loading();
            onRefresh(listView);
        }
   	}

    @TargetApi(11)
    private void fastScrollBarAlwaysVisible() {
        listView.getRefreshableView().setFastScrollAlwaysVisible(true);
    }

    protected void shouldConfigureListViewBeforeSetAdapter(PullToRefreshListView view) {

    }
 	protected abstract BaseItemListAdapter<E> createAdapter(final List<E> items);


 	public void onListItemClick(ListView l, View v, int position, long id) {
	}

	@Override
	public abstract void onRefresh(PullToRefreshBase<ListView> refreshView);

    @Override
    public void onRequestComplete(List<E> result) {
        setItems(result);
        refreshListViewData();
        listView.onRefreshComplete();
        if (result.size()>0){
            helper.content();
        }  else {
            helper.empty();
        }
    }

    protected void setItems(List<E> result) {
        items.clear();
        items.addAll(result);
    }

    protected void refreshListViewData() {
        ListAdapter adapter = listView.getRefreshableView().getAdapter();
        if (adapter instanceof WrapperListAdapter) {
            ((BaseAdapter) ((WrapperListAdapter) adapter).getWrappedAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestError(String msg) {
        helper.empty();
        listView.onRefreshComplete();
     }

    @Override
    public final void onReload() {
        onRefresh(listView);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public abstract void onCancelRequest();
}
