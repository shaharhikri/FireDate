package com.android_final_project.firedate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android_final_project.firedate.Adapters.ChatListAdapter;
import com.android_final_project.firedate.R;
import com.android_final_project.firedate.data.ChatListEntity;

import java.util.ArrayList;
import java.util.List;

public class Activity_ChatList extends AppCompatActivity {

    private RecyclerView chatList_RCV_recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<ChatListEntity> dataSetMatches = new ArrayList<ChatListEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        findViews();
        initViews();
        setRecyclerView();
        mockData();
    }

    private void mockData() {
        for (int i = 0; i < 100; i++) {
            ChatListEntity tmp = new ChatListEntity("ID: " + i, "name: " + i, "https://www.nicepng.com/png/detail/367-3671905_person-icon-person-icon-silhouette.png", "08:22AM");
            dataSetMatches.add(tmp);
        }
        adapter.notifyDataSetChanged();
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