package com.imzoee.caikid.utils.api;

import com.alibaba.fastjson.JSONArray;
import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.User;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zoey on 2016/5/6.
 *
 * Retrofit2 interface related to recipe query.
 */
public interface RecipeApiInterface {
    @POST("recipe/get")
    Call<JSONArray> getRecipe(@Query(ConstConv.RESKEY_RECIPE_PAGE) int page,
                              @Query(ConstConv.RESKEY_RECIPE_TYPE) String type);

}
