package hu.mobilalk.trainticketapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import hu.mobilalk.trainticketapp.R;
import hu.mobilalk.trainticketapp.routes.RoutesActivity;

public class NotificationHelper {
    private static final String CHANNEL_ID = "purchase_notification_channel";

    private final NotificationManager notificationManager;
    private final Context context;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = context.getSystemService(NotificationManager.class);

        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel channel = new NotificationChannel
                (CHANNEL_ID, "Purchase Notification", NotificationManager.IMPORTANCE_HIGH);

        channel.enableLights(true);
        channel.setLightColor(Color.GREEN);
        channel.enableVibration(true);
        channel.setDescription("Notifications from TrainTicketApp about purchases");

        notificationManager.createNotificationChannel(channel);
    }

    public void send(String message) {
        Intent intent = new Intent(context, RoutesActivity.class);
        int NOTIFICATION_ID = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Jegyvásárlás")
                .setContentText(message)
                .setSmallIcon(R.drawable.train_icon)
                .setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
