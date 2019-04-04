package com.onegini.mobile.exampleapp.view.activity;

import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthOtpRequestHandler;

public class MobileAuthenticationOTPActivity extends AuthenticationActivity {

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_push_simple);
    ButterKnife.bind(this);
    initialize();
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.auth_accept_button)
  public void onAcceptClicked() {
    if (MobileAuthOtpRequestHandler.CALLBACK != null) {
      MobileAuthOtpRequestHandler.CALLBACK.acceptAuthenticationRequest();
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.auth_deny_button)
  public void onDenyClicked() {
    if (MobileAuthOtpRequestHandler.CALLBACK != null) {
      MobileAuthOtpRequestHandler.CALLBACK.denyAuthenticationRequest();
    }
  }

  @Override
  protected void initialize() {
    parseIntent();
    updateTexts();
  }
}
