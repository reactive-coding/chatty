package com.reactiveapps.chatty.view.Base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reactiveapps.chatty.utils.DensityUtil;

public class ActivityToolBar2 extends AppCompatActivity {

    private ToolBarHelper2 mToolBarHelper;
    public View mToolBarBackground;
    public View mToolBar;
    protected View mContentView;
    protected TextView mNavigationUp;
    protected TextView mTitleText;
    protected TextView mMenu;

    protected boolean isFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResId) {

        mToolBarHelper = new ToolBarHelper2(this, layoutResId, isFullScreen);

        mToolBarBackground = mToolBarHelper.getToolBarBackground();
        mToolBar = mToolBarHelper.getToolBar();
        setContentView(mToolBarHelper.getContentView());

        mContentView = mToolBarHelper.getContentView();
        mNavigationUp = mToolBarHelper.getNavigationUpView();
        mTitleText = mToolBarHelper.getTitleTextView();
        mMenu = mToolBarHelper.getMenuView();
    }

    protected void setToolBarBackground(int id){
        mToolBarBackground.setBackgroundColor(id);
    }

    protected void setNavigationUp(String title){
        mNavigationUp.setText(title);
    }

    protected void setNavigationUpResId(int resId){
        if (0 < resId){
            mNavigationUp.setText(this.getString(resId));
        }
    }


    protected void setNavigationUp(String title, Drawable drawable, String direction){
        if (!TextUtils.isEmpty(title)){
            mNavigationUp.setText(title);
        }else{
            mNavigationUp.setText(" ");
        }

        drawable.setBounds(0, 0, DensityUtil.dip2px(this, 40), DensityUtil.dip2px(this, 40)); //必须设置图片大小，否则不显示
        if ("left".equals(direction)){
            mNavigationUp.setCompoundDrawables(drawable, null, null, null);
        }else if ("top".equals(direction)){
            mNavigationUp.setCompoundDrawables(null, drawable, null, null);
        }else if ("right".equals(direction)){
            mNavigationUp.setCompoundDrawables(null, null, drawable, null);
        }else if ("bottom".equals(direction)){
            mNavigationUp.setCompoundDrawables(null, null, null, drawable);
        }

//        mNavigationUp.setCompoundDrawablePadding(DensityUtil.dip2px(this, 16));
        /**
         * parent is FrameLayout
         */
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0,0,DensityUtil.dip2px(this, 8));//4个参数按顺序分别是左上右下
        layoutParams.gravity = Gravity.CENTER_VERTICAL|Gravity.LEFT;
//        mNavigationUp.setLayoutParams(layoutParams);
    }

    protected void setTitle(String title){
        mTitleText.setText(title);
    }

    protected void setTitleResId(int resId){
        if (0 < resId){
            mTitleText.setText(this.getString(resId));
        }
    }


    protected void setTitle(String title, Drawable drawable, String direction){
        mTitleText.setText(title);

        drawable.setBounds(0, 0, 60, 60); //必须设置图片大小，否则不显示
        if ("left".equals(direction)){
            mTitleText.setCompoundDrawables(drawable, null, null, null);
        }else if ("top".equals(direction)){
            mTitleText.setCompoundDrawables(null, drawable, null, null);
        }else if ("right".equals(direction)){
            mTitleText.setCompoundDrawables(null, null, drawable, null);
        }else if ("bottom".equals(direction)){
            mTitleText.setCompoundDrawables(null, null, null, drawable);
        }

        mTitleText.setCompoundDrawablePadding(10);
    }

    protected void setMenuTitle(String title){
        mMenu.setText(title);
    }

    protected void setMenuTitleResId(int resId){
        if (0 < resId){
            mMenu.setText(this.getString(resId));
        }
    }


    protected void setMenuTitle(String title, Drawable drawable, String direction){
        mMenu.setText(title);

        drawable.setBounds(0, 0, 60, 60); //必须设置图片大小，否则不显示
        if ("left".equals(direction)){
            mMenu.setCompoundDrawables(drawable, null, null, null);
        }else if ("top".equals(direction)){
            mMenu.setCompoundDrawables(null, drawable, null, null);
        }else if ("right".equals(direction)){
            mMenu.setCompoundDrawables(null, null, drawable, null);
        }else if ("bottom".equals(direction)){
            mMenu.setCompoundDrawables(null, null, null, drawable);
        }

        mMenu.setCompoundDrawablePadding(10);
    }
}
