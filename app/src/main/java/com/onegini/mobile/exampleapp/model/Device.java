package com.onegini.mobile.exampleapp.model;

import com.google.gson.annotations.SerializedName;
import com.onegini.mobile.exampleapp.Constants;

@SuppressWarnings("unused")
public class Device {

  @SerializedName("id")
  private String id;
  @SerializedName("name")
  private String name;
  @SerializedName("application")
  private String application;
  @SerializedName("platform")
  private String platform;
  @SerializedName("mobile_authentication_enabled")
  private boolean mobileAuthenticationEnabled;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getApplication() {
    return application;
  }

  public String getPlatform() {
    return platform;
  }

  public boolean isMobileAuthenticationEnabled() {
    return mobileAuthenticationEnabled;
  }

  public String getDeviceFullInfo() {
    return "ID: " + getId() + Constants.NEW_LINE + getName() + Constants.NEW_LINE + getApplication() + Constants.NEW_LINE + getPlatform() + Constants.NEW_LINE
        + "Mobile authentication enabled: " + isMobileAuthenticationEnabled();
  }
}