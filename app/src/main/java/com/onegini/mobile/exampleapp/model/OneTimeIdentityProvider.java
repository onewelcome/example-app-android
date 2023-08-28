package com.onegini.mobile.exampleapp.model;

import androidx.annotation.NonNull;
import com.onegini.mobile.exampleapp.view.action.onetimeidentityprovider.OneTimeRegistrationAction;
import com.onegini.mobile.sdk.android.handlers.action.OneginiCustomOneTimeRegistrationAction;
import com.onegini.mobile.sdk.android.handlers.action.OneginiCustomRegistrationAction;
import com.onegini.mobile.sdk.android.model.OneginiCustomIdentityProvider;

public class OneTimeIdentityProvider implements OneginiCustomIdentityProvider {
  private final OneginiCustomOneTimeRegistrationAction registrationAction;

  public OneTimeIdentityProvider() {
    this.registrationAction = new OneTimeRegistrationAction();
  }

  @NonNull
  @Override
  public String getId() {
    return "one_time";
  }

  @NonNull
  @Override
  public OneginiCustomRegistrationAction getRegistrationAction() {
    return registrationAction;
  }
}
