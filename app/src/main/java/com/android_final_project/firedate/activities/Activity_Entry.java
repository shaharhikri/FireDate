package com.android_final_project.firedate.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.android_final_project.firedate.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Activity_Entry extends AppCompatActivity {

    Button entry_BTN_login;
    Button entry_BTN_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        findViews();
        initViews();

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
}