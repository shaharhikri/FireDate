package com.android_final_project.firedate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.android_final_project.firedate.R;
import com.android_final_project.firedate.data.UserEntity;
import com.android_final_project.firedate.utils.GPS;
import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

public class Activity_UserDetails extends AppCompatActivity {

    private Bundle bundle;
    private UserEntity userEntity;
    private Location currentLocation;
    private float distance;

    private AppCompatImageView  userDetails_IMG_image       ;
    private MaterialTextView    userDetails_LBL_name        ;
    private MaterialTextView    userDetails_LBL_description ;
    private MaterialTextView    userDetails_LBL_distance    ;
    private AppCompatButton     userDetails_BTN_back        ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        initBundle();
        findViews();
        initViews();
    }

    @SuppressLint("DefaultLocale")
    private void initViews() {
        String imgUrl = userEntity.getProfileImageUrl();
        Glide
                .with(this)
                .load(imgUrl)
                .into(userDetails_IMG_image);
        String tmp;

        tmp = String.format("%s, %d", userEntity.getName(), userEntity.getAge());
        userDetails_LBL_name.setText(tmp);

        userDetails_LBL_description.setText(userEntity.getDescription());


//        int distance = GPS.getMe().calculateDistance(currentLocation, userEntity.getLocation());
        tmp = String.format("Distance: %.2f KM", distance);
//        tmp = String.format("MyDis: %.2f, %.2f; userDis: %.2f, %.2f", currentLocation.getLatitude(), currentLocation.getLongitude(),
//                userEntity.getLocation().getLatitude(), userEntity.getLocation().getLongitude());
        userDetails_LBL_distance.setText(tmp);



        userDetails_BTN_back.setOnClickListener(v -> onBackPressed());
    }

    private void findViews() {
        userDetails_IMG_image       = findViewById(R.id.userDetails_IMG_image      );
        userDetails_LBL_name        = findViewById(R.id.userDetails_LBL_name       );
        userDetails_LBL_description = findViewById(R.id.userDetails_LBL_description);
        userDetails_LBL_distance    = findViewById(R.id.userDetails_LBL_distance   );
        userDetails_BTN_back        = findViewById(R.id.userDetails_BTN_back       );



    }

    private void initBundle() {
        this.bundle = getIntent().getBundleExtra(getString(R.string.key_bundle));
        String userEntityJSON = bundle.getString(getString(R.string.key_otherUserData));
        userEntity = new Gson().fromJson(userEntityJSON, UserEntity.class);


        distance = bundle.getFloat(getString(R.string.key_distance));

        String currentLocationJSON = bundle.getString(getString(R.string.key_location));
        currentLocation = new Gson().fromJson(currentLocationJSON, Location.class);
    }
}