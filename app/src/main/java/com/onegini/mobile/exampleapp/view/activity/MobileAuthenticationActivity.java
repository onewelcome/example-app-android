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
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationRequestHandler;

public class MobileAuthenticationActivity extends AbstractMobileAuthenticationActivity {

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_push_simple);
    ButterKnife.bind(this);

    initialize();
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

  @SuppressWarnings("unused")
  @OnClick(R.id.push_accept_button)
  public void onAcceptClicked() {
    if (MobileAuthenticationRequestHandler.CALLBACK != null) {
      MobileAuthenticationRequestHandler.CALLBACK.acceptAuthenticationRequest();
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.push_deny_button)
  public void onDenyClicked() {
    if (MobileAuthenticationRequestHandler.CALLBACK != null) {
      MobileAuthenticationRequestHandler.CALLBACK.denyAuthenticationRequest();
    }
  }

  protected void initialize() {
    parseIntent();
    updateTexts();
  }
}
