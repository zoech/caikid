package com.imzoee.caikid.activity;


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
import com.imzoee.caikid.model.CaikidCart;
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
    }

    private void initData(){
        tvTotalPrice.setText(String.valueOf(cart.getTotallPrice()));
        cartItemAdapter = new CartItemAdapter(getBaseContext());
        lvContent.setAdapter(cartItemAdapter);
    }

    private void initListener(){
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private Subscriber<CaikidCart> createCartActionSubscriber(){
        return new CartActionSubscriber();
    }

    public static Subscriber<CaikidCart> getCartSubscriber(){
        if(instance == null){
            return null;
        }
        return instance.createCartActionSubscriber();
    }

    /* rxjava's subscriber */
    private class CartActionSubscriber extends Subscriber<CaikidCart> {
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
        public void onNext(CaikidCart cart) {
            // adapter.notifyDataSetChanged();
            if(cart.getItemCount() == 0){
                /* the cart is empty */
                Toast.makeText(getBaseContext(), "the cart is empty!", Toast.LENGTH_LONG).show();
            } else {

            }

            tvTotalPrice.setText(String.valueOf(cart.getTotallPrice()));
            cartItemAdapter.notifyDataSetChanged();
        }
    }
}
