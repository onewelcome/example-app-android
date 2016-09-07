package com.onegini.mobile.exampleapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.exampleapp.storage.UserStorage;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationPinRequestHandler;
import com.onegini.mobile.exampleapp.view.helper.PinInputFields;
import com.onegini.mobile.exampleapp.view.helper.PinKeyboard;

public class MobileAuthenticationPinActivity extends Activity {

  public static final String COMMAND_START = "start";
  public static final String COMMAND_FINISH = "finish";

  public static final String EXTRA_COMMAND = "command";
  public static final String EXTRA_MESSAGE = "extra_message";
  public static final String EXTRA_PROFILE_ID = "extra_profile_id";
  public static final String EXTRA_FAILED_ATTEMPTS_COUNT = "failed_attempts";
  public static final String EXTRA_MAX_FAILED_ATTEMPTS = "max_failed_attempts";

  private static final int MAX_DIGITS = 5;

  @Bind(R.id.welcome_user_text)
  TextView userTextView;
  @Bind(R.id.pin_title)
  TextView messageTextView;
  @Bind(R.id.pin_error_message)
  TextView errorTextView;
  @Bind(R.id.pin_deny_button)
  Button denyButton;
  private final ImageView[] pinInputs = new ImageView[MAX_DIGITS];

  private User user;
  private String message;
  private int failedAttemptsCount;
  private int maxFailedAttempts;
  private UserStorage userStorage;
  private PinKeyboard pinKeyboard;
  private PinInputFields pinInputFields;
  private PinInputFields.PinProvidedListener pinProvidedListener;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pin);
    ButterKnife.bind(this);
    userStorage = new UserStorage(this);
    initPinInputs();
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

  private void initPinInputs() {
    for (int input = 0; input < MAX_DIGITS; input++) {
      final int inputId = getResources().getIdentifier("pin_input_" + input, "id", getPackageName());
      pinInputs[input] = (ImageView) findViewById(inputId);
    }
  }

  private void initialize() {
    parseIntent();
    initPinListener();
    initLayout();
    initKeyboard();
  }

  private void parseIntent() {
    final Intent intent = getIntent();
    final String profileId = intent.getStringExtra(EXTRA_PROFILE_ID);
    message = intent.getStringExtra(EXTRA_MESSAGE);
    failedAttemptsCount = intent.getIntExtra(EXTRA_FAILED_ATTEMPTS_COUNT, 0);
    maxFailedAttempts = intent.getIntExtra(EXTRA_MAX_FAILED_ATTEMPTS, 0);

    user = userStorage.loadUser(new UserProfile(profileId));
  }

  private void initPinListener() {
    pinProvidedListener = pin -> MobileAuthenticationPinRequestHandler.CALLBACK.acceptAuthenticationRequest(pin);
  }

  private void initLayout() {
    initPinInputs();
    updateTexts();
    initDenyButton();
  }

  private void initKeyboard() {
    pinInputFields = new PinInputFields(pinProvidedListener, pinInputs);
    pinKeyboard = new PinKeyboard(pinInputFields);

    final TableLayout keyboardLayout = (TableLayout) findViewById(R.id.pin_keyboard);
    pinKeyboard.initLayout(keyboardLayout, getResources(), getPackageName());
    pinKeyboard.reset();
  }

  private void initDenyButton() {
    denyButton.setVisibility(View.VISIBLE);
    denyButton.setOnClickListener(v -> MobileAuthenticationPinRequestHandler.CALLBACK.denyAuthenticationRequest());
  }

  private void updateTexts() {
    userTextView.setText(getString(R.string.welcome_user_text, user.getName()));
    messageTextView.setText(message);
    if (failedAttemptsCount > 0) {
      final int remainingAttempts = maxFailedAttempts - failedAttemptsCount;
      errorTextView.setText(getString(R.string.pin_error_invalid_pin, remainingAttempts));
      errorTextView.setVisibility(View.VISIBLE);
    } else {
      errorTextView.setVisibility(View.INVISIBLE);
    }
  }
}