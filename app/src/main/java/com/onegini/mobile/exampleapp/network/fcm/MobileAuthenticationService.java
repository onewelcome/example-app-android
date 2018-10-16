/*
 * Copyright (c) 2016-2018 Onegini B.V.
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

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.util.DeregistrationUtil;
import com.onegini.mobile.exampleapp.view.handler.InitializationHandler;
import com.onegini.mobile.exampleapp.view.helper.OneginiClientInitializer;
import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthenticationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthenticationError;
import com.onegini.mobile.sdk.android.model.entity.CustomInfo;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthWithPushRequest;

public class MobileAuthenticationService extends IntentService {

  private static final String TAG = MobileAuthenticationService.class.getSimpleName();

  public static final String EXTRA_TRANSACTION_ID = "transaction_id";
  public static final String EXTRA_PROFILE_ID = "profile_id";
  public static final String EXTRA_MESSAGE = "message";

  public MobileAuthenticationService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(@Nullable final Intent intent) {
    final OneginiMobileAuthWithPushRequest request = parseRequest(intent);
    if (request != null) {
      Log.d(TAG, "Mobile authentication request " + request.getTransactionId() + " received");
      if (OneginiSDK.getOneginiClient(this).isInitialized()) {
        handleMobileAuthenticationRequest(request);
      } else {
        handleRequestAfterInitialization(request);
      }
    }
  }

  @Nullable
  private OneginiMobileAuthWithPushRequest parseRequest(final Intent intent) {
    if (intent != null) {
      final Bundle extras = intent.getExtras();
      if (extras != null) {
        final String transactionId = extras.getString(EXTRA_TRANSACTION_ID);
        final String profileId = extras.getString(EXTRA_PROFILE_ID);
        final String message = extras.getString(EXTRA_MESSAGE);
        return new OneginiMobileAuthWithPushRequest(transactionId, message, profileId);
      }
    }
    return null;
  }

  private void handleRequestAfterInitialization(final OneginiMobileAuthWithPushRequest request) {
    final OneginiClientInitializer oneginiClientInitializer = new OneginiClientInitializer(this);
    oneginiClientInitializer.startOneginiClient(new InitializationHandler() {
      @Override
      public void onSuccess() {
        handleMobileAuthenticationRequest(request);
      }

      @Override
      public void onError(final String errorMessage) {
        Log.e(TAG, errorMessage);
      }
    });
  }

  private void handleMobileAuthenticationRequest(final OneginiMobileAuthWithPushRequest request) {
    OneginiSDK.getOneginiClient(this).getUserClient().handleMobileAuthWithPushRequest(request, new OneginiMobileAuthenticationHandler() {
      @Override
      public void onSuccess(final CustomInfo customInfo) {
        Log.i(TAG, "The mobile authentication request " + request.getTransactionId() + " has finished with success");
      }

      @Override
      public void onError(final OneginiMobileAuthenticationError oneginiMobileAuthenticationError) {
        Log.e(TAG, "The mobile authentication request  " + request.getTransactionId() +
            " has finished with error: " + oneginiMobileAuthenticationError.getMessage());
        @OneginiMobileAuthenticationError.MobileAuthenticationErrorType final int errorType = oneginiMobileAuthenticationError.getErrorType();
        if (errorType == OneginiMobileAuthenticationError.DEVICE_DEREGISTERED) {
          new DeregistrationUtil(getApplicationContext()).onDeviceDeregistered();
        }
      }
    });
  }
}
