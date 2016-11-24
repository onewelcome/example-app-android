/*
 * Copyright (c) 2016 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
