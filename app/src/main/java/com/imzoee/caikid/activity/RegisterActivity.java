package com.imzoee.caikid.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.R;
import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.utils.api.HttpClient;
import com.imzoee.caikid.utils.api.UserApiInterface;
import com.rey.material.widget.Button;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ScrollView registerForm = null;

    ProgressBar progressBar = null;
    private Button btVerify = null;
    private Button btRegister = null;
    private EditText etAccount = null;
    private EditText etName = null;
    private EditText etPwd = null;
    private EditText etVerifyCode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initListener();
    }

    private void initView(){
        registerForm = (ScrollView) findViewById(R.id.register_form);

        progressBar = (ProgressBar) findViewById(R.id.register_progress);
        btVerify = (Button) findViewById(R.id.bt_verify);
        btRegister = (Button) findViewById(R.id.bt_register);
        etAccount = (EditText) findViewById(R.id.et_account);
        etPwd = (EditText) findViewById(R.id.et_password);
        etName = (EditText) findViewById(R.id.et_name);
        etVerifyCode = (EditText) findViewById(R.id.et_verify_code);
    }

    private void initListener(){
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAccount.setError(null);
                etPwd.setError(null);
                etName.setError(null);
                etVerifyCode.setError(null);

                String account = etAccount.getText().toString();
                String name = etName.getText().toString();
                String pwd = etPwd.getText().toString();
                String verifyCode = etVerifyCode.getText().toString();

                /* check these string */

                /* attempt login */
                HttpClient httpClient = BaseApp.getHttpClient();
                UserApiInterface i = httpClient.getUserApiInterface();
                Call<ResponseBody> signUp = i.signUp(account, pwd, name, verifyCode);

                showProgress(true);
                signUp.enqueue(new SignUpCallback());
            }
        });

        btVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAccount.setError(null);

                String account = etAccount.getText().toString();

                /* check account string */

                /* attempt verify */
                HttpClient httpClient = BaseApp.getHttpClient();
                UserApiInterface i = httpClient.getUserApiInterface();
                Call<ResponseBody> verify = i.verify(account);

                verify.enqueue(new VerifyCallback());
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
            registerForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    public class VerifyCallback implements retrofit2.Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            showProgress(false);

            Log.i("+++++++++++++++++++++", response.headers().toString());


            Headers headers = response.headers();
            String status = headers.get(ConstConv.HEADKEY_RESPONSTATUS);

            if(status != null && status.equals(ConstConv.RET_STATUS_OK)){

                CharSequence msg = "verify send successed!";
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "can not verify!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            showProgress(false);
            Toast.makeText(getBaseContext(), getString(R.string.msg_connect_error), Toast.LENGTH_LONG).show();
        }
    }

    public class SignUpCallback implements retrofit2.Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            showProgress(false);

            Headers headers = response.headers();
            String status = headers.get(ConstConv.HEADKEY_RESPONSTATUS);

            if(status != null && status.equals(ConstConv.RET_STATUS_OK)){

                CharSequence msg = "register successed!";
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "can not register!", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            showProgress(false);
            Toast.makeText(getBaseContext(), getString(R.string.msg_connect_error), Toast.LENGTH_LONG).show();
        }
    }
}
