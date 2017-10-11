package com.onegini.mobile.exampleapp.network.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import com.onegini.mobile.exampleapp.R;

public class NotificationHelper {

  private static final String CHANNEL_ID = "transactions";

  private final Context context;

  public NotificationHelper(final Context context) {
    this.context = context;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      registerNotificationChannel();
    }
  }

  public void showNotification() {
    final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Transaction pending")
        .setContentText("Click to confirm pending transaction")
        .setPriority(NotificationCompat.PRIORITY_MAX);

    getManager().notify(1, builder.build());
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  private final void registerNotificationChannel() {
    final NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Transactions", NotificationManager.IMPORTANCE_HIGH);
    notificationChannel.setDescription("Confirm the transaction");
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
