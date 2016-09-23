package com.onegini.mobile.exampleapp.view.handler;

import android.content.Context;
import android.content.Intent;

import com.onegini.mobile.exampleapp.view.activity.FingerprintActivity;
import com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationFingerprintActivity;
import com.onegini.mobile.sdk.android.handlers.request.OneginiMobileAuthenticationFingerprintRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiFingerprintCallback;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;

import static com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationFingerprintActivity.MSG_EXTRA_ASK_TO_ACCEPT_OR_DENY;
import static com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationFingerprintActivity.MSG_EXTRA_CLOSE;
import static com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationFingerprintActivity.MSG_EXTRA_RECEIVED_FINGERPRINT;
import static com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationFingerprintActivity.MSG_EXTRA_SHOW_SCANNING;

public class MobileAuthenticationFingerprintRequestHandler implements OneginiMobileAuthenticationFingerprintRequestHandler {

  public static OneginiFingerprintCallback fingerprintCallback;
  private final Context context;

  public MobileAuthenticationFingerprintRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startAuthentication(final OneginiMobileAuthenticationRequest oneginiMobileAuthenticationRequest, final OneginiFingerprintCallback oneginiFingerprintCallback) {
    fingerprintCallback = oneginiFingerprintCallback;
    startFingerprintActivity(MSG_EXTRA_ASK_TO_ACCEPT_OR_DENY);
  }

  @Override
  public void onNextAuthenticationAttempt() {
    startFingerprintActivity(MSG_EXTRA_RECEIVED_FINGERPRINT);
  }

  @Override
  public void onFingerprintCaptured() {
    startFingerprintActivity(MSG_EXTRA_SHOW_SCANNING);
  }

  @Override
  public void finishAuthentication() {
    startFingerprintActivity(MSG_EXTRA_CLOSE);
  }

  private void startFingerprintActivity(final String action) {
    final Intent intent = new Intent(context, MobileAuthenticationFingerprintActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.putExtra(FingerprintActivity.MSG_EXTRA_ACTION, action);
    context.startActivity(intent);
  }
}
