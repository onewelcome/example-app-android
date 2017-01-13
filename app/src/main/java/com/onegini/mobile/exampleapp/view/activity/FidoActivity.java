/*
 * Copyright (c) 2016-2017 Onegini B.V.
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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.handler.FidoAuthenticationRequestHandler;

public class FidoActivity extends AuthenticationActivity {

  @Bind(R.id.content_accept_deny)
  LinearLayout layoutAcceptDeny;
  @Bind(R.id.fallback_to_pin_button)
  Button fallbackToPinButton;
  @Bind(R.id.fido_progressbar)
  ProgressBar progressBar;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fido);
    ButterKnife.bind(this);
    initialize();
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.auth_accept_button)
  public void onAcceptClicked() {
    if (FidoAuthenticationRequestHandler.CALLBACK != null) {
      FidoAuthenticationRequestHandler.CALLBACK.acceptAuthenticationRequest();
      setFidoAuthenticationPermissionVisibility(false);
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.auth_deny_button)
  public void onDenyClicked() {
    if (FidoAuthenticationRequestHandler.CALLBACK != null) {
      FidoAuthenticationRequestHandler.CALLBACK.denyAuthenticationRequest();
    }
  }


  @SuppressWarnings("unused")
  @OnClick(R.id.fallback_to_pin_button)
  public void onFallbackClicked() {
    if (FidoAuthenticationRequestHandler.CALLBACK != null) {
      FidoAuthenticationRequestHandler.CALLBACK.fallbackToPin();
    }
  }

  protected void initialize() {
    parseIntent();
    updateTexts();
    setFidoAuthenticationPermissionVisibility(true);
  }

  protected void setFidoAuthenticationPermissionVisibility(final boolean isVisible) {
    layoutAcceptDeny.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    fallbackToPinButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    progressBar.setVisibility(isVisible ? View.GONE : View.VISIBLE);
  }
}