/*
 * Copyright (c) 2016 Onegini B.V.
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

public class SecureResourceClient {

  private static final RestAdapter.LogLevel LOG_LEVEL = RestAdapter.LogLevel.FULL;

  private static final GsonConverter gsonConverter = new GsonConverter(new GsonBuilder().disableHtmlEscaping().create());

  public static <T> T prepareSecuredUserClient(final Class<T> clazz, final Context context) {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(context);
    final RestAdapter restAdapter = new RestAdapter.Builder()
        .setClient(oneginiClient.getUserClient().getResourceRetrofitClient())
        .setEndpoint(oneginiClient.getConfigModel().getResourceBaseUrl())
        .setLogLevel(LOG_LEVEL)
        .setConverter(gsonConverter)
        .build();
    return restAdapter.create(clazz);
  }

  public static <T> T prepareSecuredAnonymousClient(final Class<T> clazz, final Context context) {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(context);
    final RestAdapter restAdapter = new RestAdapter.Builder()
        .setClient(oneginiClient.getDeviceClient().getAnonymousResourceRetrofitClient())
        .setEndpoint(oneginiClient.getConfigModel().getResourceBaseUrl())
        .setLogLevel(LOG_LEVEL)
        .setConverter(gsonConverter)
        .build();
    return restAdapter.create(clazz);
  }
}