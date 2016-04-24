package com.imzoee.caikid.utils.preferences;

import android.content.SharedPreferences;
import android.content.Context;

/**
 * Created by zoey on 2016/4/21.
 */
public class UserPref {

    private static final String PREFERNAME = "userPreferences";

    private static final String KEY_ID = "id";
    private static final String KEY_ACCOUNT = "account";
    private static final String KEY_NAME = "name";
    private static final String KEY_PWD = "pwd";
    private static final String KEY_CREDIT = "credit";

    /*
     * used only for debug
     */
    private static class DebugUse{
        public final static String account = "zoey@debug.com";
        public final static String name = "zoey";
        public final static int id = 1;
        public final static String pwd = "2b||~2b?";
    }

    public static void setPfUserId(Context context, int id){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME,Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_ID, id).commit();
    }

    public static int getPfUserId(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE);
        int id = preferences.getInt(KEY_ID, -1);
        return id;
    }


    public static void setPfUserAccount(Context context, String account){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME,Context.MODE_PRIVATE).edit();
        editor.putString(KEY_ACCOUNT, account).commit();
    }

    public static String getPfUserAccount(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE);
        String account = preferences.getString(KEY_ACCOUNT,null);
        return account;
    }


    public static void setPfUserName(Context context, String name){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME,Context.MODE_PRIVATE).edit();
        editor.putString(KEY_NAME, name).commit();
    }

    public static String getPfUserName(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE);
        String name = preferences.getString(KEY_NAME, null);
        return name;
    }


    public static void setPfUserPwd(Context context, String pwd){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME,Context.MODE_PRIVATE).edit();
        editor.putString(KEY_PWD, pwd).commit();
    }

    public static String getPfUserPwd(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE);
        String pwd = preferences.getString(KEY_PWD, null);
        return pwd;
    }


    public static void setPfUserCredit(Context context, int credit){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME,Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_CREDIT, credit).commit();
    }

    public static int getPfUserCredit(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE);
        int credit = preferences.getInt(KEY_CREDIT, -1);
        return credit;
    }


    /*
     * update the all preferences of session user at one time
     */
    public static void setPfUser(Context context, int id, String account, String name, String pwd){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME,Context.MODE_PRIVATE).edit();

        if (id != -1){
            editor.putInt(KEY_ID,id);
        }

        if (account != null){
            editor.putString(KEY_ACCOUNT,account);
        }

        if (name != null){
            editor.putString(KEY_NAME,name);
        }

        if (pwd != null){
            editor.putString(KEY_PWD,pwd);
        }

        editor.commit();
    }
}
