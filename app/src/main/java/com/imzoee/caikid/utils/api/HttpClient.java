package com.imzoee.caikid.utils.api;

import com.imzoee.caikid.BaseApp;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.imzoee.caikid.convention.ConstConv;

/**
 * Created by zoey on 2016/4/21.
 */
public class HttpClient {

    private UserApiInterface userApiInterface;
    private Retrofit retrofit;
    private OkHttpClient okhttpClient;

    public HttpClient(){
        okhttpClient = new OkHttpClient.Builder()
                .cookieJar(BaseApp.getInstance().getCookieJar())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ConstConv.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient)
                .build();

    }

    public UserApiInterface getUserApiInterface() {
        return retrofit.create(UserApiInterface.class);
    }


}
