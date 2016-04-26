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

    public static final int INDEX_RECIPE = 0;
    public static final int INDEX_ORDER = 1;
    public static final int INDEX_ME = 2;

    private Context context;
    private ArrayList<Fragment> fragments = null;
    private ArrayList<String> titles = null;

    private MainPagerAdapter(Context context,
                             FragmentManager fm){
        super(fm);

        this.context = context;
        titles = new ArrayList<>();
        fragments = new ArrayList<>();
        titles.add(context.getString(R.string.tab_recipe));
        titles.add(context.getString(R.string.tab_order));
        titles.add(context.getString(R.string.tab_me));
        fragments.add(RecipeFragment.newInstance());
        fragments.add(OrderFragment.newInstance("placeholder", "string"));
        fragments.add(MeFragment.newInstance("placeholder", "string"));
    }

    public static MainPagerAdapter instantiate(Context context,
                                               FragmentManager fm){

        return new MainPagerAdapter(context, fm);
    }

    /* set the title from outside */
    public void setTabTitle(int pos, String title){
        titles.set(pos, title);
    }

    /*
     * while the system's default language config is change,
     * call this from outside to reset the whole acitivity's
     * language.
     */
    public void notifyLangChange(){
        /* change tab title's text */
        titles.set(INDEX_RECIPE, context.getResources().getString(R.string.tab_recipe));
        titles.set(INDEX_ORDER, context.getResources().getString(R.string.tab_order));
        titles.set(INDEX_ME, context.getResources().getString(R.string.tab_me));

        /* chage 3 fragments' text */


        notifyDataSetChanged();

    }

    //viewpager中的组件数量
    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
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
