package com.reactiveapps.chatty.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.utils.DensityUtil;
import com.reactiveapps.chatty.view.ActivityUtils;
import com.reactiveapps.chatty.view.Base.ActivityBase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//登陆
public class ActivityLogin extends ActivityBase {
    @BindView(R.id.et_usertel)
    EditText etUsertel;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_qtlogin)
    Button btnQtlogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView(){
        mToolBar.setNavigationIcon(R.drawable.toolbar_btn_back_selector);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setTitle("Login", TITLE_MODE_LEFT);

        etUsertel.addTextChangedListener(new TextChange());
        etPassword.addTextChangedListener(new TextChange());
    }

    private void getLogin() {
		String userName = etUsertel.getText().toString().trim();
		String password = etPassword.getText().toString().trim();
		getLogin(userName, password);
    }

    private void getLogin(final String userName, final String password) {
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            ActivityUtils.showActivityMain(ActivityLogin.this, null);
        } else {
            showMessage("请填写账号或密码！");
        }
    }

    @OnClick({R.id.btn_login, R.id.btn_qtlogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                getLogin();
                break;
            case R.id.btn_qtlogin:

                break;
        }
    }

    // EditText监听器
    class TextChange implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before, int count) {
            boolean Sign2 = etUsertel.getText().length() > 0;
            boolean Sign3 = etPassword.getText().length() > 4;
            if (Sign2 & Sign3) {
                btnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_green));
                btnLogin.setEnabled(true);
            } else {
                btnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_green));
                btnLogin.setTextColor(0xFFD0EFC6);
                btnLogin.setEnabled(false);
            }
        }
    }

}
