package com.android_final_project.firedate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.android_final_project.firedate.R;
import com.google.android.material.button.MaterialButton;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

public class Activity_Entry extends AppCompatActivity {

    MaterialButton entry_BTN_login;
    MaterialButton entry_BTN_signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        findViews();
        initViews();

        grantLocationPermission();
    }


    private void findViews(){
        entry_BTN_login = findViewById(R.id.entry_BTN_login);
        entry_BTN_signup = findViewById(R.id.entry_BTN_signup);
    }

    private void initViews(){
        entry_BTN_login.setOnClickListener( v -> {
            Intent intent = new Intent(this, Activity_Login.class);
            startActivity(intent);
            finish();
        });

        entry_BTN_signup.setOnClickListener( v -> {
            Intent intent = new Intent(this, Activity_SignUp.class);
            startActivity(intent);
            finish();
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }

    private void grantLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
    }
}