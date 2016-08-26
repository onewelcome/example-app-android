package com.onegini.mobile.exampleapp.network.gcm;

import java.util.Set;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.onegini.mobile.android.sdk.client.OneginiClient;
import com.onegini.mobile.android.sdk.exception.OneginiInitializationException;
import com.onegini.mobile.android.sdk.handlers.OneginiInitializationHandler;
import com.onegini.mobile.android.sdk.handlers.OneginiMobileAuthenticationHandler;
import com.onegini.mobile.android.sdk.handlers.error.OneginiError;
import com.onegini.mobile.android.sdk.handlers.error.OneginiInitializationError;
import com.onegini.mobile.android.sdk.handlers.error.OneginiMobileAuthenticationError;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.storage.SettingsStorage;
import com.onegini.mobile.exampleapp.storage.UserStorage;

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

      try {
        handleMobileAuthenticationRequest(extras);
      } catch (OneginiInitializationException exception) {
        // Onegini SDK hasn't been started yet so we have to do it
        // before handling the mobile authentication request
        setupOneginiSDK(extras);
      }
    }
    // Release the wake lock provided by the WakefulBroadcastReceiver.
    GCMBroadcastReceiver.completeWakefulIntent(intent);
  }

  private void setupOneginiSDK(final Bundle extras) {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(this);
    oneginiClient.start(new OneginiInitializationHandler() {
      @Override
      public void onSuccess(final Set<UserProfile> removedUserProfiles) {
        if (removedUserProfiles.isEmpty()) {
          handleMobileAuthenticationRequest(extras);
        } else {
          removeUserProfiles(removedUserProfiles, extras);
        }
      }

      @Override
      public void onError(final OneginiInitializationError error) {
        Toast.makeText(GCMIntentService.this, error.getErrorDescription(), Toast.LENGTH_LONG).show();
      }
    });
  }

  private void handleMobileAuthenticationRequest(final Bundle extras) {
    OneginiSDK.getOneginiClient(this).getUserClient().handleMobileAuthenticationRequest(extras, new OneginiMobileAuthenticationHandler() {
      @Override
      public void onSuccess() {
        Toast.makeText(GCMIntentService.this, "Mobile authentication success", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onError(final OneginiMobileAuthenticationError oneginiMobileAuthenticationError) {
        Toast.makeText(GCMIntentService.this, oneginiMobileAuthenticationError.getErrorDescription(), Toast.LENGTH_SHORT).show();
        if (oneginiMobileAuthenticationError.getErrorType() == OneginiError.USER_DEREGISTERED) {
          new SettingsStorage(GCMIntentService.this).setMobileAuthenticationEnabled(false);
        }
      }
    });
  }

  private void removeUserProfiles(final Set<UserProfile> removedUserProfiles, final Bundle extras) {
    final UserStorage userStorage = new UserStorage(this);
    for (final UserProfile userProfile : removedUserProfiles) {
      userStorage.removeUser(userProfile);
    }
    handleMobileAuthenticationRequest(extras);
  }
}
