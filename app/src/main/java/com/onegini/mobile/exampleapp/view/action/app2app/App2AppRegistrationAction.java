package com.onegini.mobile.exampleapp.view.action.app2app;

import android.content.Context;
import com.onegini.mobile.sdk.android.handlers.action.OneginiCustomTwoStepRegistrationAction;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiCustomRegistrationCallback;
import com.onegini.mobile.sdk.android.model.entity.CustomInfo;

public class App2AppRegistrationAction implements OneginiCustomTwoStepRegistrationAction {

  public static OneginiCustomRegistrationCallback CALLBACK;

  private final Context context;

  public App2AppRegistrationAction(final Context context) {
    this.context = context;
  }

  @Override
  public void initRegistration(final OneginiCustomRegistrationCallback callback, final CustomInfo customInfo) {
    callback.returnSuccess("");
  }

  @Override
  public void finishRegistration(final OneginiCustomRegistrationCallback callback, final CustomInfo customInfo) {
    String json = customInfo.getData();
    String sessionId = "sessionIdFromJson";
    String digidRequest = "digidRequestFromJson";

    //Open digid app with digidRequest link

    String digidResponse = "response from digid app";
    callback.returnSuccess("digidResponse & sessionId");
  }
}
