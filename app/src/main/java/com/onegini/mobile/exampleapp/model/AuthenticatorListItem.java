package com.onegini.mobile.exampleapp.model;

import com.onegini.mobile.sdk.android.model.OneginiAuthenticator;

public class AuthenticatorListItem {

  private final OneginiAuthenticator authenticator;

  private boolean isRegistered;
  private boolean isProcessed;

  public AuthenticatorListItem(final OneginiAuthenticator authenticator) {
    this.authenticator = authenticator;
  }

  public boolean isProcessed() {
    return isProcessed;
  }

  public void setIsProcessed(boolean isProcessed) {
    this.isProcessed = isProcessed;
  }

  public OneginiAuthenticator getAuthenticator() {
    return authenticator;
  }

  public boolean isRegistered() {
    return isRegistered;
  }

  public void setRegistered(final boolean registered) {
    isRegistered = registered;
  }
}
