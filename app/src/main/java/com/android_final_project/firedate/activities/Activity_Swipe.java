package com.android_final_project.firedate.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.android_final_project.firedate.R;
import com.android_final_project.firedate.data.AuthSingleton;
import com.android_final_project.firedate.Adapters.UserArrayAdapter;
import com.android_final_project.firedate.data.UserEntity;
import com.android_final_project.firedate.data.UserOperator;
import com.android_final_project.firedate.utils.GPS;
import com.android_final_project.firedate.utils.NotificationBackgroundProcess;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;


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
    private ImageView swipe_BTN_logout;
    private ImageView swipe_BTN_settings;
    private ImageView swipe_BTN_chats;
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

    private GPS gps;
    private Thread gpsThread;
    private Location currentLocation;
    private int userSearchingDistance = 50;
    private long locationUpdateInterval = 300_000L;
    private DatabaseReference locationRef;
    private Runnable gpsRunnable = () -> {
        try {
            while (true) {
                gps.getLocation(newLocation -> {
                    if (newLocation != null) {
//                            Log.d("pttt", "handleGPS: " + newLocation);
                        int dis = gps.calculateDistance(currentLocation, newLocation);
                        if (Math.abs(dis) > 2) {
                            currentLocation = newLocation;
                            locationRef.setValue(currentLocation);
                        }
                    }
                });
                Thread.sleep(locationUpdateInterval);
            }
        } catch (InterruptedException ignored) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        gps = GPS.getMe();


        initUserOperator();
        currentUserId = AuthSingleton.getMe().getCurrentUser().getUid();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        findViews();

        initValues();

        //TODO add notifications
//        handleNotifications();



    }

    private void endOnCreate() {
        initViews();
        locationRef = usersDb
                .child(currentUserSexualGroup.toString())
                .child(currentUserId)
                .child("location");

        Log.d("pttt", "endOnCreate: Searching dis: " + userSearchingDistance);

        startGpsSampling();
        loadingAnimationEnd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("pttt", "onResume: " + gpsThread);
        startGpsSampling();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopGpsSampling();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }

    @Override
    public void finish() {
        super.finish();
        stopGpsSampling();
    }

    private void startGpsSampling() {
        if((gpsThread!=null && gpsThread.isAlive()) || locationRef == null)
            return;

        Log.d("pttt", "handleGPS: enter");

        currentLocation = null;
        gpsThread = new Thread(gpsRunnable);
        gpsThread.start();
    }

    public void stopGpsSampling(){
        if(gpsThread!=null && gpsThread.isAlive())
            gpsThread.interrupt();
    }

    private void handleNotifications() {
        Intent intent = new Intent(this, NotificationBackgroundProcess.class);
        intent.setAction("BackgroundProcess");

        Bundle notificationsBundle = new Bundle();
        notificationsBundle.putString("test", "test1");
        intent.putExtra("bundle", notificationsBundle);


        //Set the repeated Task
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 10, pendingIntent);

    }


    private void loadingAnimationStart() {
        swipe_LL_loading.setVisibility(View.VISIBLE);
    }

    private void loadingAnimationStart(boolean background) {
        if (background){
            swipe_LL_loading.setBackgroundColor(Color.parseColor("#AAFFFFFF"));
        } else {
            swipe_LL_loading.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        }

        loadingAnimationStart();
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
                    bundle.putInt(getString(R.string.key_currentUserSearchingDistance), userSearchingDistance);
                    endOnCreate();
                });
            });
        } else {
            String currentUserSexualGroupStr = bundle.getString(getString(R.string.key_currentUserSexualGroup));
            currentUserSexualGroup = UserOperator.SexualGroup.valueOf(currentUserSexualGroupStr);
            initFlingContainer(currentUserSexualGroup.getPreferenceGroups());
            userSearchingDistance = bundle.getInt(getString(R.string.key_currentUserSearchingDistance), 50);
            endOnCreate();
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
        swipe_LL_loading = findViewById(R.id.swipe_LL_loading);
    }

    private void initViews(){
        swipe_BTN_left.setOnClickListener(v -> {
            try {
                flingContainer.getTopCardListener().selectLeft();
            }
            catch(NullPointerException ignored){ }
        });
        swipe_BTN_right.setOnClickListener(v -> {
            try {
                flingContainer.getTopCardListener().selectRight();
            }
            catch(NullPointerException ignored){ }
        });
        swipe_BTN_logout.setOnClickListener(v -> logoutDialogMsg());
        swipe_BTN_settings.setOnClickListener(v -> {
            changeActivity(Activity_Settings.class);
            finish();});
        swipe_BTN_chats.setOnClickListener(v -> {
            changeActivity(Activity_ChatList.class);});
    }

    private void logoutDialogMsg() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.logout_dialog_title))
                .setMessage(getString(R.string.logout_dialog_supporting_text))
                .setNeutralButton(getString(R.string.logout_dialog_cancel), (dialogInterface, i1) -> {
                })
                .setPositiveButton(getString(R.string.logout_dialog_yes), (dialogInterface, i1) -> {
                    userOperator.logout();
                    finish();
                })
                .show();
    }

    private void changeActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(getString(R.string.key_bundle), bundle);
        startActivity(intent);
    }

    private void initFlingContainer(ArrayList<UserOperator.SexualGroup> userPreferenceGroups){

        initCardList(userPreferenceGroups);

        arrayAdapter = new UserArrayAdapter(this, R.layout.item_swipe_card, cardList);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(
                new SwipeFlingAdapterView.onFlingListener() {
                    @Override
                    public void removeFirstObjectInAdapter() {
                        // this is the simplest way to delete an object from the Adapter (/AdapterView)
//                        Log.d("pttt", "removed user: " + cardList.get(0).getUserId());
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
                        if(cardList.size() == 0){
                            loadingAnimationStart(false);
                        } else {
                            loadingAnimationEnd();
                        }

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
        flingContainer.setOnItemClickListener((itemPosition, dataObject) -> {
            UserEntity tmp = (UserEntity) dataObject;
            bundle.putString(getString(R.string.key_otherUserData), new Gson().toJson(tmp));
            bundle.putString(getString(R.string.key_location) , new Gson().toJson(currentLocation));
            bundle.putFloat(getString(R.string.key_distance), currentLocation.distanceTo(tmp.getLocation()) / 100_000);
            changeActivity(Activity_UserDetails.class);

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
                    DatabaseReference chatsDbRef = FirebaseDatabase.getInstance().getReference().child("Chats");
                    String chatKey = chatsDbRef.push().getKey();
                    chatsDbRef.child(chatKey).setValue(true);

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

                        UserEntity otherUser = getUserFromDB(userSnapshot);

                        // check if show this person to User
                        if(isPotential(userSnapshot, otherUser)
                        ){
//                            Log.d("pttt", "add user: " + otherUser.getUserId());
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

                private UserEntity getUserFromDB(DataSnapshot userSnapshot) {
//                    String userJSON = userSnapshot.toString();
//                    UserEntity userEntity = new Gson().fromJson(userJSON, new UserEntity().getClass());
//                    return userEntity;

                    String userID = userSnapshot.getKey();
                    String name = userSnapshot.child("name").getValue(String.class);
                    String description = userSnapshot.child("description").getValue(String.class);
                    String profileImageUrl = userSnapshot.child("profileImageUrl").getValue(String.class);
                    Long usersAgeInMillis = userSnapshot.child("usersAgeInMillis").getValue(Long.class);
                    Integer searchDistance = userSnapshot.child("searchDistance").getValue(Integer.class);

                    String locationJSON = userSnapshot.child("location").exists() ?
                            userSnapshot.child("location").getValue().toString(): null;
                    Location location = new Gson().fromJson(locationJSON, Location.class);

                    return new UserEntity(userID, name, description, profileImageUrl, usersAgeInMillis, location, searchDistance);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    isLoading = false;
                }
            });
        }
    }

    private boolean isPotential(DataSnapshot userSnapshot, UserEntity otherUser) {
        boolean flag = true;
        if (otherUser.getUserId().equals(currentUserId)){
            return false;
        }
        if (userSnapshot.child("swipes").child("left").hasChild(currentUserId)){
            flag = false;
        }
        if (userSnapshot.child("swipes").child("right").hasChild(currentUserId)){
            flag = false;
        }
        if (otherUser.getUserId().equals(lastOtherUserID)){
            flag = false;
        }
        if (cardList.contains(otherUser)){
            flag = false;
        }
//        if (!userSnapshot.child("location").exists()){
//            flag = false;
//        } else {
//            Location otherUserLocation = userSnapshot.child("location").getValue(Location.class);
//            if (otherUserLocation != null) {
//                if (gps.calculateDistance(currentLocation, otherUserLocation) <= userSearchingDistance) {
//                    flag = false;
//                }
//            }
//        }

        return flag;
    }
}