package com.reactiveapps.chatty.utils;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.reactiveapps.chatty.App;
import com.reactiveapps.chatty.R;


/**
 * Created by wt on 2016/6/24.
 */
public class ToastUtil {
    private static Toast toast = null;
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void showLongToast(String msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    public static void showLongToast(int resId) {
        showToast(resId, Toast.LENGTH_LONG);
    }

    public static void showShortToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    private static void showToast(final String msg, final int duration) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if ( null == App.getInst().getApplicationContext()){
                    return ;
                }
                if (toast == null) {
                    toast = new Toast(App.getInst().getApplicationContext());
                }
                View view = LayoutInflater.from(App.getInst().getApplicationContext()).inflate(R.layout.toast, null);
                TextView tv = (TextView) view.findViewById(R.id.register_oversea_toast_txt);
                tv.setText(msg);
                toast.setView(view);
                toast.setDuration(duration);
                toast.show();
            }
        });
    }

    private static void showToast(final int resId, final int duration) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if ( null == App.getInst().getApplicationContext()){
                    return ;
                }
                if (toast == null ) {
                    toast = new Toast(App.getInst().getApplicationContext());
                }
                View view = LayoutInflater.from(App.getInst().getApplicationContext()).inflate(R.layout.toast, null);
                TextView tv = (TextView) view.findViewById(R.id.register_oversea_toast_txt);
                tv.setText(App.getInst().getApplicationContext().getString(resId));
                toast.setView(view);
                toast.setDuration(duration);
                toast.show();
            }
        });
    }
}
