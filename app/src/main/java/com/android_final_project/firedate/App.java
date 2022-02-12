package com.android_final_project.firedate;

import android.app.Application;
import com.android_final_project.firedate.utils.GPS;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GPS.initHelper(this);
    }
}
