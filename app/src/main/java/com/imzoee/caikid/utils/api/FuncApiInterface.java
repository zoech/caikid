package com.imzoee.caikid.utils.api;

import com.imzoee.caikid.activity.OrderDetailActivity;
import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.Order;
import com.imzoee.caikid.model.OrderItem;

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
    Call<List<Order>> getOrderList(@Query(ConstConv.RESKEY_GETORDERLIST_ORDERBY) String orderType);

    @GET("getOrderDetail.php")
    Call<List<OrderItem>> getOrderItems(@Query(ConstConv.RESKEY_GETORDERITEM_ORDERID) int id);

    @GET("getOrderById.php")
    Call<Order> getOrderById(@Query(ConstConv.RESKEY_GETORDERITEM_ORDERID) int id);

    @FormUrlEncoded
    @POST("pay.php")
    Call<ResponseBody> pay(@Field(ConstConv.RESKEY_GETORDERITEM_ORDERID) int id);

    @FormUrlEncoded
    @POST("confirmOrder.php")
    Call<ResponseBody> confirmOrder(@Field(ConstConv.RESKEY_GETORDERITEM_ORDERID) int id);

    @FormUrlEncoded
    @POST("cancelOrder.php")
    Call<ResponseBody> cancelOrder(@Field(ConstConv.RESKEY_GETORDERITEM_ORDERID) int id);

    @FormUrlEncoded
    @POST("comment.php")
    Call<ResponseBody> comment(@Field(ConstConv.RESKEY_GETORDERITEM_ORDERID) int orderId,
                               @Field(ConstConv.RESKEY_COMMENT_RECIPEID) int productId,
                               @Field(ConstConv.RESCOMMENT_CONTENT) String content,
                               @Field(ConstConv.RESCOMMENT_SCORE) int score);
}
