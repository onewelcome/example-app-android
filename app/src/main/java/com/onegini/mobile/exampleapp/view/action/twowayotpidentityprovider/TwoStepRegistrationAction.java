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
package com.onegini.mobile.exampleapp.view.action.twowayotpidentityprovider;

import static com.onegini.mobile.exampleapp.view.activity.TwoStepRegistrationActivity.OTP_CHALLENGE_EXTRA;

import android.content.Context;
import android.content.Intent;
import com.onegini.mobile.exampleapp.view.activity.TwoStepRegistrationActivity;
import com.onegini.mobile.sdk.android.handlers.action.OneginiCustomTwoStepRegistrationAction;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiCustomRegistrationCallback;
import com.onegini.mobile.sdk.android.model.entity.CustomInfo;

/**
 * In case of two step registration the SDK asks the app for optional initial registration data.
 * The data is sent to the Token Server where the custom registration is initialized.
 * The optional initialization result is send back to the SDK.
 * Then the SDK asks the app for a registration data, providing the optional initialization data provided by the Token Server.
 * The registration data is sent to the Token Server where the custom registration script is executed.
 * The result of the custom script (status code and optional data) is send back to the SDK and the registration result is propagated to the app.
 */
public class TwoStepRegistrationAction implements OneginiCustomTwoStepRegistrationAction {

  public static OneginiCustomRegistrationCallback CALLBACK;

  private final Context context;

  public TwoStepRegistrationAction(final Context context) {
    this.context = context;
  }

  @Override
  public void initRegistration(final OneginiCustomRegistrationCallback callback, final CustomInfo customInfo) {
    callback.returnSuccess("12345");
  }

  @Override
  public void finishRegistration(final OneginiCustomRegistrationCallback callback, final CustomInfo customInfo) {
    CALLBACK = callback;

    final Intent intent = new Intent(context, TwoStepRegistrationActivity.class);
    intent.putExtra(OTP_CHALLENGE_EXTRA, "12345");
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
