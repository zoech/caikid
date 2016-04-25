package com.imzoee.caikid;

import android.app.Application;
import android.content.res.Configuration;

import com.imzoee.caikid.dao.User;
import com.imzoee.caikid.utils.preferences.UserPref;
import com.imzoee.caikid.utils.preferences.Settings;
import com.imzoee.caikid.utils.api.CaikidCookieJar;

/**
 * Created by zoey on 2016/4/21.
 */
public class BaseApp extends Application {
    private static BaseApp instance;

    private CaikidCookieJar cookieJar;
    private User user;
    private boolean isLogin = false;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        cookieJar = new CaikidCookieJar();

        instance = this;
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


    public CaikidCookieJar getCookieJar(){
        return cookieJar;
    }

    public User getSessionUser(){
        return user;
    }

    public void setSessionUser(User user){
        this.user = user;
        UserPref.setPfUser(this, user.getId(), user.getAccount(), user.getName(), user.getPwd());
    }

    public void setLogin(boolean status){
        isLogin = status;
        Settings.setLoginStatus(this, status); // write into preferences
    }

    public boolean isLogin(){
        return isLogin;
    }

    public static BaseApp getInstance(){
        return instance;
    }
}
