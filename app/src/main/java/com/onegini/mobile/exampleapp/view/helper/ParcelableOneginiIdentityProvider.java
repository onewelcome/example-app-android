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

package com.onegini.mobile.exampleapp.view.helper;

import android.os.Parcel;
import android.os.Parcelable;
import com.onegini.mobile.sdk.android.model.OneginiIdentityProvider;

public class ParcelableOneginiIdentityProvider implements Parcelable, OneginiIdentityProvider {

  public static final Creator<ParcelableOneginiIdentityProvider> CREATOR = new Creator<ParcelableOneginiIdentityProvider>() {
    @Override
    public ParcelableOneginiIdentityProvider createFromParcel(Parcel in) {
      return new ParcelableOneginiIdentityProvider(in);
    }

    @Override
    public ParcelableOneginiIdentityProvider[] newArray(int size) {
      return new ParcelableOneginiIdentityProvider[size];
    }
  };

  private final String id;
  private final String name;

  public ParcelableOneginiIdentityProvider(String id, String name) {
    this.id = id;
    this.name = name;
  }

  private ParcelableOneginiIdentityProvider(Parcel in) {
    id = in.readString();
    name = in.readString();
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel parcel, final int i) {
    parcel.writeString(id);
    parcel.writeString(name);
  }
}
