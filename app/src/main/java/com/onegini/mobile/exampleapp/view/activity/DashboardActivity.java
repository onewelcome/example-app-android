package com.onegini.mobile.exampleapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.sdk.android.library.OneginiClient;
import com.onegini.mobile.sdk.android.library.handlers.OneginiDisconnectHandler;
import com.onegini.mobile.sdk.android.library.handlers.OneginiLogoutHandler;

public class DashboardActivity extends AppCompatActivity {

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
  }

  @OnClick(R.id.button_logout)
  public void logout() {
    OneginiClient.getInstance().logout(
        new OneginiLogoutHandler() {
          @Override
          public void logoutSuccess() {
            // Go to login screen
            LoginActivity.startActivity(DashboardActivity.this);
          }

          @Override
          public void logoutError() {
            // Ignore failure and return to login screen
            LoginActivity.startActivity(DashboardActivity.this);
          }
        }
    );
  }

  @OnClick(R.id.button_disconnect)
  public void disconnect() {
    OneginiClient.getInstance().disconnect(

        new OneginiDisconnectHandler() {
          @Override
          public void disconnectSuccess() {
            // Go to login screen
            LoginActivity.startActivity(DashboardActivity.this);
          }

          @Override
          public void disconnectError() {
            // Ignore failure and return to login screen
            LoginActivity.startActivity(DashboardActivity.this);
          }
        }
    );
  }

  @OnClick(R.id.button_get_user_profile)
  public void getUserProfileData() {
    showToast("getUserProfileData");
  }

  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
