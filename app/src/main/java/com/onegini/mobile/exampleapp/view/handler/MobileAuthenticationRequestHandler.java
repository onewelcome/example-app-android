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
import com.onegini.mobile.exampleapp.view.activity.AbstractMobileAuthenticationActivity;
import com.onegini.mobile.exampleapp.view.activity.AuthenticationActivity;
import com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationActivity;
import com.onegini.mobile.sdk.android.handlers.request.OneginiMobileAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiAcceptDenyCallback;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;

public class MobileAuthenticationRequestHandler implements OneginiMobileAuthenticationRequestHandler {

  public static OneginiAcceptDenyCallback CALLBACK;

  private final Context context;

  public MobileAuthenticationRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startAuthentication(final OneginiMobileAuthenticationRequest oneginiMobileAuthenticationRequest,
                                  final OneginiAcceptDenyCallback oneginiAcceptDenyCallback) {
    CALLBACK = oneginiAcceptDenyCallback;
    openActivity(oneginiMobileAuthenticationRequest.getUserProfile().getProfileId(), oneginiMobileAuthenticationRequest.getMessage());
  }

  @Override
  public void finishAuthentication() {
    closeActivity();
  }

  private void openActivity(final String profileId, final String message) {
    final Intent intent = new Intent(context, MobileAuthenticationActivity.class);
    intent.putExtra(AbstractMobileAuthenticationActivity.EXTRA_COMMAND, MobileAuthenticationActivity.COMMAND_START);
    intent.putExtra(AuthenticationActivity.EXTRA_MESSAGE, message);
    intent.putExtra(AuthenticationActivity.EXTRA_USER_PROFILE_ID, profileId);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  private void closeActivity() {
    final Intent intent = new Intent(context, MobileAuthenticationActivity.class);
    intent.putExtra(MobileAuthenticationActivity.EXTRA_COMMAND, MobileAuthenticationActivity.COMMAND_FINISH);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
