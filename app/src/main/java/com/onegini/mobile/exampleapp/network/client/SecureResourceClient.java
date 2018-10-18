/*
 * Copyright (c) 2016-2018 Onegini B.V.
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
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecureResourceClient {

  private SecureResourceClient() {
  }

  public static <T> T prepareSecuredUserRetrofitClient(final Class<T> clazz, final Context context) {
    final OkHttpClient okHttpClient = OneginiSDK.getOneginiClient(context).getUserClient().getResourceOkHttpClient();
    return prepareSecuredRetrofitClient(clazz, context, okHttpClient);
  }

  public static <T> T prepareSecuredImplicitUserRetrofitClient(final Class<T> clazz, final Context context) {
    final OkHttpClient okHttpClient = OneginiSDK.getOneginiClient(context).getUserClient().getImplicitResourceOkHttpClient();
    return prepareSecuredRetrofitClient(clazz, context, okHttpClient);
  }

  public static <T> T prepareSecuredAnonymousRetrofitClient(final Class<T> clazz, final Context context) {
    final OkHttpClient okHttpClient = OneginiSDK.getOneginiClient(context).getDeviceClient().getAnonymousResourceOkHttpClient();
    return prepareSecuredRetrofitClient(clazz, context, okHttpClient);
  }

  private static <T> T prepareSecuredRetrofitClient(final Class<T> clazz, final Context context, final OkHttpClient okHttpClient) {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(context);
    final Retrofit retrofit = new Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(oneginiClient.getConfigModel().getResourceBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    return retrofit.create(clazz);
  }
}