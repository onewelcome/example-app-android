package com.onegini.mobile.exampleapp.model;

import com.google.gson.annotations.SerializedName;
import com.onegini.mobile.exampleapp.Constants;

@SuppressWarnings("unused")
public class Person {

  @SerializedName("person_id")
  private String id;
  @SerializedName("first_name")
  private String firstName;
  @SerializedName("last_name")
  private String lastName;
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
