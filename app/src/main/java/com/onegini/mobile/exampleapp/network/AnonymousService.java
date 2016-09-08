package com.onegini.mobile.exampleapp.network;

import android.content.Context;
import com.onegini.mobile.exampleapp.model.ApplicationDetails;
import com.onegini.mobile.exampleapp.network.client.AnonymousClient;
import com.onegini.mobile.exampleapp.network.client.SecureResourceClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class AnonymousService {

  private static AnonymousService instance;

  public static AnonymousService getInstance(final Context context) {
    if (instance == null) {
      instance = new AnonymousService(context);
    }
    return instance;
  }

  private final AnonymousClient applicationDetailsClient;

  private AnonymousService(final Context context) {
    applicationDetailsClient = SecureResourceClient.prepareSecuredAnonymousClient(AnonymousClient.class, context);
  }

  public Observable<ApplicationDetails> getApplicationDetails() {
    return applicationDetailsClient.getApplicationDetails()
        .observeOn(AndroidSchedulers.mainThread());
  }
}