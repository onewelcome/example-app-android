package com.onegini.mobile.exampleapp.model;

import androidx.annotation.NonNull;
import com.onegini.mobile.exampleapp.view.action.statelessidentityprovider.StatelessRegistrationAction;
import com.onegini.mobile.sdk.android.handlers.action.OneginiCustomRegistrationAction;
import com.onegini.mobile.sdk.android.handlers.action.OneginiCustomTwoStepRegistrationAction;
import com.onegini.mobile.sdk.android.model.OneginiCustomIdentityProvider;

public class StatelessIdentityProvider implements OneginiCustomIdentityProvider {
  private final OneginiCustomTwoStepRegistrationAction registrationAction;

  public StatelessIdentityProvider() {
    this.registrationAction = new StatelessRegistrationAction();
  }

  @NonNull
  @Override
  public String getId() {
    return "stateless";
  }

  @NonNull
  @Override
  public OneginiCustomRegistrationAction getRegistrationAction() {
    return registrationAction;
  }
}
