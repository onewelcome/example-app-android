package com.onegini.mobile.exampleapp.network.gcm;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.onegini.mobile.android.sdk.client.OneginiClient;
import com.onegini.mobile.android.sdk.handlers.OneginiMobileAuthenticationEnrollmentHandler;
import com.onegini.mobile.android.sdk.handlers.error.OneginiMobileAuthenticationEnrollmentError;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.storage.SettingsStorage;

import java.io.IOException;

import static com.onegini.mobile.exampleapp.OneginiSDK.getOneginiClient;

/**
 * Created by azul on 8/19/16.
 */
public class GcmHelper {

  private static final String TAG = GcmHelper.class.getSimpleName();
  private static final String SENDER_ID = "586427927998";

  private final Context context;

  public GcmHelper(final Context context) {
    this.context = context;
  }

  public void enrollMobileAuthentication() {
    final OneginiClient oneginiClient = getOneginiClient(context);
    final String token = getPushRegistrationId();
    if (!TextUtils.isEmpty(token)) {
      oneginiClient.getUserClient().enrollUserForMobileAuthentication(token, new OneginiMobileAuthenticationEnrollmentHandler() {
        @Override
        public void onSuccess() {
          storeMobileEnrollmentSuccess();
          Log.i(TAG, "Mobile authentication enrolled successfully");
        }

        @Override
        public void onError(final OneginiMobileAuthenticationEnrollmentError oneginiMobileAuthenticationEnrollmentError) {
          Log.e(TAG, oneginiMobileAuthenticationEnrollmentError.getErrorDescription());
        }
      });
    }
  }

  @Nullable
  private String getPushRegistrationId() {
    final InstanceID instanceID = InstanceID.getInstance(context);
    String token = null;
    try {
      token = instanceID.getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
    } catch (IOException ex) {
      Log.e(TAG, ex.getLocalizedMessage());
    }

    return token;
  }

  private void storeMobileEnrollmentSuccess() {
    final SettingsStorage settingsStorage = new SettingsStorage(context);
    final String profileId = OneginiSDK.getOneginiClient(context).getUserClient().getAuthenticatedUserProfile().getProfileId();
    settingsStorage.saveUserMobileEnrollment(profileId);
  }

}
