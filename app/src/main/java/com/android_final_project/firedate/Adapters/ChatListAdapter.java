package com.android_final_project.firedate.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android_final_project.firedate.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android_final_project.firedate.activities.Activity_Chat;
import com.android_final_project.firedate.data.AuthSingleton;
import com.android_final_project.firedate.data.ChatListEntity;
import com.bumptech.glide.Glide;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListsViewHolders> {

    private List<ChatListEntity> chatListEntities;
    private Context context;

    public ChatListAdapter(List<ChatListEntity> chatListEntities, Context context) {
        this.chatListEntities = chatListEntities;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatListsViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatlist, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatListsViewHolders rcv = new ChatListsViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListsViewHolders holder, int position) {
//        holder.mMatchId.setText(chatListEntities.get(position).getUserId());
        holder.chatID = chatListEntities.get(position).getChatId();
        holder.otherUserID = chatListEntities.get(position).getUserId();
        holder.otherUserSexualGroup = chatListEntities.get(position).getUserSexualGroup();
        holder.chatListItem_TXT_matchName.setText(chatListEntities.get(position).getName());
        if(!chatListEntities.get(position).getProfileImageUrl().equals("default")){
            Glide
                    .with(context)
                    .load(chatListEntities.get(position).getProfileImageUrl())
                    .into(holder.chatListItem_IMG_matchProfile);
        }
    }

    @Override
    public int getItemCount() {
        return this.chatListEntities.size();
    }

    public class ChatListsViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        private String chatID;
        private String otherUserID;
        private String otherUserSexualGroup;
        private ImageView chatListItem_IMG_matchProfile;
        private LinearLayout chatListItem_LLV_rowLayout;
        private TextView chatListItem_TXT_matchName;
        private TextView        chatListItem_TXT_matchTime;


        public ChatListsViewHolders(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            findViews(itemView);
            initViews();
        }

        private void initViews() {
        }

        private void findViews(View itemView) {
            chatListItem_IMG_matchProfile = itemView.findViewById(R.id.chatListItem_IMG_matchProfile);
            chatListItem_LLV_rowLayout = itemView.findViewById(R.id.chatListItem_LLV_rowLayout);
            chatListItem_TXT_matchName = itemView.findViewById(R.id.chatListItem_TXT_matchName);
            chatListItem_TXT_matchTime = itemView.findViewById(R.id.chatListItem_TXT_matchTime);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, Activity_Chat.class);
            Bundle bundle = new Bundle();

            bundle.putString(context.getString(R.string.key_chat_id), chatID);
            Log.d("pttt", "chatListAdpter ChatID: " + chatID);
            bundle.putString(context.getString(R.string.key_currentUserId), AuthSingleton.getMe().getUid());

            bundle.putString(context.getString(R.string.key_other_user_id), otherUserID);
            bundle.putString(context.getString(R.string.key_other_user_sexual_group), otherUserSexualGroup);

            intent.putExtra(context.getString(R.string.key_bundle), bundle);
            context.startActivity(intent);
        }
    }
}
