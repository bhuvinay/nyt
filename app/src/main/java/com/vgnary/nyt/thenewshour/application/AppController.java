package com.vgnary.nyt.thenewshour.application;

import android.app.Application;
import android.content.Context;

import com.vgnary.nyt.thenewshour.BuildConfig;
import com.vgnary.nyt.thenewshour.constants.UrlConfig;
import com.vgnary.nyt.thenewshour.debug.InAppExceptionHandler;
import com.vgnary.nyt.thenewshour.utils.LogUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;


@ReportsCrashes(httpMethod = HttpSender.Method.PUT,
        formUri = UrlConfig.ACRA_URL,
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE
        },
        mode = ReportingInteractionMode.TOAST
)
public class AppController extends Application {
    private RefWatcher refWatcher;

    private final static String TAG = LogUtils.makeLogTag(AppController.class);

    public static RefWatcher getRefWatcher(Context context) {
        AppController application = (AppController) context.getApplicationContext();
        return application.refWatcher;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        /*refWatcher = LeakCanary.install(this);*/
        Thread.setDefaultUncaughtExceptionHandler(new InAppExceptionHandler(getApplicationContext()));
        refWatcher = LeakCanary.install(this);
        if (!BuildConfig.DEBUG) {
            ACRA.init(this);
        }
    }


}
