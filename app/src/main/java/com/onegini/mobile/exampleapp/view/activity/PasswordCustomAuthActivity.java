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
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.action.passwordcustomauth.PasswordCustomAuthAuthenticationAction;
import com.onegini.mobile.exampleapp.view.handler.CustomAuthenticationRequestHandler;

public class PasswordCustomAuthActivity extends AuthenticationActivity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.authenticator_password)
  EditText passwordEditText;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.fallback_to_pin_button)
  Button fallbackToPinButton;


  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_password_custom_auth);
    ButterKnife.bind(this);
    initialize();
  }

  @Override
  protected void initialize() {
    parseIntent();
    updateTexts();
    setupUi();
  }

  private void setupUi() {
    cancelButton.setVisibility(View.VISIBLE);
    fallbackToPinButton.setVisibility(View.VISIBLE);
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.auth_accept_button)
  public void onAcceptClicked() {
    if(CustomAuthenticationRequestHandler.CALLBACK != null) {
      PasswordCustomAuthAuthenticationAction.password = passwordEditText.getText().toString();
      CustomAuthenticationRequestHandler.CALLBACK.acceptAuthenticationRequest();
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.auth_cancel_button)
  public void onCancelClicked() {
    cancelRequest();
  }

  @Override
  protected void cancelRequest() {
    if (CustomAuthenticationRequestHandler.CALLBACK != null) {
      CustomAuthenticationRequestHandler.CALLBACK.denyAuthenticationRequest();
      finish();
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.fallback_to_pin_button)
  public void onFallbackToPinClick() {
    if (CustomAuthenticationRequestHandler.CALLBACK != null) {
      CustomAuthenticationRequestHandler.CALLBACK.fallbackToPin();
      finish();
    }
  }
}
