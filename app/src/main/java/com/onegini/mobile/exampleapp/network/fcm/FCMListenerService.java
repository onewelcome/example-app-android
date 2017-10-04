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

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import java.util.Set;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.util.DeregistrationUtil;
import com.onegini.mobile.exampleapp.view.activity.LoginActivity;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.exception.OneginiInitializationException;
import com.onegini.mobile.sdk.android.handlers.OneginiInitializationHandler;
import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthenticationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiInitializationError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthenticationError;
import com.onegini.mobile.sdk.android.model.entity.CustomAuthenticatorInfo;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class FCMListenerService extends FirebaseMessagingService {

  private static final String TAG = FCMListenerService.class.getSimpleName();


  @Override
  public void onMessageReceived(final RemoteMessage message) {
    if (message != null) {
      Log.i(TAG, "Push message received");

      try {
        handleMobileAuthenticationRequest(message);
      } catch (OneginiInitializationException exception) {
        // Onegini SDK hasn't been started yet so we have to do it
        // before handling the mobile authentication request
        setupOneginiSDK(message);
      }
    }
  }

  private void setupOneginiSDK(final RemoteMessage extras) {
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
        @OneginiInitializationError.InitializationErrorType final int errorType = error.getErrorType();
        if (errorType == OneginiInitializationError.DEVICE_DEREGISTERED) {
          new DeregistrationUtil(getApplicationContext()).onDeviceDeregistered();
        }
        showToast(error.getMessage());
      }
    });
  }

  private void handleMobileAuthenticationRequest(final RemoteMessage data) {
    OneginiSDK.getOneginiClient(this).getUserClient().handleMobileAuthWithPushRequest(data, new OneginiMobileAuthenticationHandler() {
      @Override
      public void onSuccess(final CustomAuthenticatorInfo customAuthenticatorInfo) {
        Toast.makeText(FCMListenerService.this, "Mobile authentication success", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onError(final OneginiMobileAuthenticationError oneginiMobileAuthenticationError) {
        showToast(oneginiMobileAuthenticationError.getMessage());
        @OneginiMobileAuthenticationError.MobileAuthenticationErrorType final int errorType = oneginiMobileAuthenticationError.getErrorType();
        if (errorType == OneginiMobileAuthenticationError.USER_DEREGISTERED) {
          // the user was deregister, for example he provided a wrong PIN for too many times. You can handle the deregistration here, but since this application
          // supports multiple profiles we handle it when the user tries to login the next time because we don't know which user profile was deregistered at
          // this point.
          startLoginActivity();
        } else if (errorType == OneginiMobileAuthenticationError.DEVICE_DEREGISTERED) {
          new DeregistrationUtil(getApplicationContext()).onDeviceDeregistered();
        }
      }
    });
  }

  private void showToast(final String errorDescription) {
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(() -> Toast.makeText(getApplicationContext(), errorDescription, Toast.LENGTH_SHORT).show());
  }

  private void removeUserProfiles(final Set<UserProfile> removedUserProfiles, final RemoteMessage extras) {
    final DeregistrationUtil deregistrationUtil = new DeregistrationUtil(this);
    removedUserProfiles.forEach(deregistrationUtil::onUserDeregistered);
    handleMobileAuthenticationRequest(extras);
  }

  private void startLoginActivity() {
    final Intent intent = new Intent(this, LoginActivity.class);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
  }
}
