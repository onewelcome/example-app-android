package com.onegini.mobile.exampleapp;

import android.content.Context;
import com.onegini.mobile.sdk.android.library.OneginiClient;

public class OneginiSDK {

  public static OneginiClient getOneginiClient(Context context) {

    if (OneginiClient.getInstance() == null) {
      // will throw OneginiConfigNotFoundException if OneginiConfigModel class can't be found
      return OneginiClient.setupInstance(context);
    } else {
      return OneginiClient.getInstance();
    }
  }
}
