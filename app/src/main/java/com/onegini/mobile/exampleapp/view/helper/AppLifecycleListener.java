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

package com.onegini.mobile.exampleapp.view.helper;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class AppLifecycleListener implements Application.ActivityLifecycleCallbacks {

  private static int ACTIVITIES_IN_FOREGROUND_COUNTER = 0;

  public static boolean isAppInForeground() {
    return ACTIVITIES_IN_FOREGROUND_COUNTER > 0;
  }

  @Override
  public void onActivityCreated(final Activity activity, final Bundle bundle) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      View rootView = activity.getWindow().getDecorView().getRootView();
      ViewCompat.setOnApplyWindowInsetsListener(rootView, (view, insets) -> {
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);

        final WindowInsetsControllerCompat controller = ViewCompat.getWindowInsetsController(view);
        if (controller != null) {
          controller.setAppearanceLightStatusBars(true);
          controller.setAppearanceLightNavigationBars(true);
        }
        Insets barsInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());
        view.setPadding(barsInsets.left, barsInsets.top, barsInsets.right, barsInsets.bottom);
        return WindowInsetsCompat.CONSUMED;
      });
    }
  }

  @Override
  public void onActivityStarted(final Activity activity) {
  }

  @Override
  public void onActivityResumed(final Activity activity) {
    ACTIVITIES_IN_FOREGROUND_COUNTER++;
  }

  @Override
  public void onActivityPaused(final Activity activity) {
    ACTIVITIES_IN_FOREGROUND_COUNTER--;
  }

  @Override
  public void onActivityStopped(final Activity activity) {
  }

  @Override
  public void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
  }

  @Override
  public void onActivityDestroyed(final Activity activity) {
  }
}

