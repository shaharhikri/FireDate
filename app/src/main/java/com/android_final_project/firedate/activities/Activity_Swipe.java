package com.android_final_project.firedate.activities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android_final_project.firedate.R;
import com.android_final_project.firedate.data.AuthSingleton;
import com.android_final_project.firedate.data.UserArrayAdpter;
import com.android_final_project.firedate.data.UserEntity;
import com.android_final_project.firedate.data.UserOperator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;


public class Activity_Swipe extends AppCompatActivity {

//    private ArrayList<String> cardList;
    private ArrayList<UserEntity> cardList;
    private ArrayList<String> cardsId = new ArrayList<>();
//    private ArrayAdapter<String> arrayAdapter;
    private UserArrayAdpter arrayAdapter;
    private int i;

    private SwipeFlingAdapterView flingContainer;
    private Button swipe_BTN_left;
    private Button swipe_BTN_right;
    private Button swipe_BTN_logout;
    private TextView swipe_TXT_print;

    private UserOperator userOperator;
    private String currentUserId;
    private UserOperator.SexualGroup currentUserSexualGroup;
    private DatabaseReference usersDb;
    private int cardsPerPage = 20;
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
        initViews();

        UserOperator.getUserGroup(currentUserId, sexualPreferenceGroups -> {
            currentUserSexualGroup = sexualPreferenceGroups;
            initFlingContainer(sexualPreferenceGroups.getPreferenceGroups());
        });
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
        swipe_TXT_print = findViewById(R.id.swipe_TXT_print);
    }

    private void initViews(){
        swipe_BTN_left.setOnClickListener(v -> { flingContainer.getTopCardListener().selectLeft(); });
        swipe_BTN_right.setOnClickListener(v -> { flingContainer.getTopCardListener().selectRight(); });
        swipe_BTN_logout.setOnClickListener(v -> { userOperator.logout(); finish();});

    }

    private void initFlingContainer(ArrayList<UserOperator.SexualGroup> userPreferenceGroups){

        //TODO: Do somethig with the list

        initCardList(userPreferenceGroups);

        arrayAdapter = new UserArrayAdpter(this, R.layout.card, cardList);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(
                new SwipeFlingAdapterView.onFlingListener() {
                    @Override
                    public void removeFirstObjectInAdapter() {
                        // this is the simplest way to delete an object from the Adapter (/AdapterView)
                        Log.d("LIST", "removed object!");
                        cardList.remove(0);
                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onLeftCardExit(Object dataObject) {
                        //Do something on the left!
                        UserEntity obj = (UserEntity) dataObject;
                        handleSwipe(obj, "left");
                        swipe_TXT_print.setText(obj.getName() + " Left!");

                    }

                    @Override
                    public void onRightCardExit(Object dataObject) {
                        UserEntity obj = (UserEntity) dataObject;
                        handleSwipe(obj, "right");
                        swipe_TXT_print.setText(obj.getName() + " Right!");
                    }

                    @Override
                    public void onAdapterAboutToEmpty(int itemsInAdapter) {
                        // Ask for more data here
                        getUsers(userPreferenceGroups, cardsId);
//                        cardList.add("XML "+i);
//                        arrayAdapter.notifyDataSetChanged();
//                        Log.d("LIST", "notified");
//                        i++;
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
                swipe_TXT_print.setText(dataObject+" Clicked!");
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
                    Toast.makeText(Activity_Swipe.this, "Match!", Toast.LENGTH_SHORT).show();
                    usersDb
                            .child(sexualPreferenceGroups.toString())
                            .child(otherUser.getUserId())
                            .child("swipes")
                            .child("matches")
                            .child(currentUserId)
                            .setValue(true);
                    usersDb
                            .child(currentUserSexualGroup.toString())
                            .child(currentUserId)
                            .child("swipes")
                            .child("matches")
                            .child(otherUser.getUserId())
                            .setValue(true);
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
            if (nodeId == null)
                query = usersDb
                        .child(sg.toString())
                        .orderByKey()
                        .limitToFirst(cardsPerPage);
            else
                query = usersDb
                        .child(sg.toString())
                        .orderByKey()
                        .startAt(nodeId)
                        .limitToFirst(cardsPerPage);

            int finalI = i;
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        UserEntity otherUser = new UserEntity(
                                userSnapshot.getKey(),
                                userSnapshot.child("name").getValue().toString(),
                                userSnapshot.child("description").getValue().toString());
                        // TODO: check if show this person to User(duplicate)
                        if(!otherUser.getUserId().equals(currentUserId)
                                && !userSnapshot.child("swipes").child("left").hasChild(currentUserId)
                                && !userSnapshot.child("swipes").child("right").hasChild(currentUserId)
                        ){
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