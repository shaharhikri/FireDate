package com.android_final_project.firedate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android_final_project.firedate.data.AuthSingleton;
import com.android_final_project.firedate.data.UserOperator;
import com.android_final_project.firedate.R;

public class Activity_Login extends AppCompatActivity {

    private EditText login_TXTF_email;
    private EditText login_TXTF_password;
    private Button   login_BTN_signup;

    private UserOperator userOperator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        initUserOperator();
        initViews();
    }

    //Has to happen before initViews() call
    private void initUserOperator(){
        userOperator = new UserOperator(this, new UserOperator.UserOperatorCallback() {
            @Override
            public void operationFailed(String msg) {
                Toast.makeText(Activity_Login.this,msg,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void operationSucceeded() { }
        });

        AuthSingleton.setAuthCallback(new AuthSingleton.AuthCallback() {
            @Override
            public void LoggedIn() {
                Intent intent = new Intent(Activity_Login.this,Activity_Swipe.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void LoggedOut() {
                Intent intent = new Intent(Activity_Login.this,Activity_Entry.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findViews(){
        login_TXTF_email = findViewById(R.id.login_TXTF_email);
        login_TXTF_password = findViewById(R.id.login_TXTF_password);
        login_BTN_signup = findViewById(R.id.login_BTN_signup);
    }

    private void initViews(){
        login_BTN_signup.setOnClickListener( v -> {
            String email = login_TXTF_email.getText().toString();
            String pass = login_TXTF_password.getText().toString();

            userOperator.login(email, pass);
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Activity_Login.this,Activity_Entry.class);
        startActivity(intent);
        finish();
    }

}