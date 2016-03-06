package com.vgnary.nyt.thenewshour.debug;

import android.content.Context;
import android.content.Intent;


import com.vgnary.nyt.thenewshour.BuildConfig;
import com.vgnary.nyt.thenewshour.utils.LogUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Handles Exception and shows crash stack trace in other activity when
 * application is under debug mode.
 * <p/>
 * This class is written only for developer's convenience.When ever application
 * crashes then rather that looking at crash from logcat ,crash stack trace
 * appears on screen of device.
 * <p/>
 * This feature is automatically disabled when build is in production mode.
 * <p/>
 *
 * @author Gagandeep Singh
 * @version 1.0
 * @since 1.0
 */

public class InAppExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final static String TAG = LogUtils.makeLogTag(InAppExceptionHandler.class);
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;
    private Context mContext;

    public InAppExceptionHandler(Context context) {
        mContext = context;
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    /**
     * Handles generated exception ,parses stack trace ,passes string to activity
     * {@link ForceCloseActivity}.
     *
     * @param thread thread from which exception generates
     * @param ex     actual exception
     */
    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String exception = getStackTrace(ex);
                Intent intent;
                if (BuildConfig.DEBUG) {
                    intent = new Intent(mContext, ForceCloseActivity.class);
                    intent.putExtra("crash_report", exception);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(10);

                }
            }
        }).start();


        //mDefaultExceptionHandler.uncaughtException(thread,ex);
    }

    /**
     * Parses generated exception and converts stack trace into string.
     *
     * @param th application generated exception
     * @return string representation of stack trace
     */
    private String getStackTrace(Throwable th) {

        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);

        // If the exception was thrown in a background thread inside
        // AsyncTask, then the actual exception can be found with getCause
        Throwable cause = th;
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        final String stacktraceAsString = result.toString();
        printWriter.close();

        return stacktraceAsString;
    }
}
