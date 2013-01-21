/**
 * The activity is to show the post list of the chooesed board
 */

package tk.djcrazy.MyCC98;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import tk.djcrazy.MyCC98.fragment.PostListFragment;
import tk.djcrazy.MyCC98.util.Intents;
import tk.djcrazy.MyCC98.util.Intents.Builder;
import tk.djcrazy.libCC98.ICC98Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.google.inject.Inject;

@ContentView(R.layout.post_list)
public class PostListActivity extends BaseFragmentActivity {
	private static final String TAG = "PostListActivity";

	@InjectExtra(Intents.EXTRA_BOARD_NAME)
	private String boardName;
	@InjectExtra(Intents.EXTRA_BOARD_ID)
	private String boardId;

	@Inject
	private ICC98Service service;

	public static Intent createIntent(String boardName, String boardId) {
		Intent intent = new Intents.Builder("post_list.VIEW").boardId(boardId)
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
					.add(R.id.post_list_fragment_container,
							PostListFragment.createInstance(boardId, boardName))
					.commit();
		}
	}

	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setLogo(new BitmapDrawable(service.getUserAvatar()));
		actionBar.setTitle(boardName);
	}

	private void sendNewPost() {
		Intents.Builder builder = new Builder(this, EditActivity.class);
		Intent intent = builder.requestType(EditActivity.REQUEST_NEW_POST)
				.boardId(boardId).boardName(boardName).toIntent();
		startActivityForResult(intent, 0);
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
		case R.id.post_list_menu_new_post:
			sendNewPost();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu optionMenu) {
		getSupportMenuInflater().inflate(R.menu.post_list, optionMenu);
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
