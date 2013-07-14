/**
 * The activity is to show the post list of the chooesed board
 */

package tk.djcrazy.MyCC98;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import tk.djcrazy.MyCC98.fragment.BoardListFragment;
import tk.djcrazy.MyCC98.fragment.PostListFragment;
import tk.djcrazy.MyCC98.util.Intents;
import tk.djcrazy.MyCC98.util.Intents.Builder;
import tk.djcrazy.libCC98.CachedCC98Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.google.inject.Inject;

@ContentView(R.layout.activity_boad_list)
public class BoardListActivity extends BaseFragmentActivity {
	private static final String TAG = "BoardListActivity";

	@InjectExtra(Intents.EXTRA_BOARD_NAME)
	private String boardName;
	@InjectExtra(Intents.EXTRA_BOARD_ID)
	private String boardId;

	@Inject
	private CachedCC98Service service;

	public static Intent createIntent(String boardName, String boardId) {
		Intent intent = new Intents.Builder("board_list.VIEW").boardId(boardId)
				.boardName(boardName).toIntent();
		return intent;
	}

	@Override
	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);
		configureActionBar();
 		if (SavedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.board_list_fragment_container,
							BoardListFragment.createInstance(boardId, boardName))
					.commit();
		}
	}

	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setLogo(new BitmapDrawable(service.getCurrentUserAvatar()));
		actionBar.setTitle(Html.fromHtml(boardName));
	}

 	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.post_list_menu_search:
			onSearchRequested();
			return true;
 		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu optionMenu) {
		getSupportMenuInflater().inflate(R.menu.board_list, optionMenu);
		return true;
	}

	@Override
	public boolean onSearchRequested() {
		Bundle appData = new Bundle();
		appData.putString(PostSearchActivity.BOARD_ID, boardId);
		appData.putString(PostSearchActivity.BOARD_NAME, boardName);
		startSearch(null, false, appData, false);
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
 		super.onConfigurationChanged(newConfig);
	}
}
