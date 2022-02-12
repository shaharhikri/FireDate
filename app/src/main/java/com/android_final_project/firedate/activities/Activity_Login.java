package com.android_final_project.firedate.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.android_final_project.firedate.data.AuthSingleton;
import com.android_final_project.firedate.data.UserOperator;
import com.android_final_project.firedate.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Activity_Login extends AppCompatActivity {

    private TextInputEditText login_TXTF_email;
    private TextInputEditText login_TXTF_password;
    private MaterialButton login_BTN_signup;
    private TextView login_error_msg;
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
                login_error_msg.setText(msg);
            }

            @Override
            public void operationSucceeded() { }
        });

        AuthSingleton.setAuthCallback(new AuthSingleton.DefaultAuthCallback(this));
    }

    private void findViews(){
        login_TXTF_email = findViewById(R.id.login_TXTF_email);
        login_TXTF_password = findViewById(R.id.login_TXTF_password);
        login_BTN_signup = findViewById(R.id.login_BTN_signup);
        login_error_msg = findViewById(R.id.login_error_msg);
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