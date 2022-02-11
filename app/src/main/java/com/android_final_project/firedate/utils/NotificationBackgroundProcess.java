package com.android_final_project.firedate.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android_final_project.firedate.R;

import java.util.Calendar;

public class NotificationBackgroundProcess extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "This is my background process: \n" + Calendar.getInstance().getTime().toString(), Toast.LENGTH_LONG).show();
        SystemClock.sleep(2000);
        createNotificationChannel(context);
        Log.d("pttt", "handleNotifications: notice");
        for (int i = 0; i < 100; i++) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "fire")
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("My notification")
                    .setContentText("Much longer text that cannot fit one line...")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Much longer text that cannot fit one line..."))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            notificationManager.notify(100, builder.build());

            SystemClock.sleep(2000);
        }


    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "studentChannel";
            String description = "Channel for student notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("fire", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
