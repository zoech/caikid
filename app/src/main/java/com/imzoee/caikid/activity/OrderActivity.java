package com.imzoee.caikid.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.R;
import com.imzoee.caikid.adapter.OrderCartAdapter;
import com.imzoee.caikid.convention.ConstConv;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zoey on 2016/5/9.
 *
 * Cart activity
 */
public class OrderActivity extends AppCompatActivity {

    /* this int define the max time (in days) the user can
     * select to get the recipe
     */
    public static final int MAX_DAYS_INTERNAL_RECEIVE = 7;


    private static OrderActivity instance = null;

    private Calendar receiveDate = null;

    /* views */
    private View llHeader = null;
    private View llFooter = null;
    private ListView lvCart = null;
    private TextView tvDate = null;
    private TextView tvTotal = null;

    private Button btCancel = null;
    private Button btOk = null;

    private AutoCompleteTextView actvName = null;
    private AutoCompleteTextView actvAddr = null;
    private AutoCompleteTextView actvPhone = null;

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

        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        llHeader = inflater.inflate(R.layout.header_order_activity, lvCart, false);
        llFooter = inflater.inflate(R.layout.footer_order_activity, lvCart, false);

        tvDate = (TextView) llHeader.findViewById(R.id.tv_receive_time);
        tvTotal = (TextView) llFooter.findViewById(R.id.tv_total_price);

        actvName = (AutoCompleteTextView) llHeader.findViewById(R.id.actv_name);
        actvAddr = (AutoCompleteTextView) llHeader.findViewById(R.id.actv_addr);
        actvPhone = (AutoCompleteTextView) llHeader.findViewById(R.id.actv_phone);

        btCancel = (Button)findViewById(R.id.bt_cancel);
        btOk = (Button)findViewById(R.id.bt_ok);
    }

    private void initData(){
        lvCart.addHeaderView(llHeader);
        lvCart.addFooterView(llFooter);
        OrderCartAdapter adapter = new OrderCartAdapter(getBaseContext());
        lvCart.setAdapter(adapter);

        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        tvTotal.setText(df.format(BaseApp.getCart().getTotallPrice()));
    }

    private void initListener(){

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });

        btCancel.setOnClickListener(onClickListener);
        btOk.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_cancel:
                    finish();
                    break;
                case R.id.bt_ok:
                    String name = actvName.getText().toString();
                    String addr = actvAddr.getText().toString();
                    String phone = actvPhone.getText().toString();

                    boolean infoComplete = true;

                    if(TextUtils.isEmpty(name)){
                        actvName.setError(getString(R.string.msg_name_null));
                        infoComplete = false;
                    }
                    if(TextUtils.isEmpty(addr)){
                        actvAddr.setError(getString(R.string.msg_addr_null));
                        infoComplete = false;
                    }
                    if(TextUtils.isEmpty(phone)){
                        actvPhone.setError(getString(R.string.msg_phone_null));
                        infoComplete = false;
                    }
                    if(receiveDate == null){
                        Toast.makeText(getBaseContext(), getString(R.string.msg_receive_time_null), Toast.LENGTH_LONG).show();
                        infoComplete = false;
                    }

                    if(!infoComplete){
                        return;
                    }

                    java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Call<ResponseBody> order = BaseApp.getHttpClient().getFuncApiInterface().order(BaseApp.getCart().getApiUseJSONString(),
                                                                                                    addr, phone, name, format.format(receiveDate));

                    order.enqueue(orderCallback);

                    break;
            }
        }
    };

    private int lastPickYear = -1;
    private int lastPickMonth = -1;
    private int lastPickDate = -1;
    private void pickDate(){
        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(R.style.dialog_date_picker){
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                DatePickerDialog dialog = (DatePickerDialog)fragment.getDialog();

                int hour = 0;
                int minute = 0;
                if(receiveDate != null) {
                    hour = receiveDate.get(Calendar.HOUR);
                    minute = receiveDate.get(Calendar.MINUTE);
                }

                receiveDate = dialog.getCalendar();
                receiveDate.set(Calendar.HOUR, hour);
                receiveDate.set(Calendar.MINUTE, minute);
                pickTime();
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                //Toast.makeText(mActivity, "Cancelled" , Toast.LENGTH_SHORT).show();
                super.onNegativeActionClicked(fragment);
            }
        };

        Calendar cal = Calendar.getInstance();
        long minTime = cal.getTimeInMillis();
        cal.add(Calendar.DATE, MAX_DAYS_INTERNAL_RECEIVE);
        long maxTime = cal.getTimeInMillis();

        builder.dateRange(minTime, maxTime);

        if(lastPickYear != -1 && receiveDate != null){
            builder.date(receiveDate.getTime().getTime());
        }

        builder.positiveAction(getString(R.string.button_ok))
                .negativeAction(getString(R.string.button_cancel));

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    private void pickTime(){
        TimePickerDialog.Builder builder = new TimePickerDialog.Builder(R.style.dialog_time_picker, 24, 00){
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                TimePickerDialog dialog = (TimePickerDialog)fragment.getDialog();

                int hour = dialog.getHour();
                int minute = dialog.getMinute();

                receiveDate.set(Calendar.HOUR, hour);
                receiveDate.set(Calendar.MINUTE, minute);

                lastPickYear = receiveDate.get(Calendar.YEAR);
                lastPickMonth = receiveDate.get(Calendar.MONTH);
                lastPickDate = receiveDate.get(Calendar.DATE);

                java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm");
                tvDate.setText(format.format(receiveDate.getTime()));
                tvDate.setTextColor(getResources().getColor(R.color.flat_peter_river));

                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                receiveDate.set(Calendar.YEAR, lastPickYear);
                receiveDate.set(Calendar.MONTH, lastPickMonth);
                receiveDate.set(Calendar.DATE, lastPickDate);
                super.onNegativeActionClicked(fragment);
            }
        };

        if(receiveDate != null){
            builder.hour(receiveDate.get(Calendar.HOUR)).minute(receiveDate.get(Calendar.MINUTE));
        }

        builder.positiveAction(getString(R.string.button_ok))
                .negativeAction(getString(R.string.button_cancel));

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    private Callback<ResponseBody> orderCallback = new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            Headers headers = response.headers();
            String status = headers.get(ConstConv.HEADKEY_RESPONSTATUS);

            if(status == null){
                Toast.makeText(getBaseContext(),
                        getString(R.string.msg_status_header_null),
                        Toast.LENGTH_LONG).show();
                Log.i("<<<<<<<<<<<<<<<<<<<<<<<", headers.toString());
            }
            else if(status.equals(ConstConv.RET_STATUS_OK)){

                try {
                    Toast.makeText(getBaseContext(), "order id :  " + response.body().string(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), "order id can not translate from body", Toast.LENGTH_LONG).show();
                }

                finish();
            }

            else {
                Toast.makeText(getBaseContext(),
                        getString(R.string.msg_unknown_ret_status),
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
