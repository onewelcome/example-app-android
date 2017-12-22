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

package com.onegini.mobile.exampleapp.view.handler;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.onegini.mobile.exampleapp.Constants.COMMAND_FINISH;
import static com.onegini.mobile.exampleapp.Constants.COMMAND_START;
import static com.onegini.mobile.exampleapp.Constants.EXTRA_COMMAND;
import static com.onegini.mobile.exampleapp.model.BasicCustomAuthenticator.BASIC_CUSTOM_AUTHENTICATOR_ID;
import static com.onegini.mobile.exampleapp.model.PasswordCustomAuthenticator.PASSWORD_CUSTOM_AUTHENTICATOR_ID;
import static com.onegini.mobile.exampleapp.view.activity.AuthenticationActivity.EXTRA_MESSAGE;
import static com.onegini.mobile.exampleapp.view.activity.AuthenticationActivity.EXTRA_USER_PROFILE_ID;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationBasicCustomActivity;
import com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationPasswordCustomActivity;
import com.onegini.mobile.sdk.android.handlers.request.OneginiMobileAuthWithPushCustomRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiCustomCallback;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;

public class MobileAuthenticationCustomRequestHandler implements OneginiMobileAuthWithPushCustomRequestHandler {

  private static final String TAG = CustomAuthenticationRequestHandler.class.getSimpleName();

  public static OneginiCustomCallback CALLBACK;

  private final Context context;
  private String userProfileId;
  private String message;

  public MobileAuthenticationCustomRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startAuthentication(final OneginiMobileAuthenticationRequest oneginiMobileAuthenticationRequest,
                                  final String authenticatorId, final OneginiCustomCallback oneginiCustomCallback) {
    CALLBACK = oneginiCustomCallback;
    userProfileId = oneginiMobileAuthenticationRequest.getUserProfile().getProfileId();
    message = oneginiMobileAuthenticationRequest.getMessage();
    notifyActivity(COMMAND_START, authenticatorId);
  }

  @Override
  public void finishAuthentication(final String authenticatorId) {
    notifyActivity(COMMAND_FINISH, authenticatorId);
  }

  private void notifyActivity(final String command, final String authenticatorId) {
    try {
      final Intent intent = createIntent(authenticatorId);
      intent.putExtra(EXTRA_COMMAND, command);
      intent.putExtra(EXTRA_USER_PROFILE_ID, userProfileId);
      intent.putExtra(EXTRA_MESSAGE, message);
      intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    }catch (Exception e){
      Log.d(TAG, e.getMessage());
    }
  }

  private Intent createIntent(final String authenticatorId) throws Exception {
    switch (authenticatorId) {
      case BASIC_CUSTOM_AUTHENTICATOR_ID:
        return new Intent(context, MobileAuthenticationBasicCustomActivity.class);
      case PASSWORD_CUSTOM_AUTHENTICATOR_ID:
        return new Intent(context, MobileAuthenticationPasswordCustomActivity.class);
      default:
        throw new Exception("Authenticator with id: " + authenticatorId + " not implemented yet.");
    }
  }
}
