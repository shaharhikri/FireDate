package com.android_final_project.firedate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android_final_project.firedate.R;

public class Activity_ChatList extends AppCompatActivity {

    private RecyclerView chatList_RCV_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        findViews();
        initViews();
    }

    private void initViews() {
        chatList_RCV_recyclerView.setNestedScrollingEnabled(false);
        chatList_RCV_recyclerView.setHasFixedSize(true);
    }

    private void findViews() {
        chatList_RCV_recyclerView = findViewById(R.id.chatList_RCV_recyclerView);
    }
}