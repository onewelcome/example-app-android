package com.onegini.mobile.exampleapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.exampleapp.storage.UserStorage;
import com.onegini.mobile.exampleapp.view.dialog.MobileAuthenticationRequestHandler;

public class MobileAuthenticationActivity extends Activity {

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
