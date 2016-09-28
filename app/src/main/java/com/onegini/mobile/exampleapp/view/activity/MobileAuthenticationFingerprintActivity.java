/*
 * Copyright (c) 2016 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.onegini.mobile.exampleapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.util.AnimationUtils;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationFingerprintRequestHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MobileAuthenticationFingerprintActivity extends Activity {

  public static final String MSG_EXTRA_ACTION = "fingerprint-action";
  public static final String MSG_EXTRA_ASK_TO_ACCEPT_OR_DENY = "ask";
  public static final String MSG_EXTRA_SHOW_SCANNING = "show";
  public static final String MSG_EXTRA_RECEIVED_FINGERPRINT = "received";
  public static final String MSG_EXTRA_CLOSE = "close";

  @Bind(R.id.action_text)
  TextView actionText;
  @Bind(R.id.content_fingerprint)
  LinearLayout layoutFingerprint;
  @Bind(R.id.content_accept_deny)
  LinearLayout layoutAcceptDeny;
  @Bind(R.id.fallback_to_pin_button)
  Button fallbackToPinButton;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fingerprint_mobile);
    ButterKnife.bind(this);

    setupUi(getIntent().getStringExtra(MSG_EXTRA_ACTION));
  }

  @Override
  protected void onNewIntent(final Intent intent) {
    setupUi(intent.getStringExtra(MSG_EXTRA_ACTION));
  }

  private void setupUi(final String action) {
    if (MSG_EXTRA_ASK_TO_ACCEPT_OR_DENY.equals(action)) {
      setFingerprintAuthenticationPermissionVisibility(true);
    } else if (MSG_EXTRA_SHOW_SCANNING.equals(action)) {
      setFingerprintAuthenticationPermissionVisibility(false);
      actionText.setText(R.string.verifying);
    } else if (MSG_EXTRA_RECEIVED_FINGERPRINT.equals(action)) {
      setFingerprintAuthenticationPermissionVisibility(false);
      actionText.setText(R.string.try_again);
      actionText.setAnimation(AnimationUtils.getBlinkAnimation());
    } else if (MSG_EXTRA_CLOSE.equals(action)) {
      setFingerprintAuthenticationPermissionVisibility(false);
      finish();
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.fallback_to_pin_button)
  public void onFallbackToPinButtonClick() {
    if (MobileAuthenticationFingerprintRequestHandler.fingerprintCallback != null) {
      MobileAuthenticationFingerprintRequestHandler.fingerprintCallback.fallbackToPin();
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.auth_accept_button)
  public void onAuthenticationAcceptButtonClick() {
    if (MobileAuthenticationFingerprintRequestHandler.fingerprintCallback != null) {
      MobileAuthenticationFingerprintRequestHandler.fingerprintCallback.acceptAuthenticationRequest();
      actionText.setText(R.string.scan_fingerprint);
      setFingerprintAuthenticationPermissionVisibility(false);
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.auth_deny_button)
  public void onDenyAuthenticationButtonClick() {
    if (MobileAuthenticationFingerprintRequestHandler.fingerprintCallback != null) {
      MobileAuthenticationFingerprintRequestHandler.fingerprintCallback.denyAuthenticationRequest();
      finish();
    }
  }

  private void setFingerprintAuthenticationPermissionVisibility(final boolean isVisible) {
    layoutAcceptDeny.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    layoutFingerprint.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    fallbackToPinButton.setVisibility(isVisible ? View.GONE : View.VISIBLE);
  }
}