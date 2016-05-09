package com.imzoee.caikid.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.imzoee.caikid.R;
import com.imzoee.caikid.model.CaikidCart;

import rx.Subscriber;

/**
 * Created by zoey on 2016/5/9.
 *
 * Cart activity
 */
public class CartActivity extends AppCompatActivity {
    private static CartActivity instance = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        instance = this;

        initView();
        initData();
        initListener();
    }

    private void initView(){

    }

    private void initData(){

    }

    private void initListener(){

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
        }
    }
}
