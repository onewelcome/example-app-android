package com.onegini.mobile.exampleapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class GCMStorage {

  private static final String FILENAME = "gcm_shared_preferences";
  private static final String KEY_REGISTRATION_ID = "registration_id";
  private static final String KEY_APP_VERSION = "app_version";

  private final SharedPreferences sharedPreferences;
  private final SharedPreferences.Editor editor;

  public GCMStorage(final Context context) {
    sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();
  }

  public void setRegistrationId(final String regId) {
    editor.putString(KEY_REGISTRATION_ID, regId);
  }

  public String getRegistrationId() {
    return sharedPreferences.getString(KEY_REGISTRATION_ID, "");
  }

  public void setAppVersion(final int appVersion) {
    editor.putInt(KEY_APP_VERSION, appVersion);
  }

  public int getAppVersion() {
    return sharedPreferences.getInt(KEY_APP_VERSION, 0);
  }

  public void save() {
    editor.apply();
  }
}
