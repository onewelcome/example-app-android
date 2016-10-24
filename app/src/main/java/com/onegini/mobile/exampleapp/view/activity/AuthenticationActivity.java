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
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import com.onegini.mobile.exampleapp.R;

public abstract class AuthenticationActivity extends Activity {

  public static final String EXTRA_TITLE = "title";
  public static final String EXTRA_MESSAGE = "message";
  public static final String EXTRA_USER_NAME = "user_name";

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.welcome_user_text)
  TextView welcomeTextView;
  @SuppressWarnings("unused")
  @Bind(R.id.pin_title)
  TextView screenTitleTextView;

  private String userName;
  private String screenTitle;
  protected String screenMessage;

  protected void parseIntent() {
    final Bundle extras = getIntent().getExtras();
    screenTitle = extras.getString(EXTRA_TITLE, "");
    screenMessage = extras.getString(EXTRA_MESSAGE, "");
    userName = extras.getString(EXTRA_USER_NAME, "");
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
    if (isNotBlank(screenTitle)) {
      screenTitleTextView.setText(screenTitle);
    } else {
      screenTitleTextView.setVisibility(View.INVISIBLE);
    }
  }

  protected boolean isNotBlank(final String string) {
    return !isBlank(string);
  }

  protected boolean isBlank(final String string) {
    return string == null || string.length() == 0;
  }

}
