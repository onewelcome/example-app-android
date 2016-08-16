package com.onegini.mobile.exampleapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsStorage {

  private static final String PREFS_NAME = "settings_storage";
  private static final String KEY_MOBILE_AUTH_ENABLES = "key_mobile_auth_enabled";

  private final SharedPreferences sharedPreferences;

  public SettingsStorage(final Context context) {
    sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
  }

  public void saveMobileAuthenticationEnabled(final boolean isEnabled) {
    final SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(KEY_MOBILE_AUTH_ENABLES, isEnabled);
    editor.apply();
  }

  public boolean isMobileAuthenticationEnabled() {
    return sharedPreferences.getBoolean(KEY_MOBILE_AUTH_ENABLES, false);
  }
}
