package com.imzoee.caikid.convention;

/**
 * Created by zoey on 2016/4/24.
 */
public class LogoutConv {
    /*
    * RET, result
    */
    public static final String RET_STATUS_OK = "ok";
    public static final String RET_STATUS_SESSIONERR = "session-invalid";
    public static final String RET_STATUS_IDNOTEXIST = "userID-notexist";
    public static final String RET_STATUS_ACCOUNTERR = "account-not-match";
    public static final String RET_STATUS_PWDERR = "pwd-incorrect";

    /*
     * RES, request
     */
    public static final String RESKEY_SESSIONID = "sessionid";
    public static final String RESKEY_USERID = "id";
    public static final String RESKEY_ACCOUNT = "account";
    public static final String RESKEY_PWD = "pwd";

    /*
     * RSP, response
     */
    public static final String RSPKEY_STATUS = "status";
}
