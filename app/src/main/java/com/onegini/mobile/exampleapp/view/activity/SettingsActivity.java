package com.onegini.mobile.exampleapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.network.gcm.GCMRegistrationService;
import com.onegini.mobile.exampleapp.storage.SettingsStorage;
import com.onegini.mobile.sdk.android.handlers.OneginiChangePinHandler;
import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthenticationEnrollmentHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiChangePinError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthenticationEnrollmentError;

public class SettingsActivity extends AppCompatActivity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.toolbar)
  Toolbar toolbar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.button_mobile_authentication)
  Button mobileAuthButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.button_change_pin)
  Button changePinButton;

  private SettingsStorage settingsStorage;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    ButterKnife.bind(this);

    settingsStorage = new SettingsStorage(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    setupView();
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setupView() {
    setupActionBar();
    setupMobileAuthButton();
  }

  private void setupMobileAuthButton() {
    mobileAuthButton.setEnabled(false);
    mobileAuthButton.setText(R.string.settings_mobile_enrollment_not_available);

    final int googlePlayServicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
    if (googlePlayServicesAvailable == ConnectionResult.SUCCESS) {
      if (settingsStorage.isMobileAuthenticationEnabled()) {
        mobileAuthButton.setText(R.string.settings_mobile_enrollment_on);
      } else {
        mobileAuthButton.setText(R.string.settings_mobile_enrollment_off);
        mobileAuthButton.setEnabled(true);
      }
    }
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

  @SuppressWarnings("unused")
  @OnClick(R.id.button_mobile_authentication)
  public void enrollMobileAuthentication() {
    final OneginiMobileAuthenticationEnrollmentHandler mobileAuthenticationEnrollmentHandler = new OneginiMobileAuthenticationEnrollmentHandler() {
      @Override
      public void onSuccess() {
        settingsStorage.setMobileAuthenticationEnabled(true);
        onMobileAuthEnabled();
        showToast("Mobile authentication enabled");
      }

      @Override
      public void onError(final OneginiMobileAuthenticationEnrollmentError oneginiMobileAuthenticationEnrollmentError) {
        showToast("Mobile authentication error - " + oneginiMobileAuthenticationEnrollmentError.getErrorDescription());
      }
    };
    final GCMRegistrationService gcmRegistrationService = new GCMRegistrationService(this);
    gcmRegistrationService.registerGCMService(mobileAuthenticationEnrollmentHandler);
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_change_pin)
  public void startChangePinFlow() {
    OneginiSDK.getOneginiClient(this).getUserClient().changePin(new OneginiChangePinHandler() {
      @Override
      public void onSuccess() {
        showToast("Change PIN action finished successfully");
      }

      @Override
      public void onError(final OneginiChangePinError oneginiChangePinError) {
        @OneginiChangePinError.ChangePinErrorType int errorType = oneginiChangePinError.getErrorType();
        if (errorType == OneginiChangePinError.USER_DEREGISTERED) {
          userDeregistered();
        }
        showToast(oneginiChangePinError.getErrorDescription());
      }
    });
  }

  private void userDeregistered() {
    final Intent intent = new Intent(this, LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    finish();
  }

  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }

  private void onMobileAuthEnabled() {
    mobileAuthButton.setText(R.string.settings_mobile_enrollment_on);
    mobileAuthButton.setEnabled(false);
  }
}
