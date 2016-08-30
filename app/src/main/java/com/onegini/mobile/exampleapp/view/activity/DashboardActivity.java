package com.onegini.mobile.exampleapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.handlers.OneginiDeregisterUserProfileHandler;
import com.onegini.mobile.sdk.android.handlers.OneginiLogoutHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiDeregistrationError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiLogoutError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.Profile;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.exampleapp.network.PersonService;
import com.onegini.mobile.exampleapp.storage.UserStorage;
import rx.Subscription;

public class DashboardActivity extends AppCompatActivity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.toolbar)
  Toolbar toolbar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.tv_user_profile_info)
  TextView userInfoTextView;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.dashboard_welcome_text)
  TextView dashboardWelcomeText;

  private Subscription subscription;
  private UserStorage userStorage;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);
    ButterKnife.bind(this);

    userStorage = new UserStorage(this);
    setupUserInterface();
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_logout)
  public void logout() {
    OneginiSDK.getOneginiClient(this).getUserClient().logout(
        new OneginiLogoutHandler() {
          @Override
          public void onSuccess() {
            // Go to login screen
            showToast("logoutSuccess");
            startLoginActivity();
          }

          @Override
          public void onError(final OneginiLogoutError oneginiLogoutError) {
            handleLogoutError(oneginiLogoutError);
          }
        }
    );
  }

  private void handleLogoutError(final OneginiLogoutError oneginiLogoutError) {
    if (oneginiLogoutError.getErrorType() == OneginiLogoutError.LOCAL_LOGOUT) {
      showToast("The user was only logged out on the device. The access token has not been invalidated on the server-side.");
    } else if (oneginiLogoutError.getErrorType() == OneginiLogoutError.GENERAL_ERROR) {
      // General error handling for other, less relevant errors
      showToast("Logout error: " + oneginiLogoutError.getErrorDescription());
    }
    startLoginActivity();
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_deregister_user)
  public void deregisterUser() {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(this);
    final UserProfile userProfile = oneginiClient.getUserClient().getAuthenticatedUserProfile();
    if (userProfile == null) {
      showToast("userProfile == null");
      return;
    }

    oneginiClient.getUserClient().deregisterUser(userProfile, new OneginiDeregisterUserProfileHandler() {
          @Override
          public void onSuccess() {
            onUserDeregistered(userProfile);
          }

          @Override
          public void onError(final OneginiDeregistrationError oneginiDeregistrationError) {
            handleDeregistrationError(oneginiDeregistrationError);
          }
        }
    );
  }

  private void handleDeregistrationError(final OneginiDeregistrationError oneginiDeregistrationError) {
    if (oneginiDeregistrationError.getErrorType() == OneginiDeregistrationError.LOCAL_DEREGISTRATION) {
      showToast("The user was only logged out on the device. The access token has not been invalidated on the server-side.");
    } else if (oneginiDeregistrationError.getErrorType() == OneginiDeregistrationError.GENERAL_ERROR) {
      // General error handling for other, less relevant errors
      showToast("Logout error: " + oneginiDeregistrationError.getErrorDescription());
    }
    startLoginActivity();
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_get_user_profile)
  public void getUserProfileData() {
    subscription = PersonService.getInstance(this)
        .getPerson()
        .subscribe(this::onPersonFetched, throwable -> onPersonFetchFail());
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_settings)
  public void startSettingsActivity() {
    startActivity(new Intent(this, SettingsActivity.class));
  }

  private void onPersonFetchFail() {
    showToast("onPersonFetchFail");
  }

  private void onPersonFetched(final Profile profile) {
    userInfoTextView.setText(profile.getPersonFullInfo());
  }

  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  private void setupUserInterface() {
    setupActionBar();
    setupWelcomeText();
  }

  private void setupWelcomeText() {
    final UserProfile userProfile = OneginiSDK.getOneginiClient(this).getUserClient().getAuthenticatedUserProfile();
    final User user = userStorage.loadUser(userProfile);
    dashboardWelcomeText.setText(getString(R.string.welcome_user_text, user.getName()));
  }

  private void setupActionBar() {
    setSupportActionBar(toolbar);

    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayShowHomeEnabled(true);
      actionBar.setLogo(R.mipmap.ic_launcher);
      actionBar.setDisplayUseLogoEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }
  }

  @Override
  public void onDestroy() {
    if (subscription != null) {
      subscription.unsubscribe();
    }
    super.onDestroy();
  }

  private void onUserDeregistered(final UserProfile userProfile) {
    showToast("deregisterUserSuccess");
    userStorage.removeUser(userProfile);

    startLoginActivity();
  }

  private void startLoginActivity() {
    final Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
    finish();
  }
}
