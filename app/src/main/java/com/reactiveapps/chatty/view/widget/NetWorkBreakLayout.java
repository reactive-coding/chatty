package com.reactiveapps.chatty.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reactiveapps.chatty.R;

public class NetWorkBreakLayout extends RelativeLayout {

	private Button mReload;
	private TextView mRefresh;

	public NetWorkBreakLayout(Context context) {
		super(context);
		View view = LayoutInflater.from(context).inflate(R.layout.layout_empty, this, true);
		setBackgroundColor(getResources().getColor(R.color.windowsBackground));
		mReload = (Button) view.findViewById(R.id.network_break_reload);
		mRefresh = (TextView) view.findViewById(R.id.network_break_refresh);
	}

	public NetWorkBreakLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.layout_empty, this, true);
		setBackgroundColor(getResources().getColor(R.color.windowsBackground));
		mReload = (Button) view.findViewById(R.id.network_break_reload);
		mRefresh = (TextView) view.findViewById(R.id.network_break_refresh);
	}

	public void setReloadVisibility(int visibility) {
		mReload.setVisibility(visibility);
	}

	public void setRefreshVisibility(int visibility) {
		mRefresh.setVisibility(visibility);
	}

	public void setOnClickListener(OnClickListener listener) {
		mReload.setOnClickListener(listener);
		mRefresh.setOnClickListener(listener);
	}
}