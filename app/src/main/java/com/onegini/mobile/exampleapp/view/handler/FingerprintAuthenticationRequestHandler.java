package com.onegini.mobile.exampleapp.view.handler;

import android.content.Context;
import android.content.Intent;

import com.onegini.mobile.exampleapp.view.activity.FingerprintActivity;
import com.onegini.mobile.sdk.android.handlers.request.OneginiFingerprintAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiFingerprintCallback;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

import static com.onegini.mobile.exampleapp.view.activity.FingerprintActivity.MSG_EXTRA_CLOSE;
import static com.onegini.mobile.exampleapp.view.activity.FingerprintActivity.MSG_EXTRA_RECEIVED_FINGERPRINT;
import static com.onegini.mobile.exampleapp.view.activity.FingerprintActivity.MSG_EXTRA_SHOW_SCANNING;
import static com.onegini.mobile.exampleapp.view.activity.FingerprintActivity.MSG_EXTRA_START;

public class FingerprintAuthenticationRequestHandler implements OneginiFingerprintAuthenticationRequestHandler {

  public static OneginiFingerprintCallback fingerprintCallback;
  private final Context context;

  public FingerprintAuthenticationRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startAuthentication(final UserProfile userProfile, final OneginiFingerprintCallback oneginiFingerprintCallback) {
    fingerprintCallback = oneginiFingerprintCallback;
    startFingerprintActivity(MSG_EXTRA_START);
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
    final Intent intent = new Intent(context, FingerprintActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.putExtra(FingerprintActivity.MSG_EXTRA_ACTION, action);
    context.startActivity(intent);
  }
}
