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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationPinRequestHandler;
import com.onegini.mobile.exampleapp.view.helper.PinInputFields;
import com.onegini.mobile.exampleapp.view.helper.PinKeyboard;

public class MobileAuthenticationPinActivity extends AbstractMobileAuthenticationActivity {

  public static final String EXTRA_FAILED_ATTEMPTS_COUNT = "failed_attempts";
  public static final String EXTRA_MAX_FAILED_ATTEMPTS = "max_failed_attempts";

  private static final int MAX_DIGITS = 5;

  @Bind(R.id.pin_error_message)
  TextView errorTextView;
  @Bind(R.id.pin_deny_button)
  Button denyButton;
  private final ImageView[] pinInputs = new ImageView[MAX_DIGITS];

  private int failedAttemptsCount;
  private int maxFailedAttempts;
  private PinKeyboard pinKeyboard;
  private PinInputFields pinInputFields;
  private PinInputFields.PinProvidedListener pinProvidedListener;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pin);
    ButterKnife.bind(this);
    initPinInputs();
    initialize();
  }

  private void initPinInputs() {
    for (int input = 0; input < MAX_DIGITS; input++) {
      final int inputId = getResources().getIdentifier("pin_input_" + input, "id", getPackageName());
      pinInputs[input] = (ImageView) findViewById(inputId);
    }
  }

  protected void initialize() {
    parseIntent();
    initPinListener();
    initLayout();
    initKeyboard();
  }

  protected void parseIntent() {
    super.parseIntent();
    final Intent intent = getIntent();
    failedAttemptsCount = intent.getIntExtra(EXTRA_FAILED_ATTEMPTS_COUNT, 0);
    maxFailedAttempts = intent.getIntExtra(EXTRA_MAX_FAILED_ATTEMPTS, 0);
  }

  private void initPinListener() {
    pinProvidedListener = pin -> MobileAuthenticationPinRequestHandler.CALLBACK.acceptAuthenticationRequest(pin);
  }

  private void initLayout() {
    initPinInputs();
    updateTexts();
    initDenyButton();
  }

  private void initKeyboard() {
    pinInputFields = new PinInputFields(pinProvidedListener, pinInputs);
    pinKeyboard = new PinKeyboard(pinInputFields);

    final TableLayout keyboardLayout = (TableLayout) findViewById(R.id.pin_keyboard);
    pinKeyboard.initLayout(keyboardLayout, getResources(), getPackageName());
    pinKeyboard.reset();
  }

  private void initDenyButton() {
    denyButton.setVisibility(View.VISIBLE);
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.pin_deny_button)
  public void onDenyClicked() {
    if (MobileAuthenticationPinRequestHandler.CALLBACK != null) {
      MobileAuthenticationPinRequestHandler.CALLBACK.denyAuthenticationRequest();
    }
  }

  protected void updateTexts() {
    super.updateTexts();
    if (failedAttemptsCount > 0) {
      final int remainingAttempts = maxFailedAttempts - failedAttemptsCount;
      errorTextView.setText(getString(R.string.pin_error_invalid_pin, remainingAttempts));
      errorTextView.setVisibility(View.VISIBLE);
    } else {
      errorTextView.setVisibility(View.INVISIBLE);
    }
  }
}