package com.onegini.mobile.exampleapp;

import android.content.Context;
import com.onegini.mobile.exampleapp.view.dialog.CreatePinDialog;
import com.onegini.mobile.exampleapp.view.dialog.CurrentPinDialog;
import com.onegini.mobile.sdk.android.library.OneginiClient;

public class OneginiSDK {

  public static OneginiClient getOneginiClient(final Context context) {
    OneginiClient oneginiClient = OneginiClient.getInstance();
    if (oneginiClient == null) {
      // will throw OneginiConfigNotFoundException if OneginiConfigModel class can't be found
      oneginiClient = OneginiClient.setupInstance(context);
      oneginiClient.setCreatePinDialog(new CreatePinDialog(context));
      oneginiClient.setCurrentPinDialog(new CurrentPinDialog(context));
    }
    return oneginiClient;
  }
}
