package com.onegini.mobile.exampleapp.view.dialog;

import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import com.onegini.mobile.android.sdk.dialogs.OneginiCreatePinDialog;
import com.onegini.mobile.android.sdk.exception.OneginiClientNotValidatedException;
import com.onegini.mobile.android.sdk.handlers.OneginiPinProvidedHandler;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.OneginiSDK;
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

  @Override
  public void pinBlackListed() {
    notifyActivity(applicationContext.getString(R.string.pin_title_choose_pin), applicationContext.getString(R.string.pin_error_blacklisted));
  }

  @Override
  public void pinShouldNotBeASequence() {
    notifyActivity(applicationContext.getString(R.string.pin_title_choose_pin), applicationContext.getString(R.string.pin_error_sequence));
  }

  @Override
  public void pinShouldNotUseSimilarDigits(final int maxSimilar) {
    notifyActivity(applicationContext.getString(R.string.pin_title_choose_pin), applicationContext.getString(R.string.pin_error_similar));
  }

  @Override
  public void pinTooShort() {
    notifyActivity(applicationContext.getString(R.string.pin_title_choose_pin), applicationContext.getString(R.string.pin_error_too_short));
  }

  private void notifyActivity(final String title, final String message) {
    final Intent intent = new Intent(applicationContext, PinActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(PinActivity.EXTRA_TITLE, title);
    intent.putExtra(PinActivity.EXTRA_MESSAGE, message);
    applicationContext.startActivity(intent);
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
      boolean isPinValid = false;
      try {
        isPinValid = OneginiSDK.getOneginiClient(applicationContext).getUserClient().isPinValid(pin, CreatePinDialog.this);
      } catch (final OneginiClientNotValidatedException e) {

      }

      // if pin is not valid, then the SDK will call one of error methods, like OneginiCreatePinDialog#pinpinBlackListed()
      if (isPinValid) {
        this.pin = pin;
        notifyActivity(applicationContext.getString(R.string.pin_title_verify_pin), "");
      }
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
