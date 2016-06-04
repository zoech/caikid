package com.imzoee.caikid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
import com.imzoee.caikid.utils.misc.ObservablesFactory;
import com.rey.material.widget.Button;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;

/**
 * Created by zoey on 2016/6/4.
 *
 * OrderDetailActivity. When user login, he can change to the order fragment,
 * and click one order, then we come to this activity.
 */
public class OrderDetailActivity extends AppCompatActivity {
    public static final String INTENT_KEY_ORDER = "order";

    private static OrderDetailActivity instance = null;

    /* the order we show in this activity */
    private Order order = null;
    private List<OrderItem> orderItems = null;
    private List<OrderTerm> orderTerms = null;

    private OrderItemAdapter orderItemAdapter = null;

    /* view reference */
    private ListView lvOrderItem = null;
    private LinearLayout llOperation = null;
    private TextView tvStatus = null;
    private Button btCanceled = null;
    private Button btOk = null;

    private LinearLayout llHeader = null;
    private TextView tvReceiverName = null;
    private TextView tvReceiveAddr = null;
    private TextView tvReceiverPhone = null;
    private TextView tvReceiveDate = null;

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
                finish();
                return;
            }
        }
        orderItems = new ArrayList<>();
        orderTerms = new ArrayList<>();

        initView();
        initData();
        initLogic();

        instance = this;

        getOrderItems();
    }

    public static OrderDetailActivity getInstance(){
        return instance;
    }

    public void initView(){
        lvOrderItem = (ListView) findViewById(R.id.lv_order_content);
        llOperation = (LinearLayout) findViewById(R.id.ll_operation_panel);
        tvStatus = (TextView) findViewById(R.id.tv_status_inform);
        btCanceled = (Button) findViewById(R.id.bt_cancel);
        btOk = (Button) findViewById(R.id.bt_ok);

        llHeader = (LinearLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.header_order_detail, lvOrderItem, false);
        tvReceiverName = (TextView) llHeader.findViewById(R.id.tv_receiver_name);
        tvReceiveAddr = (TextView) llHeader.findViewById(R.id.tv_receive_addr);
        tvReceiverPhone = (TextView) llHeader.findViewById(R.id.tv_receiver_phone);
        tvReceiveDate = (TextView) llHeader.findViewById(R.id.tv_receive_date);
    }

    public void initData(){
        lvOrderItem.addHeaderView(llHeader);
        orderItemAdapter = new OrderItemAdapter(getBaseContext(), order);
        orderItemAdapter.setOrderList(orderTerms);
        lvOrderItem.setAdapter(orderItemAdapter);

        tvReceiverName.setText(order.getName());
        tvReceiveAddr.setText(order.getOrderAddress());
        tvReceiverPhone.setText(order.getPhone());
        tvReceiveDate.setText(order.getRecieveTime());

        orderItemAdapter.setOrder(order);

        if (order.getOrderFlag().equals(ConstConv.ORDER_STATUS_CANCELED)){
            llOperation.setVisibility(View.GONE);
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(ConstConv.ORDER_STATUS_CANCELED);
        } else if (order.getOrderFlag().equals(ConstConv.ORDER_STATUS_COMPLETED)){
            llOperation.setVisibility(View.GONE);
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(ConstConv.ORDER_STATUS_COMPLETED);
        } else if (order.getOrderFlag().equals(ConstConv.ORDER_STATUS_ORDERED)){
            llOperation.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.GONE);
            btOk.setText(getString(R.string.order_pay));
        } else if (order.getOrderFlag().equals(ConstConv.ORDER_STATUS_PAYED)){
            llOperation.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.GONE);
            btOk.setText(getString(R.string.order_confirm));
        }
    }

    public void initLogic(){
        btCanceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpClient httpClient = BaseApp.getHttpClient();
                FuncApiInterface i = httpClient.getFuncApiInterface();
                Call<ResponseBody> getOrderById = i.cancelOrder(order.getOrderId());
                getOrderById.enqueue(orderCallback);
            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(order.getOrderFlag().equals(ConstConv.ORDER_STATUS_ORDERED)){
                    /* logic for pay */
                    HttpClient httpClient = BaseApp.getHttpClient();
                    FuncApiInterface i = httpClient.getFuncApiInterface();
                    Call<ResponseBody> getOrderById = i.pay(order.getOrderId());
                    getOrderById.enqueue(orderCallback);
                } else if (order.getOrderFlag().equals(ConstConv.ORDER_STATUS_PAYED)){
                    /* logic for confirm */
                    HttpClient httpClient = BaseApp.getHttpClient();
                    FuncApiInterface i = httpClient.getFuncApiInterface();
                    Call<ResponseBody> getOrderById = i.confirmOrder(order.getOrderId());
                    getOrderById.enqueue(orderCallback);
                }
            }
        });
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
                    orderTerms.clear();
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

    private Subscriber<String> createOrderSubscriber(){
        return new OrderSubscriber();
    }

    public static Subscriber<String> getOrderSubscriber(){
        if(instance == null){
            return null;
        }
        return instance.createOrderSubscriber();
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

    /* rxjava's subscriber */
    private class OrderSubscriber extends Subscriber<String> {
        @Override
        public void onNext(String ignoreStr) {
            //getOrderItems();
            HttpClient httpClient = BaseApp.getHttpClient();
            FuncApiInterface i = httpClient.getFuncApiInterface();
            Call<Order> getOrderById = i.getOrderById(order.getOrderId());

            getOrderById.enqueue(new Callback<Order>(){
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    Headers headers = response.headers();
                    String status = headers.get(ConstConv.HEADKEY_RESPONSTATUS);

                    if (status == null){
                        Toast.makeText(getBaseContext(),
                                getString(R.string.msg_status_header_null),
                                Toast.LENGTH_LONG).show();
                    } else if (status.equals(ConstConv.RET_STATUS_OK)){

                        order = response.body();
                        orderItemAdapter.setOrder(order);
                        getOrderItems();

                        if (order.getOrderFlag().equals(ConstConv.ORDER_STATUS_CANCELED)){
                            llOperation.setVisibility(View.GONE);
                            tvStatus.setVisibility(View.VISIBLE);
                            tvStatus.setText(ConstConv.ORDER_STATUS_CANCELED);
                        } else if (order.getOrderFlag().equals(ConstConv.ORDER_STATUS_COMPLETED)){
                            llOperation.setVisibility(View.GONE);
                            tvStatus.setVisibility(View.VISIBLE);
                            tvStatus.setText(ConstConv.ORDER_STATUS_COMPLETED);
                        } else if (order.getOrderFlag().equals(ConstConv.ORDER_STATUS_ORDERED)){
                            llOperation.setVisibility(View.VISIBLE);
                            tvStatus.setVisibility(View.GONE);
                            btOk.setText(getString(R.string.order_pay));
                        } else if (order.getOrderFlag().equals(ConstConv.ORDER_STATUS_PAYED)){
                            llOperation.setVisibility(View.VISIBLE);
                            tvStatus.setVisibility(View.GONE);
                            btOk.setText(getString(R.string.order_confirm));
                        }

                    } else if (status.equals(ConstConv.RET_STATUS_TIMEOUT)){
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
                public void onFailure(Call<Order> call, Throwable t) {
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

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getBaseContext(), "error in observer", Toast.LENGTH_LONG)
                    .show();
            Log.i("----------------------", e.getMessage());
        }

        @Override
        public void onCompleted() {
            if(!isUnsubscribed()) {
                this.unsubscribe();
            }
        }
    }

    private Callback<ResponseBody> orderCallback = new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            Headers headers = response.headers();
            String status = headers.get(ConstConv.HEADKEY_RESPONSTATUS);

            if (status == null){
                Toast.makeText(getBaseContext(),
                        getString(R.string.msg_status_header_null),
                        Toast.LENGTH_LONG).show();
            } else if (status.equals(ConstConv.RET_STATUS_OK)){

                ObservablesFactory.orderObservable(null);

            } else if (status.equals(ConstConv.RET_STATUS_TIMEOUT)){
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
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            if(t instanceof java.net.ConnectException){
                CharSequence msg = getString(R.string.msg_connect_error);
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("+++++++++++++++++++++++", t.toString());
                t.printStackTrace();
            }
        }
    };
}