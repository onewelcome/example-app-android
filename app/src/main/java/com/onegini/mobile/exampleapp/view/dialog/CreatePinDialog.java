package com.onegini.mobile.exampleapp.view.dialog;

import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.onegini.mobile.android.sdk.dialogs.OneginiCreatePinDialog;
import com.onegini.mobile.android.sdk.handlers.OneginiPinProvidedHandler;
import com.onegini.mobile.android.sdk.handlers.error.OneginiPinValidationError;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.activity.PinActivity;

public class CreatePinDialog implements OneginiCreatePinDialog {

  public static OneginiPinProvidedHandler oneginiPinProvidedHandler;

  private final Context applicationContext;

  public CreatePinDialog(final Context context) {
    applicationContext = context.getApplicationContext();
  }

  @Override
  public void createPin(final UserProfile userProfile, final OneginiPinProvidedHandler pinProvidedHandler) {
    PinActivity.setIsCreatePinFlow(true);
    notifyActivity(applicationContext.getString(R.string.pin_title_choose_pin), "");
    oneginiPinProvidedHandler = new PinWithConfirmationHandler(pinProvidedHandler);
  }

  private void notifyActivity(final String title, final String message) {
    final Intent intent = new Intent(applicationContext, PinActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(PinActivity.EXTRA_TITLE, title);
    intent.putExtra(PinActivity.EXTRA_MESSAGE, message);
    applicationContext.startActivity(intent);
  }

  @Override
  public void onSuccess() {
    Toast.makeText(applicationContext, "onSuccess", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onError(final OneginiPinValidationError oneginiPinValidationError) {
    int errorType = oneginiPinValidationError.getErrorType();

    switch (errorType) {
      case OneginiPinValidationError.PIN_TOO_SHORT:
        notifyActivity(applicationContext.getString(R.string.pin_title_choose_pin), applicationContext.getString(R.string.pin_error_too_short));
        break;
      case OneginiPinValidationError.PIN_BLACKLISTED:
        notifyActivity(applicationContext.getString(R.string.pin_title_choose_pin), applicationContext.getString(R.string.pin_error_blacklisted));
        break;
      case OneginiPinValidationError.PIN_IS_A_SEQUENCE:
        notifyActivity(applicationContext.getString(R.string.pin_title_choose_pin), applicationContext.getString(R.string.pin_error_sequence));
        break;
      case OneginiPinValidationError.PIN_USES_SIMILAR_DIGITS:
        notifyActivity(applicationContext.getString(R.string.pin_title_choose_pin), applicationContext.getString(R.string.pin_error_similar));
        break;
      default:
        // TODO add general error handling
        break;
    }
  }

  /**
   * Extended pin handler, used to create PIN verification step
   */
  private class PinWithConfirmationHandler implements OneginiPinProvidedHandler {

    private final OneginiPinProvidedHandler originalHandler;
    private char[] pin;

    public PinWithConfirmationHandler(final OneginiPinProvidedHandler originalHandler) {
      this.originalHandler = originalHandler;
    }

    @Override
    public void onPinProvided(final char[] pin) {
      if (isPinSet()) {
        secondPinProvided(pin);
      } else {
        firstPinProvided(pin);
      }
    }

    private void firstPinProvided(final char[] pin) {
      notifyActivity(applicationContext.getString(R.string.pin_title_verify_pin), "");
      this.pin = pin;
    }

    public void secondPinProvided(final char[] pin) {
      final boolean pinsEqual = Arrays.equals(this.pin, pin);
      nullifyPinArray();
      if (pinsEqual) {
        originalHandler.onPinProvided(pin);
      } else {
        notifyActivity(applicationContext.getString(R.string.pin_title_choose_pin), applicationContext.getString(R.string.pin_error_not_equal));
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
  }
}
