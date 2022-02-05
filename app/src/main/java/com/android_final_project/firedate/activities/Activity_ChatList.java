package com.android_final_project.firedate.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android_final_project.firedate.Adapters.ChatListAdapter;
import com.android_final_project.firedate.R;
import com.android_final_project.firedate.data.ChatListEntity;
import com.android_final_project.firedate.data.UserEntity;
import com.android_final_project.firedate.data.UserOperator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Activity_ChatList extends AppCompatActivity {

    private Bundle bundle;

    private DatabaseReference matchesDb;
    private DatabaseReference usersDb;

    private UserOperator userOperator;

    private UserEntity currentUserEntity;
    private UserOperator.SexualGroup currentUserSexualGroup;
    private String currentUserId;

    private RecyclerView chatList_RCV_recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<ChatListEntity> dataSetMatches = new ArrayList<ChatListEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);


        this.bundle = getIntent().getBundleExtra(getString(R.string.key_bundle));
        initBundle();
        initDB();

        findViews();
        initViews();
        setRecyclerView();
        getUserMatchId();
    }

    private void initDB() {
        usersDb = FirebaseDatabase.getInstance().getReference()
                .child("Users");
        matchesDb = usersDb
                .child(currentUserSexualGroup.toString())
                .child(currentUserId)
                .child("swipes")
                .child("matches");

        matchesDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot match: snapshot.getChildren()) {
                        fetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchMatchInformation(String key) {
        UserOperator.getUserGroup(key, sexualGroup -> {
            DatabaseReference otherUserRef = usersDb
                    .child(sexualGroup.toString())
                    .child(key);
            otherUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String userID = snapshot.getKey();


                        String chatId = getValue(snapshot, "ChatId");
                        String name = getValue(snapshot, "name");
                        String profileImageUrl = getValue(snapshot, "profileImageUrl");

//                        if (snapshot.child("ChatId").getValue() != null){
//                            chatId = snapshot.child("ChatId").getValue().toString();
//                        }
//                        if (snapshot.child("name").getValue() != null){
//                            name = snapshot.child("name").getValue().toString();
//                        }
//                        if (snapshot.child("profileImageUrl").getValue() != null){
//                            profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
//                        }


                        UserOperator.getUserGroup(key, sexualGroup -> {
                            ChatListEntity tmp = new ChatListEntity(chatId, userID, sexualGroup.toString(), name, profileImageUrl, "08:22AM");
                            dataSetMatches.add(tmp);
                            adapter.notifyDataSetChanged();
                        });


                    }
                }

                private String getValue(DataSnapshot snapshot, String key) {
                    String output = "";
                    if (snapshot.child(key).getValue() != null){
                        output = snapshot.child(key).getValue().toString();
                    }
                    return output;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    private void initBundle() {
        currentUserId = bundle.getString(getString(R.string.key_currentUserId));
        String currentUserSexualGroupStr = bundle.getString(getString(R.string.key_currentUserSexualGroup));
        currentUserSexualGroup = UserOperator.SexualGroup.valueOf(currentUserSexualGroupStr);

        String currentUserEntityStr = bundle.getString(getString(R.string.key_currentUserData));
        currentUserEntity = new Gson().fromJson(currentUserEntityStr, UserEntity.class);

    }

    private void getUserMatchId() {

    }

    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(Activity_ChatList.this);
        chatList_RCV_recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatListAdapter(getDataSetMatches(), Activity_ChatList.this);
        chatList_RCV_recyclerView.setAdapter(adapter);
    }

    private List<ChatListEntity> getDataSetMatches() {
        return dataSetMatches;
    }

    private void initViews() {
        chatList_RCV_recyclerView.setNestedScrollingEnabled(false);
        chatList_RCV_recyclerView.setHasFixedSize(true);
    }

    private void findViews() {
        chatList_RCV_recyclerView = findViewById(R.id.chatList_RCV_recyclerView);
    }
}