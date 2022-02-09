package com.android_final_project.firedate.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android_final_project.firedate.R;
import com.android_final_project.firedate.data.UserEntity;
import com.android_final_project.firedate.data.UserOperator;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Activity_Settings extends AppCompatActivity {

    private Bundle bundle;

    private ImageView   settings_IMG_profile;
    private TextInputEditText settings_ETXT_name;
    private TextInputEditText    settings_ETXT_description;
    private MaterialButton      settings_BTN_confirm;
    private MaterialButton settings_BTN_back;

    private DatabaseReference usersDb;
    private UserOperator.SexualGroup currentUserSexualGroup;
    private String currentUserId;
    private String profileImgUrl, name, description;
    private Uri profileImgPath;

    private UserEntity currentUserEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.bundle = getIntent().getBundleExtra(getString(R.string.key_bundle));
        initBundle();
        findViews();
        initViews();
    }

    private void initBundle() {
        currentUserId = bundle.getString(getString(R.string.key_currentUserId));
        String currentUserSexualGroupStr = bundle.getString(getString(R.string.key_currentUserSexualGroup));
        currentUserSexualGroup = UserOperator.SexualGroup.valueOf(currentUserSexualGroupStr);

        String currentUserEntityStr = bundle.getString(getString(R.string.key_currentUserData));
        currentUserEntity = new Gson().fromJson(currentUserEntityStr, UserEntity.class);

        usersDb = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(currentUserSexualGroupStr)
                .child(currentUserId);

        name = currentUserEntity.getName();
        description = currentUserEntity.getDescription();
        profileImgUrl = currentUserEntity.getProfileImageUrl();
        Log.d("pttt", "initBundle: profileImgUrl" + profileImgUrl);
    }

    private void initViews() {
        settings_BTN_confirm.setOnClickListener(v -> confirmForm());
        settings_BTN_back.setOnClickListener(v -> {
            changeActivity(Activity_Swipe.class);
        });
        settings_ETXT_name.setText(name);
        settings_ETXT_description.setText(description);

        if (profileImgUrl != null) {
            Glide.with(getApplication())
                    .load(profileImgUrl)
                    .into(settings_IMG_profile);
        }

        settings_IMG_profile.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            profileImgPath = imageUri;
            settings_IMG_profile.setImageURI(profileImgPath);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        changeActivity(Activity_Swipe.class);
    }

    private void confirmForm() {
        name = settings_ETXT_name.getText().toString();
        description = settings_ETXT_description.getText().toString();

        currentUserEntity.setName(name).setDescription(description);

        // update firebase
        UserOperator.updateUserInDB(currentUserEntity, currentUserSexualGroup);

        // update bundle
        bundle.putString(getString(R.string.key_currentUserSexualGroup), currentUserSexualGroup.toString());
        bundle.putString(getString(R.string.key_currentUserData), new Gson().toJson(currentUserEntity));

        // save image
        if (profileImgPath != null){
                        StorageReference ref = FirebaseStorage
                    .getInstance()
                    .getReference()
                    .child("profileImages")
                    .child(currentUserId);

            Uri file = profileImgPath;
            StorageReference dateRef = ref.child("mainPhoto");
            dateRef.putFile(file).addOnSuccessListener(taskSnapshot -> {
                // Download file From Firebase Storage
                dateRef.getDownloadUrl().addOnSuccessListener(downloadPhotoUrl -> {
                    Map userInfo = new HashMap();
                    userInfo.put("profileImageUrl", downloadPhotoUrl.toString());
                    UserOperator.updateUserInDB(currentUserId, currentUserSexualGroup, userInfo);

                    // update bundle
                    currentUserEntity.setProfileImageUrl(downloadPhotoUrl.toString());
                    bundle.putString(getString(R.string.key_currentUserData), new Gson().toJson(currentUserEntity));

                    changeActivity(Activity_Swipe.class);
                });
            });
        } else {
            changeActivity(Activity_Swipe.class);
        }

        Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show();
    }

    private void changeActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        bundle.putString(getString(R.string.key_currentUserSexualGroup), currentUserSexualGroup.toString());
        bundle.putString(getString(R.string.key_currentUserData), new Gson().toJson(currentUserEntity));
//        intent.putExtra(getString(R.string.key_bundle), bundle);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        settings_IMG_profile = findViewById(R.id.settings_IMG_profile);
        settings_ETXT_name = findViewById(R.id.settings_ETXT_name);
        settings_ETXT_description = findViewById(R.id.settings_ETXT_description);
        settings_BTN_confirm = findViewById(R.id.settings_BTN_confirm);
        settings_BTN_back = findViewById(R.id.settings_BTN_back);
    }
}

