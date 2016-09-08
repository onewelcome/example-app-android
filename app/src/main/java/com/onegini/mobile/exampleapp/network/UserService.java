package com.onegini.mobile.exampleapp.network;

import android.content.Context;
import com.onegini.mobile.exampleapp.network.client.UserClient;
import com.onegini.mobile.exampleapp.network.client.SecureResourceClient;
import com.onegini.mobile.exampleapp.network.response.DevicesResponse;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class UserService {

  private static UserService instance;

  public static UserService getInstance(final Context context) {
    if (instance == null) {
      instance = new UserService(context);
    }
    return instance;
  }

  private final UserClient userClient;

  private UserService(final Context context) {
    userClient = SecureResourceClient.prepareSecuredUserClient(UserClient.class, context);
  }

  public Observable<DevicesResponse> getDevices() {
    return userClient.getDevices()
        .observeOn(AndroidSchedulers.mainThread());
  }
}
