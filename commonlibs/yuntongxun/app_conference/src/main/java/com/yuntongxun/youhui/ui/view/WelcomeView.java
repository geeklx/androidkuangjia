package com.yuntongxun.youhui.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

/**
 * Created by jorstinchan on 2017/3/26.
 */

public abstract class WelcomeView extends LinearLayout {

    /**
     * @param context
     */
    public WelcomeView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public WelcomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static void doViewAnimation(View view , Animation.AnimationListener l) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0F, 1.0F);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setDuration(300);
        alphaAnimation.setAnimationListener(l);
        view.startAnimation(alphaAnimation);
    }

    public static void doViewAnimation(View view , long duration) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0F, 1.0F);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setDuration(duration);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }
        });
        view.startAnimation(alphaAnimation);
    }

    public abstract void onInitialized();

    public abstract void onPause();

    public abstract void onResume();
}
