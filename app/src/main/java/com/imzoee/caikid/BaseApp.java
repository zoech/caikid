package com.imzoee.caikid;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;

import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.User;
import com.imzoee.caikid.fragment.MeFragment;
import com.imzoee.caikid.fragment.OrderFragment;
import com.imzoee.caikid.utils.api.HttpClient;
import com.imzoee.caikid.utils.api.UserApiInterface;
import com.imzoee.caikid.utils.misc.ObservablesFactory;
import com.imzoee.caikid.utils.preferences.UserPref;
import com.imzoee.caikid.utils.preferences.Settings;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by zoey on 2016/4/21.
 */
public class BaseApp extends Application {
    private static BaseApp instance;

    private UserPref userPref = null;
    private Settings settings = null;
    private HttpClient httpClient = null;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        instance = this;

        userPref = new UserPref(this);
        settings = new Settings(this);
        httpClient = new HttpClient();

        Log.i("--------auto-login----",String.valueOf(settings.isAutoLogin()));

        if( settings.isAutoLogin() && settings.isLogin() ){
            Log.i("------------ss---","auto");
            String account = userPref.getPfUserAccount();
            String pwd = userPref.getPfUserPwd();

            UserApiInterface i = httpClient.getUserApiInterface();
            Call<User> loginCall = i.login(account, pwd);

            loginCall.enqueue(new LoginCallBack());
        }
    }



    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }


   /* public UserPref getUserPref(){
        return userPref;
    }*/

    public void setSessionUser(User user){ this.userPref.setPfUser(user); }

    public static Settings getSettings(){ return instance.settings; }

    public static BaseApp getInstance(){ return instance; }

    public static UserPref getUserPref(){ return instance.userPref; }

    public static HttpClient getHttpClient() { return instance.httpClient; }


    private class LoginCallBack implements Callback<User> {
        @Override
        public void onResponse(Call<User> call, Response<User> response){

            Headers headers = response.headers();
            String status = headers.get(ConstConv.HEADKEY_RESPONSTATUS);

            Log.i("---------b-----------", response.headers().toString());

            if(status != null && status.equals(ConstConv.RET_STATUS_OK)){

                final User user = response.body();

                /* save the basic information to userPref and settings */
                BaseApp.getUserPref().setPfUser(user);
                BaseApp.getSettings().setLoginStatus(true);

                /* alert the relative components to update their data or view */
                Observable<User> logoutObservable = ObservablesFactory.loginStateObservable(user);
                Subscriber<User> orderLogoutSubscriber = OrderFragment.getLoginStateSubscriber();
                Subscriber<User> meLogoutSubscriber = MeFragment.getLoginStateSubscriber();
                if (orderLogoutSubscriber != null){
                    logoutObservable.subscribe(orderLogoutSubscriber);
                }
                if (meLogoutSubscriber != null) {
                    logoutObservable.subscribe(meLogoutSubscriber);
                }

            } else {
                settings.setLoginStatus(false);
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t){

            settings.setLoginStatus(false);

            if(t instanceof java.net.ConnectException){
                CharSequence msg = getString(R.string.msg_connect_error);
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("+++++++++++++++++++++++", t.toString());
                t.printStackTrace();
            }
        }
    }
}
