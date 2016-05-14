package com.imzoee.caikid.utils.misc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zoey on 2016/4/29.
 *
 * Interface related to text checking.
 */
public class TextCheck {
    /**
     * Determine whether a string is a valid password.
     *
     * @param str
     * The string that you want to judge if it's a valid password.
     *
     * @return
     * True if str is a valid password, otherwise false.
     */
    public static boolean validPwd(String str){
        //Pattern pattern = Pattern.compile("^[a-zA-Z]\\w{5,17}$");
        Pattern pattern = Pattern.compile("^(\\w|\\p{Graph}){6,18}$");
        Matcher m = pattern.matcher(str);
        return m.matches();
    }

    /**
     * Determine whether a string is a valid handset number.
     *
     * @param str
     * The string that you want to judge if it's a valid handset number.
     *
     * @return
     * True if str is a valid handset number, otherwise false.
     */
    public static boolean validHandset(String str){
        Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = pattern.matcher(str);
        return m.matches();
    }

    /**
     * Determine whether a string is a valid email address.
     *
     * @param str
     * The string that you want to judge if it's a valid email address.
     *
     * @return
     * True if str is a valid email address, otherwise false.
     */
    public static boolean validEmail(String str){
        Pattern pattern = Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher m = pattern.matcher(str);
        return m.matches();
    }
}
