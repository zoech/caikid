package com.imzoee.caikid.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.dao.Recipe;
import com.imzoee.caikid.utils.api.HttpClient;
import com.imzoee.caikid.utils.api.RecipeApiInterface;
import com.imzoee.caikid.utils.misc.ObservablesFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by zoey on 2016/5/9.
 *
 * The model holding contents in cart
 */
public class CaikidCart {

    /* the same preference as the userPreference. */
    private final static String PREFERNAME = "userPreferences";
    private final static String KEYCART = "caikidcart";

    private static CaikidCart instance = null;

    private Context context = null;
    private HttpClient httpClient = null;

    private double totallPrice = 0;
    List<CartItem> itemList = null;

    public static CaikidCart getCart(Context context){
        if(instance == null) {
            instance = new CaikidCart(context);
        }
        return instance;
    }

    private CaikidCart(Context context){
        itemList = new ArrayList<>();
        totallPrice = 0;
        this.context = context;
        this.httpClient = BaseApp.getHttpClient();
    }

    public int getItemCount(){
        return this.itemList.size();
    }

    public double getTotallPrice(){
        return this.totallPrice;
    }

    public void addItem(Recipe recipe){
        for(int pos = 0; pos < itemList.size(); ++pos){
            if(itemList.get(pos).getRecipeId() == recipe.getId()){
                increaseItem(pos);
                return;
            }
        }

        CartItem item = new CartItem(recipe);
        this.itemList.add(item);
        this.totallPrice += item.getRecipe().getPrice();
    }

    public void increaseItem(int pos){
        CartItem i = itemList.get(pos);
        i.increase();
        this.totallPrice += i.getRecipe().getPrice();
    }

    public void decreaseItem(int pos){
        CartItem i = itemList.get(pos);
        this.totallPrice -= i.getRecipe().getPrice();
        i.decrease();
        if(i.getCount() == 0){
            this.itemList.remove(pos);
        }
    }

    public void removeItem(int pos){
        this.totallPrice -= this.itemList.get(pos).getPrice();
        this.itemList.remove(pos);
    }

    /**
     * Init the cart from the sharepreference.
     */
    public void initFromSPF(){
        SharedPreferences preferences = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE);
        String jstr = preferences.getString(KEYCART,null);
        if (jstr == null){
            return;
        }

        final List<Integer> idList = JSON.parseArray(jstr, Integer.class);

        Observable<CaikidCart> obs = Observable.create(new Observable.OnSubscribe<CaikidCart>(){
            @Override
            public void call(Subscriber<? super CaikidCart> subscriber) {
                Iterator<Integer> iterator = idList.iterator();
                while(iterator.hasNext()){

                    int rid = iterator.next();

                    RecipeApiInterface i = httpClient.getRecipeApiInterface();
                    Call<Recipe> getRecipeById = i.getRecipeById(rid);

                    Recipe recipe = null;
                    try {
                        recipe = getRecipeById.execute().body();
                    } catch (IOException e){
                        Log.i("----------", "the getRecipeById api connect return exception,in CaikidCart.obtainRecipe(" + rid + ")");
                    }

                    if(recipe != null){
                        CartItem item = new CartItem(recipe);
                        itemList.add(item);
                    }
                }

                subscriber.onNext(CaikidCart.this);
                subscriber.onCompleted();
            }
        });

        ObservablesFactory.cartActionObservable(obs);
    }

    /**
     * Store the current cart state to sharepreference.
     */
    public void storeIntoSPF(){
        List<Integer> idList = new ArrayList<>();
        Iterator<CartItem> iterator = itemList.iterator();
        while(iterator.hasNext()){
            CartItem i = iterator.next();
            idList.add(i.getRecipeId());
        }

        String jstr = JSON.toJSONString(idList);
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEYCART, jstr).apply();
    }


    public class CartItem{

        private int id;
        private Recipe recipeHolder;
        private int count;

        public CartItem(int id){
            this.id = id;
            this.recipeHolder = null;
            this.count = 1;
        }

        public CartItem(Recipe recipe){
            this.id = recipe.getId();
            this.recipeHolder = recipe;
            this.count = 1;
        }

        public int getRecipeId(){
            return this.id;
        }

        public Recipe getRecipe(){
            return this.recipeHolder;
        }

        public int getCount(){
            return this.count;
        }

        public double getPrice(){
            return this.recipeHolder.getPrice() * this.count;
        }

        public void increase(){
            this.count++;
        }

        public void decrease(){
            this.count--;
        }



    }
}
