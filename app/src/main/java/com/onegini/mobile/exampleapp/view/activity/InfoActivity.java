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

import static com.onegini.mobile.exampleapp.view.helper.ErrorMessageParser.parseErrorMessage;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.ApplicationDetails;
import com.onegini.mobile.exampleapp.model.ImplicitUserDetails;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.exampleapp.network.AnonymousService;
import com.onegini.mobile.exampleapp.network.ImplicitUserService;
import com.onegini.mobile.exampleapp.util.DeregistrationUtil;
import com.onegini.mobile.exampleapp.view.helper.AlertDialogFragment;
import com.onegini.mobile.sdk.android.handlers.OneginiDeviceAuthenticationHandler;
import com.onegini.mobile.sdk.android.handlers.OneginiImplicitAuthenticationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiDeviceAuthenticationError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiImplicitTokenRequestError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class InfoActivity extends AppCompatActivity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.user_profile_id)
  TextView userProfileId;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.user_profile_name)
  TextView userProfileName;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.implicit_details)
  TextView implicitDetails;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.device_details)
  RelativeLayout deviceDetails;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.application_id)
  TextView applicationId;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.application_version)
  TextView applicationVersion;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.platform)
  TextView platform;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.device_details_fetch_failed)
  TextView deviceDetailsFetchFailed;
  @BindView(R.id.bottom_navigation)
  BottomNavigationView bottomNavigationView;


  private CompositeDisposable disposables = new CompositeDisposable();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_info);
    ButterKnife.bind(this);
    setupUi();
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(0, 0);
  }

  @Override
  protected void onResume() {
    super.onResume();
    bottomNavigationView.getMenu()
        .findItem(R.id.action_info)
        .setChecked(true);
    authenticateDevice();
  }

  @Override
  public void onDestroy() {
    disposables.clear();
    super.onDestroy();
  }

  private void setupUi() {
    setupActionBar();
    setupNavigationBar();
    setupUserDetails();
  }

  private void setupUserDetails() {
    final User user = LoginActivity.selectedUser;
    if (user == null) {
      return;
    }

    userProfileId.setText(user.getUserProfile().getProfileId());
    userProfileName.setText(user.getName());
    getImplicitUserDetails(user.getUserProfile());
  }

  private void showError(final String errorMessage) {
    final DialogFragment dialog = AlertDialogFragment.newInstance(errorMessage);
    dialog.show(getFragmentManager(), "alert_dialog");
  }

  private void setupActionBar() {
    setSupportActionBar(toolbar);

    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setLogo(R.mipmap.ic_launcher);
      actionBar.setDisplayUseLogoEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }
  }

  private void setupNavigationBar() {
    bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
      final int itemId = item.getItemId();
      if (itemId == R.id.action_home) {
        onHomeClicked();
      } else if (itemId == R.id.action_notifications) {
        onNotificationsClicked();
      }
      return true;
    });
  }

  private void onNotificationsClicked() {
    startActivity(new Intent(this, PendingPushMessagesActivity.class));
    finish();
  }

  private void onHomeClicked() {
    final UserProfile authenticatedUserProfile = OneginiSDK.getOneginiClient(this).getUserClient().getAuthenticatedUserProfile();
    if (authenticatedUserProfile == null) {
      startActivity(new Intent(this, LoginActivity.class));
    } else {
      startActivity(new Intent(this, DashboardActivity.class));
    }
    finish();
  }

  private void authenticateDevice() {
    OneginiSDK.getOneginiClient(this).getDeviceClient()
        .authenticateDevice(new String[]{ "application-details" }, new OneginiDeviceAuthenticationHandler() {
              @Override
              public void onSuccess() {
                callAnonymousResourceCallToFetchApplicationDetails();
              }

              @Override
              public void onError(final OneginiDeviceAuthenticationError error) {
                onApplicationDetailsFetchFailed();
                OneginiDeviceAuthenticationError.Type errorType = error.getErrorType();

                if (errorType == OneginiDeviceAuthenticationError.Type.DEVICE_DEREGISTERED) {
                  new DeregistrationUtil(InfoActivity.this).onDeviceDeregistered();
                }
                InfoActivity.this.onError(error);
              }
            }
        );
  }

  private void callAnonymousResourceCallToFetchApplicationDetails() {
    disposables.add(
        AnonymousService.getInstance(this)
            .getApplicationDetails()
            .subscribe(this::onApplicationDetailsFetched, throwable -> onApplicationDetailsFetchFailed())
    );
  }

  private void onApplicationDetailsFetched(final ApplicationDetails details) {
    deviceDetails.setVisibility(View.VISIBLE);
    deviceDetailsFetchFailed.setVisibility(View.GONE);
    applicationId.setText(details.getApplicationIdentifier());
    applicationVersion.setText(details.getApplicationVersion());
    platform.setText(details.getApplicationPlatform());
  }

  private void onApplicationDetailsFetchFailed() {
    deviceDetails.setVisibility(View.GONE);
    deviceDetailsFetchFailed.setVisibility(View.VISIBLE);
  }

  private void onError(final OneginiError error) {
    error.printStackTrace();
    showError(parseErrorMessage(error) + " Check logcat for more details.");
  }

  private void getImplicitUserDetails(final UserProfile userProfile) {
    OneginiSDK.getOneginiClient(this).getUserClient()
        .authenticateUserImplicitly(userProfile, new String[]{ "read" }, new OneginiImplicitAuthenticationHandler() {
          @Override
          public void onSuccess(final UserProfile profile) {
            callImplicitResourceCallToFetchImplicitUserDetails();
          }

          @Override
          public void onError(final OneginiImplicitTokenRequestError error) {
            onImplicitDetailsFetchFailed(error);
            OneginiImplicitTokenRequestError.Type errorType = error.getErrorType();
            if (errorType == OneginiImplicitTokenRequestError.Type.DEVICE_DEREGISTERED) {
              new DeregistrationUtil(InfoActivity.this).onDeviceDeregistered();
            } else if (errorType == OneginiImplicitTokenRequestError.Type.USER_DEREGISTERED) {
              new DeregistrationUtil(InfoActivity.this).onUserDeregistered(userProfile);
            }
          }
        });
  }

  private void callImplicitResourceCallToFetchImplicitUserDetails() {
    disposables.add(
        ImplicitUserService.getInstance(this)
            .getImplicitUserDetails()
            .subscribe(this::onImplicitUserDetailsFetched, this::onImplicitDetailsFetchFailed)
    );
  }

  private void onImplicitUserDetailsFetched(final ImplicitUserDetails implicitUserDetails) {
    implicitDetails.setText(implicitUserDetails.toString());
  }

  private void onImplicitDetailsFetchFailed(final Throwable throwable) {
    implicitDetails.setText(R.string.implicit_user_details_fetch_failed_label);
    throwable.printStackTrace();
  }
}
