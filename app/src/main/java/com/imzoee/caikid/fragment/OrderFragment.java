package com.imzoee.caikid.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;

import com.alibaba.fastjson.JSON;
import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.R;
import com.imzoee.caikid.activity.OrderDetailActivity;
import com.imzoee.caikid.adapter.OrderAdapter;
import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.Order;
import com.imzoee.caikid.dao.User;
import com.imzoee.caikid.utils.api.FuncApiInterface;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnOrderFragmentListener} interface
 * to handle interaction events.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    private static OrderFragment instance;

    private OnOrderFragmentListener mListener;

    /* view preferences */
    private View view = null;
    private RelativeLayout rlUnlogin = null;
    private RelativeLayout rlLogin = null;
    private ListView lvOrders = null;

    private OrderAdapter orderAdapter = null;
    private List<Order> listOrder= null;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listOrder = new ArrayList<>();

        instance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_main_order, container, false);
        initView();
        initData();
        initLogic();

        if(BaseApp.getSettings().isLogin()) {
            getOrderList();
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOrderFragmentListener) {
            mListener = (OnOrderFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOrderFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initData();
        //getView().invalidate();
    }

    private void initView(){
        rlLogin = (RelativeLayout) view.findViewById(R.id.rl_login);
        rlUnlogin = (RelativeLayout) view.findViewById(R.id.rl_unlogin);
        lvOrders = (ListView) view.findViewById(R.id.lv_order);
    }

    private void initData(){
        if(BaseApp.getSettings().isLogin()){
            rlLogin.setVisibility(View.VISIBLE);
            rlUnlogin.setVisibility(View.GONE);
        } else {
            rlLogin.setVisibility(View.GONE);
            rlUnlogin.setVisibility(View.VISIBLE);
        }
    }

    private void initLogic(){
        orderAdapter = new OrderAdapter(getContext(), listOrder);
        lvOrders.setAdapter(orderAdapter);

        lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = orderAdapter.getItem(position);
                String recipeJsonStr = JSON.toJSONString(order);
                Intent intent = new Intent(getContext(), OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.INTENT_KEY_ORDER, recipeJsonStr);
                startActivity(intent);
            }
        });
    }

    private void getOrderList(){
        FuncApiInterface i = BaseApp.getHttpClient().getFuncApiInterface();
        Call<List<Order>> getRecipe = i.getOrderList("date");
        getRecipe.enqueue(new Callback<List<Order>>(){
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                Headers headers = response.headers();
                String status = headers.get(ConstConv.HEADKEY_RESPONSTATUS);

                Log.i("--------------", headers.toString());

                if (status == null){
                    Toast.makeText(getContext(),
                            getString(R.string.msg_status_header_null),
                            Toast.LENGTH_LONG).show();
                } else if (status.equals(ConstConv.RET_STATUS_OK)){

                    listOrder = response.body();
                    orderAdapter.setOrderList(listOrder);
                    orderAdapter.notifyDataSetChanged();

                } else if (status.equals(ConstConv.RET_STATUS_TIMEOUT)){
                    Toast.makeText(getContext(),
                            getString(R.string.msg_time_out),
                            Toast.LENGTH_LONG).show();
                }

                else {
                    Toast.makeText(getContext(),
                            getString(R.string.msg_unknown_ret_status),
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

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

    private Subscriber<User> createLoginStateSubscriber(){
        return new LoginStateSubscriber();
    }

    public static Subscriber<User> getLoginStateSubscriber(){
        if(instance == null){
            return null;
        }
        return instance.createLoginStateSubscriber();
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
    public interface OnOrderFragmentListener {
        // TODO: Update argument type and name
        void onOrderInteraction(Uri uri);
    }



    /************************************************************************************************/
    /**************************** below are some private classes ************************************/

    private class LoginStateSubscriber extends Subscriber<User> {
        @Override
        public void onNext(User user) {
            initData();

            if(user != null){
                getOrderList();
            }
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getContext(), "error in observer", Toast.LENGTH_LONG)
                    .show();
            Log.i("----------------------", e.getMessage());
        }

        @Override
        public void onCompleted() {
            if(!isUnsubscribed()) {
                this.unsubscribe();
            }
        }
    }
}
