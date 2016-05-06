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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.imzoee.caikid.dao.User;
import com.imzoee.caikid.utils.misc.ObservablesFactory;
import com.imzoee.caikid.utils.preferences.Settings;
import com.imzoee.caikid.utils.preferences.UserPref;
import com.rey.material.widget.Switch;
import com.squareup.picasso.Picasso;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import rx.Subscriber;

import com.imzoee.caikid.R;
import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.activity.LoginActivity;
import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.utils.api.HttpClient;
import com.imzoee.caikid.utils.api.UserApiInterface;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnMeFragmentListener} interface
 * to handle interaction events.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    private static MeFragment instance = null;

    private OnMeFragmentListener mListener;

    /* global preference references */
    private UserPref userPref = null;
    private Settings settings = null;
    private HttpClient httpClient = null;

    /* view references */
    private View view;
    private RelativeLayout rlBottomLayer = null;
    private LinearLayout llUserInfo = null;
    private ImageView avatar = null;
    private TextView tvUnderAvatar = null;
    private Button btRegLogout = null;
    private TextView tvUserName = null;
    private TextView tvCredit = null;
    private LinearLayout llInfoCard = null;

    /* setting panel view references */
    private Switch swAutoLogin = null;

    public MeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        userPref = BaseApp.getUserPref();
        settings = BaseApp.getSettings();
        httpClient = BaseApp.getHttpClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_main_me, container, false);
        initView();
        initData();
        initListener();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMeFragmentListener) {
            mListener = (OnMeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMeFragmentListener");
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_avatar:
                if(settings.isLogin()) {
                    /* if already login, we start the activity for modified user's preference */
                } else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.bt_register_logout:
                if(settings.isLogin()) {

                    UserApiInterface i = httpClient.getUserApiInterface();
                    Call<ResponseBody> logoutCall = i.logout();
                    logoutCall.enqueue(new LogoutCallBack());

                } else {
                    /* if the current state is not login, then here is the register button */
                }
                break;

            default:
                break;
        }
    }

    private void initView(){
        rlBottomLayer = (RelativeLayout) view.findViewById(R.id.rl_bottom_layer_not_login);
        llUserInfo = (LinearLayout) view.findViewById(R.id.ll_user_info_panel);
        avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        tvUnderAvatar = (TextView) view.findViewById(R.id.tv_underAvatar);
        btRegLogout = (Button) view.findViewById(R.id.bt_register_logout);
        tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        tvCredit = (TextView) view.findViewById(R.id.tv_credit);
        llInfoCard = (LinearLayout) view.findViewById(R.id.ll_user_info_card);
        swAutoLogin = (Switch) view.findViewById(R.id.sw_auto_login);
    }

    private void initData(){
        if(settings.isLogin()) {
            rlBottomLayer.setVisibility(View.GONE);
            llUserInfo.setVisibility(View.VISIBLE);
            tvUnderAvatar.setVisibility(View.GONE);
            btRegLogout.setText(getString(R.string.logout));
            tvUserName.setText(userPref.getPfUserName());
            tvCredit.setText(String.valueOf(userPref.getPfUserCredit()));
            llInfoCard.setVisibility(View.VISIBLE);
            Picasso.with(getContext())
                    .load(userPref.getPfAvatarUrl())
                    .transform(new CropCircleTransformation())
                    .into(avatar);

            swAutoLogin.setChecked(settings.isAutoLogin());

        } else {
            rlBottomLayer.setVisibility(View.VISIBLE);
            llUserInfo.setVisibility(View.GONE);
            tvUnderAvatar.setVisibility(View.VISIBLE);
            btRegLogout.setText(getString(R.string.avatar_sign_up));
            llInfoCard.setVisibility(View.GONE);
            Picasso.with(getContext()).load(R.drawable.avatar_default)
                    .transform(new CropCircleTransformation())
                    .into(avatar);
        }

    }

    private void initListener(){
        avatar.setOnClickListener(this);
        btRegLogout.setOnClickListener(this);
        swAutoLogin.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                settings.setAutoLogin(checked);
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
    public interface OnMeFragmentListener {
        // TODO: Update argument type and name
        void onMeInteraction(Uri uri);
    }


    /**********************************************************************************************/
    /******************************** below are some private class ********************************/

    /*
     * retrofit callback, set up in the logout request enqueue() method, invoke after request send
     * and response is back;
     */
    private class LogoutCallBack implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            Headers headers = response.headers();
            String status = headers.get(ConstConv.HEADKEY_RESPONSTATUS);

            Log.i("-----------------------", status);

            if (status == null){
                /* if code arrived here, it means the server didn't set the status header */
                Toast.makeText(getContext(),
                        getString(R.string.msg_status_header_null),
                        Toast.LENGTH_LONG).show();
            } else if (status.equals(ConstConv.RET_STATUS_OK)) {
                settings.setLoginStatus(false);

                /* send a observable to alert logout */
                ObservablesFactory.loginStateObservable(null);

            } else if(status.equals(ConstConv.RET_STATUS_SESSIONNOTEXIST)){
                /* the session is not valid, maybe the session is timeout, or the account login in another device */
                Toast.makeText(getContext(),
                        getString(R.string.msg_session_valid),
                        Toast.LENGTH_LONG).show();
            } else if(status.equals(ConstConv.RET_STATUS_TIMEOUT)){
                /* the connection is time out */
                Toast.makeText(getContext(),
                        getString(R.string.msg_time_out),
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(),
                        getString(R.string.msg_unknown_ret_status),
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Toast.makeText(getContext(), getString(R.string.msg_connect_error), Toast.LENGTH_LONG)
                    .show();
        }
    }

    /* rxjava's subscriber */
    private class LoginStateSubscriber extends Subscriber<User> {
        @Override
        public void onCompleted() {
            if(!isUnsubscribed()) {
                this.unsubscribe();
            }
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getContext(), "error in observable,Mefragment", Toast.LENGTH_LONG)
                    .show();
            Log.i("----------------------", e.getMessage());
        }

        @Override
        public void onNext(User user) {
            initData();
        }
    }
}
