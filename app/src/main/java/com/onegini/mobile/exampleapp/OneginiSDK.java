package com.onegini.mobile.exampleapp;

import android.content.Context;
import com.onegini.mobile.android.sdk.client.OneginiClient;
import com.onegini.mobile.android.sdk.client.OneginiClientBuilder;
import com.onegini.mobile.exampleapp.view.dialog.CreatePinDialog;
import com.onegini.mobile.exampleapp.view.dialog.CurrentPinDialog;

public class OneginiSDK {

  public static OneginiClient getOneginiClient(final Context context) {
    OneginiClient oneginiClient = OneginiClient.getInstance();
    if (oneginiClient == null) {
      final Context applicationContext = context.getApplicationContext();
      final CreatePinDialog createPinDialog = new CreatePinDialog(applicationContext);
      final CurrentPinDialog currentPinDialog = new CurrentPinDialog(applicationContext);

      // will throw OneginiConfigNotFoundException if OneginiConfigModel class can't be found
      oneginiClient = new OneginiClientBuilder(applicationContext, createPinDialog, currentPinDialog).build();
    }
    return oneginiClient;
  }
}
