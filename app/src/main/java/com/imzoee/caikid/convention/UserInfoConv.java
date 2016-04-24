package com.imzoee.caikid.convention;

/**
 * Created by zoey on 2016/4/24.
 */
public class UserInfoConv {
    /*
    * RET, result
    */
    public static final String RET_STATUS_OK = "ok";
    public static final String RET_STATUS_SESSIONERR = "session-invalid";
    public static final String RET_STATUS_IDNOTEXIST = "userID-notexist";

    /*
     * RES, request
     */
    public static final String HEADKEY_SESSIONID = "sessionid";
    public static final String RESKEY_USERID = "id";
    public static final String RESKEY_ACCOUNT = "account";
    public static final String RESKEY_PWD = "pwd";

    /*
     * RSP, response
     */
    public static final String RSPKEY_STATUS = "status";
    public static final String RSPKEY_USERID = "id";
    public static final String RSPKEY_USERACCOUNT = "account";
    public static final String RSPKEY_USERNAME = "name";
    public static final String RSPKEY_USERPHONE = "phone";
    public static final String RSPKEY_USEREMAIL = "email";
    public static final String RSPKEY_USERCREDIT = "credit";
}
