package com.onegini.mobile.exampleapp.network.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.onegini.mobile.android.sdk.handlers.OneginiMobileAuthenticationHandler;
import com.onegini.mobile.android.sdk.handlers.error.OneginiMobileAuthenticationError;
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
      OneginiSDK.getOneginiClient(this).getUserClient().handleMobileAuthenticationRequest(extras, new OneginiMobileAuthenticationHandler() {
        @Override
        public void onSuccess() {
          Toast.makeText(GCMIntentService.this, "Mobile authentication success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(final OneginiMobileAuthenticationError oneginiMobileAuthenticationError) {
          Toast.makeText(GCMIntentService.this, oneginiMobileAuthenticationError.getErrorDescription(), Toast.LENGTH_SHORT).show();
        }
      });
    }
    // Release the wake lock provided by the WakefulBroadcastReceiver.
    GCMBroadcastReceiver.completeWakefulIntent(intent);
  }
}
