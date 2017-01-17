/*
 * Copyright (c) 2016-2017 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onegini.mobile.exampleapp.network.client;

import android.content.Context;
import com.google.gson.GsonBuilder;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.exampleapp.OneginiSDK;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecureResourceClient {

  private static final RestAdapter.LogLevel LOG_LEVEL = RestAdapter.LogLevel.FULL;

  private static final GsonConverter gsonConverter = new GsonConverter(new GsonBuilder().disableHtmlEscaping().create());

  // preparing the client using Retrofit 1.9
  public static <T> T prepareSecuredUserRetrofitClient(final Class<T> clazz, final Context context) {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(context);
    final RestAdapter restAdapter = new RestAdapter.Builder()
        // deprecated method usage; please use https://github.com/JakeWharton/retrofit1-okhttp3-client instead
        .setClient(oneginiClient.getUserClient().getResourceRetrofitClient())
        .setEndpoint(oneginiClient.getConfigModel().getResourceBaseUrl())
        .setLogLevel(LOG_LEVEL)
        .setConverter(gsonConverter)
        .build();
    return restAdapter.create(clazz);
  }

  // preparing the client using Retrofit 2.X
  public static <T> T prepareSecuredUserRetrofit2Client(final Class<T> clazz, final Context context) {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(context);
    final Retrofit retrofit = new Retrofit.Builder()
        .client(oneginiClient.getUserClient().getResourceOkHttpClient())
        .baseUrl(oneginiClient.getConfigModel().getResourceBaseUrl() + "/") // In Retrofit 2.X the base URL should end with '/'
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    return retrofit.create(clazz);
  }

  // preparing the client using Retrofit 1.9
  public static <T> T prepareSecuredAnonymousRetrofitClient(final Class<T> clazz, final Context context) {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(context);
    final RestAdapter restAdapter = new RestAdapter.Builder()
        // deprecated method usage; please use https://github.com/JakeWharton/retrofit1-okhttp3-client instead
        .setClient(oneginiClient.getDeviceClient().getAnonymousResourceRetrofitClient())
        .setEndpoint(oneginiClient.getConfigModel().getResourceBaseUrl())
        .setLogLevel(LOG_LEVEL)
        .setConverter(gsonConverter)
        .build();
    return restAdapter.create(clazz);
  }

  // preparing the client using Retrofit 2.X
  public static <T> T prepareSecuredAnonymousRetrofit2Client(final Class<T> clazz, final Context context) {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(context);
    final Retrofit retrofit = new Retrofit.Builder()
        .client(oneginiClient.getDeviceClient().getAnonymousResourceOkHttpClient())
        .baseUrl(oneginiClient.getConfigModel().getResourceBaseUrl() + "/") // In Retrofit 2.X the base URL should end with '/'
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    return retrofit.create(clazz);
  }
}