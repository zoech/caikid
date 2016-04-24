package com.imzoee.caikid.utils.preferences;

import android.content.SharedPreferences;
import android.content.Context;

/**
 * Created by zoey on 2016/4/21.
 */
public class Settings {

    private static final String PREFERNAME = "settings";

    private static final String KEY_ISLOGIN = "status";
    private static final String KEY_AUTOLOGIN = "autologin";

    public static void setLoginStatus(Context context, boolean status){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME,Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_ISLOGIN, status).commit();
    }

    public static boolean isLogin(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE);
        boolean status = preferences.getBoolean(KEY_ISLOGIN, false);
        return status;
    }


    public static void setAutoLgin(Context context, boolean auto){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME,Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_AUTOLOGIN, auto).commit();
    }

    public static boolean isAutoLogin(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE);
        boolean status = preferences.getBoolean(KEY_AUTOLOGIN, true);
        return status;
    }
}
