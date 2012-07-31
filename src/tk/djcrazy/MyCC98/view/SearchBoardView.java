package tk.djcrazy.MyCC98.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import tk.djcrazy.MyCC98.PostListActivity;
import tk.djcrazy.MyCC98.R;
import tk.djcrazy.MyCC98.adapter.SearchResultListAdapter;
import tk.djcrazy.libCC98.CC98ClientImpl;
import tk.djcrazy.libCC98.CC98ParserImpl;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * New Implementation
 * Get the list one when started, use user's keyword as filter
 * The board are sorted by the number of post posted on "today"
 * 
 * @author zsy
 * 
 */
public class SearchBoardView extends LinearLayout implements ChildView {
	private List<NameValuePair> currentResult;
	private List<NameValuePair> boardList;
	private EditText searchContent;
	private ListView lvResultList;
	private ProgressDialog progressDialog;
	private SearchResultListAdapter listAdapter;
	private static final String TAG = "SearchBoardView";
	private ParentView pv;
	private static final int FETCH_SUCC = 0;
	private int lastquerylen;
	private Bitmap userImage;

	/**
	 * @param userImage the userImage to set
	 */
	public void setUserImage(Bitmap userImage) {
		this.userImage = userImage;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FETCH_SUCC:
				progressDialog.dismiss();
				searchContent.setText("");
				break;
			default:
				break;
			}
		}
	};

	public SearchBoardView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.search_board, this, true);
		findViews();
		progressDialog = new ProgressDialog(getContext());
		fetchBoardlist();
		setListeners();	
	}

	private void fetchBoardlist() {
		progressDialog.show();
		new Thread() {
			@Override
			public void run() {
				try {
					boardList = CC98ParserImpl.getTodayBoardList();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(FETCH_SUCC);
			}
		}.start();
	}

	private void findViews() {
		searchContent = (EditText) findViewById(R.id.search_board_bar);
		lvResultList = (ListView) findViewById(R.id.search_board_result_list);
	}

	public void scrollListTo(int x, int y) {
		lvResultList.scrollTo(x, y);
	}
	@Override
	public void setParentView(ParentView pv) {
		this.pv = pv;
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
						CC98ClientImpl.getCC98Domain() + currentResult.get(arg2).getValue());
				bundle.putString(PostListActivity.BOARD_NAME,
						currentResult.get(arg2).getName());
				bundle.putInt(PostListActivity.PAGE_NUMBER, 1);
				bundle.putParcelable(PostListActivity.USER_IMAGE, userImage);

				Intent intent = new Intent().putExtra(
						PostListActivity.BOARD_ENTITY, bundle).setClass(
						getContext(), PostListActivity.class);
				getContext().startActivity(intent);
			}
		});
	}

	protected void doSearch(String string) {
		if (string.equals("")) {
			currentResult = boardList;
		} else if(string.length()<lastquerylen){
			List<NameValuePair> tmplist = new ArrayList<NameValuePair>();
			for(NameValuePair np : boardList){
				if(np.getName().toLowerCase().contains(string.toLowerCase())){
					tmplist.add(np);
				}
			}
			currentResult = tmplist;
		} else {
			List<NameValuePair> tmplist = new ArrayList<NameValuePair>();
			for(NameValuePair np : currentResult){
				if(np.getName().toLowerCase().contains(string.toLowerCase())){
					tmplist.add(np);
				}
			}
			currentResult = tmplist;
		}
		lastquerylen = string.length();
		listAdapter = new SearchResultListAdapter((Activity) getContext(), currentResult);
		lvResultList.setAdapter(listAdapter);
		lvResultList.invalidate();
	}

	@Override
	public void onSwitch() {
		searchContent.requestFocus();
		lvResultList.invalidate();
		pv.setTitle("版面导航");
	}
}
