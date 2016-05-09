package com.imzoee.caikid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.R;
import com.imzoee.caikid.dao.Recipe;
import com.imzoee.caikid.utils.misc.ObservablesFactory;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

public class RecipeDetailActivity extends AppCompatActivity {
    public static final String INTENT_KEY_RECIPE = "recipe";

    /* the recipe we show in this activity */
    Recipe recipe = null;

    /* view reference */
    ImageView ivRecipe = null;
    Button btAddCart = null;
    TextView tvName = null;
    TextView tvDesc = null;
    TextView tvSold = null;
    TextView tvPrice = null;

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

        initView();
        initData();
        initListener();
    }

    public void initView(){
        ivRecipe = (ImageView) findViewById(R.id.iv_recipe);
        btAddCart = (Button) findViewById(R.id.bt_add_to_cart);
        tvName = (TextView) findViewById(R.id.tv_recipe_name);
        tvSold = (TextView) findViewById(R.id.tv_recipe_sales);
        tvDesc = (TextView) findViewById(R.id.tv_recipe_desc);
        tvPrice = (TextView) findViewById(R.id.tv_recipe_price);
    }

    public void initData(){
        Picasso.with(getBaseContext())
                .load(recipe.getImg_path())
                .fit()
                .centerCrop()
                .into(ivRecipe);
        tvName.setText(recipe.getName());
        tvSold.setText(String.valueOf(recipe.getSales()));
        tvDesc.setText(recipe.getDesc());
        tvPrice.setText(String.valueOf(recipe.getPrice()));
    }

    public void initListener(){
        btAddCart.setOnClickListener(new View.OnClickListener() {
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
