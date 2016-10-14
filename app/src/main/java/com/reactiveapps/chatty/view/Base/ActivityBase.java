package com.reactiveapps.chatty.view.Base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.utils.EventBusUtils;
import com.reactiveapps.chatty.utils.NetUtils;
import com.reactiveapps.chatty.view.fragment.FragmentLoading;
import com.reactiveapps.chatty.view.widget.EmptyLayout;
import com.reactiveapps.chatty.view.widget.NetWorkBreakLayout;
import com.reactiveapps.chatty.view.widget.NetworkBreakTopTips;
//import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iamcxl369 on 15-11-26.
 */
public class ActivityBase extends ActivityToolBar3 {
    protected static final String TAG = ActivityBase.class.getName();
    /**
     * 界面在进行网络请求的时候的加载进度条
     */
    private FragmentLoading loadingFragment;
    /**
     * 当前Activity的View;
     */
    private View mActivityContentView;

    /**
     * 网络连接改变的广播
     */
    protected ConnectivityReceiver mConnectivityReceiver = new ConnectivityReceiver();

    /**
     *
     */
    private RelativeLayout mNetworkBreakLayout;

    /**
     * Toast
     */
    private Toast mToast;

    /**
     * 图文Toast
     */
    private Toast mPositiveToast;
    private TextView mPositiveToastMessage;
    private ImageView mPositiveIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate() ------>");
        super.onCreate(savedInstanceState);

        initRegisterEvent();
        initToastView();
        Log.d(TAG,"onCreate() <------");
    }

    @Override
    public void setContentView(int layoutResId) {
        Log.d(TAG,"setContentView() ------>");
        super.setContentView(layoutResId);



        fitSystemWindow();

        Log.d(TAG,"setContentView() <------");
    }

    /**
     * For fitSystemWindow:
     * 1. Style.xml add below:
     *    <item name="android:fitsSystemWindows">true</item>
          <item name="android:windowTranslucentStatus">true</item>
       2. Create values-v21 folder and create styles.xml.
          copy below to styles.xml:
         <!-- Make the status bar traslucent -->
         <style name="AppTheme" parent="AppTheme.Base">

         <!-- below code will make the toolbar move up merger with status bar!!!  -->
         <item name="android:windowTranslucentStatus">true</item>

         <!-- This is the color of the Toolbar -->
         <item name="colorPrimary">@color/colorPrimary</item>
         <!-- This is the color of the Status bar -->
         <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
         <!-- The Color of the Status bar -->
         <item name="android:statusBarColor">@color/colorPrimaryDark</item>
         </style>
     */
    private void fitSystemWindow(){
        /**
         *
         *
         // The github demo as below:
         // create our manager instance after the content view is set
         SystemBarTintManager tintManager = new SystemBarTintManager(this);
         // enable status bar tint
         tintManager.setStatusBarTintEnabled(true);
         // enable navigation bar tint
         tintManager.setNavigationBarTintEnabled(true);
         *
         */

//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        // enable status bar tint
//        tintManager.setStatusBarTintEnabled(true);
//        // enable navigation bar tint
//        tintManager.setNavigationBarTintEnabled(true);

        //设定状态栏的颜色，当版本大于4.4时起作用
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            //此处可以重新指定状态栏颜色
//            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
//
//            // Set the padding to match the Status Bar height
//            mToolBar.setPadding(0, getStatusBarHeight(), 0, 0);
//        }

    }

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }



    private void initRegisterEvent(){
        /**
         * 注册网络改变时的广播监听器
         */
        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mConnectivityReceiver, filter);
        EventBusUtils.register(this);
    }

    private void unRegisterEvent(){
        unregisterReceiver(mConnectivityReceiver);
        EventBusUtils.unRegister(this);
    }

    private void initToastView(){
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);

        View root = LayoutInflater.from(this).inflate(R.layout.toast_positive, null, false);
        mPositiveToastMessage = (TextView) root.findViewById(R.id.message);
        mPositiveIcon = (ImageView) root.findViewById(R.id.icon);
        mPositiveToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mPositiveToast.setGravity(Gravity.CENTER, 0, 0);
        mPositiveToast.setView(root);
    }
    /**
     * EventBus Exception:
     * java.lang.NoClassDefFoundError: android/os/PersistableBundle
     * on below android 21 <5.0> version.
     */
//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//        //TODO:回收发生,需要保存全局变量中关键数据
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG,"onSaveInstanceState() ------>");
        super.onSaveInstanceState(outState);
        //TODO:回收发生,需要保存全局变量中关键数据
        Log.d(TAG,"onSaveInstanceState() <------");
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onResume() ------>");
        super.onResume();


        Log.d(TAG,"onResume() <------");
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"onPause() ------>");
        super.onPause();
        saveIpc();
        Log.d(TAG,"onPause() <------");
    }

    protected boolean isDestroy;
    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy() ------>");
        isDestroy = true;
        super.onDestroy();
        unRegisterEvent();
        Log.d(TAG,"onDestroy() <------");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){}
                catch(Exception e){}
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    protected void saveIpc(){

    }

    /**
     * EventBus注册之后默认一定要实现一个方法,否则会报异常.
     * @param object
     */
    public void onEvent(Object object){

    }

    public void showMessage(String msg){
        if (null != mToast){
            mToast.setText(msg);
            mToast.show();
        }
    }

    /**
     * 显示loading对话框
     */
    public void showLoading(){
        try{
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingFragment = FragmentLoading.newInstance();
                    if (loadingFragment.isVisible()){
                        dismissLoading();
                    }
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    loadingFragment.show(ft, FragmentLoading.class.getName());
                }
            });
        }catch (Exception e){
            Log.e(TAG, "------ ActivityBase.showLoading(), Exception: ------", e);
        }
    }

    /**
     * 取消Loading对话框的显示
     */
    public void dismissLoading(){
        try{
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null != loadingFragment){
                        loadingFragment.dismissAllowingStateLoss();
                    }
                }
            });
        }catch (Exception e){
            Log.e(TAG, "------ ActivityBase.dismissLoading(), Exception: ------", e);
        }
    }

    /**
     * 现实空布局
     * eg.listview中的数据为空
     * 网络请求成功,但是数据为空的场景
     */
    public void showEmptyViewTips(){
        View contentView = ((ViewGroup)mContentView).getChildAt(0);
        if (!(contentView instanceof EmptyLayout)) {
            View view = new EmptyLayout(this);
            mActivityContentView = contentView;
            ((ViewGroup) mContentView).removeView(mActivityContentView);
            ((ViewGroup) mContentView).addView(view, 0);

            // view init
            ImageView mEmptyImageView = (ImageView) view.findViewById(R.id.empty_image);
            TextView mEmptyTextView = (TextView) view.findViewById(R.id.empty_text);
            Button mReloadButton = (Button) view.findViewById(R.id.empty_reload);
            TextView mRefreshView = (TextView) view.findViewById(R.id.empty_refresh);
            mReloadButton.setVisibility(View.GONE);
            mRefreshView.setVisibility(View.GONE);
        }
    }

    public void dismissEmptyViewTips(){
        if (null != mActivityContentView) {
            View parentView = ((ViewGroup)mContentView).getChildAt(0);
            ((ViewGroup) parentView).removeViewAt(0);
            ((ViewGroup) parentView).addView(mActivityContentView, 0);
            mActivityContentView = null;
        }
    }

    public boolean isEmptyViewShow() {
        View contentview = ((ViewGroup)mContentView).getChildAt(0);
        if (contentview instanceof EmptyLayout) {
            return true;
        }
        return false;
    }

    /**
     * 网络请求失败后的listview界面显示的空数据
     * 网络失败,重新加载界面
     */
    public void showNetworkBreakyViewTips(){
        View contentView = ((ViewGroup)mContentView).getChildAt(0);
        if (!(contentView instanceof NetWorkBreakLayout)) {
            View view = new NetWorkBreakLayout(this);
            mActivityContentView = contentView;
            ((ViewGroup) mContentView).removeView(mActivityContentView);
            ((ViewGroup) mContentView).addView(view, 0);

            // view init
            ImageView mErrImageView = (ImageView) view.findViewById(R.id.network_break_image);
            TextView mErrTipText = (TextView) view.findViewById(R.id.network_break_text);
            Button mErrBtn = (Button) view.findViewById(R.id.network_break_reload);
            TextView mErrRefreshView = (TextView) view.findViewById(R.id.network_break_refresh);
            mErrBtn.setVisibility(View.GONE);
            mErrRefreshView.setVisibility(View.GONE);
        }
    }

    public void dismissNetworkBreakViewTips(){
        if (null != mActivityContentView) {
            View parentView = ((ViewGroup)mContentView).getChildAt(0);
            ((ViewGroup) parentView).removeViewAt(0);
            ((ViewGroup) parentView).addView(mActivityContentView, 0);
            mActivityContentView = null;
        }
    }

    public boolean isNetworkBreakViewShow() {
        View contentview = ((ViewGroup)mContentView).getChildAt(0);
        if (contentview instanceof NetWorkBreakLayout) {
            return true;
        }
        return false;
    }

    /**
     * 监听网络连接变化的广播
     * 如果网络变化:
     * 1.显示界面顶部的提示浮层;
     * 2.修改网络连接的默认超时时间;
     * 20151229
     */
    public class ConnectivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            onNetworkChanged();
            for (ConnectivityChangeListener listener : mConnectivityChangeListeners) {
                listener.onNetworkChanged();
                if(checkNetworkWasChanged()){
                    NetUtils.setNetworkEvnParameter(context);
                }
            }
        }
    }


    public void onNetworkChanged(){
        if (!NetUtils.isNetworkAvailable(getApplicationContext())) {
            if (mNetworkBreakLayout == null) {
                mNetworkBreakLayout = new NetworkBreakTopTips(this);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin = (int)getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
                addContentView(mNetworkBreakLayout, layoutParams);

            } else {
                mNetworkBreakLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (mNetworkBreakLayout != null) {
                mNetworkBreakLayout.setVisibility(View.GONE);
            }
        }
    };

    private ArrayList<ConnectivityChangeListener> mConnectivityChangeListeners = new ArrayList<>();

    public interface ConnectivityChangeListener {
        void onNetworkChanged();
    }

    NetworkInfo mCurrentConnectedNetworkInfo = new NetworkInfo();
    NetworkInfo mNowNetworkInfo = new NetworkInfo();
    //检测上次连接上的与本地网络切换后的网络是否同一网络 ，如果不是就启动自动重登录，如果无上次连接成功标志，就启动后台重登业务
    private boolean checkNetworkWasChanged() {
        if (mCurrentConnectedNetworkInfo.mNetType != null) {
            mNowNetworkInfo.mNetType = NetUtils.getNetType(this);
            if (mNowNetworkInfo.mNetType.summaryType != mCurrentConnectedNetworkInfo.mNetType.summaryType) {
                //wifi和移动网络环境发生改变
//	    		Log.d("", "NotificationService->networkWasChanged->wifi和移动网络环境发生改变");
                return true;
            }
            mNowNetworkInfo.obtainNetworkInfo();
            if (!TextUtils.isEmpty(mNowNetworkInfo.IP) && !mNowNetworkInfo.IP.equals(mCurrentConnectedNetworkInfo.IP)) {
                //ip地址发生改变
//				Log.d("", "NotificationService->networkWasChanged->ip地址发生改变");
                return true;
            } else if (!TextUtils.isEmpty(mNowNetworkInfo.BSSID)  && !mNowNetworkInfo.BSSID.equals(mCurrentConnectedNetworkInfo.BSSID)) {
                //所连接的WIFI设备的MAC地址发生改变
//				Log.d("", "NotificationService->networkWasChanged->所连接的WIFI设备的MAC地址发生改变");
                return true;
            } else if (!TextUtils.isEmpty(mNowNetworkInfo.SSID)  && !mNowNetworkInfo.SSID.equals(mCurrentConnectedNetworkInfo.SSID)) {
                //所连接的WIFI的网络名称
//				Log.d("", "NotificationService->networkWasChanged->所连接的WIFI的网络名称改变");
                return true;
            } else {
//				Log.d("", "NotificationService->networkWasChanged->什么都没变，什么也不做");
            }
        } else {
//			Log.d("", "NotificationService->mMgrNetState.mNetType == null");
        }
        return false;
    }

    public class NetworkInfo {
        public NetUtils.NetType mNetType;
        public String IP = null;		//IP字符串地址：192.168.191.3
        public String BSSID = null;		//wifi-BSSID属性（所连接的WIFI设备的MAC地址）：a2:56:f2:35:e2:41
        public String SSID = null;		//wifi-SSID（所连接的WIFI的网络名称）："pc_bjwyu2"
        public void clear() {
            mNetType = null;
            IP = null;
            BSSID = null;
            SSID = null;
        }
        public void obtainNetworkInfo() {
            Log.d(TAG,"obtainNetworkInfo() ------>");
            IP = null;
            BSSID = null;
            SSID = null;
            if (mNetType != null) {
                StringBuffer sb = new StringBuffer();
                if (NetUtils.NetType.SUMMARY_TYPE_MOBILE == mNetType.summaryType) {
                    IP = NetUtils.getIPAddress();
                    sb.append("\n获取移动网络信息");
                    sb.append("\n获取移动网络IP地址：" + IP);
                } else if (NetUtils.NetType.SUMMARY_TYPE_WIFI == mNetType.summaryType) {
                    WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                    if (wifiInfo != null) {
                        IP = NetUtils.intToIp(wifiInfo.getIpAddress());
                        BSSID = wifiInfo.getBSSID();
                        SSID = wifiInfo.getSSID();
                        sb.append("\n获取wifi网络信息");
                        sb.append("\n获取BSSID属性（所连接的WIFI设备的MAC地址）：" + BSSID);
                        sb.append("\n获取IP 字符串地址：" + IP);
                        sb.append("\n获取SSID（所连接的WIFI的网络名称）：" + SSID);
                    }
                }
                Log.d(TAG, "------ ActivityBase, obtainNetworkInfo(), 当前连接上的网络信息如下：" + sb.toString());
            }
            Log.d(TAG,"obtainNetworkInfo() <------");
        }
    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "handleResult()  ------>");
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
        Log.d(TAG, "handleResult()  <------");
    }

}
