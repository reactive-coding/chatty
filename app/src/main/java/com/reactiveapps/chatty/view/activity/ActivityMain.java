package com.reactiveapps.chatty.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.utils.DensityUtil;
import com.reactiveapps.chatty.view.Base.ActivityBase;
import com.reactiveapps.chatty.view.adapter.MainTabFragmentAdapter;
import com.reactiveapps.chatty.view.fragment.FragmentChats;
import com.reactiveapps.chatty.view.fragment.FragmentContacts;
import com.reactiveapps.chatty.view.fragment.FragmentMine;
import com.reactiveapps.chatty.view.fragment.FragmentNews;
import com.reactiveapps.chatty.view.widget.CheckableFrameLayout;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityMain extends ActivityBase implements
        FragmentChats.OnFragmentChatListListener,
        FragmentContacts.OnFragmentContactsListener,
        FragmentNews.OnFragmentNewsListener,
        FragmentMine.OnFragmentMineListener {

    @BindView(R.id.activity_main_tab_a_ctv)
    CheckedTextView activityMainTabACtv;
    @BindView(R.id.activity_main_tab_b_ctv)
    CheckableFrameLayout activityMainTabBCtv;
    @BindView(R.id.activity_main_tab_c_ctv)
    CheckedTextView activityMainTabCCtv;
    @BindView(R.id.activity_main_tab_d_ctv)
    CheckedTextView activityMainTabDCtv;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private MainTabFragmentAdapter mMainTabFragmentAdapter;
    private List<Fragment> mFragmentList;

    private FragmentContacts mFragmentContacts;
    private FragmentChats mFragmentChatList;
    private FragmentNews mFragmentNews;
    private FragmentMine mFragmentMine;


    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();

        setTabSelection(0);
        this.getWindow().getCallback().getClass();
        this.getWindow().getDecorView().getClass();
    }

    @Override
    protected void onStart(){
        super.onStart();
//        mToolBar.setNavigationIcon(R.drawable.toolbar_btn_back_selector);
//        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "NavigationOnClickListener", Toast.LENGTH_SHORT).show();
//            }
//        });

        setTitle("WeChat", TITLE_MODE_LEFT);
        mToolBar.setPadding(DensityUtil.dip2px(this, 14), 0, 0, 0);
    }

    private void initView() {
        mFragmentList = new ArrayList<>();
        mFragmentContacts = new FragmentContacts();
        mFragmentChatList = new FragmentChats();
        mFragmentNews = new FragmentNews();
        mFragmentMine = new FragmentMine();
        mFragmentList.add(mFragmentChatList);
        mFragmentList.add(mFragmentContacts);
        mFragmentList.add(mFragmentNews);
        mFragmentList.add(mFragmentMine);

        mMainTabFragmentAdapter = new MainTabFragmentAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mMainTabFragmentAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetTabBtn();
                switch (position) {
                    case 0:
                        setTabSelection(0);
                        break;
                    case 1:
                        setTabSelection(1);
                        break;
                    case 2:
                        setTabSelection(2);
                        break;
                    case 3:
                        setTabSelection(3);
                        break;
                }

                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void resetTabBtn() {
        activityMainTabACtv.setChecked(false);
        activityMainTabBCtv.setChecked(false);
        activityMainTabCCtv.setChecked(false);
        activityMainTabDCtv.setChecked(false);
    }

    public void setTabSelection(int index) {
        resetTabBtn();
        switch (index){
            case 0:
                activityMainTabACtv.setChecked(true);
                break;
            case 1:
                activityMainTabBCtv.setChecked(true);
                break;
            case 2:
                activityMainTabCCtv.setChecked(true);
                break;
            case 3:
                activityMainTabDCtv.setChecked(true);
                break;
        }
    }

    public void changeFragment(int index){
        mViewPager.setCurrentItem(index, true);
    }

    @Override
    public void onFragmentInteraction(int which) {

    }

    @OnClick({R.id.activity_main_tab_a_ctv, R.id.activity_main_tab_b_ctv, R.id.activity_main_tab_c_ctv, R.id.activity_main_tab_d_ctv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_tab_a_ctv:
                setTabSelection(0);
                changeFragment(0);
                break;
            case R.id.activity_main_tab_b_ctv:
                setTabSelection(1);
                changeFragment(1);
                break;
            case R.id.activity_main_tab_c_ctv:
                setTabSelection(2);
                changeFragment(2);
                break;
            case R.id.activity_main_tab_d_ctv:
                setTabSelection(3);
                changeFragment(3);
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //条用基类的方法，以便调出系统菜单（如果有的话）
        //super.onCreateOptionsMenu(menu);
        MenuItem item1 = menu.add(0, 1, 1, "Search");
        item1.setIcon(R.mipmap.icon_search);
        item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem item2 = menu.add(0, 2, 2, "Overflow");
        item2.setIcon(R.mipmap.icon_add);
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem item3 = menu.add(0, 2, 3, "Group Chat");
        item3.setIcon(R.mipmap.icon_menu_group_chat);
        item3.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem item4 = menu.add(0, 2, 4, "Add Contacts");
        item4.setIcon(R.mipmap.icon_menu_add_contacts);
        item4.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem item5 = menu.add(0, 2, 5, "Scan QR Code");
        item5.setIcon(R.mipmap.icon_menu_scan_qr);
        item5.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem item6 = menu.add(0, 2, 6, "Money");
        item6.setIcon(R.mipmap.icon_menu_scan_qr);
        item6.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem item7 = menu.add(0, 2, 7, "Help & Feedback");
        item7.setIcon(R.mipmap.icon_menu_scan_qr);
        item7.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        //返回值为“true”,表示菜单可见，即显示菜单
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == 2) {
            Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == 3) {
            Toast.makeText(this, "Overflow Menu", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == 4) {
            Toast.makeText(this, "Add Contacts", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == 5) {
            Toast.makeText(this, "Scan QR Code", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == 6) {
            Toast.makeText(this, "Money", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == 7) {
            Toast.makeText(this, "Help & Feedback", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
