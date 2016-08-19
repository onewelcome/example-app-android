package com.onegini.mobile.exampleapp.network.gcm;

import android.app.IntentService;
import android.content.Intent;

import com.onegini.mobile.android.sdk.handlers.OneginiMobileAuthenticationHandler;
import com.onegini.mobile.android.sdk.handlers.error.OneginiMobileAuthenticationError;
import com.onegini.mobile.exampleapp.OneginiSDK;

/**
 * An {@link IntentService} to handle Push Messages
 */
public class GcmIntentService extends IntentService {

  private static final String TAG = GcmIntentService.class.getSimpleName();

  /**
   * Creates an instance of this {@link IntentService}
   */
  public GcmIntentService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(final Intent intent) {
    OneginiSDK.getOneginiClient(this).getUserClient().handleMobileAuthenticationRequest(intent.getExtras(), new OneginiMobileAuthenticationHandler() {

      @Override
      public void onSuccess() {

      }

      @Override
      public void onError(final OneginiMobileAuthenticationError oneginiMobileAuthenticationError) {

      }
    });

    releaseWakeLock(intent);
  }

  private void releaseWakeLock(final Intent intent) {
    GcmBroadcastReceiver.completeWakefulIntent(intent);
  }
}
