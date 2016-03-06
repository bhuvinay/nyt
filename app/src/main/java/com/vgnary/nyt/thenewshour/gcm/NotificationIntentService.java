package com.vgnary.nyt.thenewshour.gcm;



import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.vgnary.nyt.thenewshour.R;
import com.vgnary.nyt.thenewshour.activities.SplashScreen;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Random;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotificationIntentService extends IntentService {

    public static final int DEFAULT_SOUND = 1;
    public static final String notifTitle = "Nyt Updated";
    PendingIntent mNotificationClickIntent;
    private Random mRandomGenerator;

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("url", "on handle intent called" + String.valueOf(intent));
        if (intent != null) {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            generateNotification("abcd", "abcd", "abcd", "abcd");
            NotificationReceiver.completeWakefulIntent(intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void generateNotification(String msg, String from, String date, String title) {
        mRandomGenerator=  new Random();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.ic_splash);
        notificationBuilder.setContentTitle(notifTitle);
        notificationBuilder.setContentText("News Feed");
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        NotificationManager notifManage = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        setNotificationAction();
        notificationBuilder.setContentIntent(mNotificationClickIntent);
        notifManage.notify(mRandomGenerator.nextInt(), notificationBuilder.build());
    }

    private void setNotificationAction() {
        Intent resultIntent = new Intent(this, SplashScreen.class);
        mNotificationClickIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
    }


}


