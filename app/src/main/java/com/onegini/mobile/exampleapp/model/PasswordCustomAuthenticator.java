package com.onegini.mobile.exampleapp.model;


import android.content.Context;
import com.onegini.mobile.exampleapp.view.action.passwordcustomauth.PasswordCustomAuthAuthenticationAction;
import com.onegini.mobile.exampleapp.view.action.passwordcustomauth.PasswordCustomAuthDeregistrationAction;
import com.onegini.mobile.exampleapp.view.action.passwordcustomauth.PasswordCustomAuthRegistrationAction;
import com.onegini.mobile.sdk.android.handlers.customauth.OneginiCustomAuthAuthenticationAction;
import com.onegini.mobile.sdk.android.handlers.customauth.OneginiCustomAuthDeregistrationAction;
import com.onegini.mobile.sdk.android.handlers.customauth.OneginiCustomAuthRegistrationAction;
import com.onegini.mobile.sdk.android.model.OneginiCustomAuthenticator;

public class PasswordCustomAuthenticator implements OneginiCustomAuthenticator {

  public static final String PASSWORD_CUSTOM_AUTHENTICATOR_ID = "PASSWORD_CA_ID";

  private final OneginiCustomAuthRegistrationAction registrationAction;
  private final OneginiCustomAuthDeregistrationAction deregistrationAction;
  private final OneginiCustomAuthAuthenticationAction authAuthenticationAction;

  public PasswordCustomAuthenticator(final Context context) {
    registrationAction = new PasswordCustomAuthRegistrationAction(context);
    deregistrationAction = new PasswordCustomAuthDeregistrationAction();
    authAuthenticationAction = new PasswordCustomAuthAuthenticationAction();
  }

  @Override
  public OneginiCustomAuthRegistrationAction getRegistrationAction() {
    return registrationAction;
  }

  @Override
  public OneginiCustomAuthDeregistrationAction getDeregistrationAction() {
    return deregistrationAction;
  }

  @Override
  public OneginiCustomAuthAuthenticationAction getAuthenticationAction() {
    return authAuthenticationAction;
  }

  @Override
  public String getId() {
    return PASSWORD_CUSTOM_AUTHENTICATOR_ID;
  }
}
