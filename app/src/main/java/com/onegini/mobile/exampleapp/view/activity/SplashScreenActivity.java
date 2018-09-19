/*
 * Copyright (c) 2016-2018 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onegini.mobile.exampleapp.view.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.network.fcm.NotificationHelper;
import com.onegini.mobile.exampleapp.view.handler.InitializationHandler;
import com.onegini.mobile.exampleapp.view.helper.AlertDialogFragment;
import com.onegini.mobile.exampleapp.view.helper.OneginiClientInitializer;

public class SplashScreenActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_splashscreen);
    setupOneginiSDK();
    cancelAllNotifications();
  }

  private void setupOneginiSDK() {
    final OneginiClientInitializer oneginiClientInitializer = new OneginiClientInitializer(this);
    oneginiClientInitializer.startOneginiClient(new InitializationHandler() {
      @Override
      public void onSuccess() {
        startLoginActivity();
      }

      @Override
      public void onError(final String errorMessage) {
        showError(errorMessage);
      }
    });
  }

  private void cancelAllNotifications() {
    NotificationHelper.getInstance(this).cancelAllNotifications();
  }

  private void startLoginActivity() {
    startActivity(new Intent(this, LoginActivity.class));
    finish();
  }

  private void showError(final String message) {
    final DialogFragment dialog = AlertDialogFragment.newInstance(message);
    dialog.show(getFragmentManager(), "alert_dialog");
  }
}
