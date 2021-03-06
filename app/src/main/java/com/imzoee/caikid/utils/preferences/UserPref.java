package com.imzoee.caikid.utils.preferences;

import android.content.SharedPreferences;
import android.content.Context;

import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.User;

/**
 * Created by zoey on 2016/4/21.
 *
 * Class used to store the login user's information.
 * Note that there should be only one instance of this
 * class during the whole app.
 */
public class UserPref {

    private static final String PREFERNAME = "userPreferences";

    private static final String KEY_ID = "id";
    private static final String KEY_ACCOUNT = "account";
    private static final String KEY_NAME = "name";
    private static final String KEY_PWD = "pwd";
    private static final String KEY_CREDIT = "credit";
    private static final String KEY_AVATAR = "avatar";

    private Context context = null;
    private User userHolder = null;

    public UserPref(Context context){

        this.context = context;
        userHolder = new User();

        SharedPreferences preferences = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE);

        int id = preferences.getInt(KEY_ID, -1);
        String account = preferences.getString(KEY_ACCOUNT,null);
        String name = preferences.getString(KEY_NAME, null);
        String pwd = preferences.getString(KEY_PWD, null);
        int credit = preferences.getInt(KEY_CREDIT, -1);
        String avatarUrl = preferences.getString(KEY_AVATAR, ConstConv.AVATAR_DEAFAULT);

        userHolder.setId(id);
        userHolder.setAccount(account);
        userHolder.setName(name);
        userHolder.setPwd(pwd);
        userHolder.setCredit(credit);
        userHolder.setAvatarUrl(avatarUrl);
    }

    public void setPfUserId(int id){
        if(id >= 0 && id != userHolder.getId()) {
            userHolder.setId(id);
            SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE).edit();
            editor.putInt(KEY_ID, id).apply();
        }
    }

    public int getPfUserId(){
        return userHolder.getId();
    }


    public void setPfUserAccount(String account){
        if(userHolder.getAccount() == null
                || ( account != null && !account.equals(userHolder.getAccount()))) {
            userHolder.setAccount(account);
            SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE).edit();
            editor.putString(KEY_ACCOUNT, account).apply();
        }
    }

    public String getPfUserAccount(){
        return userHolder.getAccount();
    }


    public void setPfUserName(String name){
        if(userHolder.getName() == null
                || ( name != null && !name.equals(userHolder.getName()))) {
            userHolder.setName(name);
            SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE).edit();
            editor.putString(KEY_NAME, name).apply();
        }
    }

    public String getPfUserName(){
        return userHolder.getName();
    }


    public void setPfUserPwd(String pwd){
        if(userHolder.getPwd() == null
                ||  ( pwd != null && !pwd.equals(userHolder.getPwd()))) {
            userHolder.setPwd(pwd);
            SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE).edit();
            editor.putString(KEY_PWD, pwd).apply();
        }
    }

    public String getPfUserPwd(){
        return userHolder.getPwd();
    }


    public void setPfUserCredit(int credit){
        if(credit > 0 && credit != userHolder.getCredit()) {
            userHolder.setCredit(credit);
            SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE).edit();
            editor.putInt(KEY_CREDIT, credit).apply();
        }
    }

    public int getPfUserCredit(){
        return userHolder.getCredit();
    }


    public void setPfAvatarUrl(String avatar){
        if(userHolder.getAvatarUrl() == null
                || ( avatar != null && !avatar.equals(userHolder.getAvatarUrl()))) {
            userHolder.setAvatarUrl(avatar);
            SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE).edit();
            editor.putString(KEY_AVATAR, avatar).apply();
        }
    }

    public String getPfAvatarUrl(){
        return userHolder.getAvatarUrl();
    }


    /*
     * update the all preferences of session user at one time
     */
    public void setPfUser(int id, String account, String name, String pwd, int credit, String avatar){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME,Context.MODE_PRIVATE).edit();

        if (id >= 0 && id != userHolder.getId()){
            userHolder.setId(id);
            editor.putInt(KEY_ID, id);
        }

        if (userHolder.getAccount() == null
                || ( account != null && !account.equals(userHolder.getAccount()))){
            userHolder.setAccount(account);
            editor.putString(KEY_ACCOUNT, account);
        }

        if (userHolder.getName() == null
                || ( name != null && !name.equals(userHolder.getName()))){
            userHolder.setName(name);
            editor.putString(KEY_NAME, name);
        }

        if (userHolder.getPwd() == null
                ||  ( pwd != null && !pwd.equals(userHolder.getPwd()))){
            userHolder.setPwd(pwd);
            editor.putString(KEY_PWD, pwd);
        }

        if (credit > 0 && credit != userHolder.getCredit()){
            userHolder.setCredit(credit);
            editor.putInt(KEY_CREDIT, credit);
        }

        if (userHolder.getAvatarUrl() == null
                || ( avatar != null && !avatar.equals(userHolder.getAvatarUrl()))){
            userHolder.setAvatarUrl(avatar);
            editor.putString(KEY_AVATAR, avatar);
        }

        editor.apply();
    }

    public void setPfUser(User user){
        if(user != null){
            setPfUser(user.getId(), user.getAccount(), user.getName(), user.getPwd(), user.getCredit(), user.getAvatarUrl());
        }
    }
}
