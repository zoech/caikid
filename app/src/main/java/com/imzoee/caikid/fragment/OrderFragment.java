package com.imzoee.caikid.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import rx.Subscriber;

import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.R;
import com.imzoee.caikid.dao.User;


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

        instance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_main_order, container, false);
        initView();
        initData();
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
