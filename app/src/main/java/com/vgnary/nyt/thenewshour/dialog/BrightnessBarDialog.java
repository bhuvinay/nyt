package com.vgnary.nyt.thenewshour.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Window;
import android.widget.SeekBar;

import com.vgnary.nyt.thenewshour.R;
import com.vgnary.nyt.thenewshour.utils.AppUtils;


public class BrightnessBarDialog extends Dialog {
    private ContentResolver mContentResolver;
    private Window mBrighntessWindow;
    private SeekBar mBrighntessBar;
    public int mLocalBrightness;

    public BrightnessBarDialog(Activity a, ContentResolver contentResolver) {
        super(a);
        // TODO Auto-generated constructor stub
        mContentResolver = contentResolver;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_brightness_dialog);
        mBrighntessWindow = getWindow();
        try {
            mLocalBrightness = Settings.System.getInt(
                    mContentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        mBrighntessBar = (SeekBar) findViewById(R.id.sb_brightness);
        mBrighntessBar.getProgressDrawable().setColorFilter(0xAA000000, PorterDuff.Mode.MULTIPLY);
        mBrighntessBar.getThumb().setColorFilter(0xFF000000, PorterDuff.Mode.MULTIPLY);
        setDefaultProgressStatus();
        mBrighntessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLocalBrightness = 255 * progress / 100;
                AppUtils.setAppBrightness(mLocalBrightness,mContentResolver,mBrighntessWindow);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void setDefaultProgressStatus() {
        int progress = mLocalBrightness * 100 / 255;
        mBrighntessBar.setProgress(progress);
    }



}
