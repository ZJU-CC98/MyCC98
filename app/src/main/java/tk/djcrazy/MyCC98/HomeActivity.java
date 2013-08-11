package tk.djcrazy.MyCC98;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.google.inject.Inject;
import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.slidingmenu.lib.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import tk.djcrazy.MyCC98.adapter.GlobalBoardListAdapter;
import tk.djcrazy.MyCC98.adapter.HomeFragmentPagerAdapter;
import tk.djcrazy.MyCC98.fragment.HomeBehindMenuFragment;
import tk.djcrazy.MyCC98.listener.LoadingListener;
import tk.djcrazy.MyCC98.service.NewVersionDownloadService;
import tk.djcrazy.MyCC98.util.DisplayUtil;
import tk.djcrazy.libCC98.CachedCC98Service;
import tk.djcrazy.libCC98.NewCC98Service;
import tk.djcrazy.libCC98.data.InboxInfo;
import tk.djcrazy.libCC98.data.PmInfo;
import tk.djcrazy.libCC98.util.RequestResultListener;

public class HomeActivity extends BaseSlidingFragmentActivity  {

    private static final String TAG = "HomeActivity";
    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    public String[] boardNames;
    public String[] boardIds;
    Timer tExit = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            isExit = false;
            hasTask = true;
        }
    };
    @InjectView(R.id.main_pages)
    private JazzyViewPager viewPager;
     @Inject
    private NewCC98Service mCC98Service;

    @Inject
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        setupSlidingMenu();
        setupViewPager();
        configureActionBar();
        setupSecondBehindView();
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            t.replace(R.id.home_behind_view, new HomeBehindMenuFragment()).commit();
        }
        requestForUpdateInfo();
        requestForInboxInfo();
    }


    private void requestForInboxInfo() {
        final int NODIFICATION_ID = 58484654;
        mCC98Service.submitPmInfoRequest(this.getClass(),0,1,new RequestResultListener<InboxInfo>() {
            @Override
            public void onRequestComplete(InboxInfo result) {
                int totalUnread = 0;
                for (PmInfo pmInfo : result.getPmInfos()) {
                    if (pmInfo.isNew()) {
                        totalUnread++;
                    }
                }
                if (totalUnread != 0) {
                     NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(HomeActivity.this).setSmallIcon(
                            R.drawable.ic_launcher).setContentTitle("您有" + totalUnread + "条未读消息")
                             .setTicker("您有" + totalUnread + "条未读消息").setContentText("请点击查看");
                    Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    mBuilder.setSound(alert).setAutoCancel(true);
                    Intent resultIntent = new Intent(HomeActivity.this, PmActivity.class);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(HomeActivity.this);
                    stackBuilder.addParentStack(PmActivity.class);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent( 0,PendingIntent.FLAG_UPDATE_CURRENT );
                    mBuilder.setContentIntent(resultPendingIntent);
                    mNotificationManager.notify(NODIFICATION_ID, mBuilder.build());
                }
            }

            @Override
            public void onRequestError(String msg) {
            }
        });
    }
    private void requestForUpdateInfo() {
        mCC98Service.submitUpdateRequest(this.getClass(), new RequestResultListener<JSONObject>() {
            @Override
            public void onRequestComplete(JSONObject result) {
                try {
                    int versionCode = result.getInt("versionCode");
                    if (versionCode > getVersionCode()) {
                        final String downloadLink = result.getString("downloadLink");
                        String versionName = result.getString("versionName");
                        String updateHint = result.getString("hint");
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setTitle("发现新版本").setMessage("版本号：" + versionName + "\n" + "更新内容：" + updateHint)
                                .setPositiveButton("下载", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startService(NewVersionDownloadService.createIntent(HomeActivity.this, downloadLink));
                                    }
                                }).setNegativeButton("取消", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onRequestError(String msg) {
                Log.d(TAG, msg);
            }
        });
    }

    private int getVersionCode() {
        try {
            PackageInfo packInfo =  getPackageManager().getPackageInfo(getPackageName(), 0);
            return packInfo.versionCode;
        } catch (NameNotFoundException e) {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    protected void onStop() {
        mCC98Service.cancelRequest(HomeActivity.class);
        flushCache();
        super.onStop();
    }

    private void setupViewPager() {
        HomeFragmentPagerAdapter adapter = new HomeFragmentPagerAdapter(
                getSupportFragmentManager(), viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Tablet);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setIndicatorColor(Color.parseColor("#1faeff"));
    }

    private void setupSecondBehindView() {
        boardNames = getResources().getStringArray(R.array.global_board_name);
        boardIds = getResources().getStringArray(R.array.global_board_id);
        ListView globalBoardListView = (ListView) findViewById(R.id.global_boards);
        globalBoardListView.setAdapter(new GlobalBoardListAdapter(this));
        globalBoardListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                startActivity(BoardListActivity.createIntent(boardNames[arg2], boardIds[arg2]));
            }
        });
    }

    private void setupSlidingMenu() {
        getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
        getSlidingMenu().setSecondaryMenu(R.layout.home_second_behind_view);
        setBehindContentView(R.layout.home_behind_view);
        SlidingMenu sm = getSlidingMenu();
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        sm.setShadowWidth(10);
        sm.setBehindScrollScale(0f);
        sm.setBehindOffset(DisplayUtil.dip2px(getApplicationContext(), 150));
        sm.setFadeDegree(0.35f);
    }

    private void flushCache() {
        try {
            Object cache = Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("getInstalled").invoke(null);
            if (cache != null) {
                Class.forName("android.net.http.HttpResponseCache")
                        .getMethod("flush").invoke(cache);
            }
        } catch (Exception e) {
        }
    }

    private void configureActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(new BitmapDrawable(getResources(), mCC98Service.getCurrentUserAvatar()));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(mCC98Service.getCurrentUserData().getUserName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu optionMenu) {
        getSupportMenuInflater().inflate(R.menu.home, optionMenu);
        return true;
    }

    public void refresh() {
        viewPager.invalidate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSlidingMenu().toggle();
                return true;
            case R.id.menu_search:
                onSearchRequested();
                return true;
            case R.id.menu_message:
                startActivity(new Intent(HomeActivity.this, PmActivity.class));
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        appData.putString(PostSearchActivity.BOARD_ID, "0");
        appData.putString(PostSearchActivity.BOARD_NAME, "全站");
        startSearch(null, false, appData, false);
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit == false) {
                isExit = true;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                if (!hasTask) {
                    tExit.schedule(task, 2000);
                }
            } else {
                finish();
            }
        }
        return false;
    }
}
