package com.imzoee.caikid.utils.preferences;

import android.content.SharedPreferences;
import android.content.Context;

/**
 * Created by zoey on 2016/4/21.
 */
public class Settings {

    private static final String PREFERNAME = "settings";

    private static final String KEY_ISLOGIN = "islogin";
    private static final String KEY_AUTOLOGIN = "autologin";

    private Context context;
    private boolean isLogin = false;
    private boolean autoLogin = true;

    public Settings(Context context){
        this.context = context;
        SharedPreferences preferences = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE);
        isLogin = false; //preferences.getBoolean(KEY_ISLOGIN, false);
        autoLogin = preferences.getBoolean(KEY_AUTOLOGIN, true);
    }

    public void setLoginStatus(boolean status){
        isLogin = status;
    }

    public boolean isLogin(){
        return isLogin;
    }


    public void setAutoLgin(boolean auto){
        autoLogin = auto;
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME,Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_AUTOLOGIN, auto).apply();
    }

    public boolean isAutoLogin(){
        return autoLogin;
    }
}
