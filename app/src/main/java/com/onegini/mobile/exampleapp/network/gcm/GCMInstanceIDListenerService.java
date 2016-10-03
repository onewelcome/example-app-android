/*
 * Copyright (c) 2016 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.onegini.mobile.exampleapp.network.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.storage.SettingsStorage;
import com.onegini.mobile.exampleapp.util.DeregistrationUtil;
import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthenticationEnrollmentHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthenticationEnrollmentError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class GCMInstanceIDListenerService extends InstanceIDListenerService {

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
        if (oneginiMobileAuthenticationEnrollmentError.getErrorType() == OneginiMobileAuthenticationEnrollmentError.DEVICE_DEREGISTERED) {
          new DeregistrationUtil(GCMInstanceIDListenerService.this).onDeviceDeregistered();
        }
        setMobileAuthenticationEnabled(false);
      }
    };
    final GCMRegistrationService gcmRegistrationService = new GCMRegistrationService(this);
    gcmRegistrationService.registerGCMService(mobileAuthenticationEnrollmentHandler);
  }

  private void setMobileAuthenticationEnabled(final boolean isEnabled) {
    UserProfile authenticatedUserProfile = OneginiSDK.getOneginiClient(this).getUserClient().getAuthenticatedUserProfile();
    if (authenticatedUserProfile == null) {
      return;
    }

    final SettingsStorage settingsStorage = new SettingsStorage(GCMInstanceIDListenerService.this);
    settingsStorage.setMobileAuthenticationEnabled(authenticatedUserProfile, isEnabled);
  }
}
