package com.onegini.mobile.exampleapp.network.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.onegini.mobile.android.sdk.client.OneginiClient;
import com.onegini.mobile.android.sdk.handlers.OneginiMobileAuthenticationEnrollmentHandler;
import com.onegini.mobile.android.sdk.handlers.error.OneginiMobileAuthenticationEnrollmentError;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.storage.SettingsStorage;

import java.io.IOException;

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
    final InstanceID instanceID = InstanceID.getInstance(this);
    final String token;
    try {
      token = instanceID.getToken(getString(R.string.gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
      persistMobileAuthenticationEnabled();
      enrollMobileAuthentication(token);
    } catch (IOException ex) {
      Log.e(TAG, ex.getLocalizedMessage());
    }

    releaseWakeLock(intent);
  }

  private void enrollMobileAuthentication(final String token) {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(this);
    oneginiClient.getUserClient().enrollUserForMobileAuthentication(token, new OneginiMobileAuthenticationEnrollmentHandler() {
      @Override
      public void onSuccess() {
        Log.i(TAG, "Mobile authentication enrolled successfully");
      }

      @Override
      public void onError(final OneginiMobileAuthenticationEnrollmentError oneginiMobileAuthenticationEnrollmentError) {
        Log.e(TAG, oneginiMobileAuthenticationEnrollmentError.getErrorDescription());
      }
    });
  }

  private void persistMobileAuthenticationEnabled() {
    final SettingsStorage settingsStorage = new SettingsStorage(this);
    settingsStorage.saveMobileAuthenticationEnabled(true);
  }

  private void releaseWakeLock(final Intent intent) {
    GcmBroadcastReceiver.completeWakefulIntent(intent);
  }
}
