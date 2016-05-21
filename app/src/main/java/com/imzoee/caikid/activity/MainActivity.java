package com.imzoee.caikid.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.adapter.MainPagerAdapter;
import com.imzoee.caikid.fragment.RecipeFragment;
import com.imzoee.caikid.fragment.OrderFragment;
import com.imzoee.caikid.fragment.MeFragment;
import com.imzoee.caikid.R;
import com.imzoee.caikid.model.CaikidCart;
import com.imzoee.caikid.utils.misc.CaikidAnimation;
import com.rey.material.widget.FloatingActionButton;

import rx.Subscriber;

public class MainActivity extends AppCompatActivity implements RecipeFragment.OnRecipeFragmentListener,
                                                                OrderFragment.OnOrderFragmentListener,
                                                                MeFragment.OnMeFragmentListener {

    private static MainActivity instance = null;

    ViewPager pager = null;
    MainPagerAdapter mainPagerAdapter = null;
    TabLayout tabLayout = null;
    FloatingActionButton fbCart = null;

    /* this field is need to use in the drag listener */
    View rlTop = null;


    public String TAG = "tag";                        // debug used


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        setContentView(R.layout.activity_main);

        initView();
        initData();
        initListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pager = null;
        mainPagerAdapter = null;
        tabLayout = null;
        fbCart = null;
        rlTop = null;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        notifyLangChange();
    }

    private Subscriber<String> createCartActionSubscriber(){
        return new CartActionSubscriber();
    }

    public static Subscriber<String> getCartSubscriber(){
        if(instance == null){
            return null;
        }
        return instance.createCartActionSubscriber();
    }

    private void initData() {
        if(BaseApp.getCart().getTotalCount() == 0){
            fbCart.setVisibility(View.GONE);
        } else {
            fbCart.setVisibility(View.VISIBLE);
        }

        initViewPager();
    }

    private void initView() {

        pager = (ViewPager) this.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) this.findViewById(R.id.tl_tab);
        fbCart = (FloatingActionButton) this.findViewById(R.id.fb_cart);
        rlTop = this.findViewById(R.id.rl_top);

    }

    private void initListener(){
        fbCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        fbCart.setTag("Cart View");
        fbCart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());

                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData dragData = new ClipData(v.getTag().toString(),
                        mimeTypes, item);

                // Instantiates the drag shadow builder.
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(fbCart);

                // Starts the drag
                v.startDrag(dragData,  // the data to be dragged
                        myShadow,  // the drag shadow builder
                        null,      // no need to use local data
                        0          // flags (not currently used, set to 0)
                );
                return true;
            }
        });

        final String msg = "########################";
        rlTop.setOnDragListener( new View.OnDragListener(){
            @Override
            public boolean onDrag(View v,  DragEvent event){

                switch(event.getAction())
                {
                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                        return true;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED :

                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION  :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED   :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");
                        return true;
                    case DragEvent.ACTION_DROP:
                        Log.d(msg, "ACTION_DROP event");
                        fbCart.setX(event.getX() - fbCart.getWidth()/2);
                        fbCart.setY(event.getY() - fbCart.getHeight()/2);
                        return true;
                    default: break;
                }
                return false;
            }
        });
/*
        final Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        fbCart.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                fbCart.setX(event.getX() - fbCart.getWidth()/2);
                fbCart.setY(event.getY() - fbCart.getHeight()/2 - rect.top);
                return false;
            }
        });
        */

    }

    private void initViewPager() {

        mainPagerAdapter = MainPagerAdapter.instantiate(this, getSupportFragmentManager());
        pager.setAdapter(mainPagerAdapter);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
            @Override
            public void onPageScrollStateChanged(int arg0) {
                Log.d(TAG, "--------changed:" + arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                Log.d(TAG, "-------scrolled arg0:" + arg0);
                Log.d(TAG, "-------scrolled arg1:" + arg1);
                Log.d(TAG, "-------scrolled arg2:" + arg2);
            }

            @Override
            public void onPageSelected(int pos) {
                Log.d(TAG, "------selected:" + pos);
            }
        });

        tabLayout.setupWithViewPager(pager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tabDownLine));
        tabLayout.setTabTextColors(getResources().getColor(R.color.whiteUnselectd),
                getResources().getColor(R.color.whiteSelect));
    }

    private void notifyLangChange(){

        mainPagerAdapter.notifyLangChange();

    }

    /*
     * below are the override methods of the interfaces in the 3 fragement class,
     * used to perform action from those fragments to this activity
     */
    @Override
    public void onRecipeInteraction(Uri uri) {
        /*
         * react with recipe fragment
         */
    }

    @Override
    public void onOrderInteraction(Uri uri) {

    }

    @Override
    public void onMeInteraction(Uri uri) {

    }

    /* rxjava's subscriber */
    private class CartActionSubscriber extends Subscriber<String> {
        @Override
        public void onCompleted() {
            if(!isUnsubscribed()) {
                this.unsubscribe();
            }
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getBaseContext(), "error in observable,Mefragment", Toast.LENGTH_LONG)
                    .show();
            Log.i("----------------------", e.getMessage());
        }

        @Override
        public void onNext(String opt) {
            // adapter.notifyDataSetChanged();
            CaikidCart cart = BaseApp.getCart();

            if(opt.equals(CaikidCart.OBSERVE_ADDITEM)){
                if (cart.getTotalCount() == 1){
                    fbCart.setVisibility(View.VISIBLE);
                    CaikidAnimation.bounceInAnimate(fbCart).start();
                } else {
                    CaikidAnimation.tadaAnimator(fbCart).start();
                }
            } else if (opt.equals(CaikidCart.OBSERVE_DELITEM)){
                if (cart.getTotalCount() == 0){
                    AnimatorSet animatorSet = CaikidAnimation.zoomOutAnimator(fbCart);
                    animatorSet.addListener(

                            new Animator.AnimatorListener() {

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    fbCart.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationStart(Animator animation) {}
                                @Override
                                public void onAnimationCancel(Animator animation) {}
                                @Override
                                public void onAnimationRepeat(Animator animation) {}

                            });

                    animatorSet.start();

                } else {
                    CaikidAnimation.tadaAnimator(fbCart).start();
                }
            }

        }
    }

}
