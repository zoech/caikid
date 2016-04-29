package com.imzoee.caikid;

import android.app.Application;
import android.content.res.Configuration;

import com.imzoee.caikid.dao.User;
import com.imzoee.caikid.utils.api.HttpClient;
import com.imzoee.caikid.utils.preferences.UserPref;
import com.imzoee.caikid.utils.preferences.Settings;
import com.imzoee.caikid.utils.api.CaikidCookieJar;

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


/*
        if( Settings.isAutoLogin(this) ){
            int id = UserPref.getPfUserId(this);
            String account = UserPref.getPfUserAccount(this);
            String pwd = UserPref.getPfUserPwd(this);

            int status = HttpClient.login(account, pwd);
            switch (status){
                case LoginConv.LOGIN_SUCCESSED :
                    setLogin(true);
                    setSessionUser(HttpClient.getUser(id, account, "zoey", pwd)
                                 );
                    break;
                case LoginConv.LOGIN_PWD_INCORRECT:
                    setLogin(false);
                    break;
                case LoginConv.LOGIN_USER_NOT_EXIT:
                    setLogin(false);
                    break;
            }
        }*/
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
}
