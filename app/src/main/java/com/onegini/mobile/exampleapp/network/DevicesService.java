package com.onegini.mobile.exampleapp.network;

import android.content.Context;
import com.onegini.mobile.exampleapp.network.client.DevicesClient;
import com.onegini.mobile.exampleapp.network.client.SecuredClient;
import com.onegini.mobile.exampleapp.network.response.DevicesResponse;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class DevicesService {

  private static DevicesService instance;

  public static DevicesService getInstance(final Context context) {
    if (instance == null) {
      instance = new DevicesService(context);
    }
    return instance;
  }

  private final DevicesClient devicesClient;

  private DevicesService(final Context context) {
    devicesClient = SecuredClient.prepareSecuredClient(DevicesClient.class, context);
  }

  public Observable<DevicesResponse> getDevices() {
    return devicesClient.getDevices()
        .observeOn(AndroidSchedulers.mainThread());
  }
}
