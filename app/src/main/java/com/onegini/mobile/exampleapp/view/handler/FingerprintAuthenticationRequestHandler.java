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

import static com.onegini.mobile.exampleapp.view.activity.FingerprintActivity.EXTRA_CLOSE;
import static com.onegini.mobile.exampleapp.view.activity.FingerprintActivity.EXTRA_RECEIVED_FINGERPRINT;
import static com.onegini.mobile.exampleapp.view.activity.FingerprintActivity.EXTRA_SHOW_SCANNING;
import static com.onegini.mobile.exampleapp.view.activity.FingerprintActivity.EXTRA_START;

import android.content.Context;
import android.content.Intent;
import com.onegini.mobile.exampleapp.view.activity.AuthenticationActivity;
import com.onegini.mobile.exampleapp.view.activity.FingerprintActivity;
import com.onegini.mobile.sdk.android.handlers.request.OneginiFingerprintAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiFingerprintCallback;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class FingerprintAuthenticationRequestHandler implements OneginiFingerprintAuthenticationRequestHandler {

  public static OneginiFingerprintCallback fingerprintCallback;

  private static String userProfileId;

  private final Context context;

  public FingerprintAuthenticationRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startAuthentication(final UserProfile userProfile, final OneginiFingerprintCallback oneginiFingerprintCallback) {
    fingerprintCallback = oneginiFingerprintCallback;
    userProfileId = userProfile.getProfileId();
    startFingerprintActivity(EXTRA_START);
  }

  @Override
  public void onNextAuthenticationAttempt() {
    startFingerprintActivity(EXTRA_RECEIVED_FINGERPRINT);
  }

  @Override
  public void onFingerprintCaptured() {
    startFingerprintActivity(EXTRA_SHOW_SCANNING);
  }

  @Override
  public void finishAuthentication() {
    startFingerprintActivity(EXTRA_CLOSE);
  }

  private void startFingerprintActivity(final String action) {
    final Intent intent = new Intent(context, FingerprintActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.putExtra(FingerprintActivity.EXTRA_ACTION, action);
    intent.putExtra(AuthenticationActivity.EXTRA_USER_PROFILE_ID, userProfileId);
    context.startActivity(intent);
  }
}
