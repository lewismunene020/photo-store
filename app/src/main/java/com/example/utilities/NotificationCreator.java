package com.example.utilities;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.photostore.R;

public class NotificationCreator {
    private final int NOTIFICATION_CHANNEL_ID;
    private final String notificationName;
    private final Context notificationContext;


    public NotificationCreator(Context context, String notificationName, int CHANNEL_ID) {
        this.notificationContext = context;
        this.notificationName = notificationName;
        this.NOTIFICATION_CHANNEL_ID = CHANNEL_ID;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(notificationName,
                    notificationName, IMPORTANCE_DEFAULT);
            NotificationManager manager = notificationContext.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder createInitialNotification(String contentTitle, String contentText) {
        createNotificationChannel();
        return new NotificationCompat.Builder(notificationContext,
                notificationName).
                setSmallIcon(R.mipmap.ic_launcher).
                setContentTitle(contentTitle).
                setContentText(contentText).
                setPriority(NotificationCompat.PRIORITY_DEFAULT).
                setAutoCancel(true);
    }

    public void createAlertNotification(String contentTitle, String contentText) {
        createNotificationChannel();
        NotificationCompat.Builder builder = createInitialNotification(contentTitle, contentText);
        buildNotification(builder);
    }

    private void buildNotification(@NonNull NotificationCompat.Builder builder) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(notificationContext);
        notificationManagerCompat.notify(NOTIFICATION_CHANNEL_ID, builder.build());
    }


    public void createUploadNotification(String contentTitle, int progress, String progressString) {
        NotificationCompat.Builder builder = createInitialNotification(contentTitle, progressString);
        builder.setProgress(100, progress, true);
        buildNotification(builder);

    }
}
