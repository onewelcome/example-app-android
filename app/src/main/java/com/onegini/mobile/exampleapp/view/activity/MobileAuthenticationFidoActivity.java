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
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.exampleapp.storage.UserStorage;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationFidoRequestHandler;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class MobileAuthenticationFidoActivity extends Activity {

  public static final String COMMAND_START = "start";
  public static final String COMMAND_FINISH = "finish";

  public static final String EXTRA_COMMAND = "command";
  public static final String EXTRA_MESSAGE = "extra_message";
  public static final String EXTRA_PROFILE_ID = "extra_profile_id";

  @Bind(R.id.welcome_user_text)
  TextView userTextView;
  @Bind(R.id.push_text)
  TextView messageTextView;

  private User user;
  private String message;
  private UserStorage userStorage;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_push_simple);
    ButterKnife.bind(this);
    userStorage = new UserStorage(this);
    initialize();
  }

  @Override
  protected void onNewIntent(final Intent intent) {
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
    if (MobileAuthenticationFidoRequestHandler.CALLBACK != null) {
      MobileAuthenticationFidoRequestHandler.CALLBACK.acceptAuthenticationRequest();
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.push_deny_button)
  public void onDenyClicked() {
    if (MobileAuthenticationRequestHandler.CALLBACK != null) {
      MobileAuthenticationFidoRequestHandler.CALLBACK.denyAuthenticationRequest();
    }
  }

  private void initialize() {
    parseIntent();
    initLayout();
  }

  private void parseIntent() {
    final Intent intent = getIntent();
    final String profileId = intent.getStringExtra(EXTRA_PROFILE_ID);
    message = intent.getStringExtra(EXTRA_MESSAGE);

    user = userStorage.loadUser(new UserProfile(profileId));
  }

  private void initLayout() {
    userTextView.setText(getString(R.string.welcome_user_text, user.getName()));
    messageTextView.setText(message);
  }
}
