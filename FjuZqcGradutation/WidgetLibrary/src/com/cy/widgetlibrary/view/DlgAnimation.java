package com.cy.widgetlibrary.view;


import android.view.View;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by ejianshen on 15/10/30.
 */
public class DlgAnimation {
    private final static int mDuration=1000;

    public static AnimatorSet animatorFadeIn(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration)
        );
        return animatorSet;
    }
    public static AnimatorSet animatorFadeOut(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(3*mDuration)
        );
        return animatorSet;
    }
    public static AnimatorSet animatorFall(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 2, 1.5f, 1).setDuration(mDuration),
                ObjectAnimator.ofFloat(view,"scaleY",2,1.5f,1).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2)
        );
        return animatorSet;
    }
    public static AnimatorSet animatorFlipH(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "rotationY", -90, 0).setDuration(mDuration)
        );
        return  animatorSet;
    }
    public static AnimatorSet animatorFlipV(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "rotationX", -90, 0).setDuration(mDuration)
        );
        return  animatorSet;
    }
    public static AnimatorSet animatorNewPaper(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "rotation", 1080,720,360,0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2),
                ObjectAnimator.ofFloat(view, "scaleX", 0.1f, 0.5f, 1).setDuration(mDuration),
                ObjectAnimator.ofFloat(view,"scaleY",0.1f,0.5f,1).setDuration(mDuration)
        );
        return animatorSet;
    }
    public static AnimatorSet animatorRotateB(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "rotationX",90, 0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "translationY", 300, 0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2)
        );
        return animatorSet;
    }
    public static AnimatorSet animatorRotateL(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "rotationY", 90, 0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "translationX", -300, 0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2)
        );
        return animatorSet;
    }
    public static AnimatorSet animatorShake(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, .10f, -25, .26f, 25,.42f, -25, .58f, 25,.74f,-25,.90f,1,0).setDuration(mDuration)
        );
        return animatorSet;
    }
    public static AnimatorSet animatorSideFall(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 2, 1.5f, 1).setDuration(mDuration),
                ObjectAnimator.ofFloat(view,"scaleY",2,1.5f,1).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "rotation", 25,0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "translationX",80,0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2)
        );
        return animatorSet;
    }
    public static AnimatorSet animatorSideBottom(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "translationY", 300, 0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2)
        );
        return animatorSet;
    }
    public static AnimatorSet animatorSideLeft(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", -300, 0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2)
        );
        return animatorSet;
    }
    public static AnimatorSet animatorSideRight(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "translationX",300,0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2)
        );
        return animatorSet;
    }
    public static AnimatorSet animatorTop(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "translationY", -300, 0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2)
        );
        return animatorSet;
    }
    public static AnimatorSet animatorSlit(View view){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "rotationY", 90,88,88,45,0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0,0.4f,0.8f, 1).setDuration(mDuration*3/2),
                ObjectAnimator.ofFloat(view, "scaleX", 0,0.5f, 0.9f, 0.9f, 1).setDuration(mDuration),
                ObjectAnimator.ofFloat(view,"scaleY",0,0.5f, 0.9f, 0.9f, 1).setDuration(mDuration)
        );
        return animatorSet;
    }

}
