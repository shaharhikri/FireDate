package com.android_final_project.firedate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android_final_project.firedate.Adapters.ChatAdapter;
import com.android_final_project.firedate.Adapters.ChatListAdapter;
import com.android_final_project.firedate.R;
import com.android_final_project.firedate.data.ChatEntity;
import com.android_final_project.firedate.data.ChatListEntity;

import java.util.List;

public class Activity_Chat extends AppCompatActivity {

    private Bundle bundle;
    private String otherUserId;
    private String otherUserSexualGroup;

    private RecyclerView chat_RCV_recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.bundle = getIntent().getBundleExtra(getString(R.string.key_bundle));
        initBundle();

        setRecyclerView();

//        Toast.makeText(this, otherUserId + " : "+ otherUserSexualGroup, Toast.LENGTH_SHORT).show();
        findViews();
        initViews();
    }

    private void initBundle() {
        otherUserId = bundle.getString(getString(R.string.key_other_user_id));
        otherUserSexualGroup = bundle.getString(getString(R.string.key_other_user_sexual_group));
    }

    private void findViews() {
        chat_RCV_recyclerView = findViewById(R.id.chat_RCV_recyclerView);
    }

    private void initViews() {
        chat_RCV_recyclerView.setNestedScrollingEnabled(false);
        chat_RCV_recyclerView.setHasFixedSize(true);
    }

    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(Activity_Chat.this);
        chat_RCV_recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(getDataSetMatches(), Activity_Chat.this);
        chat_RCV_recyclerView.setAdapter(adapter);
    }

    private List<ChatEntity> getDataSetMatches() {
        return null;
    }


}