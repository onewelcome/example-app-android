package com.onegini.mobile.exampleapp.model;


import com.onegini.mobile.sdk.android.library.model.entity.UserProfile;

public class User {

  private String name;

  private UserProfile userProfile;

  public User(final UserProfile userProfile) {
    setUserProfile(userProfile);
  }

  public User(final UserProfile userProfile, final String userName) {
    setUserProfile(userProfile);
    setName(userName);
  }

  public UserProfile getUserProfile() {
    return userProfile;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setUserProfile(final UserProfile userProfile) {
    this.userProfile = userProfile;
  }
}
