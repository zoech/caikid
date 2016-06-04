package com.imzoee.caikid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.R;
import com.imzoee.caikid.adapter.OrderItemAdapter;
import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.Order;
import com.imzoee.caikid.dao.Recipe;
import com.imzoee.caikid.model.OrderItem;
import com.imzoee.caikid.utils.api.FuncApiInterface;
import com.imzoee.caikid.utils.api.HttpClient;
import com.imzoee.caikid.utils.api.RecipeApiInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zoey on 2016/6/4.
 *
 * OrderDetailActivity. When user login, he can change to the order fragment,
 * and click one order, then we come to this activity.
 */
public class OrderDetailActivity extends AppCompatActivity {
    public static final String INTENT_KEY_ORDER = "order";

    /* the recipe we show in this activity */
    private Order order = null;
    private List<OrderItem> orderItems = null;
    private List<OrderTerm> orderTerms = null;

    private OrderItemAdapter orderItemAdapter = null;

    /* view reference */
    private ListView lvOrderItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        /* get intent data */
        Intent intent = getIntent();
        if (intent != null){
            String recipeStr = intent.getStringExtra(INTENT_KEY_ORDER);
            order = JSON.parseObject(recipeStr, Order.class);
            if(order == null){
                /* the activity start this activity do not trans the recipe data */
                return;
            }
        }
        orderItems = new ArrayList<>();
        orderTerms = new ArrayList<>();

        initView();
        initData();
        initLogic();

        getOrderItems();
    }

    public void initView(){
        lvOrderItem = (ListView) findViewById(R.id.lv_order_content);
    }

    public void initData(){
        orderItemAdapter = new OrderItemAdapter(getBaseContext());
        orderItemAdapter.setOrderList(orderTerms);
        lvOrderItem.setAdapter(orderItemAdapter);
    }

    public void initLogic(){

    }

    /**
     * Get a list of the orderItem for this order,
     * The orderItem contain a recipe id, and an
     * amount field indicated how many copies is
     * bought.
     */
    private void getOrderItems(){
        HttpClient httpClient = BaseApp.getHttpClient();
        FuncApiInterface i = httpClient.getFuncApiInterface();
        Call<List<OrderItem>> getOrderItems = i.getOrderItems(order.getOrderId());
        getOrderItems.enqueue(new Callback<List<OrderItem>>(){
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                Headers headers = response.headers();
                String status = headers.get(ConstConv.HEADKEY_RESPONSTATUS);

                if (status == null){
                    Toast.makeText(getBaseContext(),
                            getString(R.string.msg_status_header_null),
                            Toast.LENGTH_LONG).show();
                } else if (status.equals(ConstConv.RET_STATUS_OK)){

                    orderItems = response.body();
                    Iterator<OrderItem> iterator = orderItems.iterator();
                    while(iterator.hasNext()){
                        OrderItem item = iterator.next();
                        OrderTerm term = new OrderTerm();
                        term.setOrderItem(item);
                        orderTerms.add(term);
                        term.retriveRecipe();
                    }

                } else if (status.equals(ConstConv.RET_STATUS_NOMORE_CONTENTS)){

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
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
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
/*
    public class OrderItem{
        private int productId;
        private int amount;
        private boolean isComment;

        public int getProductId(){
            return productId;
        }

        public void setProductId(int id){
            this.productId = id;
        }

        public int getAmount(){
            return this.amount;
        }

        public void setAmount(int amount){
            this.amount = amount;
        }

        public boolean getIsComment(){
            return this.isComment;
        }

        public void setIsComment(boolean isComment){
            this.isComment = isComment;
        }
    }
    */

    public class OrderTerm{
        private OrderItem orderItem;
        private Recipe recipe;

        public int getProductId(){
            return orderItem.getProductId();
        }

        public void setProductId(int id){
            this.orderItem.setProductId(id);
        }

        public int getAmount(){
            return this.orderItem.getAmount();
        }

        public void setAmount(int amount){
            this.orderItem.setAmount(amount);
        }

        public boolean getIsComment(){
            return this.orderItem.getIsComment();
        }

        public void setIsComment(boolean isComment){
            this.orderItem.setIsComment(isComment);
        }


        public OrderItem getOrderItem(){
            return this.orderItem;
        }

        public void setOrderItem(OrderItem item){
            this.orderItem = item;
        }

        public Recipe getRecipe(){
            return this.recipe;
        }

        public void setRecipe(Recipe recipe){
            this.recipe = recipe;
        }

        public void retriveRecipe(){
            HttpClient httpClient = BaseApp.getHttpClient();
            RecipeApiInterface i = httpClient.getRecipeApiInterface();
            Call<Recipe> getOrderItems = i.getRecipeById(this.getProductId());
            getOrderItems.enqueue(new Callback<Recipe>(){
                @Override
                public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                    Headers headers = response.headers();
                    String status = headers.get(ConstConv.HEADKEY_RESPONSTATUS);

                    if (status == null){
                        Toast.makeText(getBaseContext(),
                                getString(R.string.msg_status_header_null),
                                Toast.LENGTH_LONG).show();
                    } else if (status.equals(ConstConv.RET_STATUS_OK)){

                        Recipe recipe = response.body();
                        setRecipe(recipe);
                        orderItemAdapter.notifyDataSetChanged();

                    } else if (status.equals(ConstConv.RET_STATUS_NOMORE_CONTENTS)){

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
                public void onFailure(Call<Recipe> call, Throwable t) {
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
}