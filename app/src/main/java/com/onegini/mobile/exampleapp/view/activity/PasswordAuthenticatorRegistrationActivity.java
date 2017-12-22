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
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.action.passwordcustomauth.PasswordCustomAuthRegistrationAction;

public class PasswordAuthenticatorRegistrationActivity extends AppCompatActivity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.authenticator_password)
  EditText passwordEditText;

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.welcome_user_text)
  TextView welcomeTextView;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_password_custom_auth);
    ButterKnife.bind(this);
    welcomeTextView.setText("Auth registration");
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.auth_accept_button)
  public void onAcceptClicked() {
    if (PasswordCustomAuthRegistrationAction.CALLBACK != null) {
      PasswordCustomAuthRegistrationAction.CALLBACK.acceptRegistrationRequest(
          passwordEditText.getText().toString());
      finish();
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.auth_cancel_button)
  public void onCancelClicked() {
    if (PasswordCustomAuthRegistrationAction.CALLBACK != null) {
      PasswordCustomAuthRegistrationAction.CALLBACK.denyRegistrationRequest();
      finish();
    }
  }

  @Override
  public void onBackPressed() {
    onCancelClicked();
    super.onBackPressed();
  }
}
