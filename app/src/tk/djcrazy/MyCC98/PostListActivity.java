/**
 * The activity is to show the post list of the chooesed board
 */

package tk.djcrazy.MyCC98;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import tk.djcrazy.MyCC98.fragment.PostListFragment;
import tk.djcrazy.libCC98.ICC98Service;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.google.inject.Inject;

@ContentView(R.layout.post_list)
public class PostListActivity extends BaseActivity {
	private static final String TAG = "PostListActivity";

	public static final String BOARD_ID = "boardId";
	public static final String BOARD_NAME = "boardName";

	@InjectExtra(BOARD_NAME)
	private String boardName;
	@InjectExtra(BOARD_ID)
	private String boardId;

 	@Inject
	private ICC98Service service;

	@Override
	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);
		configureActionBar();
		getSupportFragmentManager().beginTransaction().add(
				R.id.post_list_fragment_container,
				PostListFragment.createInstance(boardId, boardName)).commit();
	}

	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setLogo(new BitmapDrawable(service.getUserAvatar()));
		actionBar.setTitle(boardName);
	}

	private void sendNewPost() {
		Intent intent = new Intent(PostListActivity.this, EditActivity.class);
		intent.putExtra(EditActivity.MOD, EditActivity.MOD_NEW_POST);
		intent.putExtra(EditActivity.BOARD_ID, boardId);
		intent.putExtra(EditActivity.BOARD_NAME, boardName);
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
	
}
