package com.onegini.mobile.exampleapp.network;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.onegini.mobile.android.sdk.client.UserClient;
import com.onegini.mobile.android.sdk.handlers.OneginiMobileAuthenticationEnrollmentHandler;
import com.onegini.mobile.android.sdk.handlers.error.OneginiError;
import com.onegini.mobile.android.sdk.handlers.error.OneginiMobileAuthenticationEnrollmentError;
import com.onegini.mobile.exampleapp.Constants;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.storage.GCMStorage;
import com.onegini.mobile.exampleapp.util.AppInfo;

public class GCMRegistrationService {

  private static final String TAG = "GCMDemo";

  private final Context context;
  private final GCMStorage storage;

  private OneginiMobileAuthenticationEnrollmentHandler enrollmentHandler;

  public GCMRegistrationService(final Context context) {
    this.context = context;
    storage = new GCMStorage(context);
  }

  public void registerGCMService(final OneginiMobileAuthenticationEnrollmentHandler handler) {
    enrollmentHandler = handler;
    final String regid = getRegistrationId(context);
    if (regid.isEmpty()) {
      registerInBackground();
    } else {
      enrollForMobileAuthentication(regid);
    }
  }

  /**
   * Gets the current registration ID for application on GCM service. If result is empty, the app needs to register.
   *
   * @return registration ID, or empty string if there is no existing registration ID.
   */
  private String getRegistrationId(final Context context) {
    final String registrationId = storage.getRegistrationId();
    if (registrationId == null || registrationId.isEmpty()) {
      Log.i(TAG, "Registration not found.");
      return "";
    }

    // Check if app was updated; if so, it must clear the registration ID since the existing regID is not guaranteed to work with the new app version.
    final int registeredVersion = storage.getAppVersion();
    final int currentVersion = AppInfo.getAppVersion(context);
    if (registeredVersion != currentVersion) {
      Log.i(TAG, "App version changed.");
      return "";
    }
    return registrationId;
  }

  /**
   * Registers the application with GCM servers asynchronously.
   * Stores the registration ID and app versionCode in the application's
   * shared preferences.
   */
  private void registerInBackground() {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        try {
          final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
          final String regid = gcm.register(Constants.GCM_SENDER_ID);
          enrollForMobileAuthentication(regid);
          storeRegisteredId(regid);
        } catch (final IOException ex) {
          enrollmentHandler.onError(new OneginiMobileAuthenticationEnrollmentError(OneginiError.GENERAL_ERROR, "Unable to register in GCM"));
        }
        return null;
      }
    }.execute(null, null, null);
  }

  private void storeRegisteredId(final String regid) {
    storage.setRegistrationId(regid);
    storage.setAppVersion(AppInfo.getAppVersion(context));
    storage.save();
  }

  private void enrollForMobileAuthentication(final String regId) {
    final UserClient userClient = OneginiSDK.getOneginiClient(context).getUserClient();
    userClient.enrollUserForMobileAuthentication(regId, enrollmentHandler);
  }
}
