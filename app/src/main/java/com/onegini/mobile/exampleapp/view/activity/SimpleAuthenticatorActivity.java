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
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.SimpleCustomAuthenticator;
import com.onegini.mobile.exampleapp.view.action.SimpleCustomAuthRegistrationAction;

public class SimpleAuthenticatorActivity extends AppCompatActivity {

  @SuppressWarnings({ "unused" })
  @Bind(R.id.title_text)
  TextView titleText;
  @SuppressWarnings({ "unused" })
  @Bind(R.id.custom_auth_positive_button)
  Button positiveButton;
  @SuppressWarnings({ "unused" })
  @Bind(R.id.custom_auth_negative_button)
  Button negativeButton;
  @SuppressWarnings({ "unused" })
  @Bind(R.id.custom_auth_error_button)
  Button errorButton;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_custom_auth_registration);
    ButterKnife.bind(this);
    titleText.setText("Auth registration");
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.custom_auth_positive_button)
  public void onPossitiveButtonClicked() {
    if (SimpleCustomAuthRegistrationAction.CALLBACK != null) {
      SimpleCustomAuthRegistrationAction.CALLBACK.acceptRegistrationRequest(SimpleCustomAuthenticator.AUTH_DATA);
    }
    finish();
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.custom_auth_negative_button)
  public void onNegativeButtonClicked() {
    if (SimpleCustomAuthRegistrationAction.CALLBACK != null) {
      SimpleCustomAuthRegistrationAction.CALLBACK.denyRegistrationRequest();
    }
    finish();
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.custom_auth_error_button)
  public void onErrorButtonClicked() {
    if (SimpleCustomAuthRegistrationAction.CALLBACK != null) {
      SimpleCustomAuthRegistrationAction.CALLBACK.returnError(new Exception("Fake exception"));
    }
    finish();
  }

  @Override
  public void onBackPressed() {
    onNegativeButtonClicked();
  }
}
