package com.android_final_project.firedate.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.android_final_project.firedate.R;
import android.animation.Animator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.android_final_project.firedate.data.AuthSingleton;
import com.android_final_project.firedate.data.UserOperator;

public class Activity_Splash extends AppCompatActivity {


    private UserOperator userOperator;
    private ImageView splash_IMG_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViews();
        showAnimation(1.0F, 1.0F, 0.8f, 0.8f,300, 7);
    }

    private void findViews() {
        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);
    }

    public void showAnimation(float scale1, float alpha1, float scale2, float alpha2, int anim_duration, int count) {
        if (count <= 0) {
            firstAuth();
            return;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        splash_IMG_logo.setScaleY(scale2);
        splash_IMG_logo.setScaleX(scale2);
        splash_IMG_logo.setAlpha(alpha2 );
        splash_IMG_logo.animate()
                .scaleY(scale1)
                .scaleX(scale1)
                .alpha (alpha1)
                .setDuration(anim_duration).setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) { }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        showAnimation(scale2, alpha2, scale1, alpha1, anim_duration, count-1);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) { }

                    @Override
                    public void onAnimationRepeat(Animator animation) { }
                });
    }

    private void firstAuth() {
        AuthSingleton.initAuthSingleton();
        AuthSingleton.setAuthCallback(new AuthSingleton.DefaultAuthCallback(this));
    }

}