package com.reactiveapps.chatty.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.view.adapter.ListDialogAdapter;

import java.util.ArrayList;

public class ListDialog extends Dialog implements OnItemClickListener, View.OnClickListener {
    private Context mContext;
    private TextView mTitleTextView;
    private ListItemClick mListener;
    private Button mCancel;
    private ListView mListView;
    private ListDialogAdapter mAdapter;
    private int mWidth = -1;
    private int mHeight = -1;

    public ListDialog(Context context, ListItemClick listener) {
        super(context, R.style.CustomProgressDialog);
        mContext = context;
        mListener = listener;
        customDialog(-1, -1);
    }

    public ListDialog(Context context, int theme) {
        super(context, R.style.CustomProgressDialog);
        mContext = context;
        customDialog(-1, -1);
    }

    public ListDialog(Context context, int width, int height, ListItemClick listener) {
        super(context, R.style.CustomProgressDialog);
        mContext = context;
        mListener = listener;
        customDialog(width, height);
    }

    public void customDialog(int width, int height) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_list_dialog);

        mWidth = width;
        mHeight = height;

        LayoutParams layoutParams = getWindow().getAttributes();
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();

        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(mDisplayMetrics);

        if (-1 == width) {
            layoutParams.width = (mDisplayMetrics.widthPixels * 4 / 5);
        } else {
            layoutParams.width = width;
        }

        getWindow().setAttributes(layoutParams);
        mTitleTextView = (TextView) this.findViewById(R.id.layout_identifying_code_dialog_title_text);
        mCancel = (Button) findViewById(R.id.layout_list_dialog_cancle);
        mCancel.setOnClickListener(this);
        mListView = (ListView) this.findViewById(R.id.layout_list_dialog_list);
        mListView.setOnItemClickListener(this);
        mAdapter = new ListDialogAdapter((Activity) mContext);
        mListView.setAdapter(mAdapter);
    }


    public void setCancelVisibility(boolean show) {
        if (show) {
            LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.height = mHeight;
            getWindow().setAttributes(layoutParams);
            mCancel.setVisibility(View.VISIBLE);
        } else {
            mCancel.setVisibility(View.GONE);
        }
    }

    public void setTitleText(String title) {
        if (null != mTitleTextView)
            mTitleTextView.setText(title);
    }

    public void setListData(ArrayList<Object> data) {
        mAdapter.setItems(data);
        int height = mListView.getHeight();

        LayoutParams layoutParams = getWindow().getAttributes();
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();

        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        if (-1 == mHeight && height > (mDisplayMetrics.heightPixels / 3)) {
            layoutParams.height = (int) (mDisplayMetrics.heightPixels / 3.5);
        } else if (-1 != mHeight && height > mHeight) {
            layoutParams.height = mHeight;
        }

        getWindow().setAttributes(layoutParams);
    }

    public interface ListItemClick {
        void onItemClick(int position);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (null != mListener) {
            mListener.onItemClick(arg2);
        }
        dismiss();
    }

    @Override
    public void onClick(View arg0) {
        dismiss();
    }
}