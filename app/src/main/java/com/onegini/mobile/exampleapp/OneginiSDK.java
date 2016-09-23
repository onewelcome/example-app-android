package com.onegini.mobile.exampleapp;

import android.content.Context;

import com.onegini.mobile.exampleapp.view.handler.FingerprintAuthenticationRequestHandler;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationFingerprintRequestHandler;
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
        .setFingerprintAuthenticatioRequestHandler(new FingerprintAuthenticationRequestHandler(applicationContext))
        .setMobileAuthenticationFingerprintRequestHandler(new MobileAuthenticationFingerprintRequestHandler(applicationContext))
        .build();
  }
}
