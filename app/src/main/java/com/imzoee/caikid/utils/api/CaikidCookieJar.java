package com.imzoee.caikid.utils.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by zoey on 2016/4/25.
 *
 * Special CookieJar, used to handle cookie for our HttpClient.
 * For more details, read the docs for okHttp and retrofit2.
 */
public class CaikidCookieJar implements CookieJar{

    /** currently, this client only serve for our app, which means that this httpclient may
     * only access our server specified by ConstConv.API_URL. And therefore we dont hava
     * to handle multi site cookie saving, we only save the cookies used by our server.
     * If some time whenever we need to used this app to access different site,change
     * the line follow to :
     *      private final HashMap<String, Cookie> cookies = new HashMap<String, Cookie>();
     * and for the <String,Cookie> key-value pair in the map, the Strin is the host to
     * a specified server, we can get this String by using:
     *      String host = url.host();
     */
    private final List<Cookie> cookieList = new ArrayList<>();

    public CaikidCookieJar(){

    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

        if (cookieList.isEmpty()){
            cookieList.addAll(cookies);
            return;
        }

        //for ( int i = 0; i < cookies.size())
        Iterator<Cookie> iterator = cookies.iterator();
        while(iterator.hasNext()){
            Cookie c = iterator.next();
            String key = c.name();

            int index;
            int size = cookieList.size();
            for(index = 0; index < size; ++index){
                if (key.equals(cookieList.get(index).name())){
                    break;
                }
            }

            if (index < size){
                cookieList.set(index, c);
            } else {
                cookieList.add(c);
            }

        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
         return cookieList;
    }
}
