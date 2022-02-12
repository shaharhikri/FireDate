package com.android_final_project.firedate.data;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.android_final_project.firedate.activities.Activity_Entry;
import com.android_final_project.firedate.activities.Activity_Swipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthSingleton {
    // Auth -Singleton
    private static FirebaseAuth auth;
    private static AuthCallbackHolder authCallback_holder;

    public static void initAuthSingleton(){
        auth = FirebaseAuth.getInstance();
        authCallback_holder = new AuthCallbackHolder(new AuthCallback() {
            @Override
            public void LoggedIn() { }

            @Override
            public void LoggedOut() { }
        });

        auth.addAuthStateListener(firebaseAuth -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null){
                authCallback_holder.get().LoggedIn();
            }else{
                authCallback_holder.get().LoggedOut();
            }
        });
    }

    public static void setAuthCallback(AuthCallback callback){
        authCallback_holder.set(callback);
    }

    public static FirebaseAuth getMe(){
        return auth;
    }

    private static class AuthCallbackHolder{
        private AuthCallback my_callback;

        public AuthCallbackHolder(AuthCallback callback){
            my_callback = callback;
        }

        public AuthCallback get(){
            return my_callback;
        }

        public void set(AuthCallback callback){
            my_callback = callback;
        }
    }

    //Callback classes
    public interface AuthCallback{
        void LoggedIn();
        void LoggedOut();
    }

    public static class DefaultAuthCallback implements AuthCallback{
        private AppCompatActivity from_activity;
        public DefaultAuthCallback(AppCompatActivity from_activity){
            this.from_activity = from_activity;
        }

        @Override
        public void LoggedIn() {
            Intent intent = new Intent(from_activity, Activity_Swipe.class);
            from_activity.startActivity(intent);
            from_activity.finish();
        }

        @Override
        public void LoggedOut() {
            Intent intent = new Intent(from_activity, Activity_Entry.class);
            from_activity.startActivity(intent);
            from_activity.finish();
        }
    }
}
