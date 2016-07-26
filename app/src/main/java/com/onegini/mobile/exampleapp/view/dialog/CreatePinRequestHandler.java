package com.onegini.mobile.exampleapp.view.dialog;

import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.onegini.mobile.android.sdk.handlers.OneginiPinValidationHandler;
import com.onegini.mobile.android.sdk.handlers.error.OneginiPinValidationError;
import com.onegini.mobile.android.sdk.handlers.request.OneginiCreatePinRequestHandler;
import com.onegini.mobile.android.sdk.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.activity.PinActivity;

public class CreatePinRequestHandler implements OneginiCreatePinRequestHandler {

  public static PinWithConfirmationHandler oneginiPinCallback;
  private final Context context;

  public CreatePinRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startPinCreation(final UserProfile userProfile, final OneginiPinCallback oneginiPinCallback) {
    PinActivity.setIsCreatePinFlow(true);
    notifyActivity(context.getString(R.string.pin_title_choose_pin), "");

    CreatePinRequestHandler.oneginiPinCallback = new PinWithConfirmationHandler(oneginiPinCallback);
  }

  @Override
  public void onNextPinCreationAttempt(final OneginiPinValidationError oneginiPinValidationError) {
    handlePinValidationError(oneginiPinValidationError);
  }

  @Override
  public void finishPinCreation() {
    Toast.makeText(context, "CreatePinRequestHandler#finishPinCreation", Toast.LENGTH_LONG).show();
  }

  /**
   * Extended pin handler, used to create PIN verification step
   */
  public class PinWithConfirmationHandler {

    private final OneginiPinCallback originalHandler;

    private char[] pin;

    public PinWithConfirmationHandler(final OneginiPinCallback originalHandler) {
      this.originalHandler = originalHandler;
    }

    public void onPinProvided(final char[] pin) {
      if (isPinSet()) {
        secondPinProvided(pin);
      } else {
        firstPinProvided(pin);
      }
    }

    private void firstPinProvided(final char[] pin) {
      OneginiSDK.getOneginiClient(context).getUserClient().validatePinWithPolicy(pin, new OneginiPinValidationHandler() {
        @Override
        public void onSuccess() {
          PinWithConfirmationHandler.this.pin = pin;
          notifyActivity(context.getString(R.string.pin_title_verify_pin), "");
        }

        @Override
        public void onError(final OneginiPinValidationError oneginiPinValidationError) {
          handlePinValidationError(oneginiPinValidationError);
        }
      });
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
  }

  private void handlePinValidationError(final OneginiPinValidationError oneginiPinValidationError) {
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

  private void notifyActivity(final String title, final String message) {
    final Intent intent = new Intent(context, PinActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(PinActivity.EXTRA_TITLE, title);
    intent.putExtra(PinActivity.EXTRA_MESSAGE, message);
    context.startActivity(intent);
  }
}
