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

package com.onegini.mobile.exampleapp.view.activity;

import java.util.Set;

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
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.adapter.PendingPushMessagesAdapter;
import com.onegini.mobile.sdk.android.handlers.OneginiPendingMobileAuthWithPushRequestsHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiPendingMobileAuthWithPushRequestError;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthWithPushRequest;

public class PendingPushMessagesActivity extends AppCompatActivity {


  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_devices_list);
    ButterKnife.bind(this);
    setupActionBar();
    fetchPendingTransactions();
  }

  private void fetchPendingTransactions() {
    OneginiSDK.getOneginiClient(this).getUserClient().getPendingMobileAuthWithPushRequests(new OneginiPendingMobileAuthWithPushRequestsHandler() {
      @Override
      public void onSuccess(final Set<OneginiMobileAuthWithPushRequest> set) {
        displayFetchedMessages(set);
        progressBar.setVisibility(View.INVISIBLE);
      }

      @Override
      public void onError(final OneginiPendingMobileAuthWithPushRequestError oneginiPendingMobileAuthWithPushRequestError) {
        showToast("Fetch transactions error: "+oneginiPendingMobileAuthWithPushRequestError.getMessage());
      }
    });
  }


  private void displayFetchedMessages(final Set<OneginiMobileAuthWithPushRequest> set) {
    if (set.isEmpty()) {
      showToast("No pending transactions");
      return;
    }

    final PendingPushMessagesAdapter adapter = new PendingPushMessagesAdapter(set);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
