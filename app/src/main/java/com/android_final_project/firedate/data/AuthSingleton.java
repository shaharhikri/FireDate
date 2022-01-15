package com.android_final_project.firedate.data;

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

    public static interface AuthCallback{
        public void LoggedIn();
        public void LoggedOut();
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
}
