package com.android_final_project.firedate.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.android_final_project.firedate.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android_final_project.firedate.data.AuthSingleton;
import com.android_final_project.firedate.data.UserOperator;

public class Activity_Splash extends AppCompatActivity {


    private UserOperator userOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AuthSingleton.setAuthCallback(new AuthSingleton.DefaultAuthCallback(this));
        Log.d("pttt", "Activity_Splash onCreate");
    }

}