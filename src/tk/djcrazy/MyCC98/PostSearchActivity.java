package tk.djcrazy.MyCC98;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;

import tk.djcrazy.MyCC98.adapter.NewTopicListAdapter;
import tk.djcrazy.MyCC98.view.HeaderView;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.data.SearchResultEntity;
import tk.djcrazy.libCC98.exception.ParseContentException;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.google.inject.Inject;
 
public class PostSearchActivity extends BaseActivity {

	public static final String BOARDID = "boardid";
	public static final String BOARDNAME = "boardname";
	public static final String USER_IMAGE = "userImage";
	public final String SEARCH_TYPE_TITLE = "2";
	public final String SEARCH_TYPE_AUTHOR = "1";

	private NewTopicListAdapter newTopicListAdapter;
	private EditText etKeyword;
 	private View vNext;
	private View vPrev;
	private RadioGroup rg;
	private RadioButton rbByTitle;
 	private HeaderView mHeaderView;
	private Bitmap userImage;
	private ListView listView;
	private List<SearchResultEntity> datalist;

	private int currentPage = 1;
	private int totalPage;
	private String boardId;
	private String boardname = "全站";
	private String currentType = SEARCH_TYPE_TITLE;

	private ProgressDialog pg;

	@Inject
	private ICC98Service service;
	
	private static final int FETCH_SUCC = 0;
	private static final int NOTFOUND = 1;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FETCH_SUCC:
				int totalpost = Integer.parseInt(datalist.get(0)
						.getTotalResult());
				datalist.remove(0);
				totalPage = totalpost % 20 == 0 ? totalpost / 20
						: totalpost / 20 + 1;
				if (currentPage <= 1) {
					vPrev.setVisibility(View.GONE);
				} else {
					vPrev.setVisibility(View.VISIBLE);
				}
				if (currentPage >= totalPage) {
					vNext.setVisibility(View.GONE);
				} else {
					vNext.setVisibility(View.VISIBLE);
				}
				setTitle("MyCC98:搜索");
				mHeaderView.setTitle("搜索" + boardname + ": " + currentPage + "/"
						+ totalPage);
				newTopicListAdapter = new NewTopicListAdapter(
						PostSearchActivity.this, datalist);
				listView.setAdapter(newTopicListAdapter);
				pg.dismiss();
				getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
				break;
			case NOTFOUND:
				pg.dismiss();
				Toast.makeText(PostSearchActivity.this, "木有找到...",
						Toast.LENGTH_SHORT).show();
			default:
				break;
			}
		}
	};

	private void fetchContent() {
		pg.show();
		new Thread() {
			@Override
			public void run() {
				try {
					datalist = service.searchPost(etKeyword.getText()
							.toString(), boardId, currentType, currentPage);
					if (datalist.size()>1) {
						handler.sendEmptyMessage(FETCH_SUCC);
					} else {
						handler.sendEmptyMessage(NOTFOUND);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseContentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		boardId = getIntent().getStringExtra(BOARDID);
		boardname = getIntent().getStringExtra(BOARDNAME);
		userImage = getIntent().getParcelableExtra(USER_IMAGE);
		boardname = boardname == null ? "全站" : boardname;
		setContentView(R.layout.post_search);
		findviews();
		setListeners();
		setTitle("搜索" + boardname);
		mHeaderView.setTitle("搜索" + boardname);
		mHeaderView.setTitleTextSize(12f);
		mHeaderView.setUserImg(userImage);
		pg = new ProgressDialog(this);
		rbByTitle.setChecked(true);
	}

	private void setListeners() {
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if (arg1 == R.id.rb_postsearch_by_author) {
					currentType =  SEARCH_TYPE_AUTHOR;
				} else if (arg1 == R.id.rb_postsearch_by_title) {
					currentType =  SEARCH_TYPE_TITLE;
				}
			}
		});
		mHeaderView.setButtonOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				fetchContent();
			}
		});
		mHeaderView.setUserImg(service.getUserAvatar());
		vNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentPage < totalPage) {
					++currentPage;
					fetchContent();
				}
			}
		});
		vPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentPage > 1) {
					--currentPage;
					fetchContent();
				}
			}
		});
	}

	private void findviews() {
		etKeyword = (EditText) findViewById(R.id.et_postsearch_key);
		rg = (RadioGroup) findViewById(R.id.rg_postsearch_stype);
		rbByTitle = (RadioButton) findViewById(R.id.rb_postsearch_by_title);
  		listView = (ListView) findViewById(R.id.lv_postsearch);
		vNext = findViewById(R.id.tv_postsearch_next);
		vPrev = findViewById(R.id.tv_postsearch_prev);
		mHeaderView = (HeaderView) findViewById(R.id.main_header);
	}

}
