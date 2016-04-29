package com.imzoee.caikid.utils.misc;

/**
 * Created by zoey on 2016/4/29.
 */
public class TextCheck {
    public static boolean validAccount(String account){
        if(account.contains(" ")){
            return false;
        }
        return true;
    }

    public static boolean validPwd(String pwd){
        for(int i = 0; i < pwd.length(); ++i){
            if(pwd.charAt(i) < 33 || pwd.charAt(i) > 126){
                return false;
            }
        }
        return true;
    }
}
