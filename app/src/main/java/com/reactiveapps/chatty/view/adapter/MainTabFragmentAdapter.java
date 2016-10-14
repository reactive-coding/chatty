package com.reactiveapps.chatty.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.List;

/**
 * Project name: chatty
 * Class description:
 * Auther: iamcxl369
 * Date: 16-9-13 下午3:27
 * Modify by: iamcxl369
 * Modify date: 16-9-13 下午3:27
 * Modify detail:
 */

public class MainTabFragmentAdapter extends FragmentPagerAdapter {

    public List<Fragment> tabs;

    public MainTabFragmentAdapter(FragmentManager fm, List<Fragment> tabs) {
        super(fm);
        // TODO Auto-generated constructor stub
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        try {
            return tabs.get(position);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return tabs.size();
    }

}
