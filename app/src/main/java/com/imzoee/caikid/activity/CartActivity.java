package com.imzoee.caikid.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.R;
import com.imzoee.caikid.adapter.CartItemAdapter;
import com.imzoee.caikid.dao.User;
import com.imzoee.caikid.model.CaikidCart;
import com.rey.material.widget.Button;
import com.rey.material.widget.ListView;

import rx.Subscriber;

/**
 * Created by zoey on 2016/5/9.
 *
 * Cart activity
 */
public class CartActivity extends AppCompatActivity {
    private static CartActivity instance = null;

    private CaikidCart cart = null;
    private CartItemAdapter cartItemAdapter = null;
    private ListView lvContent = null;
    private TextView tvTotalPrice = null;
    private Button btOrder = null;
    private TextView tvNotLogin = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        instance = this;

        cart = BaseApp.getCart();
        initView();
        initData();
        initListener();
    }

    private void initView(){
        lvContent = (ListView) findViewById(R.id.lv_content);
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        btOrder = (Button) findViewById(R.id.bt_order);
        tvNotLogin = (TextView) findViewById(R.id.tv_not_login_arlert);
    }

    private void initData(){
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        tvTotalPrice.setText(df.format(cart.getTotallPrice()));
        cartItemAdapter = new CartItemAdapter(getBaseContext());
        lvContent.setAdapter(cartItemAdapter);

        if(cart.getItemCount() == 0){
            btOrder.setVisibility(View.INVISIBLE);
        } else {
            btOrder.setVisibility(View.VISIBLE);
        }
        if(BaseApp.getSettings().isLogin()){
            tvNotLogin.setVisibility(View.GONE);
            btOrder.setText(getString(R.string.cart_order));
        } else {
            tvNotLogin.setVisibility(View.VISIBLE);
            btOrder.setText(R.string.login);
        }
    }

    private void initListener(){
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        btOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BaseApp.getSettings().isLogin()){
                    Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private Subscriber<String> createCartActionSubscriber(){
        return new CartActionSubscriber();
    }

    public static Subscriber<String> getCartSubscriber(){
        if(instance == null){
            return null;
        }
        return instance.createCartActionSubscriber();
    }

    private Subscriber<User> createLoginSubscriber(){
        return new LoginStateSubscriber();
    }

    public static Subscriber<User> getLoginSubscriber(){
        if(instance == null){
            return null;
        }
        return instance.createLoginSubscriber();
    }

    /* rxjava's subscriber */
    private class CartActionSubscriber extends Subscriber<String> {
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
        public void onNext(String opt) {
            // adapter.notifyDataSetChanged();
            if(cart.getItemCount() == 0){
                /* the cart is empty */
                Toast.makeText(getBaseContext(), "the cart is empty!", Toast.LENGTH_LONG).show();
                btOrder.setVisibility(View.INVISIBLE);
            } else {
                btOrder.setVisibility(View.VISIBLE);
            }

            java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
            tvTotalPrice.setText(df.format(cart.getTotallPrice()));
            cartItemAdapter.notifyDataSetChanged();
        }
    }

    /* rxjava's subscriber */
    private class LoginStateSubscriber extends Subscriber<User> {
        @Override
        public void onCompleted() {
            if(!isUnsubscribed()) {
                this.unsubscribe();
            }
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getBaseContext(), "error in observable,CartActivity", Toast.LENGTH_LONG)
                    .show();
            Log.i("----------------------", e.getMessage());
        }

        @Override
        public void onNext(User user) {
            if(user != null){
                tvNotLogin.setVisibility(View.GONE);
                btOrder.setText(getString(R.string.cart_order));
            } else {
                tvNotLogin.setVisibility(View.VISIBLE);
                btOrder.setText(R.string.login);
            }
        }
    }
}
