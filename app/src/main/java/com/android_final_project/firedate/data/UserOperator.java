package com.android_final_project.firedate.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android_final_project.firedate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserOperator {

    // Male_Female : { Female_male},
    // Female_Bisexual : { Female_Bisexul, Femal_Female, Male_Bisexual, Male_Female }
//    final public static Map<SexualGroup, ArrayList<SexualGroup>> groupMap = new HashMap<SexualGroup, ArrayList<SexualGroup>>()
//            .put(SexualGroup.Male_Male,new ArrayList<SexualGroup>(SexualGroup.Male_Male,))


    private FirebaseAuth auth;
    AppCompatActivity activity;
    private UserOperatorCallback callback;

    public UserOperator(AppCompatActivity activity, UserOperatorCallback callback){
        this.callback = callback;
        this.activity = activity;
        initFirebaseAuth();
    }

    private void initFirebaseAuth(){
        auth = FirebaseAuth.getInstance();

        auth.addAuthStateListener(firebaseAuth -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null){
                callback.alreadyLoggedin();
            }
        });
    }

    public void logout(){
        auth.signOut();
        callback.afterLogout();
    }

    public void login(String email, String pass){
        if(email==null || email.isEmpty()){
            callback.operationFailed(activity.getString(R.string.empty_email));
        }
        else if(pass==null || pass.isEmpty()) {
            callback.operationFailed(activity.getString(R.string.empty_pass));
        }
        else{
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(activity,
                    task -> {
                        if (!task.isSuccessful()) { // if email is taken, or password is too short
                            task.addOnFailureListener(exception -> callback.operationFailed(exception.getMessage()));
                        }
                    });
        }
    }

    public void signup(String email, String pass1, String pass2, String name, String description, SexualGroup group){
        if(email==null || email.isEmpty()){
            callback.operationFailed(activity.getString(R.string.empty_email));
        }
        else if(pass1==null || pass2==null || pass1.isEmpty() || pass2.isEmpty()) {
            callback.operationFailed(activity.getString(R.string.empty_pass));
        }
        else if(name==null || name.isEmpty()) {
            callback.operationFailed(activity.getString(R.string.signup_empty_name));
        }
        else if(pass1.compareTo(pass2)!=0){
            callback.operationFailed(activity.getString(R.string.signup_passworddidntmatch));
        }
        else{
            auth.createUserWithEmailAndPassword(email, pass1).addOnCompleteListener(activity,
                    task -> { //sign up failed
                        if (!task.isSuccessful()) { // if email is taken, or password is too short
                            task.addOnFailureListener(exception -> callback.operationFailed(exception.getMessage()));
                        }
                        else{ //sign up succeeded
                            String userId = auth.getCurrentUser().getUid();
                            addUserToDB(userId, name, (description==null? "": description), group);
                            callback.operationSucceeded();
                        }
                    });
        }
    }

    private void addUserToDB(String userId, String name, String description, SexualGroup group){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("Users")
                .child(group.toString())
                .child(userId)
                .setValue(new UserEntity(name, description));

        db.child("UsersGroup")
                .child(userId)
                .setValue(group.toString());
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

    public static ArrayList<SexualGroup> getUserPreferenceGroups(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
//        String group = db.child("UsersGroup").child(userId).get().getResult().getKey();
//        Log.d("pttt", "getUserPreferenceGroups: "+group);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("UsersGroup");
        myRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String group = snapshot.getValue().toString();
                Log.d("pttt", "onDataChange: " + group);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return null;
    }

    public enum SexualGroup{
        Male_Female, Male_Male, Male_Bisexual, Female_Male, Female_Female, Female_Bisexual;

        public ArrayList<SexualGroup> getPreferenceGroups(){
            ArrayList<SexualGroup> groups = new ArrayList<SexualGroup>();
            switch (this){
                case Male_Female:
                    groups.add(Female_Male);
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
        public void operationFailed(String msg);
        public void operationSucceeded();
        public void alreadyLoggedin();
        public void afterLogout();
    }

}