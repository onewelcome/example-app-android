package com.onegini.mobile.exampleapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.Person;
import com.onegini.mobile.exampleapp.network.PersonService;
import com.onegini.mobile.exampleapp.util.LocalStorageUtil;
import com.onegini.mobile.sdk.android.library.OneginiClient;
import com.onegini.mobile.sdk.android.library.handlers.OneginiDeregisterUserProfileHandler;
import com.onegini.mobile.sdk.android.library.handlers.OneginiLogoutHandler;
import com.onegini.mobile.sdk.android.library.model.entity.UserProfile;
import rx.Subscription;

public class DashboardActivity extends AppCompatActivity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.toolbar)
  Toolbar toolbar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.tv_user_profile_info)
  TextView userInfoTextView;

  private Subscription subscription;

  public static void startActivity(@NonNull final Activity context) {
    final Intent intent = new Intent(context, DashboardActivity.class);
    context.startActivity(intent);
    context.finish();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);
    ButterKnife.bind(this);

    setupActionBar();
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
            LoginActivity.startActivity(DashboardActivity.this);
          }

          @Override
          public void logoutError() {
            // Ignore failure and return to login screen
            showToast("logoutError");
            LoginActivity.startActivity(DashboardActivity.this);
          }
        }
    );
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_deregister_user)
  public void deregisterUser() {
    UserProfile userProfile = OneginiSDK.getOneginiClient(getApplicationContext()).getAuthenticatedUserProfile();
    if (userProfile == null) {
      showToast("userProfile == null");
      return;
    }

    OneginiClient.getInstance().deregisterUser(userProfile, new OneginiDeregisterUserProfileHandler() {
          @Override
          public void onSuccess() {
            // Go to login screen
            showToast("deregisterUserSuccess");
            LocalStorageUtil.removeUser(getApplicationContext(), userProfile);
            LoginActivity.startActivity(DashboardActivity.this);
          }

          @Override
          public void onRequestError() {
            // Ignore failure and return to login screen
            showToast("deregisterUserError");
            LoginActivity.startActivity(DashboardActivity.this);
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

  private void setupActionBar() {
    setSupportActionBar(toolbar);

    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
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
}
