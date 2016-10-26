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

import static com.onegini.mobile.exampleapp.Constants.COMMAND_FINISH;
import static com.onegini.mobile.exampleapp.Constants.COMMAND_START;
import static com.onegini.mobile.exampleapp.Constants.EXTRA_COMMAND;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import butterknife.Bind;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationPinRequestHandler;

public class MobileAuthenticationPinActivity extends PinActivity {

  @Bind(R.id.auth_deny_button)
  Button denyButton;

  @Override
  protected void initialize() {
    super.initialize();
    initDenyButton();
  }

  @Override
  protected void updateErrorText() {
    if (failedAttemptsCount > 0) {
      final int remainingFailedAttempts = maxFailedAttempts - failedAttemptsCount;

      errorTextView.setText(getString(R.string.pin_error_invalid_pin, remainingFailedAttempts));
      errorTextView.setVisibility(View.VISIBLE);
    } else {
      errorTextView.setVisibility(View.INVISIBLE);
    }
  }

  @Override
  protected void initPinListener() {
    pinProvidedListener = pin -> {
      errorTextView.setVisibility(View.INVISIBLE);
      MobileAuthenticationPinRequestHandler.CALLBACK.acceptAuthenticationRequest(pin);
    };
  }

  private void initDenyButton() {
    denyButton.setVisibility(View.VISIBLE);
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.auth_deny_button)
  public void onDenyClicked() {
    if (MobileAuthenticationPinRequestHandler.CALLBACK != null) {
      MobileAuthenticationPinRequestHandler.CALLBACK.denyAuthenticationRequest();
    }
  }

  @Override
  protected void onNewIntent(final Intent intent) {
    super.onNewIntent(intent);
    final String command = intent.getStringExtra(EXTRA_COMMAND);
    if (COMMAND_FINISH.equals(command)) {
      finish();
    } else if (COMMAND_START.equals(command)) {
      setIntent(intent);
      initialize();
    }
  }
}