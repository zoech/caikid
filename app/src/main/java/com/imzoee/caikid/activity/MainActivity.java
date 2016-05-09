package com.imzoee.caikid.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Toast;

import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.adapter.MainPagerAdapter;
import com.imzoee.caikid.fragment.RecipeFragment;
import com.imzoee.caikid.fragment.OrderFragment;
import com.imzoee.caikid.fragment.MeFragment;
import com.imzoee.caikid.R;
import com.imzoee.caikid.model.CaikidCart;
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
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        notifyLangChange();
    }

    private Subscriber<CaikidCart> createCartActionSubscriber(){
        return new CartActionSubscriber();
    }

    public static Subscriber<CaikidCart> getCartSubscriber(){
        if(instance == null){
            return null;
        }
        return instance.createCartActionSubscriber();
    }

    private void initData() {
        if(BaseApp.getCart().getItemCount() == 0){
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

    }

    private void initListener(){
        fbCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
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
    private class CartActionSubscriber extends Subscriber<CaikidCart> {
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
        public void onNext(CaikidCart cart) {
            // adapter.notifyDataSetChanged();
            if(cart.getItemCount() == 0){
                /* the cart is empty */
                fbCart.setVisibility(View.GONE);
            } else {
                fbCart.setVisibility(View.VISIBLE);
            }
        }
    }
}
