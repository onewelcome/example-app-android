package com.onegini.mobile.exampleapp.model;

import androidx.annotation.NonNull;
import com.onegini.mobile.sdk.android.handlers.action.OneginiCustomRegistrationAction;
import com.onegini.mobile.sdk.android.model.OneginiCustomIdentityProvider;

public class StatelessIdentityProvider implements OneginiCustomIdentityProvider {

  @NonNull
  @Override
  public String getId() {
    return "stateless-test";
  }

  @NonNull
  @Override
  public OneginiCustomRegistrationAction getRegistrationAction() {
    return new StatelessIdentityProviderRegistrationAction();
  }
}
