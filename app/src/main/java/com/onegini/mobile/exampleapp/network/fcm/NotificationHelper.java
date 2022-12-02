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

package com.onegini.mobile.exampleapp.network.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.NotificationId;

import java.util.Random;

public class NotificationHelper {

  private static final String CHANNEL_ID = "transactions";
  private static NotificationHelper INSTANCE;
  private final Context context;

  private NotificationHelper(final Context context) {
    this.context = context;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      registerNotificationChannel();
    }
  }

  public static NotificationHelper getInstance(final Context context) {
    if (INSTANCE == null) {
      INSTANCE = new NotificationHelper(context.getApplicationContext());
    }
    return INSTANCE;
  }

  public void cancelAllNotifications() {
    getManager().cancelAll();
  }

  void showNotification(final Intent intent, final String message) {
    final int uniqueId = NotificationId.getId();

    final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Confirm the transaction")
        .setContentText(message)
        .setContentIntent(
            PendingIntent.getActivity(context, new Random().nextInt(), intent, PendingIntent.FLAG_IMMUTABLE))
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setAutoCancel(true);

    getManager().notify(uniqueId, builder.build());
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  private void registerNotificationChannel() {
    final NotificationChannel notificationChannel =
        new NotificationChannel(CHANNEL_ID, "Transactions", NotificationManager.IMPORTANCE_HIGH);
    notificationChannel.setDescription("Onegini SDK");
    notificationChannel.enableLights(true);
    notificationChannel.setLightColor(Color.BLUE);
    notificationChannel.enableVibration(true);
    notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

    getManager().createNotificationChannel(notificationChannel);
  }

  private NotificationManager getManager() {
    return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
  }
}
