package com.imzoee.caikid.utils.api;

import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import okhttp3.ResponseBody;

/**
 * Created by zoey on 2016/4/24.
 *
 * Retrofit2 interfaces related to user, such as
 * login, logout, etc.
 */
public interface UserApiInterface {
    @FormUrlEncoded
    @POST("login.php")
    Call<User> login(@Field(ConstConv.RESKEY_ACCOUNT) String account,
                     @Field(ConstConv.RESKEY_PWD) String pwd);

    @FormUrlEncoded
    @POST("verify.php")
    Call<ResponseBody> verify(@Field(ConstConv.RESKEY_ACCOUNT) String account);

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> signUp(@Field(ConstConv.RESKEY_ACCOUNT) String account,
                              @Field(ConstConv.RESKEY_PWD) String pwd,
                              @Field(ConstConv.RESKEY_NAME) String name,
                              @Field(ConstConv.RESKEY_VERIFYCODE) String verifyCode);

    @POST("logout.php")
    Call<ResponseBody> logout();





    /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    /*
     * debug used
     */
    @GET("test/cookietest")
    Call<ResponseBody> test();


}
