package com.reactiveapps.chatty.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.FrameLayout;

/**
 * 实现了Checkable的FrameLayout
 *
 * @author Tank
 */
public class CheckableFrameLayout extends FrameLayout implements Checkable {
    private boolean mIsChecked;
    private final static int[] STATE_CHECKED = new int[]{android.R.attr.state_checked};

    public CheckableFrameLayout(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public CheckableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableFrameLayout(Context context) {
        super(context);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] state;
        if (mIsChecked) {
            state = mergeDrawableStates(
                    super.onCreateDrawableState(extraSpace + 1), STATE_CHECKED);
        } else {
            state = super.onCreateDrawableState(extraSpace);
        }
        return state;
    }

    @Override
    public void setChecked(boolean checked) {
        mIsChecked = checked;
        refreshDrawableState();
        checkTraversal(this, checked);
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    protected void checkTraversal(ViewGroup view, boolean isChecked) {
        for (int index = 0, count = view.getChildCount(); index < count; index++) {
            View child = getChildAt(index);
            if (child instanceof Checkable) {
                ((Checkable) child).setChecked(isChecked);
            }
            if (child instanceof ViewGroup) {
                checkTraversal((ViewGroup) child, isChecked);
            }
        }
    }
}
