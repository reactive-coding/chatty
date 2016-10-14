package com.reactiveapps.chatty.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.utils.ToastUtil;
import com.reactiveapps.chatty.view.Base.ActivityBase;
import com.reactiveapps.chatty.view.adapter.ImageSelectViewPagerAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class ActivityImageSelect extends ActivityBase implements OnClickListener {
    private ViewPager mViewPager;
    private ImageSelectViewPagerAdapter mPagerAdapter;
    private TextView mBackButton;
    //private TextView mImageNumber;
    private TextView mSendButton;
    private CheckBox mImageSelectCheckBox;

    private ArrayList<String> mPaths;
    private ArrayList<ImageState> mStateList;
    private HashMap<Integer, Object> mSelectedMap;
    private int mCurrentIndex = 0;

    public static String IMAGE_SELECT_PATHS = "paths";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        initView();
    }

    public void initView() {
//        mBackButton = (TextView) findViewById(R.id.leftText);
//        mBackButton.setCompoundDrawablePadding(DisplayUtils.dip2px(5));
//        mBackButton.setVisibility(View.VISIBLE);
//        Drawable drawable = getResources().getDrawable(R.drawable.toolbar_btn_back_selector);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        mBackButton.setCompoundDrawables(drawable, null, null, null);
//        mBackButton.setOnClickListener(this);
//
//        mSendButton = (TextView) findViewById(R.id.firstRightText);
//        mSendButton.setVisibility(View.VISIBLE);
//        mSendButton.setText(R.string.ok);
//        mSendButton.setOnClickListener(this);

        mPaths = getIntent().getStringArrayListExtra(IMAGE_SELECT_PATHS);
        mSelectedMap = (HashMap<Integer, Object>) getIntent().getSerializableExtra("map");
        mBackButton.setText((mCurrentIndex + 1) + "/" + mPaths.size());

        mImageSelectCheckBox = (CheckBox) findViewById(R.id.image_select);
        mImageSelectCheckBox.setChecked(true);
        mImageSelectCheckBox.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.select_viewpager);
        mStateList = new ArrayList<>();
        for (String path : mPaths) {
            ImageState state = new ImageState();
            state.path = path;
            state.sel = true;
            mStateList.add(state);
        }
        updateSelectCount();
        mPagerAdapter = new ImageSelectViewPagerAdapter(this, mStateList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new ViewPagerChangeListener());
    }

    private void updateSelectCount() {
        if (null == mSendButton) {
            return;
        }

        int count = 0;
        if (mStateList != null) {
            for (ImageState state : mStateList) {
                if (state.sel) {
                    count++;
                }
            }
        }
        if (count > 0) {
            mSendButton.setText("发送(" + count + ")");
        } else {
            mSendButton.setText("发送");
        }
    }

    class ViewPagerChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            mCurrentIndex = arg0;
            if (null != mPaths && mPaths.size() > 0) {
                mBackButton.setText(((arg0 + 1) + "/" + mPaths.size()));
            }
            mImageSelectCheckBox.setChecked(mStateList.get(mCurrentIndex).sel);
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() ==  R.id.image_select) {
            mStateList.get(mCurrentIndex).sel = mImageSelectCheckBox.isChecked();
            updateSelectCount();
        }
//        if(v.getId() ==  R.id.leftText) {
//            BackPressed(false);
//            onBackPressed();
//        }
//
//        if(v.getId() ==  R.id.firstRightText) {
//            if (BackPressed(true)) {
//                finish();
//            }
//        }
    }

    public class ImageState {
        public String path;
        public boolean sel;
    }

    private boolean BackPressed(boolean send) {
        Intent intent = new Intent();
        ArrayList<String> paths = new ArrayList<>();
        HashMap<Integer, Object> tempMap = new HashMap<>();//保存未选中的图片

        for (ImageState state : mStateList) {
            if (state.sel) {
                if (!send) {
                    paths.add(state.path);
                } else {
                    paths.add(state.path);
                }
            } else {
                //去掉map中对应的图片
                Set<Entry<Integer, Object>> set = mSelectedMap.entrySet();
                for (Entry entry : set) {
                    Object obj = entry.getValue();
                    if (obj instanceof Bundle) {
                        Bundle b = (Bundle) obj;
                        if (b.getString("path").equals(state.path)) {
                            tempMap.put((Integer) entry.getKey(), entry.getValue());
                        }
                    }
                }
                for (Integer key : tempMap.keySet()) {
                    mSelectedMap.remove(key);
                }
            }
        }
        if (0 == paths.size() && send) {
            ToastUtil.showShortToast(R.string.select_picture);
            return false;
        }
        if (send) {
            intent.putExtra("send", send);
        }
        intent.putExtra(IMAGE_SELECT_PATHS, paths);
        intent.putExtra("map", mSelectedMap);
        setResult(RESULT_OK, intent);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            BackPressed(false);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
