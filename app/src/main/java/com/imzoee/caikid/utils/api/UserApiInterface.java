package com.imzoee.caikid.utils.api;

import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.User;

import retrofit2.Call;
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
    @POST("user/login")
    Call<User> login(@Query(ConstConv.RESKEY_ACCOUNT) String account,
                     @Query(ConstConv.RESKEY_PWD) String pwd);

    @POST("user/verify")
    Call<ResponseBody> verify(@Query(ConstConv.RESKEY_ACCOUNT) String account);

    @POST("user/signup")
    Call<ResponseBody> signUp(@Query(ConstConv.RESKEY_ACCOUNT) String account,
                            @Query(ConstConv.RESKEY_PWD) String pwd,
                            @Query(ConstConv.RESKEY_NAME) String name,
                            @Query(ConstConv.RESKEY_VERIFYCODE) String verifyCode);

    @POST("user/logout")
    Call<ResponseBody> logout();





    /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    /*
     * debug used
     */
    @GET("test/cookietest")
    Call<ResponseBody> test();


}
