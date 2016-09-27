package com.onegini.mobile.exampleapp.network.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.onegini.mobile.exampleapp.storage.SettingsStorage;
import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthenticationEnrollmentHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthenticationEnrollmentError;

public class MyInstanceIDListenerService extends InstanceIDListenerService {

  /**
   * Called if InstanceID token is updated. This may occur if the security of the previous token had been compromised. This call is initiated by the InstanceID
   * provider.
   */
  @Override
  public void onTokenRefresh() {

    final OneginiMobileAuthenticationEnrollmentHandler mobileAuthenticationEnrollmentHandler = new OneginiMobileAuthenticationEnrollmentHandler() {
      @Override
      public void onSuccess() {
        setMobileAuthenticationEnabled(true);
      }

      @Override
      public void onError(final OneginiMobileAuthenticationEnrollmentError oneginiMobileAuthenticationEnrollmentError) {
        setMobileAuthenticationEnabled(false);
      }
    };
    final GCMRegistrationService gcmRegistrationService = new GCMRegistrationService(this);
    gcmRegistrationService.registerGCMService(mobileAuthenticationEnrollmentHandler);
  }

  private void setMobileAuthenticationEnabled(final boolean isEnabled) {
    SettingsStorage settingsStorage = new SettingsStorage(MyInstanceIDListenerService.this);
    settingsStorage.setMobileAuthenticationEnabled(isEnabled);
  }
}
