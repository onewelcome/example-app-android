package com.onegini.mobile.exampleapp;

import android.content.Context;
import com.onegini.mobile.android.sdk.client.OneginiClient;
import com.onegini.mobile.android.sdk.client.OneginiClientBuilder;
import com.onegini.mobile.exampleapp.view.dialog.CreatePinRequestHandler;
import com.onegini.mobile.exampleapp.view.dialog.PinAuthenticationRequestHandler;

public class OneginiSDK {

  public static OneginiClient getOneginiClient(final Context context) {
    OneginiClient oneginiClient = OneginiClient.getInstance();
    if (oneginiClient == null) {
      final Context applicationContext = context.getApplicationContext();
      final CreatePinRequestHandler createPinRequestHandler = new CreatePinRequestHandler(applicationContext);
      final PinAuthenticationRequestHandler pinAuthenticationRequestHandler = new PinAuthenticationRequestHandler(applicationContext);

      // will throw OneginiConfigNotFoundException if OneginiConfigModel class can't be found
      oneginiClient = new OneginiClientBuilder(applicationContext, createPinRequestHandler, pinAuthenticationRequestHandler).build();
    }
    return oneginiClient;
  }
}
