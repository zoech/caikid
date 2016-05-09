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

    /**
     * Get a list of recipe, according to the pages, type.
     *
     * @param page
     * When we show a list of recipe on android, we could not
     * show the list of the whole recipes stored in db, and
     * therefore we show them page by page. And this parameter
     * indicate the required page.
     *
     * @param type
     * The type of recipe the user want to filter.
     *
     * @return
     * List of the recipe.
     */
    @GET("recipe/getList")
    Call<List<Recipe>> getRecipe(@Query(ConstConv.RESKEY_RECIPE_PAGE) int page,
                                 @Query(ConstConv.RESKEY_RECIPE_TYPE) String type);

    /**
     * Access a concrete recipe by its id.
     *
     * @param recipeId
     * The id of the recipe you want to get.
     *
     * @return
     * The required recipe.
     */
    @GET("recipe/getById")
    Call<Recipe> getRecipeById(@Query(ConstConv.RESKEY_ID) int recipeId);

}
