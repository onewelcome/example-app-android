package com.onegini.mobile.exampleapp.network;

import android.content.Context;
import com.onegini.mobile.exampleapp.model.ApplicationDetails;
import com.onegini.mobile.exampleapp.network.client.DeviceRelatedAnonymousClient;
import com.onegini.mobile.exampleapp.network.client.SecuredResourceGatewayClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class ApplicationDetailsService {

  private static ApplicationDetailsService instance;

  public static ApplicationDetailsService getInstance(final Context context) {
    if (instance == null) {
      instance = new ApplicationDetailsService(context);
    }
    return instance;
  }

  private final DeviceRelatedAnonymousClient applicationDetailsClient;

  private ApplicationDetailsService(final Context context) {
    applicationDetailsClient = SecuredResourceGatewayClient.prepareSecuredAnonymousClient(DeviceRelatedAnonymousClient.class, context);
  }

  public Observable<ApplicationDetails> getApplicationDetails() {
    return applicationDetailsClient.getApplicationDetails()
        .observeOn(AndroidSchedulers.mainThread());
  }
}