package com.imzoee.caikid.utils.api;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.convention.LoginConv;
import com.imzoee.caikid.convention.LogoutConv;
import com.imzoee.caikid.convention.SignupConv;
import com.imzoee.caikid.convention.VerifyConv;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import okhttp3.ResponseBody;

/**
 * Created by zoey on 2016/4/24.
 */
public interface UserApiInterface {
    @POST("user/login")
    Call<JSONObject> login(@Query(LoginConv.RESKEY_ACCOUNT) String account, @Query(LoginConv.RESKEY_PWD) String pwd);

    @POST("user/verify")
    Call<JSONObject> verify(@Query(VerifyConv.RESKEY_ACCOUNT) String account,
                            @Query(VerifyConv.RESKEY_NAME) String name,
                            @Query(VerifyConv.RESKEY_PWD) String pwd);

    @POST("user/signup")
    Call<JSONObject> signUp(@Query(SignupConv.RESKEY_ACCOUNT) String account,
                            @Query(SignupConv.RESKEY_PWD) String pwd,
                            @Query(SignupConv.RESKEY_NAME) String name,
                            @Query(SignupConv.RESKEY_TOKEN) String token,
                            @Query(SignupConv.RESKEY_VERIFYCODE) String verifyCode);

    @POST("user/logout")
    Call<JSONObject> logout(@Query(LogoutConv.RESKEY_USERID) String userId,
                            @Query(LogoutConv.RESKEY_ACCOUNT) String account,
                            @Query(LogoutConv.RESKEY_PWD) String pwd);





    /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    /*
     * debug used
     */
    @GET("test/cookietest")
    Call<ResponseBody> test();


}
