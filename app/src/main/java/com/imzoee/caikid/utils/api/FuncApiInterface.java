package com.imzoee.caikid.utils.api;

import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.Order;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zoey on 2016/5/13.
 *
 * The network interface related to business, such as
 * order, pay, etc.
 */
public interface FuncApiInterface {
    @FormUrlEncoded
    @POST("makeOrder.php")
    Call<ResponseBody> order(@Field(ConstConv.RESKEY_ORDER_CART) String cartContent,
                             @Field(ConstConv.RESKEY_ORDER_ADDR) String addr,
                             @Field(ConstConv.RESKEY_ORDER_PHONE) String phone,
                             @Field(ConstConv.RESKEY_ORDER_NAME) String name,
                             @Field(ConstConv.RESKEY_ORDER_RECIEVETIME) String date);

    @GET("getOrderList.php")
    Call<List<Order>> getOrderList(@Query("orderBy") String orderType);
}
