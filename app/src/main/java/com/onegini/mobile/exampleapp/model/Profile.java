package com.onegini.mobile.exampleapp.model;

import com.google.gson.annotations.SerializedName;
import com.onegini.mobile.exampleapp.Constants;

@SuppressWarnings("unused")
public class Profile {

  @SerializedName("full_name")
  private String fullName;
  @SerializedName("address")
  private String address;
  @SerializedName("city")
  private String city;
  @SerializedName("date_of_birth")
  private String dateOfBirth;

  public String getFullName() {
    return fullName;
  }

  public String getAddress() {
    return address;
  }

  public String getCity() {
    return city;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public String getPersonFullInfo() {
    return getFullName() + Constants.NEW_LINE + getDateOfBirth() + Constants.NEW_LINE + getAddress() + Constants.SPACE + getCity();
  }
}
