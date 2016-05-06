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

public class DashboardActivity extends AppCompatActivity {

  public static void startActivity(@NonNull final Activity context) {
    final Intent contactDetailsIntent = new Intent(context, DashboardActivity.class);
    context.startActivity(contactDetailsIntent);
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
    showToast("logout");
  }

  @OnClick(R.id.button_disconnect)
  public void disconnect() {
    showToast("disconnect");
  }

  @OnClick(R.id.button_get_user_profile)
  public void getUserProfileData() {
    showToast("getUserProfileData");
  }

  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
