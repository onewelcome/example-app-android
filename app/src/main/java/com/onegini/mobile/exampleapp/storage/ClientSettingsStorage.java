package com.onegini.mobile.exampleapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class ClientSettingsStorage {

  private static final String PREFS_NAME = "client_settings_storage";
  private static final String KEY_RETROFIT = "retrofit";

  private final SharedPreferences sharedPreferences;

  public ClientSettingsStorage(final Context context) {
    sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
  }

  public boolean shouldUseRetrofit2() {
    return sharedPreferences.getBoolean(KEY_RETROFIT, false);
  }

  public void setShouldUseRetrofit2(final boolean use) {
    sharedPreferences.edit().putBoolean(KEY_RETROFIT, use).commit();
  }
}
