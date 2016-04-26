package com.imzoee.caikid.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.imzoee.caikid.R;
import com.imzoee.caikid.fragment.RecipeFragment;
import com.imzoee.caikid.fragment.OrderFragment;
import com.imzoee.caikid.fragment.MeFragment;

import java.util.ArrayList;

/**
 * Created by zoey on 2016/4/26.
 * used in main activity, to set as the main view pager's adapter
 */
public class MainPagerAdapter extends FragmentPagerAdapter{

    private static MainPagerAdapter instance = null;
    private static ArrayList<Fragment> viewContainer = null;
    private static ArrayList<String> titleContainer = null;

    private MainPagerAdapter(Context context,
                             FragmentManager fm){
        super(fm);

        titleContainer = new ArrayList<>();
        viewContainer = new ArrayList<>();
        titleContainer.add(context.getString(R.string.tab_recipe));
        titleContainer.add(context.getString(R.string.tab_order));
        titleContainer.add(context.getString(R.string.tab_pay));
        viewContainer.add(RecipeFragment.newInstance());
        viewContainer.add(OrderFragment.newInstance("placeholder", "string"));
        viewContainer.add(MeFragment.newInstance("placeholder", "string"));
    }

    public static MainPagerAdapter instantiate(Context context,
                                               FragmentManager fm){

        if(instance == null) {
            instance = new MainPagerAdapter(context, fm);
        }
        return instance;
    }

    //viewpager中的组件数量
    @Override
    public int getCount() {
        return viewContainer.size();
    }

    @Override
    public Fragment getItem(int position) {
        return viewContainer.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleContainer.get(position);
    }


    //滑动切换的时候销毁当前的组件
/*    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        container.removeView(viewContainer.get(position));
    }*/

    //每次滑动的时候生成的组件
/*    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewContainer.get(position));
        return viewContainer.get(position);
    }*/

/*
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
*/


}
