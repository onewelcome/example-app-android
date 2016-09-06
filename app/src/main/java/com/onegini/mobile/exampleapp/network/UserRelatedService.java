package com.onegini.mobile.exampleapp.network;

import android.content.Context;
import com.onegini.mobile.exampleapp.network.client.UserRelatedClient;
import com.onegini.mobile.exampleapp.network.client.SecuredResourceGatewayClient;
import com.onegini.mobile.exampleapp.network.response.DevicesResponse;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class UserRelatedService {

  private static UserRelatedService instance;

  public static UserRelatedService getInstance(final Context context) {
    if (instance == null) {
      instance = new UserRelatedService(context);
    }
    return instance;
  }

  private final UserRelatedClient userRelatedClient;

  private UserRelatedService(final Context context) {
    userRelatedClient = SecuredResourceGatewayClient.prepareSecuredClient(UserRelatedClient.class, context);
  }

  public Observable<DevicesResponse> getDevices() {
    return userRelatedClient.getDevices()
        .observeOn(AndroidSchedulers.mainThread());
  }
}
