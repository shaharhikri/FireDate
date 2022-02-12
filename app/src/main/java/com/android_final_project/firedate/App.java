package com.android_final_project.firedate;

import android.app.Application;

import com.android_final_project.firedate.data.AuthSingleton;
import com.android_final_project.firedate.data.UserOperator;
import com.android_final_project.firedate.utils.GPS;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //AuthSingleton.initAuthSingleton();
        GPS.initHelper(this);
    }
}
