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
import com.onegini.mobile.android.sdk.client.OneginiClient;
import com.onegini.mobile.android.sdk.handlers.OneginiDeregisterUserProfileHandler;
import com.onegini.mobile.android.sdk.handlers.OneginiLogoutHandler;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.Person;
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
    OneginiClient.getInstance().logout(
        new OneginiLogoutHandler() {
          @Override
          public void logoutSuccess() {
            // Go to login screen
            showToast("logoutSuccess");
            startLoginActivity();
          }

          @Override
          public void logoutError() {
            // Ignore failure and return to login screen
            showToast("logoutError");
            startLoginActivity();
          }
        }
    );
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_deregister_user)
  public void deregisterUser() {
    final UserProfile userProfile = OneginiSDK.getOneginiClient(getApplicationContext()).getAuthenticatedUserProfile();
    if (userProfile == null) {
      showToast("userProfile == null");
      return;
    }

    OneginiClient.getInstance().deregisterUser(userProfile, new OneginiDeregisterUserProfileHandler() {
          @Override
          public void onSuccess() {
            onUserDeregistered(userProfile);
          }

          @Override
          public void onRequestError() {
            // Ignore failure and return to login screen
            showToast("deregisterUserError");
            startLoginActivity();
          }
        }
    );
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_get_user_profile)
  public void getUserProfileData() {
    subscription = PersonService.getInstance(this)
        .getPerson()
        .subscribe(this::onPersonFetched, throwable -> onPersonFetchFail());
  }

  private void onPersonFetchFail() {
    showToast("onPersonFetchFail");
  }

  private void onPersonFetched(final Person person) {
    userInfoTextView.setText(person.getPersonFullInfo());
  }

  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  private void setupUserInterface() {
    setupActionBar();
    setupWelcomeText();
  }

  private void setupWelcomeText() {
    final UserProfile userProfile = OneginiSDK.getOneginiClient(this)
        .getAuthenticatedUserProfile();
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
