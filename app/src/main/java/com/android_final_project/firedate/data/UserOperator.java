package com.android_final_project.firedate.data;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android_final_project.firedate.R;
import com.android_final_project.firedate.activities.Activity_Swipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserOperator {

    // Male_Female : { Female_male},
    // Female_Bisexual : { Female_Bisexul, Femal_Female, Male_Bisexual, Male_Female }
//    final public static Map<SexualGroup, ArrayList<SexualGroup>> groupMap = new HashMap<SexualGroup, ArrayList<SexualGroup>>()
//            .put(SexualGroup.Male_Male,new ArrayList<SexualGroup>(SexualGroup.Male_Male,))

    AppCompatActivity activity;
    static UserOperatorCallback userOpCallback;

    public UserOperator(AppCompatActivity activity, UserOperatorCallback callback){
        this.activity = activity;
        userOpCallback = callback;
    }

    public void logout(){
        AuthSingleton.getMe().signOut();
    }

    public void login(String email, String pass){
        if(email==null || email.isEmpty()){
            userOpCallback.operationFailed(activity.getString(R.string.empty_email));
        }
        else if(pass==null || pass.isEmpty()) {
            userOpCallback.operationFailed(activity.getString(R.string.empty_pass));
        }
        else{
            AuthSingleton.getMe().signInWithEmailAndPassword(email, pass).addOnCompleteListener(activity,
                    task -> {
                        if (!task.isSuccessful()) { // if email is taken, or password is too short
                            task.addOnFailureListener(exception -> userOpCallback.operationFailed(exception.getMessage()));
                        }
                        else{
                            userOpCallback.operationSucceeded();
                        }
                    });
        }
    }

    @Deprecated
    public void signup(String email, String pass1, String pass2, String name, String description, SexualGroup group){
        if(email==null || email.isEmpty()){
            userOpCallback.operationFailed(activity.getString(R.string.empty_email));
        }
        else if(pass1==null || pass2==null || pass1.isEmpty() || pass2.isEmpty()) {
            userOpCallback.operationFailed(activity.getString(R.string.empty_pass));
        }
        else if(name==null || name.isEmpty()) {
            userOpCallback.operationFailed(activity.getString(R.string.signup_empty_name));
        }
        else if(pass1.compareTo(pass2)!=0){
            userOpCallback.operationFailed(activity.getString(R.string.signup_passworddidntmatch));
        }
        else{
            AuthSingleton.getMe().createUserWithEmailAndPassword(email, pass1).addOnCompleteListener(activity,
                    task -> { //sign up failed
                        if (!task.isSuccessful()) { // if email is taken, or password is too short
                            task.addOnFailureListener(exception -> userOpCallback.operationFailed(exception.getMessage()));
                        }
                        else{ //sign up succeeded
                            String userId = AuthSingleton.getMe().getCurrentUser().getUid();
                            UserEntity userEntity = new UserEntity(userId, name, (description==null? "": description), null);
                            addUserToDB(userEntity, group);
                            userOpCallback.operationSucceeded();
                        }
                    });
        }
    }

    public void signup(String email, String pass1, String pass2, String name, Long usersAgeInMillis, String description, Uri profileImgPath, SexualGroup group) {
        if(email==null || email.isEmpty()){
            userOpCallback.operationFailed(activity.getString(R.string.empty_email));
        }
        else if(pass1==null || pass2==null || pass1.isEmpty() || pass2.isEmpty()) {
            userOpCallback.operationFailed(activity.getString(R.string.empty_pass));
        }
        else if(pass1.compareTo(pass2)!=0){
            userOpCallback.operationFailed(activity.getString(R.string.signup_passworddidntmatch));
        }
        else if(name==null || name.isEmpty()) {
            userOpCallback.operationFailed(activity.getString(R.string.signup_empty_name));
        }
        else if(usersAgeInMillis==null) {
            userOpCallback.operationFailed(activity.getString(R.string.signup_empty_age));
        }
        else if(profileImgPath==null) {
            userOpCallback.operationFailed(activity.getString(R.string.signup_empty_img));
        }
        else{
            AuthSingleton.getMe().createUserWithEmailAndPassword(email, pass1).addOnCompleteListener(activity,
                    task -> { //sign up failed
                        if (!task.isSuccessful()) { // if email is taken, or password is too short
                            task.addOnFailureListener(exception -> userOpCallback.operationFailed(exception.getMessage()));
                        }
                        else{ //sign up succeeded
                            String userId = AuthSingleton.getMe().getCurrentUser().getUid();
                            UserEntity userEntity = new UserEntity(userId, name, (description==null? "": description), null, usersAgeInMillis, 20);
                            addUserToDB(userEntity, group);
                            saveImgInStorage(userId, group, profileImgPath, imgUrl -> {
                                userEntity.setProfileImageUrl(imgUrl);
                                addUserToDB(userEntity, group);
                                userOpCallback.operationSucceeded();
                            });
                        }
                    });
        }
    }

    private void addUserToDB(UserEntity userEntity, SexualGroup group){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference user = db.child("Users")
                .child(group.toString())
                .child(userEntity.getUserId());
        user.child("name").setValue(userEntity.getName());
        user.child("description").setValue(userEntity.getDescription());
        user.child("usersAgeInMillis").setValue(userEntity.getUsersAgeInMillis());
        user.child("profileImageUrl").setValue(userEntity.getProfileImageUrl());

        db.child("UsersGroup")
                .child(userEntity.getUserId())
                .setValue(group.toString());
    }

    public static void updateUserInDB(UserEntity userEntity, SexualGroup group){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        DatabaseReference user = db.child("Users")
                .child(group.toString())
                .child(userEntity.getUserId());

        user.child("name").setValue(userEntity.getName());
        user.child("description").setValue(userEntity.getDescription());
        user.child("searchDistance").setValue(userEntity.getSearchDistance());

        db.child("UsersGroup")
                .child(userEntity.getUserId())
                .setValue(group.toString());
    }

    public static void updateUserInDB(String userId, SexualGroup group, Map userInfo){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        DatabaseReference user = db.child("Users")
                .child(group.toString())
                .child(userId);

        user.updateChildren(userInfo);

        if(userOpCallback != null){
            userOpCallback.operationSucceeded();
        }
    }

    private interface ImageUploaded{
        void uploadSucceeded(String imgUrl);
    }
    private void saveImgInStorage(String userId, SexualGroup group, Uri profileImgPath, ImageUploaded iu){
        // save image
        if (profileImgPath != null){
            StorageReference ref = FirebaseStorage
                    .getInstance()
                    .getReference()
                    .child("profileImages")
                    .child(userId);

            StorageReference dateRef = ref.child("mainPhoto");
            dateRef
                    .putFile(profileImgPath)
                    .addOnSuccessListener(taskSnapshot -> {
                dateRef.getDownloadUrl().addOnSuccessListener(downloadPhotoUrl -> {
                    Map userInfo = new HashMap();
                    userInfo.put("profileImageUrl", downloadPhotoUrl.toString());
                    UserOperator.updateUserInDB(userId, group, userInfo);
                    iu.uploadSucceeded(downloadPhotoUrl.toString());
                });
            });
        }
    }

    public interface UserDataCallback{
        void UserData(UserEntity userEntity);
    }
    public static void getUserData(String userId, SexualGroup group, UserDataCallback callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(group.toString()).child(userId);
        UserEntity userEntity = new UserEntity().setUserId(userId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userEntity.setName(snapshot.child("name").getValue().toString());
                userEntity.setDescription(snapshot.child("description").getValue().toString());
                // TODO delete
                if ( snapshot.child("profileImageUrl").getValue() != null) {
                    userEntity.setProfileImageUrl(snapshot.child("profileImageUrl").getValue().toString());
                }
                if ( snapshot.child("searchingDistance").getValue(int.class) != null) {
                    userEntity.setSearchDistance(snapshot.child("searchingDistance").getValue(int.class));
                }
                callback.UserData(userEntity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static SexualGroup getSexualGroup(String gender, String pref) throws GendersStringException {
        if(gender==null || !(gender.compareToIgnoreCase("male")==0 || gender.compareToIgnoreCase("female")==0 ) ||
                pref==null || !(pref.compareToIgnoreCase("male")==0 || pref.compareToIgnoreCase("female")==0 || pref.compareToIgnoreCase("both")==0 )){
            throw new GendersStringException();
        }

        gender = gender.substring(0,1).toUpperCase()+gender.substring(1).toLowerCase();
        pref = pref.substring(0,1).toUpperCase()+pref.substring(1).toLowerCase();
        if(pref.compareToIgnoreCase("both")==0)
            pref = "Bisexual";

        SexualGroup group = SexualGroup.valueOf(gender+"_"+pref);
        return group;
    }

    public interface PreferenceGroupsCallback{
        void userPreferenceGroups(SexualGroup sexualGroup);
    }
    public static void getUserGroup(String userId, PreferenceGroupsCallback preferenceGroupsCallback){
//        String userId = AuthSingleton.getMe().getCurrentUser().getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("UsersGroup");
        myRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SexualGroup userSexualGroup = SexualGroup.valueOf(snapshot.getValue().toString());
                preferenceGroupsCallback.userPreferenceGroups(userSexualGroup);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public enum SexualGroup {
        Male_Female, Male_Male, Male_Bisexual, Female_Male, Female_Female, Female_Bisexual;

        public ArrayList<SexualGroup> getPreferenceGroups() {
            ArrayList<SexualGroup> groups = new ArrayList<SexualGroup>();
            switch (this) {
                case Male_Female:
                    groups.add(Female_Male);
                    groups.add(Female_Bisexual);
                    break;
                case Male_Male:
                    groups.add(Male_Male);
                    groups.add(Male_Bisexual);
                    break;
                case Male_Bisexual:
                    groups.add(Male_Male);
                    groups.add(Male_Bisexual);
                    groups.add(Female_Male);
                    groups.add(Female_Bisexual);
                    break;
                case Female_Male:
                    groups.add(Male_Female);
                    groups.add(Male_Bisexual);
                    break;
                case Female_Female:
                    groups.add(Female_Female);
                    groups.add(Female_Bisexual);
                    break;
                case Female_Bisexual:
                    groups.add(Female_Female);
                    groups.add(Female_Bisexual);
                    groups.add(Male_Female);
                    groups.add(Male_Bisexual);
                    break;
            }
            return groups;
        }
    }

    public static class GendersStringException extends Exception{ }

    public interface UserOperatorCallback{
        void operationFailed(String msg);
        void operationSucceeded();
    }


}
