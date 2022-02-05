package com.android_final_project.firedate.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android_final_project.firedate.R;
import com.android_final_project.firedate.activities.Activity_Chat;
import com.android_final_project.firedate.data.ChatEntity;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatsViewHolders> {

    private List<ChatEntity> chatEntities;
    private Context context;

    public ChatAdapter(List<ChatEntity> chatEntities, Context context) {
        this.chatEntities = chatEntities;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatsViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatsViewHolders rcv = new ChatsViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolders holder, int position) {

    }

    @Override
    public int getItemCount() {
        return this.chatEntities.size();
    }

    public class ChatsViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        private String otherUserID;
        private String otherUserSexualGroup;


        public ChatsViewHolders(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            findViews(itemView);
            initViews();
        }

        private void initViews() {
        }

        private void findViews(View itemView) {
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, Activity_Chat.class);
            Bundle bundle = new Bundle();

            bundle.putString(context.getString(R.string.key_other_user_id), otherUserID);
            bundle.putString(context.getString(R.string.key_other_user_sexual_group), otherUserSexualGroup);

            intent.putExtra(context.getString(R.string.key_bundle), bundle);
            context.startActivity(intent);
        }
    }
}
