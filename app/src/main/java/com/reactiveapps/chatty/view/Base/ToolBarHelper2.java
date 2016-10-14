package com.reactiveapps.chatty.view.Base;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.reactiveapps.chatty.R;

/**
 * Created by iamcxl369 on 15-11-24.
 */
public class ToolBarHelper2 {

    /*上下文，创建view的时候需要用到*/
    private Context mContext;

    /*base view*/
    private FrameLayout mContentView;

    /*用户定义的view*/
    private View mActivityView;

    /*toolbar*/
    private View mToolBarBackground;
    private View mToolBar;

    /*toolbar 的标题*/
    private TextView mNavigationUpView;
    private TextView mTextView;
    private TextView mMenuView;

    /*视图构造器*/
    private LayoutInflater mInflater;

    private boolean isFullScreen = false;


    /*
    * 两个属性
    * 1、toolbar是否悬浮在窗口之上
    * 2、toolbar的高度获取
    * */
    private static int[] ATTRS = {
//            R.attr.windowActionBarOverlay,
//            R.attr.actionBarSize
    };

    public ToolBarHelper2(Context context, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        /*初始化整个内容*/
        initContentView();
        /*初始化用户定义的布局*/
        initActivityView(layoutId);
        /*初始化toolbar*/
        initToolBar();
    }

    public ToolBarHelper2(Context context, int layoutId, boolean isFullScreen) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.isFullScreen = isFullScreen;
        /*初始化整个内容*/
        initContentView();
        /*初始化用户定义的布局*/
        initActivityView(layoutId);

        if (!isFullScreen){
            /*初始化toolbar*/
            initToolBar();
        }

    }

    private void initContentView() {
        /*直接创建一个帧布局，作为视图容器的父容器*/
        mContentView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);

    }

    private void initToolBar() {
        /*通过inflater获取toolbar的布局文件*/
        View toolbar = mInflater.inflate(R.layout.layout_toolbar2, mContentView);
        mToolBarBackground = toolbar.findViewById(R.id.activity_toolbar_2_fl);
        mToolBar = toolbar.findViewById(R.id.toolbar);
        mNavigationUpView = (TextView) toolbar.findViewById(R.id.toolbar_navigation_up);
        mTextView = (TextView) toolbar.findViewById(R.id.toolbar_title_center);
        mMenuView = (TextView) toolbar.findViewById(R.id.toolbar_title_right);
    }

    private void initActivityView(int id) {
        mActivityView = mInflater.inflate(id, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(ATTRS);
        /*获取主题中定义的悬浮标志*/
        boolean overly = typedArray.getBoolean(0, false);
        /*获取主题中定义的toolbar的高度*/
//        int toolBarSize = (int) typedArray.getDimension(1,(int) mContext.getResources().getDimension(R.dimen.dp_56));
        int toolBarSize = (int) mContext.getResources().getDimension(R.dimen.dp_80);
        typedArray.recycle();
        /*如果是悬浮状态，则不需要设置间距*/
        params.topMargin = toolBarSize;
        if (isFullScreen){
            mContentView.addView(mActivityView);
        }else {
            mContentView.addView(mActivityView, params);
        }

    }

    public void setFullScreen(boolean isFullScreen){
        this.isFullScreen = isFullScreen;
    }

    public FrameLayout getContentView() {
        return mContentView;
    }

    public View getToolBar() {
        return mToolBar;
    }
    public View getToolBarBackground() {
        return mToolBarBackground;
    }

    public TextView getNavigationUpView() {
        return mNavigationUpView;
    }
    public TextView getTitleTextView() {
        return mTextView;
    }
    public TextView getMenuView() {
        return mMenuView;
    }
}
