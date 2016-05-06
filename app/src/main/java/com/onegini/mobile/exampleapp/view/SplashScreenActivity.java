package com.onegini.mobile.exampleapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;

public class SplashScreenActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    setupOneginiSDK();

    // delay next activity, so the splash screen will be shown for at least one second
    final Handler handler = new Handler();
    handler.postDelayed(this::startLoginActivity, 1000);
  }

  private void setupOneginiSDK() {
    OneginiSDK.getOneginiClient(getApplicationContext());
  }

  private void startLoginActivity() {
    startActivity(new Intent(this, LoginActivity.class));
    finish();
  }
}
