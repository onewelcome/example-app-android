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

import android.os.Bundle;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.util.AnimationUtils;
import com.onegini.mobile.exampleapp.view.handler.FingerprintAuthenticationRequestHandler;

public class FingerprintActivity extends AuthenticationActivity {

  public static final String EXTRA_ACTION = "fingerprint_action";
  public static final String EXTRA_START = "start";
  public static final String EXTRA_SHOW_SCANNING = "show";
  public static final String EXTRA_RECEIVED_FINGERPRINT = "received";
  public static final String EXTRA_CLOSE = "close";

  @Bind(R.id.action_text)
  TextView actionTextView;

  private String actionText;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fingerprint);
    ButterKnife.bind(this);

    initialize();
  }

  protected void initialize() {
    parseIntent();
    updateTexts();
  }

  protected void parseIntent() {
    super.parseIntent();
    actionText = getIntent().getStringExtra(EXTRA_ACTION);
  }

  protected void updateTexts() {
    super.updateTexts();
    if (EXTRA_SHOW_SCANNING.equals(actionText)) {
      actionTextView.setText(R.string.verifying);
    } else if (EXTRA_START.equals(actionText)) {
      actionTextView.setText(R.string.scan_fingerprint);
    } else if (EXTRA_RECEIVED_FINGERPRINT.equals(actionText)) {
      actionTextView.setText(R.string.try_again);
      actionTextView.setAnimation(AnimationUtils.getBlinkAnimation());
    } else if (EXTRA_CLOSE.equals(actionText)) {
      finish();
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.fallback_to_pin_button)
  public void onFallbackToPinButtonClick() {
    if (FingerprintAuthenticationRequestHandler.fingerprintCallback != null) {
      FingerprintAuthenticationRequestHandler.fingerprintCallback.fallbackToPin();
      finish();
    }
  }
}
