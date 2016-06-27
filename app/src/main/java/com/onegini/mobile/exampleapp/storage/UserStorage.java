package com.onegini.mobile.exampleapp.storage;

import android.content.Context;
import android.content.SharedPreferences;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.sdk.android.library.model.entity.UserProfile;

public class UserStorage {
  private static final String PREFS_NAME = "user_storage";
  private static final String EMPTY = "";

  private final SharedPreferences sharedPreferences;

  public UserStorage(final Context context) {
    sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
  }

  public void saveUser(final User user) {
    final SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(user.getUserProfile().getProfileId(), user.getName());
    editor.apply();
  }

  public User loadUser(final UserProfile userProfile) {
    return new User(userProfile, sharedPreferences.getString(userProfile.getProfileId(), EMPTY));
  }

  public void removeUser(final UserProfile userProfile) {
    final SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.remove(userProfile.getProfileId());
    editor.apply();
  }
}
