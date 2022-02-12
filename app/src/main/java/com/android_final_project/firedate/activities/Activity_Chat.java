package com.android_final_project.firedate.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import com.android_final_project.firedate.Adapters.ChatAdapter;
import com.android_final_project.firedate.R;
import com.android_final_project.firedate.data.ChatEntity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Chat extends AppCompatActivity {

    private Bundle bundle;
    private String chatId;
    private String currentUserId;
    private String currentUserSexualGroup;
    private String otherUserId;
    private String otherUserSexualGroup;

    private RecyclerView chat_RCV_recyclerView;
    private ChatAdapter adapter;
//    private RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager layoutManager;

    private ArrayList<ChatEntity> dataSetChats = new ArrayList<ChatEntity>();

    private DatabaseReference chatDb;


    private EditText chat_TXTF_message;
    private MaterialButton chat_BTN_send;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.bundle = getIntent().getBundleExtra(getString(R.string.key_bundle));
        initBundle();

        chatDb = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatId);

        findViews();
        initViews();

        setRecyclerView();

        getChatMsg();
    }

    private void getChatMsg() {
        chatDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (!snapshot.exists()){
                    return;
                }
                String msg = getValue(snapshot, "text");
                String createdBy = getValue(snapshot, "CreateBy");
                Date time = snapshot.child("time").getValue(Date.class);

                if (msg != null && createdBy != null){
                    boolean currentUserMsg = false;
                    if (createdBy.equals(currentUserId)){
                        currentUserMsg = true;
                    }

                    ChatEntity entity = new ChatEntity(msg, currentUserMsg, time);
                    dataSetChats.add(entity);
                    adapter.notifyDataSetChanged();

                    int pos = chat_RCV_recyclerView.getAdapter().getItemCount();
                    chat_RCV_recyclerView.smoothScrollToPosition(pos);
                }



            }

            private String getValue(DataSnapshot snapshot, String key) {
                String output = null;
                if (snapshot.child(key).getValue() != null){
                    output = snapshot.child(key).getValue().toString();
                }
                return output;
            };

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }

        });
    }

    private void initBundle() {
        chatId = bundle.getString(getString(R.string.key_chat_id));
        currentUserId = bundle.getString(getString(R.string.key_currentUserId));
        currentUserSexualGroup = bundle.getString(getString(R.string.key_currentUserSexualGroup));
        otherUserId = bundle.getString(getString(R.string.key_other_user_id));
        otherUserSexualGroup = bundle.getString(getString(R.string.key_other_user_sexual_group));
    }

    private void findViews() {
        chat_RCV_recyclerView = findViewById(R.id.chat_RCV_recyclerView);
        chat_TXTF_message = findViewById(R.id.chat_TXTF_message);
        chat_BTN_send = findViewById(R.id.chat_BTN_send);
    }

    private void initViews() {
        chat_BTN_send.setOnClickListener(view -> {
            String msg = chat_TXTF_message.getText().toString();
            if (msg.compareTo("") == 0){
                return;
            }
            sendMessage(msg);

            chat_TXTF_message.setText(null);
        });
    }

    private void sendMessage(String msg) {
        DatabaseReference msgRef = chatDb.child(chatDb.push().getKey());

        Map newMsg = new HashMap();

        newMsg.put("CreateBy", currentUserId);
        newMsg.put("text", msg);
        newMsg.put("time", Calendar.getInstance().getTime());

        msgRef.setValue(newMsg);
    }

    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        chat_RCV_recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(getDataSetMatches(), this);
        chat_RCV_recyclerView.setAdapter(adapter);

    }

    private List<ChatEntity> getDataSetMatches() {
        return dataSetChats;
    }


}