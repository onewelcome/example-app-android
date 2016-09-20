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
package com.onegini.mobile.exampleapp;

import android.content.Context;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.client.OneginiClientBuilder;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationPinRequestHandler;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationRequestHandler;
import com.onegini.mobile.exampleapp.view.handler.CreatePinRequestHandler;
import com.onegini.mobile.exampleapp.view.handler.PinAuthenticationRequestHandler;

public class OneginiSDK {

  public static OneginiClient getOneginiClient(final Context context) {
    OneginiClient oneginiClient = OneginiClient.getInstance();
    if (oneginiClient == null) {
      oneginiClient = buildSDK(context);
    }
    return oneginiClient;
  }

  private static OneginiClient buildSDK(final Context context) {
    final Context applicationContext = context.getApplicationContext();
    final CreatePinRequestHandler createPinRequestHandler = new CreatePinRequestHandler(applicationContext);
    final PinAuthenticationRequestHandler pinAuthenticationRequestHandler = new PinAuthenticationRequestHandler(applicationContext);

    // will throw OneginiConfigNotFoundException if OneginiConfigModel class can't be found
    return new OneginiClientBuilder(applicationContext, createPinRequestHandler, pinAuthenticationRequestHandler)
        .setMobileAuthenticationRequestHandler(new MobileAuthenticationRequestHandler(applicationContext))
        .setMobileAuthenticationPinRequestHandler(new MobileAuthenticationPinRequestHandler(applicationContext))
        .build();
  }
}
