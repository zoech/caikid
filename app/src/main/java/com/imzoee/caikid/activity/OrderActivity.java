package com.imzoee.caikid.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.imzoee.caikid.R;

/**
 * Created by zoey on 2016/5/9.
 *
 * Cart activity
 */
public class OrderActivity extends AppCompatActivity {
    private static OrderActivity instance = null;
/*
    private CaikidCart cart = null;
    private CartItemAdapter cartItemAdapter = null;
    private ListView lvContent = null;
    private TextView tvTotalPrice = null;
    private Button btOrder = null;
    private TextView tvNotLogin = null;
*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        instance = this;

        //cart = BaseApp.getCart();
        initView();
        initData();
        initListener();
    }

    private void initView(){
        /*
        lvContent = (ListView) findViewById(R.id.lv_content);
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        btOrder = (Button) findViewById(R.id.bt_order);
        tvNotLogin = (TextView) findViewById(R.id.tv_not_login_arlert);
        */
    }

    private void initData(){
        /*
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        tvTotalPrice.setText(df.format(cart.getTotallPrice()));
        cartItemAdapter = new CartItemAdapter(getBaseContext());
        lvContent.setAdapter(cartItemAdapter);
        if(BaseApp.getSettings().isLogin()){
            tvNotLogin.setVisibility(View.GONE);
            btOrder.setText(getString(R.string.cart_order));
        } else {
            tvNotLogin.setVisibility(View.VISIBLE);
            btOrder.setText(R.string.login);
        }
        */
    }

    private void initListener(){
        /*
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        btOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BaseApp.getSettings().isLogin()){

                } else {
                    Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        */
    }
}
