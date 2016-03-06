package com.vgnary.nyt.thenewshour.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vgnary.nyt.thenewshour.utils.AppUtils;

public class AlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AppUtils.clearSavedImages(context);

    }
}
