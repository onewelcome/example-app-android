package com.onegini.mobile.exampleapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.dialog.CreatePinDialog;
import com.onegini.mobile.exampleapp.view.dialog.CurrentPinDialog;
import com.onegini.mobile.exampleapp.view.helper.PinInputFields;
import com.onegini.mobile.exampleapp.view.helper.PinKeyboard;

public class PinActivity extends Activity {

  private static final int MAX_DIGITS = 5;

  public static final String EXTRA_TITLE = "title";
  public static final String EXTRA_MESSAGE = "message";

  private static boolean isCreatePinFlow = false;
  private static int remainingFailedAttempts = 0;

  public static void setIsCreatePinFlow(final boolean isCreatePinFlow) {
    PinActivity.isCreatePinFlow = isCreatePinFlow;
  }

  public static void setRemainingFailedAttempts(final int remainingFailedAttempts) {
    PinActivity.remainingFailedAttempts = remainingFailedAttempts;
  }

  @Bind(R.id.pin_title)
  TextView screenTitleTextView;
  @Bind(R.id.pin_error_message)
  TextView errorTextView;
  private final ImageView[] pinInputs = new ImageView[MAX_DIGITS];

  private String screenTitle;
  private String screenMessage;

  private PinKeyboard pinKeyboard;
  private PinInputFields pinInputFields;
  private PinInputFields.PinProvidedListener pinProvidedListener;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pin);
    ButterKnife.bind(this);
    initPinInputs();
    initialize();
  }

  @Override
  protected void onPause() {
    super.onPause();
    resetView();
  }

  @Override
  protected void onNewIntent(final Intent intent) {
    setIntent(intent);
    initialize();
  }

  @Override
  public void onBackPressed() {
    // we don't want to be able to go back from the pin screen
  }

  private void initialize() {
    parseIntent();
    initListeners();
    initLayout();
    initKeyboard();
  }

  private void parseIntent() {
    final Intent intent = getIntent();
    screenMessage = intent.getStringExtra(EXTRA_MESSAGE);
    screenTitle = intent.getStringExtra(EXTRA_TITLE);
  }

  private void initListeners() {
    pinProvidedListener = pin -> {
      errorTextView.setVisibility(View.INVISIBLE);
      callHandler(pin);
    };
  }

  private void initLayout() {
    initPinInputs();
    updateTexts();
  }

  private void initPinInputs() {
    final Resources resources = getResources();
    final String packageName = getPackageName();
    for (int input = 0; input < MAX_DIGITS; input++) {
      final int inputId = resources.getIdentifier("pin_input_" + input, "id", packageName);
      pinInputs[input] = (ImageView) findViewById(inputId);
    }
  }

  private void initKeyboard() {
    pinInputFields = new PinInputFields(pinProvidedListener, pinInputs);
    pinKeyboard = new PinKeyboard(pinInputFields);

    final TableLayout keyboardLayout = (TableLayout) findViewById(R.id.pin_keyboard);
    pinKeyboard.initLayout(keyboardLayout, getResources(), getPackageName());
  }

  private void updateTexts() {
    updateTitleText();
    updateErrorText();
  }

  private void updateTitleText() {
    if (isNotBlank(screenTitle)) {
      screenTitleTextView.setText(screenTitle);
    } else {
      screenTitleTextView.setVisibility(View.INVISIBLE);
    }
  }

  private void updateErrorText() {
    if (isCreatePinFlow && isNotBlank(screenMessage)) {
      errorTextView.setText(screenMessage);
      errorTextView.setVisibility(View.VISIBLE);
    } else if (!isCreatePinFlow && remainingFailedAttempts > 0) {
      errorTextView.setText(getString(R.string.pin_error_invalid_pin, remainingFailedAttempts));
      errorTextView.setVisibility(View.VISIBLE);
    } else {
      errorTextView.setVisibility(View.INVISIBLE);
    }
  }

  private boolean isNotBlank(final String string) {
    return !isBlank(string);
  }

  private boolean isBlank(final String string) {
    return string == null || string.length() == 0;
  }

  private void resetView() {
    pinKeyboard.reset();
    pinInputFields.reset();
    errorTextView.setVisibility(View.INVISIBLE);
  }

  private void callHandler(final char[] pin) {
    if (isCreatePinFlow) {
      CreatePinDialog.oneginiPinProvidedHandler.onPinProvided(pin);
    } else {
      CurrentPinDialog.oneginiPinProvidedHandler.onPinProvided(pin);
    }
    finish();
  }
}
