package com.android_final_project.firedate.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android_final_project.firedate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_SignUp extends AppCompatActivity {
    private EditText signup_TXTF_email;
    private EditText signup_TXTF_password;
    private EditText signup_TXTF_verifypassword;
    private Button signup_BTN_signup;

    private FirebaseAuth auth;
    private SigninCallback signinCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initFirebaseAuth();
        findViews();
        initViews();
    }

    private void findViews(){
        signup_TXTF_email = findViewById(R.id.signup_TXTF_email);
        signup_TXTF_password = findViewById(R.id.signup_TXTF_password);
        signup_TXTF_verifypassword = findViewById(R.id.signup_TXTF_verifypassword);
        signup_BTN_signup = findViewById(R.id.signup_BTN_signup);
    }

    private void initFirebaseAuth(){
        auth = FirebaseAuth.getInstance();
        signinCallback = new SigninCallback() {
            @Override
            public void afterSigninSucceeded() {
                Toast.makeText(Activity_SignUp.this,getString(R.string.signup_emailalreadytaken),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_SignUp.this,Activity_Swipe.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void afterSigninFailed() {
                Toast.makeText(Activity_SignUp.this,getString(R.string.signup_emailalreadytaken),Toast.LENGTH_SHORT).show();
            }
        };

        auth.addAuthStateListener(firebaseAuth -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null){
                signinCallback.afterSigninSucceeded();
            }
        });
    }

    private void initViews(){
        signup_BTN_signup.setOnClickListener( v-> {
            String email = signup_TXTF_email.getText().toString();
            String pass1 = signup_TXTF_password.getText().toString();
            String pass2 = signup_TXTF_verifypassword.getText().toString();

            if(pass1.compareTo(pass2)!=0){
                Toast.makeText(this,getString(R.string.signup_passworddidntmatch),Toast.LENGTH_SHORT).show();
            }
            else{
                auth.createUserWithEmailAndPassword(email,pass1).addOnCompleteListener(Activity_SignUp.this,
                        task -> {
                            if(!task.isSuccessful()){
                                signinCallback.afterSigninFailed();
                            }
                        });
            }
        });
    }

    private interface SigninCallback{
        void afterSigninSucceeded();
        void afterSigninFailed();
    }

}