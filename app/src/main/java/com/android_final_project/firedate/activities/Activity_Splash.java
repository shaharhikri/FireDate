package com.android_final_project.firedate.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.android_final_project.firedate.R;
import android.content.Intent;
import android.os.Bundle;

import com.android_final_project.firedate.data.UserOperator;

public class Activity_Splash extends AppCompatActivity {


    private UserOperator userOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUserOperator();

    }

    //Has to happen before initViews() call
    private void initUserOperator(){
        userOperator = new UserOperator(Activity_Splash.this, new UserOperator.UserOperatorCallback(){
            @Override
            public void operationFailed(String msg) { }

            @Override
            public void operationSucceeded() { }

            @Override
            public void alreadyLoggedin() {
                Intent intent = new Intent(Activity_Splash.this,Activity_Swipe.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void afterLogout() {
                Intent intent = new Intent(Activity_Splash.this,Activity_Entry.class);
                startActivity(intent);
                finish();
            }
        });
    }
}