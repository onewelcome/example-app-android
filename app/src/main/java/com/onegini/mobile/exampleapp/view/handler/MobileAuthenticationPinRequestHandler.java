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
package com.onegini.mobile.exampleapp.view.handler;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import com.onegini.mobile.sdk.android.handlers.request.OneginiMobileAuthenticationPinRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.sdk.android.model.entity.AuthenticationAttemptCounter;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;
import com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationPinActivity;

public class MobileAuthenticationPinRequestHandler implements OneginiMobileAuthenticationPinRequestHandler {

  public static OneginiPinCallback CALLBACK;

  private final Context context;

  private String message;
  private String userProfileId;
  private int failedAttemptsCount;
  private int maxAttemptsCount;

  public MobileAuthenticationPinRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startAuthentication(final OneginiMobileAuthenticationRequest oneginiMobileAuthenticationRequest, final OneginiPinCallback oneginiPinCallback, final AuthenticationAttemptCounter attemptCounter) {
    CALLBACK = oneginiPinCallback;
    message = oneginiMobileAuthenticationRequest.getMessage();
    userProfileId = oneginiMobileAuthenticationRequest.getUserProfile().getProfileId();
    failedAttemptsCount = maxAttemptsCount = 0;
    startActivity();
  }

  @Override
  public void onNextAuthenticationAttempt(final AuthenticationAttemptCounter attemptCounter) {
    this.failedAttemptsCount = attemptCounter.getFailedAttempts();
    this.maxAttemptsCount = attemptCounter.getMaxAttempts();
    startActivity();
  }

  @Override
  public void finishAuthentication() {
    closeActivity();
  }

  private void startActivity() {
    final Intent intent = new Intent(context, MobileAuthenticationPinActivity.class);
    intent.putExtra(MobileAuthenticationPinActivity.EXTRA_COMMAND, MobileAuthenticationPinActivity.COMMAND_START);
    intent.putExtra(MobileAuthenticationPinActivity.EXTRA_MESSAGE, message);
    intent.putExtra(MobileAuthenticationPinActivity.EXTRA_PROFILE_ID, userProfileId);
    intent.putExtra(MobileAuthenticationPinActivity.EXTRA_FAILED_ATTEMPTS_COUNT, failedAttemptsCount);
    intent.putExtra(MobileAuthenticationPinActivity.EXTRA_MAX_FAILED_ATTEMPTS, maxAttemptsCount);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

    context.startActivity(intent);
  }

  private void closeActivity() {
    final Intent intent = new Intent(context, MobileAuthenticationPinActivity.class);
    intent.putExtra(MobileAuthenticationPinActivity.EXTRA_COMMAND, MobileAuthenticationPinActivity.COMMAND_FINISH);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
