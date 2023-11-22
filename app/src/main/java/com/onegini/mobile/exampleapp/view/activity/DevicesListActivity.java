/*
 * Copyright (c) 2016-2020 Onegini B.V.
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

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.adapter.DevicesAdapter;
import com.onegini.mobile.exampleapp.model.Device;
import com.onegini.mobile.exampleapp.network.UserService;
import com.onegini.mobile.exampleapp.network.response.DevicesResponse;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.List;

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

  private Disposable disposable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_devices_list);
    ButterKnife.bind(this);
    setupActionBar();
    fetchUserDevices();
  }

  private void fetchUserDevices() {
    disposable = UserService.getInstance(this)
        .getDevices()
        .doFinally(this::onFetchComplete)
        .subscribe(this::onDevicesFetched, throwable -> onDevicesFetchFailed());
  }

  private void onDevicesFetched(final DevicesResponse devicesResponse) {
    displayFetchedDevices(devicesResponse.getDevices());
  }

  private void onDevicesFetchFailed() {
    showToast("onDevicesFetchFailed");
  }

  private void onFetchComplete() {
    progressBar.setVisibility(View.INVISIBLE);
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
    if (disposable != null) {
      disposable.dispose();
    }
    super.onDestroy();
  }

  private void setupActionBar() {
    setSupportActionBar(toolbar);

    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setLogo(R.drawable.thales_icon_white);
      actionBar.setDisplayUseLogoEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }
  }
}
