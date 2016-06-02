package com.imzoee.caikid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.R;
import com.rey.material.widget.Button;

/**
 * Created by zoey on 2016/6/2.
 *
 * Personal information modified activity.
 */
public class ProfileEditActivity extends AppCompatActivity{

    /* view references */
    private EditText etUserName = null;
    private Button btOk = null;
    private Button btCancel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        initView();
        initData();
        initListener();
    }

    public void initView(){
        etUserName = (EditText) findViewById(R.id.et_user_name_content);
        btOk = (Button) findViewById(R.id.bt_ok);
        btCancel = (Button) findViewById(R.id.bt_cancel);
    }

    public void initData(){
        etUserName.setText(BaseApp.getUserPref().getPfUserName());
    }

    public void initListener(){
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* set the new information */
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* drop the modification */
                finish();
            }
        });
    }

}
