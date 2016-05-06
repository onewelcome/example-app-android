package com.onegini.mobile.exampleapp.service.storage;

import android.content.Context;
import android.content.SharedPreferences;

/*
Please note that below class is implemented only for the demo purposes and must NOT be included in any production code.
 */
public class AuthenticationStorage extends Storage {

  private static final String FILENAME = "authenticationSharedPreferences";
  private static final String KEY_PIN = "pinCode";
  private static final String KEY_LOGIN_WITH_FACE = "loginWithFaceFlag";

  private final SharedPreferences.Editor editor;

  public AuthenticationStorage(final Context context) {
    super(context, FILENAME);
    editor = sharedPreferences.edit();
  }

  public void setPinCode(final char[] pin) {
    editor.putString(KEY_PIN, new String(pin));
    editor.apply();
  }

  public char[] getPinCode() {
    return sharedPreferences.getString(KEY_PIN, "").toCharArray();
  }

  public void setLoginWithFace(final boolean loginWithFace) {
    editor.putBoolean(KEY_LOGIN_WITH_FACE, loginWithFace);
    editor.apply();
  }

  public boolean isLoginWithFace() {
    return sharedPreferences.getBoolean(KEY_LOGIN_WITH_FACE, false);
  }
}
