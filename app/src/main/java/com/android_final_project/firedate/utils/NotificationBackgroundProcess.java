package com.android_final_project.firedate.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class NotificationBackgroundProcess extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Ringtone ringtone = RingtoneManager.getRingtone(context,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
//        ringtone.play();

        Log.d("ptt", "onReceive: test123");
        Log.d("pttt", "onReceive: " + intent.getBundleExtra("bundle").getString("test"));

        Toast.makeText(context, "This is my background process: \n" + Calendar.getInstance().getTime().toString(), Toast.LENGTH_LONG).show();

//        SystemClock.sleep(20000);
//        ringtone.stop();
    }


}
