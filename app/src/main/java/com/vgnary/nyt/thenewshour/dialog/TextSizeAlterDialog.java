package com.vgnary.nyt.thenewshour.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import com.vgnary.nyt.thenewshour.R;
import com.vgnary.nyt.thenewshour.constants.AppConstants;
import com.vgnary.nyt.thenewshour.eventBus.TextSizeEvent;

import de.greenrobot.event.EventBus;


public class TextSizeAlterDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private RadioButton mSmallTextButton;
    private RadioButton mNormalTextButton;
    private RadioButton mLargeTextButton;
    private int mTextTypeSelected = 0;


    public TextSizeAlterDialog(Activity context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_text_size_dialog);
        init();
        setListeners();
        setRadioButtonState();

    }

    private void setRadioButtonState() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        mTextTypeSelected = sharedPreferences.getInt(AppConstants.LOCAL_TEXTSIZE_VALUE, 0);
        switch (mTextTypeSelected) {
            case AppConstants.LARGE_TEXT:
                mLargeTextButton.setChecked(true);
                break;
            case AppConstants.NORMAL_TEXT:
                mNormalTextButton.setChecked(true);
                break;
            case AppConstants.SMALL_TEXT:
                mSmallTextButton.setChecked(true);
                break;
        }
    }

    private void setListeners() {
        mSmallTextButton.setOnClickListener(this);
        mNormalTextButton.setOnClickListener(this);
        mLargeTextButton.setOnClickListener(this);
    }

    private void init() {
        mLargeTextButton = (RadioButton) findViewById(R.id.rb_large_text);
        mNormalTextButton = (RadioButton) findViewById(R.id.rb_normal_text);
        mSmallTextButton = (RadioButton) findViewById(R.id.rb_small_text);
    }

    @Override
    public void onClick(View v) {
        int textType = 0;
        switch (v.getId()) {
            case R.id.rb_small_text:
                changeAppTextSize(mContext.getResources().getDimension(R.dimen.text_size_small_summary), mContext.getResources().getDimension(R.dimen.text_size_small_heading));
                textType = AppConstants.SMALL_TEXT;
                break;
            case R.id.rb_large_text:
                changeAppTextSize(mContext.getResources().getDimension(R.dimen.text_size_large_summary), mContext.getResources().getDimension(R.dimen.text_size_large_heading));
                textType = AppConstants.LARGE_TEXT;
                break;
            case R.id.rb_normal_text:
                changeAppTextSize(mContext.getResources().getDimension(R.dimen.text_size_normal_summary), mContext.getResources().getDimension(R.dimen.text_size_normal_heading));
                textType = AppConstants.NORMAL_TEXT;
                break;
        }
        saveTextTypeSelected(textType);
    }

    private void changeAppTextSize(float summarySize, float headingSize) {
        TextSizeEvent event = null;
        event = new TextSizeEvent(summarySize, headingSize);
        EventBus eventPostingBus = EventBus.getDefault();
        eventPostingBus.post(event);
    }

    private void saveTextTypeSelected(int textType) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AppConstants.LOCAL_TEXTSIZE_VALUE, textType);
        editor.commit();
    }
}
