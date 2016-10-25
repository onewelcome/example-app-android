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
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.storage.UserStorage;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public abstract class AuthenticationActivity extends Activity {

  public static final String EXTRA_MESSAGE = "message";
  public static final String EXTRA_ERROR_MESSAGE = "error_message";
  public static final String EXTRA_USER_PROFILE_ID = "user_name";

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.welcome_user_text)
  TextView welcomeTextView;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.authenticator_message)
  TextView authenticatorMessage;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.pin_error_message)
  TextView errorTextView;

  private String screenMessage;
  private String userName;
  protected String errorMessage;

  protected void parseIntent() {
    final Bundle extras = getIntent().getExtras();
    screenMessage = extras.getString(EXTRA_MESSAGE, "");
    errorMessage = extras.getString(EXTRA_ERROR_MESSAGE, "");

    final String userProfileId = extras.getString(EXTRA_USER_PROFILE_ID, "");
    loadUserName(userProfileId);
  }

  private void loadUserName(final String userProfileId) {
    if (TextUtils.isEmpty(userProfileId)) {
      return;
    }
    UserStorage userStorage = new UserStorage(this);
    userName = userStorage.loadUser(new UserProfile(userProfileId)).getName();
  }

  protected void updateTexts() {
    updateWelcomeText();
    updateTitleText();
  }

  private void updateWelcomeText() {
    if (isNotBlank(userName)) {
      welcomeTextView.setText(getString(R.string.welcome_user_text, userName));
    } else {
      welcomeTextView.setVisibility(View.INVISIBLE);
    }
  }

  private void updateTitleText() {
    if (isNotBlank(screenMessage)) {
      authenticatorMessage.setText(screenMessage);
    } else {
      authenticatorMessage.setVisibility(View.INVISIBLE);
    }
  }

  protected boolean isNotBlank(final String string) {
    return !isBlank(string);
  }

  protected boolean isBlank(final String string) {
    return string == null || string.length() == 0;
  }

  @Override
  public void onBackPressed() {
    // we don't want to be able to go back from the pin screen
  }
}
