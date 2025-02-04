package com.onegini.mobile.exampleapp.view.helper;

import com.onegini.mobile.sdk.android.handlers.error.OneginiError;

import java.util.Locale;

public class ErrorMessageParser {

  private ErrorMessageParser() {
  }

  public static String parseErrorMessage(final OneginiError error) {
    return String.format(Locale.getDefault(), "%s: %s", error.getErrorType(), error.getMessage());
  }
}
