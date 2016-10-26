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

import static com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationFingerprintActivity.MSG_EXTRA_ASK_TO_ACCEPT_OR_DENY;
import static com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationFingerprintActivity.MSG_EXTRA_CLOSE;
import static com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationFingerprintActivity.MSG_EXTRA_RECEIVED_FINGERPRINT;
import static com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationFingerprintActivity.MSG_EXTRA_SHOW_SCANNING;

import android.content.Context;
import android.content.Intent;
import com.onegini.mobile.exampleapp.view.activity.AuthenticationActivity;
import com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationFingerprintActivity;
import com.onegini.mobile.sdk.android.handlers.request.OneginiMobileAuthenticationFingerprintRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiFingerprintCallback;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;

public class MobileAuthenticationFingerprintRequestHandler implements OneginiMobileAuthenticationFingerprintRequestHandler {

  public static OneginiFingerprintCallback fingerprintCallback;

  private static String message;
  private static String userProfileId;

  private final Context context;

  public MobileAuthenticationFingerprintRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startAuthentication(final OneginiMobileAuthenticationRequest oneginiMobileAuthenticationRequest, final OneginiFingerprintCallback oneginiFingerprintCallback) {
    fingerprintCallback = oneginiFingerprintCallback;
    message = oneginiMobileAuthenticationRequest.getMessage();
    userProfileId = oneginiMobileAuthenticationRequest.getUserProfile().getProfileId();
    startFingerprintActivity(MSG_EXTRA_ASK_TO_ACCEPT_OR_DENY);
  }

  @Override
  public void onNextAuthenticationAttempt() {
    startFingerprintActivity(MSG_EXTRA_RECEIVED_FINGERPRINT);
  }

  @Override
  public void onFingerprintCaptured() {
    startFingerprintActivity(MSG_EXTRA_SHOW_SCANNING);
  }

  @Override
  public void finishAuthentication() {
    startFingerprintActivity(MSG_EXTRA_CLOSE);
  }

  private void startFingerprintActivity(final String action) {
    final Intent intent = new Intent(context, MobileAuthenticationFingerprintActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.putExtra(MobileAuthenticationFingerprintActivity.EXTRA_ACTION, action);
    intent.putExtra(AuthenticationActivity.EXTRA_MESSAGE, message);
    intent.putExtra(AuthenticationActivity.EXTRA_USER_PROFILE_ID, userProfileId);
    context.startActivity(intent);
  }
}
