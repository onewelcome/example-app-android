package com.onegini.mobile.exampleapp.view.dialog;

import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import com.onegini.mobile.android.sdk.handlers.error.OneginiPinValidationError;
import com.onegini.mobile.android.sdk.handlers.request.OneginiCreatePinRequestHandler;
import com.onegini.mobile.android.sdk.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.activity.PinActivity;

public class CreatePinDialog implements OneginiCreatePinRequestHandler {

  public static OneginiPinCallback oneginiPinCallback;
  private final Context context;
  private PinWithConfirmationHandler oneginiPinProvidedHandler;

  public CreatePinDialog(final Context context) {
    this.context = context;
  }

  private void notifyActivity(final String title, final String message) {
    final Intent intent = new Intent(context, PinActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(PinActivity.EXTRA_TITLE, title);
    intent.putExtra(PinActivity.EXTRA_MESSAGE, message);
    context.startActivity(intent);
  }

  @Override
  public void startPinCreation(final UserProfile userProfile, final OneginiPinCallback oneginiPinCallback) {
    PinActivity.setIsCreatePinFlow(true);
    notifyActivity(context.getString(R.string.pin_title_choose_pin), "");

    CreatePinDialog.oneginiPinCallback = oneginiPinCallback;

    oneginiPinProvidedHandler = new PinWithConfirmationHandler(oneginiPinCallback);

  }

  @Override
  public void onNextPinCreationAttempt(final OneginiPinValidationError oneginiPinValidationError) {
    int errorType = oneginiPinValidationError.getErrorType();

    switch (errorType) {
      case OneginiPinValidationError.PIN_TOO_SHORT:
        notifyActivity(context.getString(R.string.pin_title_choose_pin), context.getString(R.string.pin_error_too_short));
        break;
      case OneginiPinValidationError.PIN_BLACKLISTED:
        notifyActivity(context.getString(R.string.pin_title_choose_pin), context.getString(R.string.pin_error_blacklisted));
        break;
      case OneginiPinValidationError.PIN_IS_A_SEQUENCE:
        notifyActivity(context.getString(R.string.pin_title_choose_pin), context.getString(R.string.pin_error_sequence));
        break;
      case OneginiPinValidationError.PIN_USES_SIMILAR_DIGITS:
        notifyActivity(context.getString(R.string.pin_title_choose_pin), context.getString(R.string.pin_error_similar));
        break;
      default:
        // TODO add general error handling
        break;
    }
  }

  @Override
  public void finishPinCreation() {

  }

  /**
   * Extended pin handler, used to create PIN verification step
   */
  private class PinWithConfirmationHandler implements OneginiPinCallback {

    private final OneginiPinCallback originalHandler;
    private char[] pin;

    public PinWithConfirmationHandler(final OneginiPinCallback originalHandler) {
      this.originalHandler = originalHandler;
    }

    private void firstPinProvided(final char[] pin) {
      this.pin = pin;
      notifyActivity(context.getString(R.string.pin_title_verify_pin), "");
    }

    public void secondPinProvided(final char[] pin) {
      final boolean pinsEqual = Arrays.equals(this.pin, pin);
      nullifyPinArray();
      if (pinsEqual) {
        originalHandler.acceptAuthenticationRequest(pin);
      } else {
        notifyActivity(context.getString(R.string.pin_title_choose_pin), context.getString(R.string.pin_error_not_equal));
      }
    }

    private boolean isPinSet() {
      return pin != null;
    }

    private void nullifyPinArray() {
      final int arraySize = pin.length;
      for (int i = 0; i < arraySize; i++) {
        pin[i] = '\0';
      }
      pin = null;
    }

    @Override
    public void acceptAuthenticationRequest(final char[] chars) {
      if (isPinSet()) {
        secondPinProvided(pin);
      } else {
        firstPinProvided(pin);
      }
    }

    @Override
    public void denyAuthenticationRequest() {

    }
  }

//
//  @Override
//  public void pinBlackListed() {
//    notifyActivity(context.getString(R.string.pin_title_choose_pin), context.getString(R.string.pin_error_blacklisted));
//  }
//
//  @Override
//  public void pinShouldNotBeASequence() {
//    notifyActivity(context.getString(R.string.pin_title_choose_pin), context.getString(R.string.pin_error_sequence));
//  }
//
//  @Override
//  public void pinShouldNotUseSimilarDigits(final int maxSimilar) {
//    notifyActivity(context.getString(R.string.pin_title_choose_pin), context.getString(R.string.pin_error_similar));
//  }
//
//  @Override
//  public void pinTooShort() {
//    notifyActivity(context.getString(R.string.pin_title_choose_pin), context.getString(R.string.pin_error_too_short));
//  }
}
