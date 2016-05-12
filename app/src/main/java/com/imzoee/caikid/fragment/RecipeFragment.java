package com.imzoee.caikid.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.R;
import com.imzoee.caikid.activity.RecipeDetailActivity;
import com.imzoee.caikid.adapter.RecipeAdapter;
import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.Recipe;
import com.imzoee.caikid.model.RecipeType;
import com.imzoee.caikid.model.ShopAddr;
import com.imzoee.caikid.utils.api.HttpClient;
import com.imzoee.caikid.utils.api.RecipeApiInterface;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.LinearLayout;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Spinner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRecipeFragmentListener} interface
 * to handle interaction events.
 * Use the {@link RecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment {
    private final static int AUTO_RROBTAIN_INTERVAL = 10000;

    PopupWindow popOrderRule = null;
    PopupWindow popTypeFilter = null;

    View view = null;
    PullToRefreshListView pullToRefreshListView = null;
    ListView lvRecipe = null;
    Spinner shopSpinner = null;
    LinearLayout llTypeFilter = null;
    LinearLayout llOrderRule = null;

    HttpClient httpClient = BaseApp.getHttpClient();
    RecipeAdapter recipeAdapter = null;

    List<Recipe> recipeList = null;
    List<String> recipeTypeList = null;
    int recipePage = 1;
    boolean firstObtain = true;
    String obtainAddress = null;
    String obtainType = null;
    String obtainOrderBy = null;

    private OnRecipeFragmentListener mListener;

    public RecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeFragment newInstance() {
        RecipeFragment fragment = new RecipeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_main_recipe, container, false);
        obtainAddress = ConstConv.RESRECIPE_TYPECODE_ALL;
        obtainType = ConstConv.RESRECIPE_TYPECODE_ALL;
        obtainOrderBy = ConstConv.RESRECIPE_ORDERBYCODE_NONE;

        initView();
        initData();
        initLogic();

        refreshFirstPage();

        return view;
    }

    private void initView(){
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.refreshlv_recipe);
        lvRecipe = pullToRefreshListView.getRefreshableView();
        shopSpinner = (Spinner) view.findViewById(R.id.spinner_shop_address);
        llTypeFilter = (LinearLayout) view.findViewById(R.id.ll_recipe_type_filter);
        llOrderRule = (LinearLayout) view.findViewById(R.id.ll_recipe_order_rule);
    }

    private void initData(){
        recipeList = new ArrayList<>();

        /* berfore we have obtain the type and addr list, we hide these two view to avoid user input */
        shopSpinner.setVisibility(View.GONE);
        llTypeFilter.setVisibility(View.GONE);

        RecipeApiInterface i = httpClient.getRecipeApiInterface();
        Call<List<ShopAddr>> getShopAddrList = i.getShopAddrList();
        getShopAddrList.enqueue(getAddrListCallback);

        recipeTypeList = new ArrayList<>();
        Call<List<RecipeType>> getRecipeTypeList = i.getRecipeTypeList();
        getRecipeTypeList.enqueue(getTypeListCallback);
    }

    public void initLogic(){

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        recipeAdapter = new RecipeAdapter(getContext(), recipeList);
        pullToRefreshListView.setAdapter(recipeAdapter);

        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Recipe recipe = recipeAdapter.getItem(position);
                String recipeJsonStr = JSON.toJSONString(recipe);
                Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailActivity.INTENT_KEY_RECIPE, recipeJsonStr);
                startActivity(intent);
            }
        });

        pullToRefreshListView.setOnRefreshListener(new RefreshListener());

        shopSpinner.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner parent, View view, int position, long id) {
                if(position == 0){
                    obtainAddress = ConstConv.RESRECIPE_TYPECODE_ALL;
                } else {
                    obtainAddress = (String) shopSpinner.getAdapter().getItem(position);
                }
                refreshFirstPage();

                return true;
            }
        });


        llTypeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "filter touch!", Toast.LENGTH_LONG).show();
                showFilterPopUp();
            }
        });

        llOrderRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "order touch!", Toast.LENGTH_LONG).show();
                showOrderPopUp();
            }
        });
    }

    /**
     * init and show the order rule popup window
     */
    private void showOrderPopUp(){
        View popView = LayoutInflater.from(getContext()).inflate(R.layout.pop_orderby_filter,null);

        popOrderRule = new PopupWindow(popView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);


        popOrderRule.setBackgroundDrawable(getResources().getDrawable(R.drawable.abc_popup_background_mtrl_mult));
        //popOrderRule.setBackgroundDrawable(new ColorDrawable(0x00000000));

        final RadioButton rbDefOrder = (RadioButton) popView.findViewById(R.id.rb_order_default);
        final RadioButton rbSalesOrder = (RadioButton) popView.findViewById(R.id.rb_order_sales);
        final RadioButton rbScoreOrder = (RadioButton) popView.findViewById(R.id.rb_order_score);
        final RadioButton rbPriceOrder = (RadioButton) popView.findViewById(R.id.rb_order_price);

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    rbDefOrder.setChecked(rbDefOrder == buttonView);
                    rbSalesOrder.setChecked(rbSalesOrder == buttonView);
                    rbScoreOrder.setChecked(rbScoreOrder == buttonView);
                    rbPriceOrder.setChecked(rbPriceOrder == buttonView);

                    if(rbDefOrder == buttonView){
                        obtainOrderBy = ConstConv.RESRECIPE_ORDERBYCODE_NONE;
                    } else if(rbSalesOrder == buttonView){
                        obtainOrderBy = ConstConv.RESRECIPE_ORDERBYCODE_SALES;
                    } else if(rbScoreOrder == buttonView){
                        obtainOrderBy = ConstConv.RESRECIPE_ORDERBYCODE_SCORE;
                    } else if(rbPriceOrder == buttonView){
                        obtainOrderBy = ConstConv.RESRECIPE_ORDERBYCODE_PRICE;
                    } else {
                        obtainOrderBy = ConstConv.RESRECIPE_ORDERBYCODE_NONE;
                    }

                    refreshFirstPage();
                }

            }
        };

        rbDefOrder.setOnCheckedChangeListener(listener);
        rbSalesOrder.setOnCheckedChangeListener(listener);
        rbScoreOrder.setOnCheckedChangeListener(listener);
        rbPriceOrder.setOnCheckedChangeListener(listener);

        popOrderRule.showAsDropDown(llOrderRule);

    }

    /**
     * init and show the type filter popup window
     */
    private void showFilterPopUp(){

        if (recipeTypeList == null){
            return;
        }

        View popView = LayoutInflater.from(getContext()).inflate(R.layout.pop_type_filter,null);
        com.rey.material.widget.ListView lvContent = (com.rey.material.widget.ListView) popView.findViewById(R.id.lv_content);
        final ArrayAdapter<String> typeListAdapter = new ArrayAdapter<>(getContext(), R.layout.item_pop_list, recipeTypeList);
        lvContent.setAdapter(typeListAdapter);

        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    obtainType = ConstConv.RESRECIPE_TYPECODE_ALL;
                } else {
                    obtainType = (String) typeListAdapter.getItem(position);
                }
                refreshFirstPage();
            }
        });

        popTypeFilter = new PopupWindow(popView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);


        popTypeFilter.setBackgroundDrawable(getResources().getDrawable(R.drawable.abc_popup_background_mtrl_mult));


        popTypeFilter.showAsDropDown(llTypeFilter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeFragmentListener) {
            mListener = (OnRecipeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private class RefreshListener implements PullToRefreshBase.OnRefreshListener2<ListView> {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            refreshFirstPage();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            RecipeApiInterface i = httpClient.getRecipeApiInterface();

            Call<List<Recipe>> getRecipe = i.getRecipe(recipePage, obtainAddress, obtainType, obtainOrderBy);
            getRecipe.enqueue(new Callback<List<Recipe>>(){
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    Headers headers = response.headers();
                    String status = headers.get(ConstConv.HEADKEY_RESPONSTATUS);

                    if (status == null){
                        Toast.makeText(getContext(),
                                getString(R.string.msg_status_header_null),
                                Toast.LENGTH_LONG).show();
                    } else if (status.equals(ConstConv.RET_STATUS_OK)){

                        List<Recipe> recipes = response.body();
                        recipeList.addAll(recipes);
                        recipeAdapter.setRecipesList(recipeList);
                        recipeAdapter.notifyDataSetChanged();

                        /* don't forget to increase the page number */
                        recipePage++;

                    } else if (status.equals(ConstConv.RET_STATUS_NOMORE_CONTENTS)){
                        Toast.makeText(getContext(),
                                getString(R.string.msg_recipe_no_more),
                                Toast.LENGTH_LONG).show();
                    }
                    else if (status.equals(ConstConv.RET_STATUS_TIMEOUT)){
                        Toast.makeText(getContext(),
                                getString(R.string.msg_time_out),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(),
                                getString(R.string.msg_unknown_ret_status) + status,
                                Toast.LENGTH_LONG).show();
                    }

                    pullToRefreshListView.onRefreshComplete();
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    pullToRefreshListView.onRefreshComplete();
                    if(t instanceof java.net.ConnectException){
                        CharSequence msg = getString(R.string.msg_connect_error);
                        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i("+++++++++++++++++++++++", t.toString());
                        t.printStackTrace();
                    }
                }
            });

        }
    }

    public void refreshFirstPage(){
        RecipeApiInterface i = httpClient.getRecipeApiInterface();
        Call<List<Recipe>> getRecipe = i.getRecipe(0, obtainAddress, obtainType, obtainOrderBy);
        getRecipe.enqueue(new Callback<List<Recipe>>(){
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Headers headers = response.headers();
                String status = headers.get(ConstConv.HEADKEY_RESPONSTATUS);

                Log.i("--------------", headers.toString());

                if (status == null){
                    Toast.makeText(getContext(),
                            getString(R.string.msg_status_header_null),
                            Toast.LENGTH_LONG).show();
                } else if (status.equals(ConstConv.RET_STATUS_OK)){

                    recipeList = response.body();
                    recipeAdapter.setRecipesList(recipeList);
                    recipeAdapter.notifyDataSetChanged();

                    /* note now we need to set the page to 1 */
                    recipePage = 1;

                } else if (status.equals(ConstConv.RET_STATUS_TIMEOUT)){
                    Toast.makeText(getContext(),
                            getString(R.string.msg_time_out),
                            Toast.LENGTH_LONG).show();
                } else if (status.equals(ConstConv.RET_STATUS_NOMORE_CONTENTS)){
                    Toast.makeText(getContext(),
                            getString(R.string.msg_recipe_nothing),
                            Toast.LENGTH_LONG).show();

                    recipeList.clear();
                    recipeAdapter.notifyDataSetChanged();
                }

                else {
                    Toast.makeText(getContext(),
                            getString(R.string.msg_unknown_ret_status),
                            Toast.LENGTH_LONG).show();
                }

                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                pullToRefreshListView.onRefreshComplete();

                if(t instanceof java.net.ConnectException){
                    CharSequence msg = getString(R.string.msg_connect_error);
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("+++++++++++++++++++++++", t.toString());
                    t.printStackTrace();
                }
            }
        });
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRecipeFragmentListener {
        // TODO: Update argument type and name
        void onRecipeInteraction(Uri uri);
    }


    Callback<List<ShopAddr>> getAddrListCallback = new Callback<List<ShopAddr>>(){
        @Override
        public void onResponse(Call<List<ShopAddr>> call, Response<List<ShopAddr>> response) {

            List<ShopAddr> shopList = response.body();
            if(shopList == null || shopList.size() == 0) {
                if(firstObtain) {
                    Toast.makeText(getContext(), getString(R.string.msg_cannot_access_shop_list), Toast.LENGTH_LONG).show();
                    firstObtain = false;
                }
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        RecipeApiInterface i = httpClient.getRecipeApiInterface();
                        Call<List<ShopAddr>> getShopAddrList = i.getShopAddrList();
                        getShopAddrList.enqueue(getAddrListCallback);
                    }
                }, AUTO_RROBTAIN_INTERVAL);
            } else {
                List<String> addrStrList = new ArrayList<>();
                addrStrList.add(getString(R.string.recipe_shop_addr_all));
                Iterator<ShopAddr> iterator = shopList.iterator();
                while(iterator.hasNext()){
                    ShopAddr shopAddr = iterator.next();
                    if(shopAddr.getAddrFlag()){
                        addrStrList.add(shopAddr.getAddrName());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner_shop, addrStrList);
                adapter.setDropDownViewResource(R.layout.spinner_drop_down);
                shopSpinner.setAdapter(adapter);

                shopSpinner.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onFailure(Call<List<ShopAddr>> call, Throwable t) {
            if(firstObtain) {
                Toast.makeText(getContext(), getString(R.string.msg_cannot_access_shop_list), Toast.LENGTH_LONG).show();
                firstObtain = false;
            }
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    RecipeApiInterface i = httpClient.getRecipeApiInterface();
                    Call<List<ShopAddr>> getShopAddrList = i.getShopAddrList();
                    getShopAddrList.enqueue(getAddrListCallback);
                }
            }, AUTO_RROBTAIN_INTERVAL);
        }
    };

    Callback<List<RecipeType>> getTypeListCallback = new Callback<List<RecipeType>>(){
        @Override
        public void onResponse(Call<List<RecipeType>> call, Response<List<RecipeType>> response) {

            List<RecipeType> types = response.body();
            if(types == null || types.size() == 0) {
                if(firstObtain) {
                    Toast.makeText(getContext(), getString(R.string.msg_cannot_access_type_list), Toast.LENGTH_LONG).show();
                    firstObtain = false;
                }
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        RecipeApiInterface i = httpClient.getRecipeApiInterface();
                        Call<List<RecipeType>> getRecipeTypeList = i.getRecipeTypeList();
                        getRecipeTypeList.enqueue(getTypeListCallback);
                    }
                }, AUTO_RROBTAIN_INTERVAL);
            } else {
                recipeTypeList.add(getString(R.string.recipe_type_all));
                Iterator<RecipeType> iterator = response.body().iterator();
                while(iterator.hasNext()){
                    recipeTypeList.add(iterator.next().getName());
                }

                llTypeFilter.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onFailure(Call<List<RecipeType>> call, Throwable t) {
            if(firstObtain) {
                Toast.makeText(getContext(), getString(R.string.msg_cannot_access_shop_list), Toast.LENGTH_LONG).show();
                firstObtain = false;
            }
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    RecipeApiInterface i = httpClient.getRecipeApiInterface();
                    Call<List<ShopAddr>> getShopAddrList = i.getShopAddrList();
                    getShopAddrList.enqueue(getAddrListCallback);
                }
            }, AUTO_RROBTAIN_INTERVAL);
        }
    };
}
