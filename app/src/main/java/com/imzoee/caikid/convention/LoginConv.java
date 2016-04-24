package com.imzoee.caikid.convention;

/**
 * Created by zoey on 2016/4/21.
 */
public class LoginConv {
    /*
     * RET, result
     */
    public static final String RET_STATUS_OK = "ok";
    public static final String RET_STATUS_RELOGIN = "relogin";
    public static final String RET_STATUS_USERNOTEXIST = "account-not-exist";
    public static final String RET_STATUS_PWDERR = "pwd-incorrect";

    /*
     * RES, request
     */
    public static final String RESKEY_ACCOUNT = "account";
    public static final String RESKEY_PWD = "pwd";

    /*
     * RSP, response
     */
    public static final String RSPKEY_STATUS = "status";
    public static final String RSPKEY_USERID = "id";
    public static final String RSPKEY_SESSIONID = "sessionid";

}
