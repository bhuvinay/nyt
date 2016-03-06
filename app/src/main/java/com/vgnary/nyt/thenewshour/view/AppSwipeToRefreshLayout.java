package com.vgnary.nyt.thenewshour.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.vgnary.nyt.thenewshour.interfaces.SwipeToRefreshPermissionProvider;
import com.vgnary.nyt.thenewshour.utils.LogUtils;



public class AppSwipeToRefreshLayout extends SwipeRefreshLayout {

    private final static String TAG = LogUtils.makeLogTag(AppSwipeToRefreshLayout.class);

    private SwipeToRefreshPermissionProvider mPermissionProvider;

    public void setSwipePermissionProvider(SwipeToRefreshPermissionProvider provider) {
        mPermissionProvider = provider;
    }

    public AppSwipeToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
       /* if (isRefreshing()) {
            Log.d(TAG, "onInterceptTouchEvent :: already refreshing");
            return false;
        }
*/
       /* if (mPermissionProvider != null && !mPermissionProvider.canSwipeToRefresh()) {
            Log.d(TAG, "onInterceptTouchEvent :: not permitted to be swipe to refresh");
            return false;
        }*/
        return super.onInterceptTouchEvent(ev);
    }
}
