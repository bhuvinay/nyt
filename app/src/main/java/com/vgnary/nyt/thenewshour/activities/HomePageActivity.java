package com.vgnary.nyt.thenewshour.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vgnary.nyt.thenewshour.R;
import com.vgnary.nyt.thenewshour.adapters.HomePageViewPagerAdapter;
import com.vgnary.nyt.thenewshour.constants.AppConstants;
import com.vgnary.nyt.thenewshour.dialog.BrightnessBarDialog;
import com.vgnary.nyt.thenewshour.dialog.TextSizeAlterDialog;
import com.vgnary.nyt.thenewshour.eventBus.TextSizeEvent;
import com.vgnary.nyt.thenewshour.recievers.AlarmReciever;
import com.vgnary.nyt.thenewshour.utils.AppUtils;
import com.vgnary.nyt.thenewshour.utils.NewsFeedDatabaseHelper;
import com.vgnary.nyt.thenewshour.viewHolders.AppDrawerLayout;

import de.greenrobot.event.EventBus;


public class HomePageActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener {
    private HomePageViewPagerAdapter homePageViewPagerAdapter;
    private ViewPager mHomePageFeedPager;
    private Toolbar mAppToolbar;
    private TabLayout tabLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private AppDrawerLayout mNavigationDrawer;
    private NavigationView mHamburgerView;
    private EventBus mEventPostingBus = EventBus.getDefault();
    private SharedPreferences mSharedPreferences;
    private int mSystemBrightness;
    private int mLocalBrightness;
    private Menu mAppMenu;
    private ImageView mNavigationHeaderImage;
    private NewsFeedDatabaseHelper mUserPrefDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_home_page);
        init();
        setNavigationDrawer();
        assignListners();
        setTabSelectedListener();
        mEventPostingBus.register(this);
       /* startAlarmService();*/
    }

    private void startAlarmService() {
        Intent alarmIntent = new Intent(HomePageActivity.this, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(HomePageActivity.this, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AppConstants.ALARM_INTERVAL, pendingIntent);

    }


    private void setTabSelectedListener() {
        tabLayout.setupWithViewPager(mHomePageFeedPager);
        tabLayout.setOnTabSelectedListener(this);
    }


    private void assignListners() {
        mHamburgerView.setNavigationItemSelectedListener(this);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
    }

    private void setNavigationDrawer() {
        mNavigationDrawer = (AppDrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mNavigationDrawer, mAppToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                switch (mHomePageFeedPager.getCurrentItem()) {
                    case AppConstants.MOST_POPULAR_DATA:
                        mHamburgerView.getMenu().findItem(R.id.nv_most_popular).setChecked(true);
                        break;
                    case AppConstants.MOVIE_REVIEW_DATA:
                        mHamburgerView.getMenu().findItem(R.id.nv_movie_review).setChecked(true);
                        break;
                    case AppConstants.GEOGRAPHIC_DATA:
                        mHamburgerView.getMenu().findItem(R.id.nv_geographic_data).setChecked(true);
                        break;
                    case AppConstants.BESTSELLER_DATA:
                        mHamburgerView.getMenu().findItem(R.id.nv_bestSeller_data).setChecked(true);
                        break;
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mNavigationDrawer.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
    }

    private void init() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mHomePageFeedPager = (ViewPager) findViewById(R.id.pager);
        mHomePageFeedPager.setOffscreenPageLimit(AppConstants.MAX_NO_OF_PAGES_LOADED);
        mAppToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mAppToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(" ");
        }
        homePageViewPagerAdapter = new HomePageViewPagerAdapter(getSupportFragmentManager());
        mHomePageFeedPager.setAdapter(homePageViewPagerAdapter);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mHamburgerView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationHeaderImage = (ImageView) mHamburgerView.getHeaderView(0).findViewById(R.id.iv_nav_view_header);
        mNavigationHeaderImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {

        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_homepage, menu);
        this.mAppMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_text_size_change:
                showTextSizeDialog();
                break;
            case R.id.action_brighntess_bar_display:
                showBrightnessBar();
                break;
            case R.id.action_refresh:
                refreshTab();
            case R.id.action_clear_cache:
                AppUtils.clearCache(getApplicationContext());
                break;
            case R.id.action_clear_saved_images:
                dontsaveImageOffline();
                break;
            case R.id.action_settings:
                mAppMenu.findItem(R.id.action_clear_saved_images).setChecked(getSavedImagePreference());
                break;
            case R.id.action_gmail_fb_login:
                launchLoginActivity();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, AppConstants.LAUNCH_LOGIN_PAGE);
    }

    private boolean getSavedImagePreference() {
        return mSharedPreferences.getBoolean(AppConstants.SAVE_IMAGES_OFFLINE, false);
    }

    private void dontsaveImageOffline() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(AppConstants.SAVE_IMAGES_OFFLINE, !getSavedImagePreference());
        editor.apply();
        if (getSavedImagePreference()) {
            AppUtils.clearSavedImages(getApplicationContext());
        }

    }


    private void showTextSizeDialog() {
        TextSizeAlterDialog textSizeAlterDialog = new TextSizeAlterDialog(this);
        textSizeAlterDialog.show();
    }

    private void refreshTab() {
        String refreshEvent = "refresh";
        mEventPostingBus.post(refreshEvent);
    }

    private void showBrightnessBar() {
        final BrightnessBarDialog brightnessBarDialog = new BrightnessBarDialog(this, getContentResolver());
        brightnessBarDialog.show();
        Window window = brightnessBarDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        brightnessBarDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setActivityBrightness(brightnessBarDialog.mLocalBrightness);
                mLocalBrightness = brightnessBarDialog.mLocalBrightness;
            }
        });

    }

    private void changeAppTextSize(float summarySize, float headingSize) {
        TextSizeEvent event = null;
        event = new TextSizeEvent(summarySize, headingSize);
        mEventPostingBus.post(event);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nv_most_popular) {
            mHomePageFeedPager.setCurrentItem(0);
        } else if (id == R.id.nv_movie_review) {
            mHomePageFeedPager.setCurrentItem(1);
        } else if (id == R.id.nv_geographic_data) {
            mHomePageFeedPager.setCurrentItem(2);
        } else if (id == R.id.nv_bestSeller_data) {
            mHomePageFeedPager.setCurrentItem(3);
        } else if (id == R.id.nav_share) {
            AppUtils.showShareDialog(getResources().getString(R.string.label_checkout_app), getApplicationContext());
        } else if (id == R.id.nav_about_us) {
            showAboutUsDialog();
        } else if (id == R.id.action_clear_cache) {
            AppUtils.clearCache(getApplicationContext());
        } else if (id == R.id.nv_save_content) {
            launchSavedContentActivity();
        } else if (id == R.id.nv_clear_save_content) {
            clearSavedFeed();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearSavedFeed() {
        mUserPrefDBHelper = AppUtils.getHelper(getApplicationContext());
        mUserPrefDBHelper.clearSavedContent();
    }

    private void launchSavedContentActivity() {
        Intent intent = new Intent(this, SavedContentActivity.class);
        startActivity(intent);
    }


    private void showAboutUsDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_about_us_dialog, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("About US")
                .setView(dialogView).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mHomePageFeedPager.setCurrentItem(tab.getPosition());
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        setActivityBrightness(mSystemBrightness);
    }

    private void setActivityBrightness(int brightness) {
        AppUtils.setAppBrightness(brightness, getContentResolver(), getWindow());
        saveLocalBrightness();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mSystemBrightness = Settings.System.getInt(
                    getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mLocalBrightness = mSharedPreferences.getInt(AppConstants.LOCAL_BRIGHTNESS_KEY, mSystemBrightness);
        setActivityBrightness(mLocalBrightness);
    }

    private void saveLocalBrightness() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(AppConstants.LOCAL_BRIGHTNESS_KEY, mLocalBrightness);
        editor.apply();
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            mAppMenu.performIdentifierAction(R.id.action_settings, 0);
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.LAUNCH_LOGIN_PAGE) {
            switch (resultCode) {
                case RESULT_OK:
                    if (data != null)
                        setUserProfilePic(data.getStringExtra(AppConstants.USER_DISPLAY_PIC));
                    setMenuItem();
                    break;
                case RESULT_CANCELED:
                    break;
                default:
                    break;
            }
        }
    }

    private void setMenuItem() {
        mAppMenu.findItem(R.id.action_gmail_fb_login).setTitle("Logout");
    }

    private void setUserProfilePic(String imageUrl) {
        if (imageUrl != null) {
            Glide.with(getApplicationContext()).load(imageUrl).into(mNavigationHeaderImage);
        }
    }

    public void onEvent(Bitmap event) {

        AppUtils.animatedDialog(this,event);
    }
}