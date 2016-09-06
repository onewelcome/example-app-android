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
import butterknife.Bind;
import butterknife.ButterKnife;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.adapter.DevicesAdapter;
import com.onegini.mobile.exampleapp.model.Device;
import com.onegini.mobile.exampleapp.network.UserRelatedService;
import com.onegini.mobile.exampleapp.network.response.DevicesResponse;
import rx.Subscription;

public class DevicesListActivity extends AppCompatActivity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.toolbar)
  Toolbar toolbar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.recycler_view)
  RecyclerView recyclerView;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.progress_bar)
  ProgressBar progressBar;

  private Subscription subscription;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_devices_list);
    ButterKnife.bind(this);

    setupActionBar();
    fetchUserDevices();
  }

  private void fetchUserDevices() {
    subscription = UserRelatedService.getInstance(this)
        .getDevices()
        .subscribe(this::onDevicesFetched, throwable -> onDevicesFetchFailed(), this::onFetchComplete);
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
    if (subscription != null) {
      subscription.unsubscribe();
    }
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
