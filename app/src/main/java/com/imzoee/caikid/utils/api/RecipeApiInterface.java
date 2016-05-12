package com.imzoee.caikid.utils.api;

import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.Recipe;
import com.imzoee.caikid.model.ShopAddr;

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
     * @param address
     * The shop address, pass "all" if don't care about this.
     * @param type
     * The type of recipe the user want to filter.
     * @param orderBy
     * Define what should be the key to order this recipe list.
     *
     * @return
     * List of the recipe.
     */
    @GET("getRecipeList.php")
    Call<List<Recipe>> getRecipe(@Query(ConstConv.RESKEY_RECIPE_PAGE) int page,
                                 @Query(ConstConv.RESKEY_SHOP_ADDR) String address,
                                 @Query(ConstConv.RESKEY_RECIPE_TYPE) String type,
                                 @Query(ConstConv.RESKEY_ORDER_BY) String orderBy);

    /**
     * Access a concrete recipe by its id.
     *
     * @param recipeId
     * The id of the recipe you want to get.
     *
     * @return
     * The required recipe.
     */
    @GET("getRecipeById.php")
    Call<Recipe> getRecipeById(@Query(ConstConv.RESKEY_ID) int recipeId);

    /**
     *
     * @return
     * Return a list of all shop address.
     */
    @GET("getAllAddr.php")
    Call<List<ShopAddr>> getShopAddrList();

}
