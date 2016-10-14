package com.reactiveapps.chatty.view.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.utils.DensityUtil;

public class NetworkBreakTopTips extends RelativeLayout {

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
				mContext.startActivity(intent);
			}catch (Exception e){

			}
		}
	};

	private Context mContext;

	public NetworkBreakTopTips(Context context) {
		super(context);
		mContext = context;
		setBackgroundColor(0x99000000);
		int padding = DensityUtil.dip2px(getContext(), 10);
		setPadding(padding, padding, padding, padding);
		LayoutInflater.from(context).inflate(R.layout.layout_network_break_top, this, true);
		setOnClickListener(mClickListener);
	}

	public NetworkBreakTopTips(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setBackgroundColor(0x99000000);
		int padding = DensityUtil.dip2px(getContext(), 10);
		setPadding(padding, padding, padding, padding);
		LayoutInflater.from(context).inflate(R.layout.layout_network_break_top, this, true);
		setOnClickListener(mClickListener);
	}
}