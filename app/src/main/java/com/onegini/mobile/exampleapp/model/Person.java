package com.onegini.mobile.exampleapp.model;

import com.google.gson.annotations.SerializedName;
import com.onegini.mobile.exampleapp.Constants;

@SuppressWarnings("unused")
public class Person {

  @SuppressWarnings("unused")
  @SerializedName("person_id")
  private String id;
  @SuppressWarnings("unused")
  @SerializedName("first_name")
  private String firstName;
  @SuppressWarnings("unused")
  @SerializedName("last_name")
  private String lastName;
  @SuppressWarnings("unused")
  @SerializedName("email")
  private String email;

  public String getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFullName() {
    return getFirstName() + " " + getLastName();
  }

  public String getEmail() {
    return email;
  }

  public String getPersonFullInfo() {
    return getFullName() + Constants.NEW_LINE + getEmail();
  }
}
