package com.imzoee.caikid.convention;

/**
 * Created by zoey on 2016/4/23.
 *
 * These static string is some key or values related to networking.
 */
public class ConstConv {
    /* the server ip */
    //public static final String API_URL = "http://110.64.86.208:8080/caikid/app/";
	public static final String API_URL = "http://192.168.1.119/app/";

	/* the image storing server ip, when use the img url to obtain img, use this ip as prefix */
	public static final String IMGPATH_URLPREFIX = "http://192.168.1.119/";

    //public static final String HEADKEY_SESSIONID = "Caikid-SessionID";
    public static final String HEADKEY_RESPONSTATUS = "Caikid-ResponseStatus";

    public static final String HEADKEY_ACCOUNT = "caikid-account";
    public static final String HEADKEY_ID = "caikid-id";
    public static final String HEADKEY_PWD = "caikid-pwd";


	public static final String AVATAR_DEAFAULT = "avatar-default";
	
	
	/* request key */
	public static final String RESKEY_ID = "id";
	public static final String RESKEY_ACCOUNT = "account";
	public static final String RESKEY_NAME = "name";
	public static final String RESKEY_PWD = "pwd";
	public static final String RESKEY_VERIFYCODE = "verify-code";

	/* request key related to recipe */
    public static final String RESKEY_RECIPE_PAGE = "recipe-page";
    public static final String RESKEY_RECIPE_TYPE = "recipe-type";
	public static final String RESKEY_ORDER_BY = "recipe-order";
	public static final String RESKEY_SHOP_ADDR= "recipe-shop";
	
	
	/* response status code */
	public static final String RET_STATUS_SESSIONNOTEXIST = "session-not-exist";
	public static final String RET_STATUS_OK = "ok";
	public static final String RET_STATUS_IDNULL = "id-null";
	public static final String RET_STATUS_ACCOUNTNULL = "account-null";
	public static final String RET_STATUS_NAMENULL = "name-null";
	public static final String RET_STATUS_PWDNULL = "pwd-null";
	public static final String RET_STATUS_ACCOUNTREGED = "account-registered";
	public static final String RET_STATUS_ACCOUNTINVALID = "account-invalid";
	public static final String RET_STATUS_NAMEUSED = "name-used";
	public static final String RET_STATUS_NAMEINVALID = "name-invalid";
	public static final String RET_STATUS_PWDERR = "pwd-error";
	public static final String RET_STATUS_VERIFYERR = "verify-error";
	public static final String RET_STATUS_TIMEOUT = "verify-timeout";
	public static final String RET_STATUS_NOTSIGNUPSESSION = "not-signup-session";
	public static final String RET_STATUS_RELOGIN = "relogin";
	public static final String RET_STATUS_USERNOTEXIST = "user-not-exist";
	public static final String RET_STATUS_NOMORE_CONTENTS = "no-more-content";
	public static final String RET_STATUS_PAGE_OUT = "page-out";


    /* request code for recipe list access */
    public static final String RESRECIPE_TYPECODE_ALL = "all";
    public static final String RESRECIPE_ORDERBYCODE_NONE = "default";


    /*
     * following are some key or value used to debug. These fileds will be
     * remove after the app has completed.
     */
    public static final String RESKEY_TEST1 = "test-request-key1";
    public static final String RESKEY_TEST2 = "test-request-key2";
    public static final String RESKEY_TEST3 = "test-request-key3";

    public static final String HEADKEY_TEST1 = "test-header-key1";
    public static final String HEADKEY_TEST2 = "test-header-key2";
    public static final String HEADKEY_TEST3 = "test-header-key3";
}
