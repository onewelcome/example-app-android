package com.onegini.mobile.exampleapp.network.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.onegini.mobile.exampleapp.OneginiSDK;

public class GCMIntentService extends IntentService {

  private static final String TAG = GCMIntentService.class.getSimpleName();

  public GCMIntentService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(final Intent intent) {
    final Bundle extras = intent.getExtras();
    if (!extras.isEmpty()) {
      Log.i(TAG, "Push message received");
      OneginiSDK.getOneginiClient(this).getUserClient().handlePushNotification(extras);
    }
    // Release the wake lock provided by the WakefulBroadcastReceiver.
    GCMBroadcastReceiver.completeWakefulIntent(intent);
  }
}
