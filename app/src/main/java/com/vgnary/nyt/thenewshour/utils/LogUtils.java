package com.vgnary.nyt.thenewshour.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.vgnary.nyt.thenewshour.BuildConfig;


public class LogUtils {

    private static final String LOG_PREFIX = "my_app_";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;
    private static Toast mToast;

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }
        return LOG_PREFIX + str;
    }

    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void LOGD(final String tag, String message) {
        //noinspection PointlessBooleanExpression,ConstantConditions
        if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }

    public static void makeToast(String message, int toastLength,Context context) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, toastLength);
            }
            mToast.setText(message);
            if (!mToast.getView().isShown()) {
                mToast.show();
            }
        }

    private LogUtils() {
    }
}
