package com.onegini.mobile.exampleapp.view.helper;

import android.widget.ImageView;
import com.onegini.mobile.exampleapp.R;

public class PinInputFields {

  private final int EMPTY_FIELD_RESOURCE_ID = R.drawable.circle_empty;
  private final int FOCUSED_FIELD_RESOURCE_ID = R.drawable.circle_focused;
  private final int FILLED_FIELD_RESOURCE_ID = R.drawable.circle_filled;

  private final ImageView[] pinInputs;
  private final int pinLength;
  private final PinProvidedListener pinProvidedListener;
  private int cursorIndex;
  private char[] pin;

  public interface PinProvidedListener {
    void onPinProvided(char[] pin);
  }

  public PinInputFields(final PinProvidedListener listener, final ImageView[] pinInputs) {
    pinProvidedListener = listener;
    this.pinInputs = pinInputs;
    this.pinLength = pinInputs.length;

    pin = new char[pinLength];
  }

  public void onPinDigitRemoved() {
    if (cursorIndex > 0) {
      pinInputs[cursorIndex].setImageResource(EMPTY_FIELD_RESOURCE_ID);
      pin[--cursorIndex] = '\0';
    }

    pinInputs[cursorIndex].setImageResource(FOCUSED_FIELD_RESOURCE_ID);
  }

  public void onPinDigitPressed(final int digit) {
    if (cursorIndex >= pinLength) {
      return;
    }

    pin[cursorIndex] = Character.forDigit(digit, 10);
    pinInputs[cursorIndex].setImageResource(FILLED_FIELD_RESOURCE_ID);

    cursorIndex++;
    if (cursorIndex >= pinLength) {
      pinProvidedListener.onPinProvided(pin.clone());
      clearPin();
    } else {
      pinInputs[cursorIndex].setImageResource(FOCUSED_FIELD_RESOURCE_ID);
    }
  }

  public void reset() {
    for (int i = 1; i < pinLength; i++) {
      pinInputs[i].setImageResource(EMPTY_FIELD_RESOURCE_ID);
    }
    pinInputs[0].setImageResource(FOCUSED_FIELD_RESOURCE_ID);

    clearPin();
    cursorIndex = 0;
  }

  public boolean inPinEmpty() {
    return cursorIndex == 0;
  }

  private void clearPin() {
    for (int index = 0; index < pinLength; index++) {
      pin[index] = '\0';
    }
    pin = new char[pinLength];
  }
}
