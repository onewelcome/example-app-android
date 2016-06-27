package com.onegini.mobile.exampleapp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.sdk.android.library.model.entity.UserProfile;

public class LocalStorageUtil {
  private static final String PREFS_NAME = "local_storage";
  private static final String EMPTY = "";

  public static void saveUser(final Context context, final User user) {
    final SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, 0).edit();
    editor.putString(user.getUserProfile().getProfileId(), user.getName());
    editor.apply();
  }

  private static String getUserName(final Context context, final UserProfile userProfile) {
    final SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
    final String userName = settings.getString(userProfile.getProfileId(), EMPTY);

    return userName;
  }

  public static List<String> getUsersNames(final Context context) {
    final List<String> usersNames = new ArrayList<>();
    final List<User> users = getUsers(context);

    for (User user : users) {
      usersNames.add(user.getName());
    }

    return usersNames;
  }

  public static User getUserByName(final Context context, final String userName) {
    final Set<UserProfile> userProfiles = OneginiSDK.getOneginiClient(context).getUserProfiles();
    for (UserProfile userProfile : userProfiles) {
      String storedUserName = getUserName(context, userProfile);
      if (storedUserName.equals(userName)) {
        return new User(userProfile, userName);
      }
    }
    return null;
  }

  public static void removeUser(final Context context, final UserProfile userProfile) {
    final SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    final SharedPreferences.Editor editor = mySPrefs.edit();
    editor.remove(userProfile.getProfileId());
    editor.apply();
  }

  private static List<User> getUsers(final Context context) {
    final Set<UserProfile> userProfiles = OneginiSDK.getOneginiClient(context).getUserProfiles();
    final List<User> users = new ArrayList<>();
    String userName;
    for (UserProfile userProfile : userProfiles) {
      userName = getUserName(context, userProfile);
      if (!userName.equals(EMPTY)) {
        users.add(new User(userProfile, userName));
        Log.d(PREFS_NAME, userName);
      }
    }
    return users;
  }
}
