package com.onegini.mobile.exampleapp.network.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {

  @Override
  public void onReceive(final Context context, final Intent intent) {
    // Explicitly specify that GcmIntentService will handle the intent.
    final ComponentName comp = new ComponentName(context.getPackageName(), GCMIntentService.class.getName());
    // Start the service, keeping the device awake while it is launching.
    startWakefulService(context, (intent.setComponent(comp)));
    setResultCode(Activity.RESULT_OK);
  }
}
