package com.imzoee.caikid.utils.api;

import com.imzoee.caikid.convention.ConstConv;

import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by zoey on 2016/5/13.
 *
 * The network interface related to business, such as
 * order, pay, etc.
 */
public interface FuncApiInterface {
    @FormUrlEncoded
    @POST("makeOrder")
    Call<ResponseBody> order(@Field(ConstConv.RESKEY_ORDER_CART) String account,
                             @Field(ConstConv.RESKEY_ORDER_ADDR) String addr,
                             @Field(ConstConv.RESKEY_ORDER_PHONE) String phone,
                             @Field(ConstConv.RESKEY_ORDER_NAME) String name,
                             @Field(ConstConv.RESKEY_ORDER_RECIEVETIME) Date date);
}
