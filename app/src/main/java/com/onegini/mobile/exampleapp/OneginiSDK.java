package com.onegini.mobile.exampleapp;

import android.content.Context;
import com.onegini.mobile.android.sdk.OneginiClient;
import com.onegini.mobile.exampleapp.view.dialog.CreatePinDialog;
import com.onegini.mobile.exampleapp.view.dialog.CurrentPinDialog;

public class OneginiSDK {

  public static OneginiClient getOneginiClient(final Context context) {
    OneginiClient oneginiClient = OneginiClient.getInstance();
    if (oneginiClient == null) {
      // will throw OneginiConfigNotFoundException if OneginiConfigModel class can't be found
      final Context applicationContext = context.getApplicationContext();
      oneginiClient = OneginiClient.setupInstance(applicationContext);
      oneginiClient.setCreatePinDialog(new CreatePinDialog(applicationContext));
      oneginiClient.setCurrentPinDialog(new CurrentPinDialog(applicationContext));
    }
    return oneginiClient;
  }
}
