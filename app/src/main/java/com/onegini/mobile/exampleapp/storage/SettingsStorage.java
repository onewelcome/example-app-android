package com.onegini.mobile.exampleapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsStorage {

  private static final String PREFS_NAME = "settings_storage";
  private static final String KEY_MOBILE_AUTH = "mobile_auth_enabled";

  private final SharedPreferences sharedPreferences;

  public SettingsStorage(final Context context) {
    sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
  }

  public boolean isMobileAuthenticationEnabled() {
    return sharedPreferences.getBoolean(KEY_MOBILE_AUTH, false);
  }

  public void setMobileAuthenticationEnabled() {
    sharedPreferences.edit().
        putBoolean(KEY_MOBILE_AUTH, true)
        .apply();
  }
}
