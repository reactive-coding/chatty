package com.reactiveapps.chatty.view.Base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.reactiveapps.chatty.R;


public class ActivityToolBar extends AppCompatActivity {

    private ToolBarHelper mToolBarHelper;
    public Toolbar mToolBar;
    protected Menu mMenu;
    protected View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResId) {

        mToolBarHelper = new ToolBarHelper(this, layoutResId);
        mToolBar = mToolBarHelper.getToolBar();
        setContentView(mToolBarHelper.getContentView());
        mToolBar.setTitle("");

        mContentView = mToolBarHelper.getContentView();
        mToolBar.inflateMenu(R.menu.menu);
        mMenu = mToolBar.getMenu();
    }

    protected void addMenuItem(int groupId, int itemId, int order, int titleRes, int iconRes){
        //给一个菜单项添加Icon
        MenuItem item = mMenu.add(groupId, itemId, order, titleRes);
        item.setIcon(iconRes);
//        item.setTitle("item1");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }
}
