package com.imzoee.caikid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.R;
import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.utils.api.FuncApiInterface;
import com.imzoee.caikid.utils.api.HttpClient;
import com.imzoee.caikid.utils.misc.ObservablesFactory;
import com.rey.material.widget.Button;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zoey on 2016/6/4.
 *
 */
public class CommentActivity extends AppCompatActivity {
    public static final String INTENT_KEY_ORDERID = "orderId";
    public static final String INTENT_KEY_PRODUCTID = "productId";

    private int orderId = 0;
    private int productId = 0;


    /* view references */
    EditText etContent = null;
    Button btComment = null;
    RatingBar rbarRating = null;
    TextView tvRating = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        /* get intent data */
        Intent intent = getIntent();
        if (intent != null){
            orderId = intent.getIntExtra(INTENT_KEY_ORDERID, -1);
            productId = intent.getIntExtra(INTENT_KEY_PRODUCTID, -1);
            if(orderId == -1 || productId == -1){
                /* the activity start this activity do not trans the recipe data */
                finish();
                return;
            }
        }

        initView();
        initData();
        initLogic();

    }

    public void initView(){
        etContent = (EditText) findViewById(R.id.et_comment);
        btComment = (Button) findViewById(R.id.bt_comment);
        rbarRating = (RatingBar) findViewById(R.id.rbar_recipe_rate);
        tvRating = (TextView) findViewById(R.id.tv_recipe_rate_numbers);
    }

    public void initData(){
    }

    public void initLogic() {
        btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etContent.getText().toString().trim();
                if(content == null || content.isEmpty()){
                    Toast.makeText(getBaseContext(), getString(R.string.msg_comment_empty), Toast.LENGTH_LONG).show();
                    return;
                }
                int rating = (int) rbarRating.getRating();
                if(rating == 0){
                    Toast.makeText(getBaseContext(), getString(R.string.msg_rating_null), Toast.LENGTH_LONG).show();
                    return;
                }
                HttpClient httpClient = BaseApp.getHttpClient();
                FuncApiInterface i = httpClient.getFuncApiInterface();
                Call<ResponseBody> getOrderById = i.comment(orderId, productId, content, rating);
                getOrderById.enqueue(new Callback<ResponseBody>(){
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
                            Toast.makeText(getBaseContext(), getString(R.string.comment_success), Toast.LENGTH_LONG).show();
                            finish();

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
                });
            }
        });

        rbarRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                java.text.DecimalFormat df = new java.text.DecimalFormat("0");
                tvRating.setText(df.format(rating));
            }
        });
    }
}
