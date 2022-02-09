package com.android_final_project.firedate.activities;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android_final_project.firedate.data.AuthSingleton;
import com.android_final_project.firedate.data.UserOperator;
import com.android_final_project.firedate.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Activity_SignUp extends AppCompatActivity {
    private TextInputEditText   signup_TXTF_email               ;
    private TextInputEditText   signup_TXTF_name                ;
    private MaterialButton      signup_BTN_birthDate            ;
    private TextView            signup_TXT_birthDate            ;
    private MaterialButton      signup_BTN_uploadImg            ;
    private TextInputEditText   signup_TXTF_description         ;
    private RadioGroup          signup_gender_radioGroup        ;
    private RadioGroup          signup_preference_radioGroup    ;
    private TextInputEditText   signup_TXTF_password            ;
    private TextInputEditText   signup_TXTF_verifyPassword      ;
    private MaterialButton      signup_BTN_signup               ;

    private UserOperator userOperator;

    private long usersAgeInMillis;
    private Uri profileImgPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViews();
        initUserOperator();
        initViews();
    }

    private void findViews(){
        signup_TXTF_email               = findViewById(R.id.signup_TXTF_email               );
        signup_TXTF_name                = findViewById(R.id.signup_TXTF_name                );
        signup_BTN_birthDate            = findViewById(R.id.signup_BTN_birthDate            );
        signup_TXT_birthDate            = findViewById(R.id.signup_TXT_birthDate            );
        signup_BTN_uploadImg            = findViewById(R.id.signup_BTN_uploadImg            );
        signup_TXTF_description         = findViewById(R.id.signup_TXTF_description         );
        signup_gender_radioGroup        = findViewById(R.id.signup_gender_radioGroup        );
        signup_preference_radioGroup    = findViewById(R.id.signup_preference_radioGroup    );
        signup_TXTF_password            = findViewById(R.id.signup_TXTF_password            );
        signup_TXTF_verifyPassword      = findViewById(R.id.signup_TXTF_verifyPassword      );
        signup_BTN_signup               = findViewById(R.id.signup_BTN_signup               );
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
        initBirthDate();
        initImgUpload();

        signup_BTN_signup.setOnClickListener( v-> {
            String email = signup_TXTF_email.getText().toString();
            String pass1 = signup_TXTF_password.getText().toString();
            String pass2 = signup_TXTF_verifyPassword.getText().toString();
            String name = signup_TXTF_name.getText().toString();
            String description = signup_TXTF_description.getText().toString();

            //TODO under 18

            RadioButton genderRadioButton = findViewById(signup_gender_radioGroup.getCheckedRadioButtonId());
            String gender = genderRadioButton.getText().toString();

            RadioButton prefRadioButton = findViewById(signup_preference_radioGroup.getCheckedRadioButtonId());
            String pref = prefRadioButton.getText().toString();

            UserOperator.SexualGroup group = null;
            try {
                group = UserOperator.getSexualGroup(gender, pref);
            } catch (UserOperator.GendersStringException e) {
                e.printStackTrace();
            }

            userOperator.signup(email, pass1, pass2, name, usersAgeInMillis, description, profileImgPath, group);
        });
    }

    private void initImgUpload() {
        signup_BTN_uploadImg.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            profileImgPath = data.getData();
        }
    }

    private void initBirthDate() {
        DatePickerDialog.OnDateSetListener datePickerListener = (datePicker, year, month, day) -> {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            @SuppressLint("SimpleDateFormat")
            String format = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
            signup_TXT_birthDate.setText(format);
            usersAgeInMillis = c.getTimeInMillis();
//                tvAge.setText(Integer.toString(calculateAge(c.getTimeInMillis())));
        };

        signup_BTN_birthDate.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dateDialog = new DatePickerDialog(view.getContext(), datePickerListener, mYear, mMonth, mDay);

            Date date18yearAgo = new Date();
            date18yearAgo.setYear(new Date().getYear() - 18);
            dateDialog.getDatePicker().setMaxDate(date18yearAgo.getTime());
            dateDialog.show();
        });

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Activity_SignUp.this,Activity_Entry.class);
        startActivity(intent);
        finish();
    }

}