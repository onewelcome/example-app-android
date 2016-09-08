package com.onegini.mobile.exampleapp.network.response;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.onegini.mobile.exampleapp.model.Device;

public class DevicesResponse {

  @SerializedName("devices")
  private List<Device> devices = new ArrayList<>();

  public List<Device> getDevices() {
    return devices;
  }
}