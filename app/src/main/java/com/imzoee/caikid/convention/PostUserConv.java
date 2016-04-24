package com.imzoee.caikid.convention;

/**
 * Created by zoey on 2016/4/24.
 */
public class PostUserConv {
    /*
   * RET, result
   */
    public static final String RET_STATUS_OK = "ok";
    public static final String RET_STATUS_SESSIONERR = "session-invalid";
    public static final String RET_STATUS_IDNOTEXIST = "userID-notexist";
    public static final String RET_STATUS_PWDERR = "pwd-incorrect";

    /*
     * RES, request
     */
    public static final String RESKEY_USERID = "userid";
    public static final String RESKEY_ACCOUNT = "account";
    public static final String RESKEY_PWD = "pwd";

    public static final String RESKEY_NEWNAME = "newname";
    public static final String RESKEY_NEWPWD = "newpwd";
    public static final String RESKEY_NEWCREDIT = "newcredit";
    public static final String RESKEY_NEWPHONE = "newphone";
    public static final String RESKEY_NEWEMAIL = "newemail";

    /*
     * RSP, response
     */
    public static final String RSPKEY_STATUS = "status";
    public static final String RSPKEY_ID = "id";
    public static final String RSPKEY_ACCOUNT = "account";
    public static final String RSPKEY_PWD = "pwd";
    public static final String RSPKEY_NAME = "name";
    public static final String RSPKEY_PHONE = "phone";
    public static final String RSPKEY_EMAIL = "email";
    public static final String RSPKEY_CREDIT = "credit";
}
