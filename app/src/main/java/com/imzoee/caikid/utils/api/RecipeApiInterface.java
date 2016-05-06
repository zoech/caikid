package com.imzoee.caikid.utils.api;

import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zoey on 2016/5/6.
 *
 * Retrofit2 interface related to recipe query.
 */
public interface RecipeApiInterface {
    @GET("recipe/get")
    Call<List<Recipe>> getRecipe(@Query(ConstConv.RESKEY_RECIPE_PAGE) int page,
                                 @Query(ConstConv.RESKEY_RECIPE_TYPE) String type);

}
