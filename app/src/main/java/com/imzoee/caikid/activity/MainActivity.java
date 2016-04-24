 package com.imzoee.caikid.activity;

import java.util.ArrayList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.util.Log;
import android.content.Intent;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

//import com.imzoee.caikid.dao.UserDao;
//import com.imzoee.caikid.dao.DaoMaster;
//import com.imzoee.caikid.dao.DaoSession;
import com.imzoee.caikid.adapter.RecipeAdapter;
import com.imzoee.caikid.R;

 public class MainActivity extends AppCompatActivity {

     ViewPager pager = null;
     PagerTabStrip tabStrip = null;
     ArrayList<View> viewContainter = new ArrayList<View>();
     ArrayList<String> titleContainer = new ArrayList<String>();
     public String TAG = "tag";                        // debug used


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initView();

    }

     /*
      * init the data for this activity
      */
     private void initData(){

     }


     /*
      * init the view for this activity
      *
      * include
      * 1. viewpager
      * 2. tab strip
      * 3.
      */
     private void initView(){
         setContentView(R.layout.activity_main);

         //ViewGroup root = (ViewGroup)this.findViewById(android.R.id.content);

         pager = (ViewPager) this.findViewById(R.id.viewpager);
         tabStrip = (PagerTabStrip) this.findViewById(R.id.tabstrip);

         tabStrip.setDrawFullUnderline(false);//取消tab下面的长横线
         tabStrip.setBackgroundColor(this.getResources().getColor(R.color.mainBg));//设置tab的背景色
         tabStrip.setTabIndicatorColor(this.getResources().getColor(R.color.tabDownLine));//设置当前tab页签的下划线颜色
         tabStrip.setTextSpacing(200);

         initViewPager();
     }

     private void initViewPager(){
         View view1 = LayoutInflater.from(this).inflate(R.layout.tab_main_recipe, null);
         View view2 = LayoutInflater.from(this).inflate(R.layout.tab_main_order, null);
         View view3 = LayoutInflater.from(this).inflate(R.layout.tab_main_me, null);


        /*
        View view1 = LayoutInflater.from(this).inflate(R.layout.tab_main_recipe,root ,false);
        View view2 = LayoutInflater.from(this).inflate(R.layout.tab_main_order, root, false);
        View view3 = LayoutInflater.from(this).inflate(R.layout.tab_main_me, root, false);
        */


         //viewpager开始添加view
         viewContainter.add(view1);
         viewContainter.add(view2);
         viewContainter.add(view3);
         //页签项
         titleContainer.add(getString(R.string.tab_recipe));
         titleContainer.add(getString(R.string.tab_order));
         titleContainer.add(getString(R.string.tab_pay));


         /*----------------------------------- pager adapter ----------------------------------------------*/
         pager.setAdapter(new PagerAdapter() {

             //viewpager中的组件数量
             @Override
             public int getCount() {
                 return viewContainter.size();
             }

             //滑动切换的时候销毁当前的组件
             @Override
             public void destroyItem(ViewGroup container, int position,
                                     Object object) {
                 ((ViewPager) container).removeView(viewContainter.get(position));
             }

             //每次滑动的时候生成的组件
             @Override
             public Object instantiateItem(ViewGroup container, int position) {
                 ((ViewPager) container).addView(viewContainter.get(position));
                 return viewContainter.get(position);
             }

             @Override
             public boolean isViewFromObject(View arg0, Object arg1) {
                 return arg0 == arg1;
             }

             @Override
             public int getItemPosition(Object object) {
                 return super.getItemPosition(object);
             }

             @Override
             public CharSequence getPageTitle(int position) {
                 return titleContainer.get(position);
             }
         });

         /*---------------------------------- pager listener ----------------------------------------------*/
         pager.setOnPageChangeListener(new OnPageChangeListener() {
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

         /*---------------------------------- pager content  ----------------------------------------------*/
         ListView listView = (ListView) view1.findViewById(R.id.lv_recipe);
         RecipeAdapter adapter= new RecipeAdapter(this);
         listView.setAdapter(adapter);
         listView.setOnItemClickListener(new OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
                 startActivity(intent);
             }
         });

         ( (Button) view3.findViewById(R.id.bt_plogin)).setOnClickListener( new OnClickListener(){
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                 startActivity(intent);
             }
         });
     }


 }
