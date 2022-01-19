package com.android_final_project.firedate.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android_final_project.firedate.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class UserArrayAdpter extends ArrayAdapter<UserEntity> {

    private Context context;
    private ImageView   card_IMG_image  ;
    private TextView    card_TXT_name   ;


    public UserArrayAdpter(@NonNull Context context, int resource, @NonNull List<UserEntity> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        UserEntity userEntity = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card, parent, false);
        }

        findViews(convertView);

        String imgUrl = "https://eitrawmaterials.eu/wp-content/uploads/2016/09/person-icon.png";
        Glide
                .with(context)
                .load(imgUrl)
                .centerCrop()
                .into(card_IMG_image);
        card_TXT_name .setText(userEntity.getName());

        return convertView;
    }

    private void findViews(View convertView) {
        card_IMG_image = convertView.findViewById(R.id.card_IMG_image);
        card_TXT_name  = convertView.findViewById(R.id.card_TXT_name );
    }
}
