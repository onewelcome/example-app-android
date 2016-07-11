package com.onegini.mobile.exampleapp.network.client;

import com.onegini.mobile.exampleapp.model.Profile;
import retrofit.http.GET;
import rx.Observable;

public interface PersonClient {

  @GET("/client/resource/profile")
  Observable<Profile> getPerson();
}
