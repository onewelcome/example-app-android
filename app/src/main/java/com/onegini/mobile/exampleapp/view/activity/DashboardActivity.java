package com.onegini.mobile.exampleapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.onegini.mobile.exampleapp.R;

public class DashboardActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);
  }

  public static void startActivity(@NonNull final Context context) {
    final Intent contactDetailsIntent = new Intent(context, DashboardActivity.class);
    context.startActivity(contactDetailsIntent);
  }
}
