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

package com.onegini.mobile.exampleapp.view.activity;

import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.adapter.DevicesAdapter;
import com.onegini.mobile.exampleapp.model.Device;
import com.onegini.mobile.exampleapp.network.UserService;
import com.onegini.mobile.exampleapp.network.response.DevicesResponse;
import com.onegini.mobile.exampleapp.storage.DeviceSettingsStorage;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DevicesListActivity extends AppCompatActivity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;

  private DeviceSettingsStorage deviceSettingsStorage;
  private CompositeDisposable disposables = new CompositeDisposable();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_devices_list);
    ButterKnife.bind(this);
    deviceSettingsStorage = new DeviceSettingsStorage(this);
    setupActionBar();
    fetchUserDevices();
  }

  private void fetchUserDevices() {
    final boolean useRetrofit2 = deviceSettingsStorage.shouldUseRetrofit2();
    UserService.getInstance(this)
        .getDevices(useRetrofit2)
        .subscribe(new SingleObserver<DevicesResponse>() {
          @Override
          public void onSubscribe(final Disposable d) {
            disposables.add(d);
          }

          @Override
          public void onSuccess(final DevicesResponse devicesResponse) {
            onDevicesFetched(devicesResponse);
          }

          @Override
          public void onError(final Throwable e) {
            onDevicesFetchFailed();
          }
        });
  }

  private void onDevicesFetched(final DevicesResponse devicesResponse) {
    displayFetchedDevices(devicesResponse.getDevices());
    progressBar.setVisibility(View.INVISIBLE);
  }

  private void onDevicesFetchFailed() {
    showToast("onDevicesFetchFailed");
  }

  private void displayFetchedDevices(final List<Device> devices) {
    final DevicesAdapter adapter = new DevicesAdapter(devices);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onDestroy() {
    disposables.clear();
    super.onDestroy();
  }

  private void setupActionBar() {
    setSupportActionBar(toolbar);

    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setLogo(R.mipmap.ic_launcher);
      actionBar.setDisplayUseLogoEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }
  }
}
