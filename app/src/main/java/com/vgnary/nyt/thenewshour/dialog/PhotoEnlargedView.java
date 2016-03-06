package com.vgnary.nyt.thenewshour.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vgnary.nyt.thenewshour.R;

public class PhotoEnlargedView extends Dialog implements View.OnClickListener {
    private ImageView mUserPic;
    private Bitmap bitmap;
    private RelativeLayout rl;


    public PhotoEnlargedView(Activity context, Bitmap event) {

        super(context);
        bitmap = event;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_photo_enlarged_dialog);
        init();
        setListeners();


    }


    private void setListeners() {

    }

    private void init() {
        mUserPic = (ImageView) findViewById(R.id.iv_user_pic_enlarged);
        mUserPic.setImageBitmap(bitmap);
        rl = (RelativeLayout) findViewById(R.id.rl_enlarged_view);

    }

    @Override
    public void onClick(View v) {

    }


}
