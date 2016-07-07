package com.onegini.mobile.exampleapp.view.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.android.sdk.handlers.OneginiAuthenticationHandler;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.exampleapp.storage.UserStorage;

public class LoginActivity extends Activity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.label)
  TextView label;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.users_spinner)
  Spinner usersSpinner;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.login_button)
  Button loginButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.register_button)
  Button registerButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.progress_bar_login)
  ProgressBar progressBar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.layout_login_content)
  RelativeLayout layoutLoginContent;

  private List<User> listOfUsers = new ArrayList<>();
  private UserStorage userStorage;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    setupUserInterface();
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.login_button)
  public void loginButtonClicked() {
    setProgressbarVisibility(true);

    final User user = listOfUsers.get(usersSpinner.getSelectedItemPosition());
    loginUser(user.getUserProfile());
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.register_button)
  public void registerButtonClicked() {
    final Intent intent = new Intent(this, RegistrationActivity.class);
    startActivity(intent);
    finish();
  }

  private void setupUserInterface() {
    setProgressbarVisibility(false);

    if (isRegisteredAtLeastOneUser()) {
      prepareListOfProfiles();
      setupUsersSpinner();
      loginButton.setVisibility(View.VISIBLE);
    }
    registerButton.setVisibility(View.VISIBLE);
  }

  private void setupUsersSpinner() {
    usersSpinner.setVisibility(View.VISIBLE);

    final ArrayAdapter<User> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOfUsers);
    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    usersSpinner.setAdapter(spinnerArrayAdapter);
  }

  private void loginUser(final UserProfile userProfile) {
    OneginiSDK.getOneginiClient(this).authenticateUser(userProfile, new OneginiAuthenticationHandler() {
      @Override
      public void authenticationSuccess(final UserProfile userProfileSuccessfullyAuthenticated) {
        startDashboardActivity();
      }

      @Override
      public void authenticationError() {
        showToast("authenticationError");
      }

      @Override
      public void authenticationException(final Exception e) {
        showToast("authenticationException");
      }

      @Override
      public void authenticationErrorInvalidRequest() {
        showToast("authenticationErrorInvalidRequest");
      }

      @Override
      public void authenticationErrorClientRegistrationFailed() {
        showToast("authenticationErrorClientRegistrationFailed");
      }

      @Override
      public void authenticationErrorInvalidState() {
        showToast("authenticationErrorInvalidState");
      }

      @Override
      public void authenticationErrorInvalidGrant(final int remaining) {
        setProgressbarVisibility(true);
        PinActivity.setRemainingFailedAttempts(remaining);
        loginUser(userProfile);
      }

      @Override
      public void authenticationErrorNotAuthenticated() {
        showToast("authenticationErrorNotAuthenticated");
      }

      @Override
      public void authenticationErrorInvalidScope() {
        showToast("authenticationErrorInvalidScope");
      }

      @Override
      public void authenticationErrorNotAuthorized() {
        showToast("authenticationErrorNotAuthorized");
      }

      @Override
      public void authenticationErrorInvalidGrantType() {
        showToast("authenticationErrorInvalidGrantType");
      }

      @Override
      public void authenticationErrorTooManyPinFailures() {
        showToast("authenticationErrorTooManyPinFailures");
      }

      @Override
      public void authenticationErrorInvalidApplication() {
        showToast("authenticationErrorInvalidApplication");
      }

      @Override
      public void authenticationErrorUnsupportedOS() {
        showToast("authenticationErrorUnsupportedOS");
      }

      @Override
      public void authenticationErrorInvalidProfile() {
        showToast("authenticationErrorInvalidProfile");
      }
    });
  }

  private void startDashboardActivity() {
    final Intent intent = new Intent(this, DashboardActivity.class);
    startActivity(intent);
    finish();
  }

  private boolean isRegisteredAtLeastOneUser() {
    final Set<UserProfile> userProfiles = OneginiSDK.getOneginiClient(this).getUserProfiles();
    return userProfiles.size() > 0;
  }

  private void setProgressbarVisibility(final boolean isVisible) {
    progressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);

    registerButton.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    layoutLoginContent.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    if (isRegisteredAtLeastOneUser()) {
      loginButton.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }
  }

  private void prepareListOfProfiles() {
    final Set<UserProfile> userProfiles = OneginiSDK.getOneginiClient(this).getUserProfiles();
    userStorage = new UserStorage(this);
    listOfUsers = userStorage.loadUsers(userProfiles);
  }

  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
