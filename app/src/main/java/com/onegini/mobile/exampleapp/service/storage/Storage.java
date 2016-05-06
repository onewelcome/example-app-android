package com.onegini.mobile.exampleapp.service.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {

  protected final SharedPreferences sharedPreferences;

  /**
   * Creates an instance of this class
   *
   * @param context   current context
   * @param fileName  file name for the storage
   */
  public Storage(final Context context, final String fileName) {
    sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
  }

  /**
   * Clears the {@link SharedPreferences} storage
   */
  public void purge() {
    sharedPreferences.edit().clear().apply();
  }

}
