package com.imzoee.caikid.utils.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.imzoee.caikid.convention.ConstConv;

/**
 * Created by zoey on 2016/4/21.
 */
public class UserServices {

    private UserApiInterface userApiInterface;

    public UserServices(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstConv.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userApiInterface = retrofit.create(UserApiInterface.class);
    }

    public UserApiInterface getUserApiInterface() {
        return userApiInterface;
    }
/*
    public static Call<JSONObject> login(String account, String pwd){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        UserApiInterface i = retrofit.create(UserApiInterface.class);

        return i.login(account,pwd);
    }
*/
    /*
    public static int login( String account,
                             String pwd
                            ){

        String pwdMD5 = EncryptUtils.getMD5(pwd);    // pass this pwdMD5 as pwd field to the server, http secure

        // debug
        try {
            Thread.sleep(100);  // emulate network intency
        } catch (Exception e) {
            Log.e("DEBUG", e.toString());
        }

        if (account.equals("zoey")){
            if (pwd.equals("2b2b2b")){
                return LoginConv.LOGIN_SUCCESSED;
            } else {
                return LoginConv.LOGIN_PWD_INCORRECT;
            }
        } else {
            return LoginConv.LOGIN_USER_NOT_EXIT;
        }
    }
*/
/*
    public static int logOut( int id ){

        return 0;
    }
*/
/*
    public static int signUp( int id,
                              String account,
                              String name,
                              String pwd
                            ){

        try {
            Thread.sleep(100);  // emulate network intency
        } catch (Exception e) {
            Log.e("DEBUG", e.toString());
        }

        if(account.equals("zoey")){
            return SignUpConv.SIGNUP_ACCOUNT_USED;
        } else if (name.equals("zoey")) {
            return SignUpConv.SIGNUP_NAME_USED;
        } else {
            return SignUpConv.SIGNUP_SUCCESSED;
        }
    }
*/
    /*
    public static User getUser( int id,
                               String account,
                               String name,
                               String pwd
                            ) {

        try {
            Thread.sleep(100);  // emulate network intency
        } catch (Exception e) {
            Log.e("DEBUG", e.toString());
        }

        User user = new User( 0, "zoey", pwd, "zoey", 0 );
        return user;
    }
    */
}
