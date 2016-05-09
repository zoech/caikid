package com.imzoee.caikid.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.imzoee.caikid.utils.api.HttpClient;
import com.imzoee.caikid.utils.api.RecipeApiInterface;
import com.rey.material.widget.Spinner;

import java.util.ArrayList;
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

    View view = null;
    PullToRefreshListView pullToRefreshListView = null;
    ListView lvRecipe = null;
    Spinner shopSpinner = null;

    HttpClient httpClient = BaseApp.getHttpClient();
    RecipeAdapter recipeAdapter = null;

    List<Recipe> recipeList = null;
    int recipePage = 1;

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
/*        Bundle args = new Bundle();

        fragment.setArguments(args);*/

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
    }

    private void initData(){
        recipeList = new ArrayList<>();

        String[] items = new String[5];
        for(int i = 0; i < 5; ++i){
            items[i] = "shop" + i;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner_shop, items);
        adapter.setDropDownViewResource(R.layout.spinner_drop_down);
        shopSpinner.setAdapter(adapter);

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
    }

    // TODO: Rename method, update argument and hook method into UI event
/*    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMeInteraction(uri);
        }
    }*/

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

            Call<List<Recipe>> getRecipe = i.getRecipe(recipePage, "all");
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
                                getString(R.string.msg_unknown_ret_status),
                                Toast.LENGTH_LONG).show();
                    }

                    pullToRefreshListView.onRefreshComplete();
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    pullToRefreshListView.onRefreshComplete();
                    Toast.makeText(getContext(), getString(R.string.msg_connect_error), Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
    }

    public void refreshFirstPage(){
        RecipeApiInterface i = httpClient.getRecipeApiInterface();
        Call<List<Recipe>> getRecipe = i.getRecipe(0, "all");
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
                } else {
                    Toast.makeText(getContext(),
                            getString(R.string.msg_unknown_ret_status),
                            Toast.LENGTH_LONG).show();
                }

                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                pullToRefreshListView.onRefreshComplete();
                Toast.makeText(getContext(), getString(R.string.msg_connect_error), Toast.LENGTH_LONG)
                        .show();
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
}
