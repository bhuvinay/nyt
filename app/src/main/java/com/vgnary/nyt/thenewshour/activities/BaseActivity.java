package com.vgnary.nyt.thenewshour.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.vgnary.nyt.thenewshour.R;
import com.vgnary.nyt.thenewshour.constants.AppConstants;
import com.vgnary.nyt.thenewshour.utils.LogUtils;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import static com.vgnary.nyt.thenewshour.utils.LogUtils.makeLogTag;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = makeLogTag(BaseActivity.class);

    protected String SCREEN_LABEL = "Base Activity";
    private RelativeLayout mLoadingView;
    private ViewGroup mBaseContainer;
    private SharedPreferences mSharedPreferences;
    private GoogleCloudMessaging mGoogleCloudMessaging;
    SharedPreferences.Editor editor;
    boolean doubleBackToExitPressedState = false;
    private String reg_id;



    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.layout_base_template_parent);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutResID, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBaseContainer = (ViewGroup) findViewById(R.id.ll_parent_layout);
        mBaseContainer.addView(view, layoutParams);
        mLoadingView = (RelativeLayout) findViewById(R.id.rl_loader_view);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void showLoadingView() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingView() {
        mLoadingView.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedState && ((this instanceof HomePageActivity))) {
            super.onBackPressed();
            doubleBackToExitPressedState = false;
            overridePendingTransition(R.anim.grow_fade_in, R.anim.slide_out_right);
        } else if (!(this instanceof HomePageActivity)) {
            super.onBackPressed();
            overridePendingTransition(R.anim.grow_fade_in, R.anim.slide_out_right);
        } else if (!doubleBackToExitPressedState && ((this instanceof HomePageActivity))) {
            doubleBackToExitPressedState = true;
            LogUtils.makeToast(getApplicationContext().getResources().getString(R.string.label_back_pressed),Toast.LENGTH_SHORT,getApplicationContext());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doubleBackToExitPressedState = false;
        mSharedPreferences = getSharedPreferences(AppConstants.GCM_PREF_FILE_KEY, MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(AppConstants.GCM_PREF_STATE_KEY, false)) {
            mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(getApplicationContext());
            registerBackground();
        }
    }

    private void registerBackground() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (mGoogleCloudMessaging == null) {
                        mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    reg_id = mGoogleCloudMessaging.register(AppConstants.GCM_SENDER_ID);
                    msg = "Device registered, registration id=" + reg_id;
                    if (reg_id != null) {
                        editor = getSharedPreferences(AppConstants.GCM_PREF_FILE_KEY, MODE_PRIVATE).edit();
                        editor.putBoolean(AppConstants.GCM_PREF_STATE_KEY, true);
                        editor.commit();
                        Log.d("url", "registration id" + reg_id);
                    }
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

        }.execute(null, null, null);
    }

}
