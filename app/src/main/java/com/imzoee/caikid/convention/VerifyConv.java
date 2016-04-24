package com.imzoee.caikid.convention;

/**
 * Created by zoey on 2016/4/24.
 */
public class VerifyConv {

    /*
     * RET, result
     */
    public static final String RET_STATUS_OK = "ok";
    public static final String RET_STATUS_ACCOUNTREGED = "account-registered";
    public static final String RET_STATUS_NAMEUSED = "name-used";
    public static final String RET_STATUS_PWD2SHORT = "pwd-too-short";
    public static final String RET_STATUS_PWDFMTERR = "pwd-format-incorrect";

    /*
     * RES, request
     */
    public static final String RESKEY_ACCOUNT = "account";
    public static final String RESKEY_PWD = "pwd";
    public static final String RESKEY_NAME = "name";

    /*
     * RSP, response
     */
    public static final String RSPKEY_STATUS = "status";
    public static final String RSPKEY_TOKEN = "token";

}
