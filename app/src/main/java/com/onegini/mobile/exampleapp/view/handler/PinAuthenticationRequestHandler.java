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

import android.content.Context;
import android.content.Intent;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.activity.AuthenticationActivity;
import com.onegini.mobile.exampleapp.view.activity.PinActivity;
import com.onegini.mobile.sdk.android.handlers.request.OneginiPinAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.sdk.android.model.entity.AuthenticationAttemptCounter;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class PinAuthenticationRequestHandler implements OneginiPinAuthenticationRequestHandler {

  public static OneginiPinCallback pinCallback;

  private static String userProfileId;

  private final Context context;
  private int failedAttemptsCount;
  private int maxAttemptsCount;

  public PinAuthenticationRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startAuthentication(final UserProfile userProfile, final OneginiPinCallback oneginiPinCallback,
                                  final AuthenticationAttemptCounter attemptCounter) {
    userProfileId = userProfile.getProfileId();
    pinCallback = oneginiPinCallback;
    failedAttemptsCount = maxAttemptsCount = 0;

    PinActivity.setIsCreatePinFlow(false);
    startPinActivity();
  }

  @Override
  public void onNextAuthenticationAttempt(final AuthenticationAttemptCounter attemptCounter) {
    failedAttemptsCount = attemptCounter.getFailedAttempts();
    maxAttemptsCount = attemptCounter.getMaxAttempts();
    startPinActivity();
  }

  @Override
  public void finishAuthentication() {
  }

  private void startPinActivity() {
    final Intent intent = new Intent(context, PinActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(AuthenticationActivity.EXTRA_MESSAGE, context.getString(R.string.authenticator_message_enter_pin));
    intent.putExtra(AuthenticationActivity.EXTRA_USER_PROFILE_ID, userProfileId);
    intent.putExtra(PinActivity.EXTRA_FAILED_ATTEMPTS_COUNT, failedAttemptsCount);
    intent.putExtra(PinActivity.EXTRA_MAX_FAILED_ATTEMPTS, maxAttemptsCount);
    context.startActivity(intent);
  }
}
