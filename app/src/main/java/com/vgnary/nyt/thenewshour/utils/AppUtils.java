package com.vgnary.nyt.thenewshour.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vgnary.nyt.thenewshour.R;
import com.vgnary.nyt.thenewshour.dialog.PhotoEnlargedView;
import com.j256.ormlite.android.apptools.OpenHelperManager;


public class AppUtils {

    private static NewsFeedDatabaseHelper mUserPrefDBHelper;

    public static void showShareDialog(String message, Context context) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                message);
        shareIntent.setType("text/plain");
        String title = context.getResources().getString(R.string.title_share_title);

        Intent chooser = Intent.createChooser(shareIntent, title);
        chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(chooser);
        }

    }

    public static void setAppBrightness(int brightness, ContentResolver contentResolver, Window window) {
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        WindowManager.LayoutParams layoutpars = window.getAttributes();
        layoutpars.screenBrightness = brightness / (float) 255;
        window.setAttributes(layoutpars);
    }


    public static void clearCache(Context context) {
        NewsFeedDatabaseHelper mUserPrefDBHelper = null;
        mUserPrefDBHelper = OpenHelperManager.getHelper(context, NewsFeedDatabaseHelper.class);
        mUserPrefDBHelper.getWritableDatabase();
        LogUtils.makeToast("cache cleared", Toast.LENGTH_SHORT, context);
        clearSavedImages(context);
        mUserPrefDBHelper.clearCache();
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static void clearSavedImages(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();

    }

    public static NewsFeedDatabaseHelper getHelper(Context context) {
        if (mUserPrefDBHelper == null) {
            mUserPrefDBHelper = OpenHelperManager.getHelper(context, NewsFeedDatabaseHelper.class);
            mUserPrefDBHelper.getWritableDatabase();
        }
        return mUserPrefDBHelper;
    }

    public static void showErrorDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void animatedDialog(Context context, Bitmap event) {

        final PhotoEnlargedView brightnessBarDialog = new PhotoEnlargedView((Activity) context, event);
        brightnessBarDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        brightnessBarDialog.show();
    }

}
