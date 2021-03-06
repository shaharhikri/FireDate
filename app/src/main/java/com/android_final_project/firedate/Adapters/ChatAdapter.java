package com.android_final_project.firedate.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android_final_project.firedate.R;
import com.android_final_project.firedate.data.ChatEntity;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

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
        ChatEntity currentChat = chatEntities.get(position);
        holder.chat_TXT_message.setText(currentChat.getMessage());
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");



        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(currentChat.getTime());


        holder.chat_TXT_date.setText(formattedDate);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );


        if(currentChat.isCurrentUser()){
            holder.chat_TXT_message     .setGravity(Gravity.END);
            holder.chat_TXT_message     .setTextColor(Color.parseColor("#FFFFFF"));
            holder.chat_LL_container    .setGravity(Gravity.END);
            holder.chat_LL_container    .setGravity(Gravity.END);
            holder.chat_LL_container    .setBackgroundColor(Color.parseColor("#F4F4F4"));
            holder.chat_LL_container    .setBackground(context.getDrawable(R.drawable.chat_shape_user));
            params.setMargins(100, 20, 10, 0);
        } else {
            holder.chat_TXT_message     .setGravity(Gravity.START);
            holder.chat_TXT_message     .setTextColor(Color.parseColor("#FFFFFF"));
            holder.chat_LL_container    .setGravity(Gravity.START);
            holder.chat_LL_container    .setBackground(context.getDrawable(R.drawable.chat_shape_other));
            params.setMargins(10, 20, 100, 0);
        }
        holder.chat_LL_container    .setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return this.chatEntities.size();
    }

    public class ChatsViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout    chat_LL_container;
        private TextView        chat_TXT_message;
        private TextView        chat_TXT_date;



        public ChatsViewHolders(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            findViews(itemView);
            initViews();
        }

        private void initViews() {
        }

        private void findViews(View itemView) {
            chat_LL_container = itemView.findViewById(R.id.chat_LL_container);
            chat_TXT_message = itemView.findViewById(R.id.chat_TXT_message);
            chat_TXT_date = itemView.findViewById(R.id.chat_TXT_date);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
