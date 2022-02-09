package com.android_final_project.firedate.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android_final_project.firedate.R;
import com.android_final_project.firedate.data.UserEntity;
import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class UserArrayAdapter extends ArrayAdapter<UserEntity> {

    private Context context;
    private ImageView   card_IMG_image  ;
    private TextView    card_TXT_name   ;


    public UserArrayAdapter(@NonNull Context context, int resource, @NonNull List<UserEntity> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent){
        UserEntity userEntity = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_swipe_card, parent, false);
        }

        findViews(convertView);

        if (userEntity.getProfileImageUrl() == null){
            userEntity.setProfileImageUrl("https://eitrawmaterials.eu/wp-content/uploads/2016/09/person-icon.png");
        }

        Glide
                .with(context)
                .load(userEntity.getProfileImageUrl())
                .centerCrop()
                .into(card_IMG_image);


        if (userEntity.getUsersAgeInMillis() == null){
            Date randomDate = new Date();
            randomDate.setYear(new Date().getYear() - (new Random().nextInt(12) + 18));
            userEntity.setUsersAgeInMillis(randomDate.getTime());
        }
        String age = Integer.toString(calculateAge(userEntity.getUsersAgeInMillis()));

        card_TXT_name .setText(userEntity.getName() + ", " + age);

        return convertView;
    }

    private int calculateAge(Long usersAgeInMillis) {
        Date birthDay = new Date(usersAgeInMillis);
        return new Date().getYear() - birthDay.getYear();
    };

    private void findViews(View convertView) {
        card_IMG_image = convertView.findViewById(R.id.card_IMG_image);
        card_TXT_name  = convertView.findViewById(R.id.card_TXT_name );
    }
}
