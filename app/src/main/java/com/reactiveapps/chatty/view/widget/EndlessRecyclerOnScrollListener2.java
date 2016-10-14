package com.reactiveapps.chatty.view.widget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by .
 */
public abstract class EndlessRecyclerOnScrollListener2 extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener2.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener2(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public void setPreviousTotal(int offset) {
        this.previousTotal += offset;
    }

    public void initPreviousTotal(int offset) {
        this.previousTotal = offset;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        Log.d(TAG, "------ onScrolled() ------>");
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        Log.d(TAG, "------ onScrolled() visibleItemCount: " + visibleItemCount + "------");
        Log.d(TAG, "------ onScrolled() totalItemCount: " + totalItemCount + "------");
        Log.d(TAG, "------ onScrolled() firstVisibleItem: " + firstVisibleItem + "------");
        Log.d(TAG, "------ onScrolled() loading: " + loading + "------");
        if (loading) {
            Log.d(TAG, "------ onScrolled() loading is true ------");
            if (totalItemCount > previousTotal + 1) {
                Log.d(TAG, "------ onScrolled() totalItemCount > previousTotal + 1 ------");
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && firstVisibleItem == 0 && dy < 0) {
            Log.d(TAG, "------ onScrolled() !loading && firstVisibleItem == 0 && dy < 0 ------");
            // End has been reached
            // Do something
            onLoadMore();
            loading = true;
        }
        Log.d(TAG, "------ onScrolled() ------>");
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (null == mLinearLayoutManager || mLinearLayoutManager.getItemCount() <= 0 || null == mLinearLayoutManager.getChildAt(0)) {
            return;
        }
        int lastOffset = mLinearLayoutManager.getChildAt(0).getTop();
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            onLastOffset(lastOffset);//顶部item距上部距离
        }
    }

    public abstract void onLoadMore();

    public abstract void onLastOffset(int lastOffset);
}
