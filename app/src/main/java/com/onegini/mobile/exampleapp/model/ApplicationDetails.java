package com.onegini.mobile.exampleapp.model;

import com.google.gson.annotations.SerializedName;
import com.onegini.mobile.exampleapp.Constants;

@SuppressWarnings("unused")
public class ApplicationDetails {

  @SerializedName("application_identifier")
  private String applicationIdentifier;
  @SerializedName("application_platform")
  private String applicationPlatform;
  @SerializedName("application_version")
  private String applicationVersion;

  public String getApplicationIdentifier() {
    return applicationIdentifier;
  }

  public String getApplicationPlatform() {
    return applicationPlatform;
  }

  public String getApplicationVersion() {
    return applicationVersion;
  }

  public String getApplicationDetails() {
    return getApplicationIdentifier() + Constants.NEW_LINE + getApplicationPlatform() + Constants.NEW_LINE + getApplicationVersion();
  }
}
