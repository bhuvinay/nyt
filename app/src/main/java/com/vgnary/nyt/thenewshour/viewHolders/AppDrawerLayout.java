package com.vgnary.nyt.thenewshour.viewHolders;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AppDrawerLayout extends android.support.v4.widget.DrawerLayout {

    public AppDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppDrawerLayout(Context context) {
        super(context);
    }

    public AppDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isDrawerOpen(GravityCompat.START) || isDrawerVisible(GravityCompat.START)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }
}
