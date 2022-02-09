package com.android_final_project.firedate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.android_final_project.firedate.R;
import com.android_final_project.firedate.data.AuthSingleton;
import com.android_final_project.firedate.Adapters.UserArrayAdapter;
import com.android_final_project.firedate.data.UserEntity;
import com.android_final_project.firedate.data.UserOperator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.lorentzos.flingswipe.FlingCardListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;


public class Activity_Swipe extends AppCompatActivity {

    private Bundle bundle;
    private UserEntity userEntity;

//    private ArrayList<String> cardList;
    private ArrayList<UserEntity> cardList;
    private ArrayList<String> cardsId = new ArrayList<>();
//    private ArrayAdapter<String> arrayAdapter;
    private UserArrayAdapter arrayAdapter;
    private int i;

    private SwipeFlingAdapterView flingContainer;
    private FloatingActionButton swipe_BTN_left;
    private FloatingActionButton swipe_BTN_right;
    private Button swipe_BTN_logout;
    private Button swipe_BTN_settings;
    private Button swipe_BTN_chats;
    private LinearLayout swipe_LL_loading;
//    private TextView swipe_TXT_print;

    private UserOperator userOperator;
    private String currentUserId;
    private UserOperator.SexualGroup currentUserSexualGroup;
    private DatabaseReference usersDb;
    private int cardsPerPage = 20;
    private String lastOtherUserID;
    private boolean isLoading = false;

//    private ArrayList<UserOperator.SexualGroup> userPreferenceGroups;

    private ListView listView;
    private List<UserEntity> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);



        initUserOperator();
        currentUserId = AuthSingleton.getMe().getCurrentUser().getUid();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        findViews();

        initValues();


        // TODO finish freeze
    }

    private void loadingAnimationStart() {
        swipe_LL_loading.setVisibility(View.VISIBLE);
    }

    private void loadingAnimationEnd() {
        swipe_LL_loading.setVisibility(View.GONE);
    }

    private void initValues() {
        loadingAnimationStart();
        bundle = getIntent().getBundleExtra(getString(R.string.key_bundle));
        if (bundle == null) {
            bundle = new Bundle();
            UserOperator.getUserGroup(currentUserId, sexualPreferenceGroups -> {
                currentUserSexualGroup = sexualPreferenceGroups;
                initFlingContainer(sexualPreferenceGroups.getPreferenceGroups());

                UserOperator.getUserData(currentUserId, currentUserSexualGroup, userData -> {
                    userEntity = userData;
                    bundle.putString(getString(R.string.key_currentUserId), currentUserId);
                    bundle.putString(getString(R.string.key_currentUserSexualGroup), currentUserSexualGroup.toString());
                    bundle.putString(getString(R.string.key_currentUserData), new Gson().toJson(userEntity));

                    initViews();
                    loadingAnimationEnd();
                });
            });
        } else {
            String currentUserSexualGroupStr = bundle.getString(getString(R.string.key_currentUserSexualGroup));
            currentUserSexualGroup = UserOperator.SexualGroup.valueOf(currentUserSexualGroupStr);
            initFlingContainer(currentUserSexualGroup.getPreferenceGroups());
            initViews();
            loadingAnimationEnd();
        }
    }

    //Has to happen before initViews() call
    private void initUserOperator(){
        userOperator = new UserOperator(Activity_Swipe.this, new UserOperator.UserOperatorCallback(){
            @Override
            public void operationFailed(String msg) { }

            @Override
            public void operationSucceeded() { }

        });

        AuthSingleton.setAuthCallback(new AuthSingleton.DefaultAuthCallback(this));
    }

    private void findViews(){
        flingContainer = findViewById(R.id.swipe_FRM_cards);
        swipe_BTN_left = findViewById(R.id.swipe_BTN_left);
        swipe_BTN_right = findViewById(R.id.swipe_BTN_right);
        swipe_BTN_logout = findViewById(R.id.swipe_BTN_logout);
        swipe_BTN_settings = findViewById(R.id.swipe_BTN_settings);
        swipe_BTN_chats = findViewById(R.id.swipe_BTN_chats);
//        swipe_TXT_print = findViewById(R.id.swipe_TXT_print);
        swipe_LL_loading = findViewById(R.id.swipe_LL_loading);
    }

    private void initViews(){
        swipe_BTN_left.setOnClickListener(v -> {
            try {
                flingContainer.getTopCardListener().selectLeft();
            }
            catch(NullPointerException e){ }
        });
        swipe_BTN_right.setOnClickListener(v -> {
            try {
                flingContainer.getTopCardListener().selectRight();
            }
            catch(NullPointerException e){ }
        });
        swipe_BTN_logout.setOnClickListener(v -> {
            userOperator.logout();
            finish();});
        swipe_BTN_settings.setOnClickListener(v -> {
            changeActivity(Activity_Settings.class);
            finish();});
        swipe_BTN_chats.setOnClickListener(v -> {
            changeActivity(Activity_ChatList.class);});
    }

    private void changeActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(getString(R.string.key_bundle), bundle);
        startActivity(intent);
    }

    private void initFlingContainer(ArrayList<UserOperator.SexualGroup> userPreferenceGroups){

        //TODO: Do somethig with the list

        initCardList(userPreferenceGroups);

        arrayAdapter = new UserArrayAdapter(this, R.layout.item_swipe_card, cardList);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(
                new SwipeFlingAdapterView.onFlingListener() {
                    @Override
                    public void removeFirstObjectInAdapter() {
                        // this is the simplest way to delete an object from the Adapter (/AdapterView)
                        Log.d("pttt", "removed user: " + cardList.get(0).getUserId());
                        lastOtherUserID = cardList.get(0).getUserId();
                        cardList.remove(0);
                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onLeftCardExit(Object dataObject) {
                        //Do something on the left!
                        UserEntity obj = (UserEntity) dataObject;
                        handleSwipe(obj, "left");
//                        swipe_TXT_print.setText(obj.getName() + " Left!");

                    }

                    @Override
                    public void onRightCardExit(Object dataObject) {
                        UserEntity obj = (UserEntity) dataObject;
                        handleSwipe(obj, "right");
//                        swipe_TXT_print.setText(obj.getName() + " Right!");
                    }

                    @Override
                    public void onAdapterAboutToEmpty(int itemsInAdapter) {
                        // Ask for more data here
                        getUsers(userPreferenceGroups, cardsId);
                    }

                    @Override
                    public void onScroll(float scrollProgressPercent) {
                        View selectedCard = flingContainer.getSelectedView();
                        selectedCard.findViewById(R.id.card_VIEW_rightIndicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                        selectedCard.findViewById(R.id.card_VIEW_leftIndicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                    }
                }
        );

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
//                Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
//                swipe_TXT_print.setText(dataObject+" Clicked!");
            }
        });
    }

    private void handleSwipe(UserEntity otherUser, String direction) {
        UserOperator.getUserGroup(otherUser.getUserId(), sexualPreferenceGroups -> {
            usersDb.child(sexualPreferenceGroups.toString()).child(otherUser.getUserId()).child("swipes").child(direction).child(currentUserId).setValue(true);
            if(direction.equals("right")){
                checkMatch(otherUser, sexualPreferenceGroups);
            }
        });

    }

    private void checkMatch(UserEntity otherUser, UserOperator.SexualGroup sexualPreferenceGroups) {
        DatabaseReference currentUserDbRef = usersDb
                .child(currentUserSexualGroup.toString())
                .child(currentUserId)
                .child("swipes")
                .child("right")
                .child(otherUser.getUserId());
        currentUserDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String chatKey = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb
                            .child(sexualPreferenceGroups.toString())
                            .child(otherUser.getUserId())
                            .child("swipes")
                            .child("matches")
                            .child(currentUserId)
                            .child("ChatId")
                            .setValue(chatKey);
                    usersDb
                            .child(currentUserSexualGroup.toString())
                            .child(currentUserId)
                            .child("swipes")
                            .child("matches")
                            .child(otherUser.getUserId())
                            .child("ChatId")
                            .setValue(chatKey);
                    Toast.makeText(Activity_Swipe.this, "Match!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void initCardList(ArrayList<UserOperator.SexualGroup> userPreferenceGroups){
        cardList = new ArrayList<>();
        cardsId = new ArrayList<>();
        getUsers(userPreferenceGroups, cardsId);
    }

    private void getUsers(ArrayList<UserOperator.SexualGroup> userPreferenceGroups, ArrayList<String> nodesId) {
        Query query;
        int listSize = userPreferenceGroups.size();

        // TODO: what happen if someone join in start of list;
        for (int i = 0; i < listSize; i++) {
            UserOperator.SexualGroup sg = userPreferenceGroups.get(i);
            String nodeId;
            try {
                nodeId = nodesId.get(i);
            }catch (Exception e){
                nodeId = null;
            }
            if (nodeId == null) {
                query = usersDb
                        .child(sg.toString())
                        .orderByKey()
                        .limitToFirst(cardsPerPage);
            }
            else {
                query = usersDb
                        .child(sg.toString())
                        .orderByKey()
                        .startAt(nodeId)
                        .limitToFirst(cardsPerPage);
            }

            int finalI = i;
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        UserEntity otherUser = new UserEntity(
                                userSnapshot.getKey(),
                                userSnapshot.child("name").getValue().toString(),
                                userSnapshot.child("description").getValue().toString(),
                                userSnapshot.child("profileImageUrl").getValue() == null ?
                                        null :
                                        userSnapshot.child("profileImageUrl").getValue().toString()
                                );
                        // TODO: check if show this person to User(duplicate)
                        if(!otherUser.getUserId().equals(currentUserId)
                                && !userSnapshot.child("swipes").child("left").hasChild(currentUserId)
                                && !userSnapshot.child("swipes").child("right").hasChild(currentUserId)
                                && !otherUser.getUserId().equals(lastOtherUserID)
                                && !cardList.contains(otherUser)
                        ){
                            Log.d("pttt", "add user: " + otherUser.getUserId());
                            cardList.add(otherUser);
                        }

                    }
                    try {
                        nodesId.set(finalI, dataSnapshot.getKey());
                    }catch (Exception e){
                        nodesId.add(dataSnapshot.getKey());
                    }

                    arrayAdapter.notifyDataSetChanged();
                    isLoading = false;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    isLoading = false;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }
}