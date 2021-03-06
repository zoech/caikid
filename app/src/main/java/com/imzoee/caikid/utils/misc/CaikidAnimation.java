package com.imzoee.caikid.utils.misc;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by zoey on 2016/5/15.
 *
 * The animation utils for our app
 */
public class CaikidAnimation {
    public static AnimatorSet tadaAnimator(View target){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(target,"scaleX",1,0.9f,0.9f,1.1f,1.1f,1.1f,1.1f,1.1f,1.1f,1);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(target,"scaleY",1,0.9f,0.9f,1.1f,1.1f,1.1f,1.1f,1.1f,1.1f,1);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(target,"rotation",0 ,-3 , -3, 3, -3, 3, -3,3,-3,0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator1).with(animator2);
        animatorSet.play(animator1).with(animator3);
        animatorSet.setDuration(1000);
        return animatorSet;
    }

    public static ObjectAnimator swingAnimator(View target){
        return ObjectAnimator.ofFloat(target, "rotation", 0, 10, -10, 6, -6, 3, -3, 0).setDuration(1000);
    }

    public static AnimatorSet bounceInAnimate(View target){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(target,"alpha",0,1, 1 ,1);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(target,"scaleX",0.3f,1.05f,0.9f,1);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(target,"scaleY",0.3f,1.05f,0.9f,1);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator1).with(animator2);
        animatorSet.play(animator1).with(animator3);
        animatorSet.setDuration(1000);
        return animatorSet;
    }

    public static AnimatorSet zoomOutAnimator(View target){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(target,"alpha",1,0,0);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(target,"scaleX",1,0.3f,0);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(target,"scaleY",1,0.3f,0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator1).with(animator2);
        animatorSet.play(animator1).with(animator3);
        animatorSet.setDuration(1000);
        return animatorSet;
    }
}
