package com.reactiveapps.chatty.view.Base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reactiveapps.chatty.utils.DensityUtil;


public class ActivityToolBar3 extends AppCompatActivity {

    private ToolBarHelper3 mToolBarHelper;
    public View mToolBarBackground;
    public Toolbar mToolBar;
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

        mToolBarHelper = new ToolBarHelper3(this, layoutResId, isFullScreen);
        mToolBarBackground = mToolBarHelper.getToolBarBackground();
        mToolBar = mToolBarHelper.getToolBar();
        setContentView(mToolBarHelper.getContentView());

        mContentView = mToolBarHelper.getContentView();
        mNavigationUp = mToolBarHelper.getNavigationUpView();
        mTitleText = mToolBarHelper.getTitleTextView();
        mMenu = mToolBarHelper.getMenuView();

        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(mToolBar);
        /*自定义的一些操作*/
        onCreateCustomToolBar(mToolBar) ;
    }

    public void onCreateCustomToolBar(Toolbar toolbar){
        toolbar.setContentInsetsRelative(0,0);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            back();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void back() {
        super.onBackPressed();
    }

    protected void setNavigationIcon(int resId) {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(resId);
        }
    }

    protected void setNavigationIcon(Drawable icon) {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(icon);
        }
    }

    public static final int TITLE_MODE_LEFT = -1;
    public static final int TITLE_MODE_CENTER = -2;
    public static final int TITLE_MODE_NONE = -3;

    private int titleMode = TITLE_MODE_CENTER;

    public void setTitle(CharSequence title, int mode) {
        this.titleMode = mode;
        setTitle(title);
    }

    public void setTitleMode(int mode) {
        this.titleMode = mode;
        setTitle(getTitle());
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (titleMode == TITLE_MODE_NONE) {
            if (mToolBar != null) {
                mToolBar.setTitle("");
            }
            if (mTitleText != null) {
                mTitleText.setText("");
            }
        } else if (titleMode == TITLE_MODE_LEFT) {
            if (mToolBar != null) {
                mToolBar.setTitle(title);
            }
            if (mTitleText != null) {
                mTitleText.setText("");
            }
        } else if (titleMode == TITLE_MODE_CENTER) {
            if (mToolBar != null) {
                mToolBar.setTitle("");
            }
            if (mTitleText != null) {
                mTitleText.setText(title);
            }
        }
    }
}
