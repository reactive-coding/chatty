package com.reactiveapps.chatty.view.widget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by .
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 1; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        Log.d(TAG, "------ onScrolled() ------>");
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        Log.d(TAG, "------ onScrolled() visibleItemCount: "+visibleItemCount+"------");
        Log.d(TAG, "------ onScrolled() totalItemCount: "+totalItemCount+"------");
        Log.d(TAG, "------ onScrolled() firstVisibleItem: "+firstVisibleItem+"------");
        Log.d(TAG, "------ onScrolled() loading: "+loading+"------");
        if (loading) {
            if (totalItemCount > previousTotal+1) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached
            // Do something
            current_page++;
            Log.d(TAG, "------ onScrolled() current_page: "+current_page+"------");
            onLoadMore(current_page);
            loading = true;
        }
        Log.d(TAG, "------ onScrolled() ------>");
    }

    public abstract void onLoadMore(int current_page);
}
