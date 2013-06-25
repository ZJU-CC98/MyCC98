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

import java.util.ArrayList;
import java.util.List;

import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.BaseItemListAdapter;
import tk.djcrazy.MyCC98.util.ThrowableLoader;
import tk.djcrazy.MyCC98.util.ToastUtils;
import tk.djcrazy.MyCC98.util.ViewUtils;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Base fragment for displaying a list of items that loads with a progress bar
 * visible
 * 
 * @param <E>
 */
public abstract class PullToRefeshListFragment<E> extends RoboSherlockFragment implements
		OnRefreshListener<ListView>, LoaderCallbacks<List<E>> {

	private static final String TAG = "PullToRefeshListFragment";

	/**
	 * List items provided to {@link #onLoadFinished(Loader, List)}
	 */
	protected List<E> items = new ArrayList<E>();

	/**
	 * List view
	 */
	protected PullToRefreshListView listView;

	/**
	 * Empty view
	 */
	protected TextView emptyView;

	/**
	 * Progress bar
	 */
	protected ProgressBar progressBar;

	/**
	 * Is the list currently shown?
	 */
	protected boolean listShown;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (!items.isEmpty())
			setListShown(true, false);
		else
			getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.paged_item_list, container, false);
	}

	/**
	 * Detach from list view.
	 */
	@Override
	public void onDestroyView() {
		listShown = false;
		emptyView = null;
		progressBar = null;
		listView = null;
		super.onDestroyView();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView = (PullToRefreshListView) view.findViewById(R.id.pull_list);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onListItemClick((ListView) parent, view, position, id);
			}
		});
		listView.setOnRefreshListener(this);
		progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
		emptyView = (TextView) view.findViewById(android.R.id.empty);
		emptyView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
 				hide(emptyView).show(progressBar);
				getLoaderManager().restartLoader(0, null, PullToRefeshListFragment.this);
			}
		});
		configureList(getActivity(), getListView());
	}

	/**
	 * Configure list after view has been created
	 * 
	 * @param activity
	 * @param listView
	 */
	protected void configureList(Activity activity, PullToRefreshListView listView) {
		listView.setAdapter(createAdapter(items));
	}

	@Override
	public void onLoadFinished(Loader<List<E>> loader, List<E> items) {
		Exception exception = getException(loader);
 		this.items = items;
		if (exception != null) {
			showError();
		} else {
			getListAdapter().setItems(this.items);
			listView.onRefreshComplete();
			showList();
		}
	}

	@Override
	public void onLoaderReset(Loader<List<E>> loader) {
	}

	/**
	 * Create adapter to display items
	 * 
	 * @param items
	 * @return adapter
	 */
	protected abstract BaseItemListAdapter<E> createAdapter(final List<E> items);

	protected void showError() {
		setEmptyText("加载失败\n点击重试");
		listShown = false;
		hide(listView).hide(progressBar).show(emptyView).fadeIn(emptyView, true);
	}

	/**
	 * Set the list to be shown
	 */
	protected void showList() {
		setListShown(true, isResumed());
	}

	/**
	 * Show exception in a {@link Toast}
	 * 
	 * @param e
	 * @param defaultMessage
	 */
	protected void showError(final Exception e, final int defaultMessage) {
		ToastUtils.show(getActivity(), defaultMessage);
	}

	/**
	 * Get exception from loader if it provides one by being a
	 * {@link ThrowableLoader}
	 * 
	 * @param loader
	 * @return exception or null if none provided
	 */
	protected Exception getException(final Loader<List<E>> loader) {
		return ((ThrowableLoader<List<E>>) loader).clearException();
	}

	/**
	 * Get {@link ListView}
	 * 
	 * @return listView
	 */
	public PullToRefreshListView getListView() {
		return listView;
	}

	/**
	 * Get list adapter
	 * 
	 * @return list adapter
	 */
	@SuppressWarnings("unchecked")
	protected BaseItemListAdapter<E> getListAdapter() {
		if (listView != null)
			return (BaseItemListAdapter<E>) ((HeaderViewListAdapter) listView.getRefreshableView()
					.getAdapter()).getWrappedAdapter();
		else
			return null;
	}

	/**
	 * Set list adapter to use on list view
	 * 
	 * @param adapter
	 * @return this fragment
	 */
	protected PullToRefeshListFragment<E> setListAdapter(final BaseItemListAdapter<E> adapter) {
		if (listView != null)
			listView.setAdapter(adapter);
		return this;
	}

	private PullToRefeshListFragment<E> fadeIn(final View view, final boolean animate) {
		if (view != null) {
			if (animate)
				view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
						R.anim.activity_open_enter));
			else {
				view.clearAnimation();
			}
		}
		return this;
	}

	private PullToRefeshListFragment<E> show(final View view) {
		ViewUtils.setGone(view, false);
		return this;
	}

	private PullToRefeshListFragment<E> hide(final View view) {
		ViewUtils.setGone(view, true);
		return this;
	}

	/**
	 * Set list shown or progress bar show
	 * 
	 * @param shown
	 * @return this fragment
	 */
	public PullToRefeshListFragment<E> setListShown(final boolean shown) {
		return setListShown(shown, true);
	}

	/**
	 * Set list shown or progress bar show
	 * 
	 * @param shown
	 * @param animate
	 * @return this fragment
	 */
	public PullToRefeshListFragment<E> setListShown(final boolean shown, final boolean animate) {
		if (!isUsable())
			return this;

		if (shown == listShown) {
			if (shown)
				// List has already been shown so hide/show the empty view with
				// no fade effect
				if (items.isEmpty())
					hide(listView).hide(progressBar).show(emptyView);
				else
					hide(emptyView).hide(progressBar).show(listView);
			return this;
		}
		listShown = shown;
		if (shown) {
			if (!items.isEmpty())
				hide(progressBar).hide(emptyView).fadeIn(listView, animate).show(listView);
			else {
				setEmptyText("没有数据\n点此重试");
				hide(progressBar).hide(listView).fadeIn(emptyView, animate).show(emptyView);
			}
		} else {
			hide(listView).hide(emptyView).fadeIn(progressBar, animate).show(progressBar);
		}
		return this;
	}

	/**
	 * Set empty text on list fragment
	 * 
	 * @param message
	 * @return this fragment
	 */
	protected PullToRefeshListFragment<E> setEmptyText(final String message) {
		if (emptyView != null)
			emptyView.setText(message);
		return this;
	}

	/**
	 * Set empty text on list fragment
	 * 
	 * @param resId
	 * @return this fragment
	 */
	protected PullToRefeshListFragment<E> setEmptyText(final int resId) {
		if (emptyView != null)
			emptyView.setText(resId);
		return this;
	}

	/**
	 * Callback when a list view item is clicked
	 * 
	 * @param l
	 * @param v
	 * @param position
	 * @param id
	 */
	public void onListItemClick(ListView l, View v, int position, long id) {
	}

	/**
	 * Is this fragment still part of an activity and usable from the UI-thread?
	 * 
	 * @return true if usable on the UI-thread, false otherwise
	 */
	protected boolean isUsable() {
		return getActivity() != null;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
 		getListAdapter().notifyDataSetChanged();
		getLoaderManager().restartLoader(0, null, this);
	}
}
