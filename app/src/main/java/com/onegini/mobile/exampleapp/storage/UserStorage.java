package com.onegini.mobile.exampleapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserStorage {
  private static final String PREFS_NAME = "user_storage";
  private static final String EMPTY = "";

  private final SharedPreferences sharedPreferences;

  public UserStorage(final Context context) {
    sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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

  public List<User> loadUsers(final Set<UserProfile> userProfiles) {
    final List<User> listOfUsers = new ArrayList<>(userProfiles.size());

    for (final UserProfile userProfile : userProfiles) {
      listOfUsers.add(loadUser(userProfile));
    }

    return listOfUsers;
  }
}
