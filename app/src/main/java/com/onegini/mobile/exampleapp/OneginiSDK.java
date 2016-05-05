package com.onegini.mobile.exampleapp;

import android.content.Context;
import com.onegini.mobile.sdk.android.library.OneginiClient;
import com.onegini.mobile.sdk.android.library.handlers.OneginiPinProvidedHandler;
import com.onegini.mobile.sdk.android.library.utils.dialogs.OneginiCreatePinDialog;

public class OneginiSDK {

  public static OneginiClient getOneginiClient(final Context context) {

    OneginiClient oneginiClient = OneginiClient.getInstance();
    if (oneginiClient == null) {
      // will throw OneginiConfigNotFoundException if OneginiConfigModel class can't be found
      oneginiClient = OneginiClient.setupInstance(context);
      oneginiClient.setCreatePinDialog(new OneginiCreatePinDialog() {
        @Override
        public void createPin(final OneginiPinProvidedHandler oneginiPinProvidedHandler) {

        }

        @Override
        public void pinBlackListed() {

        }

        @Override
        public void pinShouldNotBeASequence() {

        }

        @Override
        public void pinShouldNotUseSimilarDigits(final int i) {

        }

        @Override
        public void pinTooShort() {

        }
      });
      oneginiClient.setCurrentPinDialog(oneginiPinProvidedHandler -> {

      });
    }
    return oneginiClient;
  }
}
