/*
 * Copyright (c) 2016-2017 Onegini B.V.
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

package com.onegini.mobile.exampleapp.network.fcm;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.onegini.mobile.exampleapp.util.DeregistrationUtil;
import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthWithPushEnrollmentHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthWithPushEnrollmentError;

public class FCMInstanceIdService extends FirebaseInstanceIdService {

  /**
   * Called if InstanceID token is updated. This may occur if the security of the previous token had been compromised. This call is initiated by the InstanceID
   * provider.
   */
  @Override
  public void onTokenRefresh() {
    final OneginiMobileAuthWithPushEnrollmentHandler mobileAuthWithPushEnrollmentHandler = new OneginiMobileAuthWithPushEnrollmentHandler() {
      @Override
      public void onSuccess() {

      }

      @Override
      public void onError(final OneginiMobileAuthWithPushEnrollmentError error) {
        @OneginiMobileAuthWithPushEnrollmentError.MobileAuthWithPushEnrollmentErrorType final int errorType = error.getErrorType();
        // This method is called when a mobile authentication enrollment error occurs, for example when the device is deregistered
        if (errorType == OneginiMobileAuthWithPushEnrollmentError.DEVICE_DEREGISTERED) {
          new DeregistrationUtil(FCMInstanceIdService.this).onDeviceDeregistered();
        }
      }
    };
    final FCMRegistrationService FCMRegistrationService = new FCMRegistrationService(this);
    FCMRegistrationService.registerFCMService(mobileAuthWithPushEnrollmentHandler);
  }
}
