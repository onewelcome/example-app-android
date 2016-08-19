package com.onegini.mobile.exampleapp.network.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * A {@link WakefulBroadcastReceiver} class for the Push Notifications
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

  @Override
  public void onReceive(final Context context, final Intent intent) {
    final ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
    startWakefulService(context, (intent.setComponent(comp)));
    setResultCode(Activity.RESULT_OK);
  }

}
