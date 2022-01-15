package com.android_final_project.firedate;

import android.app.Application;

import com.android_final_project.firedate.data.AuthSingleton;
import com.android_final_project.firedate.data.UserOperator;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AuthSingleton.initAuthSingleton();
    }
}
