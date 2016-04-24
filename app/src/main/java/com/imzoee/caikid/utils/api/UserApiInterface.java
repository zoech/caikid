package com.imzoee.caikid.utils.api;

import com.alibaba.fastjson.JSONObject;
import com.imzoee.caikid.convention.LoginConv;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zoey on 2016/4/24.
 */
public interface UserApiInterface {
    @POST("login")
    Call<JSONObject> login(@Query(LoginConv.RESKEY_ACCOUNT) String account, @Query(LoginConv.RESKEY_PWD) String pwd);

/*    @GET("userInfo")
    Call<JSONObject> getUser(@Query())*/
}
