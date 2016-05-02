package com.imzoee.caikid.utils.api;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.imzoee.caikid.BaseApp;
import okhttp3.OkHttpClient;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import retrofit2.Retrofit;
import com.imzoee.caikid.utils.misc.FastJsonConverterFactory;

import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.utils.preferences.UserPref;

/**
 *
 * Created by zoey on 2016/4/21.
 */
public class HttpClient {

    //private UserApiInterface userApiInterface;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;
    private UserPref userPref;

    public HttpClient(){

        userPref = BaseApp.getUserPref();
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(caikidInterceptor)
                .cookieJar(new CaikidCookieJar())
                .connectTimeout(8L, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ConstConv.API_URL)
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(okHttpClient)
                .build();

    }

    public OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }

    public UserApiInterface getUserApiInterface() {
        return retrofit.create(UserApiInterface.class);
    }


    private Interceptor caikidInterceptor= new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();

            int id = userPref.getPfUserId();
            if (id > 0){
                builder.header(ConstConv.HEADKEY_ID, String.valueOf(id));
            }
            String account = userPref.getPfUserAccount();
            if(account != null){
                builder.header(ConstConv.HEADKEY_ACCOUNT, account);
            }
            request = builder.build();


            /* debug used log information */
            Log.i("------------interceptor",request.headers().toString());



            Response response = chain.proceed(request);

            return response;
        }
    };
}
