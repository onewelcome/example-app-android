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
import com.onegini.mobile.sdk.android.handlers.OneginiAuthenticationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiAuthenticationError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
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
  private boolean userIsLoggingIn = false;

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
    userIsLoggingIn = true;
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.register_button)
  public void registerButtonClicked() {
    final Intent intent = new Intent(this, RegistrationActivity.class);
    startActivity(intent);
    finish();
  }

  private void setupUserInterface() {
    if (userIsLoggingIn) {
      registerButton.setVisibility(View.INVISIBLE);
    } else {
      setProgressbarVisibility(false);
      registerButton.setVisibility(View.VISIBLE);
    }

    if (isRegisteredAtLeastOneUser()) {
      prepareListOfProfiles();
      setupUsersSpinner();
      loginButton.setVisibility(View.VISIBLE);
    } else {
      usersSpinner.setVisibility(View.INVISIBLE);
      loginButton.setVisibility(View.INVISIBLE);
    }
  }

  private void setupUsersSpinner() {
    usersSpinner.setVisibility(View.VISIBLE);

    final ArrayAdapter<User> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOfUsers);
    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    usersSpinner.setAdapter(spinnerArrayAdapter);
  }

  private void loginUser(final UserProfile userProfile) {
    OneginiSDK.getOneginiClient(this).getUserClient().authenticateUser(userProfile, new OneginiAuthenticationHandler() {

      @Override
      public void onSuccess(final UserProfile userProfile) {
        startDashboardActivity();
      }

      @Override
      public void onError(final OneginiAuthenticationError oneginiAuthenticationError) {
        userIsLoggingIn = false;
        setProgressbarVisibility(true);

        if (oneginiAuthenticationError.getErrorType() == OneginiAuthenticationError.SERVER_NOT_REACHABLE) {
          showToast("Server not reachable");
        } else if (oneginiAuthenticationError.getErrorType() == OneginiAuthenticationError.USER_DEREGISTERED) {
          onUserDeregistered(userProfile);
        } else {
          showToast("Login error: " + oneginiAuthenticationError.getErrorDescription());
        }
      }
    });
  }

  private void onUserDeregistered(final UserProfile userProfile) {
    userStorage.removeUser(userProfile);
    showToast("User deregistered");
    setupUserInterface();
  }

  private boolean isRegisteredAtLeastOneUser() {
    final Set<UserProfile> userProfiles = OneginiSDK.getOneginiClient(this).getUserClient().getUserProfiles();
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
    final Set<UserProfile> userProfiles = OneginiSDK.getOneginiClient(this).getUserClient().getUserProfiles();
    userStorage = new UserStorage(this);
    listOfUsers = userStorage.loadUsers(userProfiles);
  }

  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  private void startDashboardActivity() {
    startActivity(new Intent(this, DashboardActivity.class));
    finish();
  }
}
