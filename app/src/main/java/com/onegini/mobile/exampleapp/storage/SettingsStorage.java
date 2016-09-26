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

  public void setMobileAuthenticationEnabled(final boolean isEnabled) {
    sharedPreferences.edit().putBoolean(KEY_MOBILE_AUTH, isEnabled).apply();
  }
}
