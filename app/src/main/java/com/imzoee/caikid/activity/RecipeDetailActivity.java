package com.imzoee.caikid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.imzoee.caikid.model.CaikidCart;
import com.imzoee.caikid.utils.api.HttpClient;
import com.imzoee.caikid.utils.api.RecipeApiInterface;
import com.imzoee.caikid.utils.misc.ObservablesFactory;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.LinearLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends AppCompatActivity {
    public static final String INTENT_KEY_RECIPE = "recipe";

    /* the recipe we show in this activity */
    Recipe recipe = null;
    List<Comment> commentsList = null;

    RecipeCommentsAdapter commentsAdapter = null;

    private int commentPage = 0;

    /* view reference */
    ListView lvContent = null;
    View llHeaderView = null;
    View llFooterView = null;
    ImageView ivRecipe = null;
    FloatingActionButton fbBack = null;
    LinearLayout llAddCart = null;
    TextView tvName = null;
    TextView tvDesc = null;
    TextView tvSold = null;
    TextView tvPrice = null;
    RatingBar rbarScore = null;
    TextView tvRating = null;
    TextView tvCommentsNum = null;

    LinearLayout llFrameComment = null;
    LinearLayout llFrameNothing = null;

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
        commentPage = 0;

        initView();
        initData();
        initListener();
    }

    public void initView(){
        lvContent = (ListView) findViewById(R.id.lv_recipe_content);
        llHeaderView = LayoutInflater.from(this).inflate(R.layout.header_recipe_detail, lvContent, false);
        llFooterView = LayoutInflater.from(this).inflate(R.layout.footer_recipe_detail, lvContent, false);
        ivRecipe = (ImageView) llHeaderView.findViewById(R.id.iv_recipe);
        fbBack = (FloatingActionButton) llHeaderView.findViewById(R.id.fb_back);
        llAddCart = (LinearLayout) llHeaderView.findViewById(R.id.ll_add_to_cart);
        tvName = (TextView) llHeaderView.findViewById(R.id.tv_recipe_name);
        tvSold = (TextView) llHeaderView.findViewById(R.id.tv_recipe_sales);
        tvDesc = (TextView) llHeaderView.findViewById(R.id.tv_recipe_desc);
        tvPrice = (TextView) llHeaderView.findViewById(R.id.tv_recipe_price);
        rbarScore = (RatingBar) llHeaderView.findViewById(R.id.rbar_recipe_rate);
        tvRating = (TextView) llHeaderView.findViewById(R.id.tv_recipe_rate_numbers);
        tvCommentsNum = (TextView) llHeaderView.findViewById(R.id.tv_comment_number);

        llFrameComment = (LinearLayout) llHeaderView.findViewById(R.id.anchor_comment);
        llFrameNothing = (LinearLayout) llHeaderView.findViewById(R.id.ll_frame_no_comment);

    }

    public void initData(){
        lvContent.addHeaderView(llHeaderView);
        lvContent.addFooterView(llFooterView);
        llFooterView.findViewById(R.id.tv_frame_no_more).setVisibility(View.GONE);
        commentsAdapter = new RecipeCommentsAdapter(getBaseContext(), commentsList);
        lvContent.setAdapter(commentsAdapter);
        getNewPageComment();
        llFooterView.findViewById(R.id.ll_load_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewPageComment();
            }
        });

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

        if(recipe.getNumber_comment() == 0){
            llFrameComment.setVisibility(View.GONE);
            llFrameNothing.setVisibility(View.VISIBLE);
            llFooterView.setVisibility(View.GONE);
        } else {
            llFrameComment.setVisibility(View.VISIBLE);
            llFrameNothing.setVisibility(View.GONE);
        }
    }

    public void initListener(){
        llAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApp.getCart().addItem(recipe);
                ObservablesFactory.cartActionObservable(CaikidCart.OBSERVE_ADDITEM);
                Toast.makeText(getBaseContext(),
                        getString(R.string.msg_recipe_added_to_cart),
                        Toast.LENGTH_LONG).show();
            }
        });

        fbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*                Paint paint = tvComment.getPaint();
                float width = paint.measureText(tvComment.getText().toString());
                float space = tvComment.getWidth() - tvComment.getPaddingLeft() - tvComment.getPaddingRight();*/
                if (position < lvContent.getHeaderViewsCount() || position >= lvContent.getHeaderViewsCount() + commentsAdapter.getCount()){
                    return;
                }
                TextView tvComment = (TextView) view.findViewById(R.id.tv_comment_content);
                if(tvComment.getLineCount() > 2){
                    tvComment.setMaxLines(2);
                } else {
                    tvComment.setMaxLines(100);
                }
            }
        });
    }

    /**
     * Get a new page of comments of this recipe. The page is indicated by the commentPage
     * field of this activity.
     */
    private void getNewPageComment(){
        HttpClient httpClient = BaseApp.getHttpClient();
        RecipeApiInterface i = httpClient.getRecipeApiInterface();
        Call<List<Comment>> getCommentList = i.getCommentList(recipe.getId(), commentPage);
        getCommentList.enqueue(new Callback<List<Comment>>(){
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                Headers headers = response.headers();
                String status = headers.get(ConstConv.HEADKEY_RESPONSTATUS);

                if (status == null){
                    Toast.makeText(getBaseContext(),
                            getString(R.string.msg_status_header_null),
                            Toast.LENGTH_LONG).show();
                } else if (status.equals(ConstConv.RET_STATUS_OK)){

                    List<Comment> comments = response.body();
                    commentsList.addAll(comments);
                    //commentsAdapter.setCommentList(commentsList);
                    commentsAdapter.notifyDataSetChanged();

                        /* don't forget to increase the page number */
                    commentPage++;

                } else if (status.equals(ConstConv.RET_STATUS_NOMORE_CONTENTS)){
                    llFooterView.findViewById(R.id.tv_frame_no_more).setVisibility(View.VISIBLE);
                    llFooterView.findViewById(R.id.ll_load_more).setVisibility(View.GONE);
                }
                else if (status.equals(ConstConv.RET_STATUS_TIMEOUT)){
                    Toast.makeText(getBaseContext(),
                            getString(R.string.msg_time_out),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(),
                            getString(R.string.msg_unknown_ret_status) + status,
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                if(t instanceof java.net.ConnectException){
                    CharSequence msg = getString(R.string.msg_connect_error);
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("+++++++++++++++++++++++", t.toString());
                    t.printStackTrace();
                }
            }
        });
    }
}
