package com.imzoee.caikid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.R;
import com.imzoee.caikid.adapter.RecipeCommentsAdapter;
import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.Comment;
import com.imzoee.caikid.dao.Recipe;
import com.imzoee.caikid.utils.misc.ObservablesFactory;
import com.rey.material.widget.LinearLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {
    public static final String INTENT_KEY_RECIPE = "recipe";

    /* the recipe we show in this activity */
    Recipe recipe = null;
    List<Comment> commentsList = null;

    /* view reference */
    ListView lvContent = null;
    View llHeaderView = null;
    ImageView ivRecipe = null;
    LinearLayout llAddCart = null;
    TextView tvName = null;
    TextView tvDesc = null;
    TextView tvSold = null;
    TextView tvPrice = null;
    RatingBar rbarScore = null;
    TextView tvRating = null;
    TextView tvCommentsNum = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        /* get intent data */
        Intent intent = getIntent();
        if (intent != null){
            String recipeStr = intent.getStringExtra(INTENT_KEY_RECIPE);
            recipe = JSON.parseObject(recipeStr, Recipe.class);
            if(recipe == null){
                /* the activity start this activity do not trans the recipe data */
                return;
            }
        }
        commentsList = new ArrayList<>();

        initView();
        initData();
        initListener();
    }

    public void initView(){
        lvContent = (ListView) findViewById(R.id.lv_recipe_content);
        llHeaderView = LayoutInflater.from(this).inflate(R.layout.header_recipe_detail, lvContent, false);
        ivRecipe = (ImageView) llHeaderView.findViewById(R.id.iv_recipe);
        llAddCart = (LinearLayout) llHeaderView.findViewById(R.id.ll_add_to_cart);
        tvName = (TextView) llHeaderView.findViewById(R.id.tv_recipe_name);
        tvSold = (TextView) llHeaderView.findViewById(R.id.tv_recipe_sales);
        tvDesc = (TextView) llHeaderView.findViewById(R.id.tv_recipe_desc);
        tvPrice = (TextView) llHeaderView.findViewById(R.id.tv_recipe_price);
        rbarScore = (RatingBar) llHeaderView.findViewById(R.id.rbar_recipe_rate);
        tvRating = (TextView) llHeaderView.findViewById(R.id.tv_recipe_rate_numbers);
        tvCommentsNum = (TextView) llHeaderView.findViewById(R.id.tv_comment_number);
    }

    public void initData(){
        lvContent.addHeaderView(llHeaderView);
        RecipeCommentsAdapter commentsAdapter = new RecipeCommentsAdapter(getBaseContext(), commentsList);
        lvContent.setAdapter(commentsAdapter);

        Picasso.with(getBaseContext())
                .load(ConstConv.IMGPATH_URLPREFIX + recipe.getImg_path())
                .fit()
                .centerCrop()
                .into(ivRecipe);
        tvName.setText(recipe.getName());
        tvSold.setText(String.valueOf(recipe.getSales()));
        tvDesc.setText(recipe.getInfo());
        tvPrice.setText(String.valueOf(recipe.getPrice()));
        rbarScore.setRating( recipe.getScore().floatValue() );
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.0");
        tvRating.setText(df.format(recipe.getScore()));
        tvCommentsNum.setText(String.valueOf(recipe.getNumber_comment()));
    }

    public void initListener(){
        llAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApp.getCart().addItem(recipe);
                ObservablesFactory.cartActionObservable(BaseApp.getCart());
                Toast.makeText(getBaseContext(),
                        getString(R.string.msg_recipe_added_to_cart),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

}
