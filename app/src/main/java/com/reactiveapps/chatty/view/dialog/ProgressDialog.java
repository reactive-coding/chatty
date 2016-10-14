package com.reactiveapps.chatty.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.reactiveapps.chatty.R;

public class ProgressDialog extends Dialog {
	private boolean mCancelable;

	public interface OnDialogDismissListener extends OnDismissListener {
		public void onBackDismiss(DialogInterface dialog);
	}

	public OnDialogDismissListener mOnDialogDismissListener;

	public ProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public ProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public ProgressDialog(Context context) {
		super(context);
	}

	@Override
	public void setCancelable(boolean flag) {
		super.setCancelable(flag);
		mCancelable = flag;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View progress = getLayoutInflater().inflate(R.layout.dialog_progress, null);
		setContentView(progress);
		getWindow().setGravity(Gravity.CENTER);
	}

	public void setOnDialogDismissListener(OnDialogDismissListener listener) {
		mOnDialogDismissListener = listener;
		setOnDismissListener(listener);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (mCancelable && null != mOnDialogDismissListener) {
			mOnDialogDismissListener.onBackDismiss(this);
		}
	}
}
