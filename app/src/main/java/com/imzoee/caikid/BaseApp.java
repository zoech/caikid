package com.imzoee.caikid;

import android.app.Application;
import android.content.res.Configuration;

import com.imzoee.caikid.dao.User;
import com.imzoee.caikid.utils.preferences.UserPref;
import com.imzoee.caikid.utils.preferences.Settings;
import com.imzoee.caikid.utils.api.UserServices;
import com.imzoee.caikid.convention.LoginConv;
/**
 * Created by zoey on 2016/4/21.
 */
public class BaseApp extends Application {
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

/*
        if( Settings.isAutoLogin(this) ){
            int id = UserPref.getPfUserId(this);
            String account = UserPref.getPfUserAccount(this);
            String pwd = UserPref.getPfUserPwd(this);

            int status = UserServices.login(account, pwd);
            switch (status){
                case LoginConv.LOGIN_SUCCESSED :
                    setLogin(true);
                    setSessionUser(UserServices.getUser(id, account, "zoey", pwd)
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
}
