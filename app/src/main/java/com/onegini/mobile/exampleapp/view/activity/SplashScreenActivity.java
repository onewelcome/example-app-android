package com.onegini.mobile.exampleapp.view.activity;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.onegini.mobile.android.sdk.client.OneginiClient;
import com.onegini.mobile.android.sdk.handlers.OneginiInitializationHandler;
import com.onegini.mobile.android.sdk.handlers.error.OneginiInitializationError;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.storage.UserStorage;

public class SplashScreenActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    setupOneginiSDK();
  }

  private void setupOneginiSDK() {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(this);
    oneginiClient.start(new OneginiInitializationHandler() {
      @Override
      public void onSuccess(final Set<UserProfile> removedUserProfiles) {
        if (removedUserProfiles.isEmpty()) {
          startLoginActivity();
        } else {
          removeUserProfiles(removedUserProfiles);
        }
      }

      @Override
      public void onError(final OneginiInitializationError error) {
        if (error.getErrorType() == OneginiInitializationError.OUTDATED_APP)
        Toast.makeText(SplashScreenActivity.this, error.getErrorDescription(), Toast.LENGTH_LONG).show();
      }
    });
  }

  private void removeUserProfiles(final Set<UserProfile> removedUserProfiles) {
    final UserStorage userStorage = new UserStorage(this);
    for (final UserProfile userProfile : removedUserProfiles) {
      userStorage.removeUser(userProfile);
    }
    startLoginActivity();
  }

  private void startLoginActivity() {
    startActivity(new Intent(this, LoginActivity.class));
    finish();
  }
}
