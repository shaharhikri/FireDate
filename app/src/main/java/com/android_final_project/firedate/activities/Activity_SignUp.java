package com.android_final_project.firedate.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.android_final_project.firedate.data.AuthSingleton;
import com.android_final_project.firedate.data.UserOperator;
import com.android_final_project.firedate.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Activity_SignUp extends AppCompatActivity {
    private EditText signup_TXTF_email;
    private EditText signup_TXTF_password;
    private EditText signup_TXTF_verifypassword;
    private EditText signup_TXTF_description;

    private RadioGroup signup_gender_radiogroup;
    private RadioGroup signup_preference_radiogroup;

    private EditText signup_TXTF_name;
    private Button signup_BTN_signup;

    private UserOperator userOperator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViews();
        initUserOperator();
        initViews();
    }

    private void findViews(){
        signup_TXTF_email = findViewById(R.id.signup_TXTF_email);
        signup_TXTF_password = findViewById(R.id.signup_TXTF_password);
        signup_TXTF_verifypassword = findViewById(R.id.signup_TXTF_verifypassword);
        signup_BTN_signup = findViewById(R.id.signup_BTN_signup);
        signup_gender_radiogroup = findViewById(R.id.signup_gender_radiogroup);
        signup_preference_radiogroup = findViewById(R.id.signup_preference_radiogroup);
        signup_TXTF_name = findViewById(R.id.signup_TXTF_name);
        signup_TXTF_description = findViewById(R.id.signup_TXTF_description);
    }

    //Has to happen before initViews() call
    private void initUserOperator(){
        userOperator = new UserOperator(Activity_SignUp.this, new UserOperator.UserOperatorCallback(){
            @Override
            public void operationFailed(String msg) {
                Toast.makeText(Activity_SignUp.this,msg,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void operationSucceeded() { }
        });

        AuthSingleton.setAuthCallback(new AuthSingleton.DefaultAuthCallback(this));
    }


    private void initViews() {
        signup_BTN_signup.setOnClickListener( v-> {
            String email = signup_TXTF_email.getText().toString();
            String pass1 = signup_TXTF_password.getText().toString();
            String pass2 = signup_TXTF_verifypassword.getText().toString();
            String name = signup_TXTF_name.getText().toString();
            String description = signup_TXTF_description.getText().toString();

            RadioButton genderRadioButton = findViewById(signup_gender_radiogroup.getCheckedRadioButtonId());
            String gender = genderRadioButton.getText().toString();

            RadioButton prefRadioButton = findViewById(signup_preference_radiogroup.getCheckedRadioButtonId());
            String pref = prefRadioButton.getText().toString();

            UserOperator.SexualGroup group = null;
            try {
                group = UserOperator.getSexualGroup(gender, pref);
            } catch (UserOperator.GendersStringException e) {
                e.printStackTrace();
            }
            userOperator.signup(email, pass1, pass2, name, description, group);
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Activity_SignUp.this,Activity_Entry.class);
        startActivity(intent);
        finish();
    }

}