package com.onegini.mobile.exampleapp.model;

import android.content.Context;
import androidx.annotation.NonNull;
import com.onegini.mobile.exampleapp.view.action.app2app.App2AppRegistrationAction;
import com.onegini.mobile.sdk.android.handlers.action.OneginiCustomRegistrationAction;
import com.onegini.mobile.sdk.android.handlers.action.OneginiCustomTwoStepRegistrationAction;
import com.onegini.mobile.sdk.android.model.OneginiCustomIdentityProvider;

public class App2AppIdentityProvider implements OneginiCustomIdentityProvider {

  private final OneginiCustomTwoStepRegistrationAction registrationAction;

  public App2AppIdentityProvider(final Context context) {
    this.registrationAction = new App2AppRegistrationAction(context);
  }

  @NonNull
  @Override
  public OneginiCustomRegistrationAction getRegistrationAction() {
    return registrationAction;
  }

  @NonNull
  @Override
  public String getId() {
    return "app2app";
  }
}
