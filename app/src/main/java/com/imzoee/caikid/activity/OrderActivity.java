package com.imzoee.caikid.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.imzoee.caikid.R;
import com.imzoee.caikid.adapter.OrderCartAdapter;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by zoey on 2016/5/9.
 *
 * Cart activity
 */
public class OrderActivity extends AppCompatActivity {
    private static OrderActivity instance = null;

    private View llHeader = null;
    private ListView lvCart = null;
    private TextView tvDate = null;
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
        lvCart = (ListView) findViewById(R.id.lv_cart_content);
        llHeader = LayoutInflater.from(getBaseContext()).inflate(R.layout.header_order_activity, lvCart, false);
        tvDate = (TextView) llHeader.findViewById(R.id.tv_receive_time);
    }

    private void initData(){
        lvCart.addHeaderView(llHeader);
        OrderCartAdapter adapter = new OrderCartAdapter(getBaseContext());
        lvCart.setAdapter(adapter);
    }

    private void initListener(){

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.Builder builder = null;
                builder = new DatePickerDialog.Builder(R.style.Material_App_Dialog_DatePicker_Light){
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        DatePickerDialog dialog = (DatePickerDialog)fragment.getDialog();
                        String date = dialog.getFormattedDate(SimpleDateFormat.getDateInstance());
                        tvDate.setText(date);
                        tvDate.setTextColor(getResources().getColor(R.color.flat_peter_river));
                        //Toast.makeText(mActivity, "Date is " + date, Toast.LENGTH_SHORT).show();
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        //Toast.makeText(mActivity, "Cancelled" , Toast.LENGTH_SHORT).show();
                        super.onNegativeActionClicked(fragment);
                    }
                };

                builder.positiveAction("OK")
                        .negativeAction("CANCEL");

                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getSupportFragmentManager(), null);
            }
        });

    }
}
