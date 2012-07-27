package tk.djcrazy.MyCC98.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.MyCC98.PostListActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.SearchResultListAdapter;
import tk.djcrazy.MyCC98.listener.LoadingListener;
import tk.djcrazy.MyCC98.view.ParentView;
import tk.djcrazy.libCC98.CC98Client;
import tk.djcrazy.libCC98.CC98Parser;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class SearchBoardFragment extends Fragment {
	private int position = 0;
	private static final String TAG = "SearchBoardFragment";
	private List<NameValuePair> currentResult;
	private List<NameValuePair> boardList;
	private EditText searchContent;
	private ListView lvResultList;
	private SearchResultListAdapter listAdapter;
	private static final int FETCH_SUCC = 0;
	private static final int FETCH_FAIL = 1;

	private int lastquerylen;
	private LoadingListener loadingListener;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (loadingListener == null) {
				throw new IllegalStateException(
						"You must set the LoadingListener first.");
			}
			switch (msg.what) {
			case FETCH_SUCC:
				searchContent.setText("");
				loadingListener.onLoadComplete(position);
				break;
			case FETCH_FAIL:
				loadingListener.onLoadFailure(position);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fetchBoardlist();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.search_board, null);
		findViews(view);
		fetchBoardlist();
		setListeners();
		return view;
	}

	private void fetchBoardlist() {
		new Thread() {
			@Override
			public void run() {
				try {
					boardList = CC98Parser.getTodayBoardList();
					handler.sendEmptyMessage(FETCH_SUCC);
				} catch (ClientProtocolException e) {
					handler.sendEmptyMessage(FETCH_FAIL);
					e.printStackTrace();
				} catch (ParseException e) {
					handler.sendEmptyMessage(FETCH_FAIL);
					e.printStackTrace();
				} catch (IOException e) {
					handler.sendEmptyMessage(FETCH_FAIL);
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void findViews(View view) {
		searchContent = (EditText) view.findViewById(R.id.search_board_bar);
		lvResultList = (ListView) view
				.findViewById(R.id.search_board_result_list);
	}

	public void scrollListTo(int x, int y) {
		lvResultList.scrollTo(x, y);
	}

	private void setListeners() {
		searchContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				doSearch(searchContent.getText().toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		lvResultList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bundle bundle = new Bundle();
				bundle.putString(PostListActivity.BOARD_LINK,
						CC98Client.getCC98Domain()
								+ currentResult.get(arg2).getValue());
				bundle.putString(PostListActivity.BOARD_NAME, currentResult
						.get(arg2).getName());
				bundle.putInt(PostListActivity.PAGE_NUMBER, 1);

				Intent intent = new Intent().putExtra(
						PostListActivity.BOARD_ENTITY, bundle).setClass(
						getActivity(), PostListActivity.class);
				getActivity().startActivity(intent);
			}
		});
	}

	protected void doSearch(String string) {
		if (string.equals("")) {
			currentResult = boardList;
		} else if (string.length() < lastquerylen) {
			List<NameValuePair> tmplist = new ArrayList<NameValuePair>();
			for (NameValuePair np : boardList) {
				if (np.getName().toLowerCase().contains(string.toLowerCase())) {
					tmplist.add(np);
				}
			}
			currentResult = tmplist;
		} else {
			List<NameValuePair> tmplist = new ArrayList<NameValuePair>();
			for (NameValuePair np : currentResult) {
				if (np.getName().toLowerCase().contains(string.toLowerCase())) {
					tmplist.add(np);
				}
			}
			currentResult = tmplist;
		}
		lastquerylen = string.length();
		listAdapter = new SearchResultListAdapter(getActivity(), currentResult);
		lvResultList.setAdapter(listAdapter);
		lvResultList.invalidate();
	}

	/**
	 * @param loadingListener
	 *            the loadingListener to set
	 */
	public void setLoadingListener(LoadingListener loadingListener) {
		this.loadingListener = loadingListener;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

}
