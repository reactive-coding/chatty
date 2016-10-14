package com.reactiveapps.chatty.view.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.reactiveapps.chatty.R;

public class OverflowPopWindow extends PopupWindow {
    private int mScreenHeight;
    private int mScreenWidth;
    private View mConentView;

    public OverflowPopWindow(final Activity context, View contentView) {
        this.mConentView = contentView;
        this.setContentView(mConentView);
        setSize(context);

        this.setWidth(LayoutParams.WRAP_CONTENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.AnimationPreview_Right);
    }

    public OverflowPopWindow(final Activity context, View contentView, int style) {
        this.mConentView = contentView;
        this.setContentView(mConentView);
        setSize(context);

        this.setWidth(LayoutParams.WRAP_CONTENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(style);
    }
    
    public OverflowPopWindow(final Activity context, View contentView,
                             int width, int height) {
        this.mConentView = contentView;
        this.setContentView(mConentView);

        this.setWidth(width);
        this.setHeight(height);

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.AnimationPreview_Right);
    }

    public void showPopupWindow(View anchor) {
        if (!this.isShowing()) {
            this.showAsDropDown(anchor, anchor.getLayoutParams().width / 2, 0);
        } else {
            this.dismiss();
        }
    }

    public void showPopupWindow(View anchor, int xoff, int yoff) {
        if (!this.isShowing()) {
            this.showAsDropDown(anchor, xoff, yoff);
        } else {
            this.dismiss();
        }
    }

    public void showPopWindowAtLocation(View anchor, int gravity, int xoff,
            int yoff) {
        if (!this.isShowing()) {
            this.showAtLocation(anchor, gravity, xoff, yoff);
        } else {
            this.dismiss();
        }
    }
    
    private void setSize(Activity context){
    	DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
    }
}
