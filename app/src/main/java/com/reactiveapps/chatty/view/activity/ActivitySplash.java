package com.reactiveapps.chatty.view.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.utils.SharePreferenceUtil;
import com.reactiveapps.chatty.view.ActivityUtils;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        boolean isFirstOpen = (boolean)SharePreferenceUtil.get(this, "FIRST_OPEN", false);
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {
            ActivityUtils.showActivityGuide(ActivitySplash.this, null);
            finish();
            return;
        }

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                enterLoginActivity();
            }
        }, 2000);
    }

    private void enterLoginActivity() {
        ActivityUtils.showActivityLogin(ActivitySplash.this, null);
        finish();
    }
}
