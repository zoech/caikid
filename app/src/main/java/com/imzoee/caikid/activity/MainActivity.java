package com.imzoee.caikid.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.design.widget.TabLayout;

import com.imzoee.caikid.adapter.MainPagerAdapter;
import com.imzoee.caikid.fragment.RecipeFragment;
import com.imzoee.caikid.fragment.OrderFragment;
import com.imzoee.caikid.fragment.MeFragment;
import com.imzoee.caikid.R;

public class MainActivity extends AppCompatActivity implements RecipeFragment.OnRecipeFragmentListener,
                                                                OrderFragment.OnOrderFragmentListener,
                                                                MeFragment.OnMeFragmentListener {

    ViewPager pager = null;
    TabLayout tabLayout = null;
    public String TAG = "tag";                        // debug used


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initView();

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

    /*
             * init the data for this activity
             */
    private void initData() {

    }


    /*
     * init the view for this activity
     *
     * include
     * 1. viewpager
     * 2. tablayout
     *
     */
    private void initView() {
        setContentView(R.layout.activity_main);

        pager = (ViewPager) this.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) this.findViewById(R.id.tl_tab);

        initViewPager();
    }

    private void initViewPager() {

        pager.setAdapter(MainPagerAdapter.instantiate(this, getSupportFragmentManager()));

        pager.addOnPageChangeListener(new OnPageChangeListener() {
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
            public void onPageSelected(int arg0) {
                Log.d(TAG, "------selected:" + arg0);
            }
        });

        tabLayout.setupWithViewPager(pager);
    }

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
}
