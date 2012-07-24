///**
// * 
// */
//package tk.djcrazy.MyCC98;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import tk.djcrazy.MyCC98.db.BoardInfoDbAdapter;
//import tk.djcrazy.MyCC98.view.SearchResultListAdapter;
//import tk.djcrazy.libCC98.CC98Client;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//
//import com.flurry.android.FlurryAgent;
//
///**
// * @author DJ
// * 
// */
//public class SearchBoardActivity extends ChildActivity {
//
//	public static final String ID = "SearchBoardActivity";
//	private static final String TAG = "SearchBoardActivity";
//
//	private EditText searchContent;
//	private ListView resultList;
//	private String searchArg = "";
//	private BoardInfoDbAdapter adapter;
//	List<String> boardNameList = new ArrayList<String>();
//	Map<String, String> boardInfoMap = new HashMap<String, String>();
//	SearchResultListAdapter listAdapter;
//	Resources res;
//
//	@Override
//	public void onStart() {
//		super.onStart();
//		FlurryAgent.onStartSession(this, "5EXV7SIGMTTDKYNXTKR4");
//	}
//
//	@Override
//	public void onStop() {
//		super.onStop();
//		FlurryAgent.onEndSession(this);
//	}
//
//	/**
//	 * Called when the activity is first created.
//	 **/
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.search_board);
//
//		setTitle("版面搜索");
//		adapter = new BoardInfoDbAdapter(this, WelcomeActivity.CURRENTDBVERSION);
//		findViews();
//		res = getResources();
//		setListeners();
//		adapter.open();
//
//		listAdapter = new SearchResultListAdapter(SearchBoardActivity.this,
//				boardNameList, group);
//		resultList.setAdapter(listAdapter);
//		searchContent.requestFocus();
//	}
//	
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		group.setTitle(CC98Client.getUserName()+"::版面搜索");
//		group.refresh();
//	}
//
//	private void setListeners() {
//		searchContent.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				doSearch(searchContent.getText().toString());
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//
//			}
//		});
//
//		resultList.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				adapter.closeclose();
//				Bundle bundle = new Bundle();
//				bundle.putString(PostListFlingActivity.BOARD_LINK,
//						CC98Client.getCC98Domain() + "list.asp?boardid="
//								+ boardInfoMap.get(boardNameList.get(arg2)));
//				bundle.putString(PostListFlingActivity.BOARD_NAME,
//						boardNameList.get(arg2));
//				bundle.putInt(PostListFlingActivity.PAGE_NUMBER, 1);
//
//				Intent intent = new Intent().putExtra(
//						PostListActivity.BOARD_ENTITY, bundle).setClass(
//						SearchBoardActivity.this, PostListActivity.class);
//				startActivity(intent);
//			}
//		});
//	}
//
//	protected void doSearch(String string) {
//		if (string.contains("'") || string == "") {
//
//		} else {
//			Cursor cursor = adapter.getBoardInfo(string);
//			boardNameList.clear();
//			Log.d(TAG, cursor.getColumnCount() + "");
//			while (!cursor.isAfterLast()) {
//				boardNameList.add(cursor.getString(0));
//				Log.d(TAG, cursor.getInt(0) + cursor.getString(1));
//				boardInfoMap.put(cursor.getString(0), cursor.getString(1));
//				cursor.moveToNext();
//			}
//			cursor.close();
//
//		}
//		resultList.setAdapter(listAdapter);
//	}
//
//	private void findViews() {
//		searchContent = (EditText) findViewById(R.id.search_board_bar);
//		resultList = (ListView) findViewById(R.id.search_board_result_list);
//	}
//}
