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

package com.onegini.mobile.exampleapp;

import android.content.Context;
import com.onegini.mobile.exampleapp.model.BasicCustomAuthenticator;
import com.onegini.mobile.exampleapp.model.PasswordCustomAuthenticator;
import com.onegini.mobile.exampleapp.model.QrCodeIdentityProvider;
import com.onegini.mobile.exampleapp.model.StatelessIdentityProvider;
import com.onegini.mobile.exampleapp.model.TwoStepIdentityProvider;
import com.onegini.mobile.exampleapp.view.handler.BasicCustomAuthenticationRequestHandler;
import com.onegini.mobile.exampleapp.view.handler.BiometricAuthenticationRequestHandler;
import com.onegini.mobile.exampleapp.view.handler.CreatePinRequestHandler;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthOtpRequestHandler;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationBasicCustomRequestHandler;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationBiometricRequestHandler;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationPinRequestHandler;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationRequestHandler;
import com.onegini.mobile.exampleapp.view.handler.PinAuthenticationRequestHandler;
import com.onegini.mobile.exampleapp.view.handler.RegistrationRequestHandler;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.client.OneginiClientBuilder;
import com.onegini.mobile.sdk.android.model.OneginiCustomAuthenticator;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    final RegistrationRequestHandler registrationRequestHandler = new RegistrationRequestHandler(applicationContext);
    final CreatePinRequestHandler createPinRequestHandler = new CreatePinRequestHandler(applicationContext);
    final PinAuthenticationRequestHandler pinAuthenticationRequestHandler = new PinAuthenticationRequestHandler(applicationContext);

    // will throw OneginiConfigNotFoundException if OneginiConfigModel class can't be found
    return new OneginiClientBuilder(applicationContext, createPinRequestHandler, pinAuthenticationRequestHandler)
        // handlers for optional functionalities
        .setBrowserRegistrationRequestHandler(registrationRequestHandler)
        .setBiometricAuthenticationRequestHandler(new BiometricAuthenticationRequestHandler(applicationContext))
        .setCustomAuthenticationRequestHandler(new BasicCustomAuthenticationRequestHandler(applicationContext))
        .setMobileAuthWithPushRequestHandler(new MobileAuthenticationRequestHandler(applicationContext))
        .setMobileAuthWithPushPinRequestHandler(new MobileAuthenticationPinRequestHandler(applicationContext))
        .setMobileAuthWithPushBiometricRequestHandler(new MobileAuthenticationBiometricRequestHandler(applicationContext))
        .setMobileAuthWithPushCustomRequestHandler(new MobileAuthenticationBasicCustomRequestHandler(applicationContext))
        .setMobileAuthWithOtpRequestHandler(new MobileAuthOtpRequestHandler(applicationContext))
        // add custom authenticators
        .setCustomAuthenticators(prepareCustomAuthenticators(applicationContext))
        // add a custom identity provider
        .addCustomIdentityProvider(new TwoStepIdentityProvider(applicationContext))
        .addCustomIdentityProvider(new QrCodeIdentityProvider(applicationContext))
        .addCustomIdentityProvider(new StatelessIdentityProvider())
        // Set http connect / read timeout
        .setHttpConnectTimeout((int) TimeUnit.SECONDS.toMillis(5))
        .setHttpReadTimeout((int) TimeUnit.SECONDS.toMillis(20))
        .build();
  }

  private static Set<OneginiCustomAuthenticator> prepareCustomAuthenticators(final Context applicationContext) {
    final Set<OneginiCustomAuthenticator> authenticators = new HashSet<>(2);
    authenticators.add(new BasicCustomAuthenticator(applicationContext));
    authenticators.add(new PasswordCustomAuthenticator(applicationContext));
    return authenticators;
  }
}
