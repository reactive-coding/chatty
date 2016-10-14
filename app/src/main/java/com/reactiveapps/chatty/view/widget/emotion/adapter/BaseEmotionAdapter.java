package com.reactiveapps.chatty.view.widget.emotion.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.reactiveapps.chatty.view.widget.emotion.EmotionView;
import com.reactiveapps.chatty.view.widget.emotion.data.Emoticon;
import com.reactiveapps.chatty.view.widget.emotion.data.EmotionData;

/**
 * Created by Administrator on 2015/11/12.
 */
public abstract class BaseEmotionAdapter<T extends BaseEmotionAdapter.BaseListAdapter> extends PagerAdapter implements GridView.OnItemClickListener {
    protected static final String EMOTION_ADAPTER_TAG = "emotion_adapter";
    protected static final int INDEX_TAG = -1001;

    protected Context mContext;
    protected ViewPager mEmotionViewPager;
    protected EmotionData mEmotionData;
    protected EmotionView.EmotionClickListener mEmotionClickListener;

    /**
     * 表示在横向上，一个元素能够分得到的长度大小(包含margin&padding)
     */
    protected int mSize;
    /**
     * 表示总共有几页
     */
    protected int mCount;

    protected int viewPageWidth;

    protected ViewHolder mViewHolder;

    protected int mRow;
    protected int mColumn;
    /**
     * 表示一页有几个元素
     */
    protected int mPageCount;

    public void setEmotionClickListener(EmotionView.EmotionClickListener clickListener) {
        this.mEmotionClickListener = clickListener;
    }

    public BaseEmotionAdapter(Context context, ViewPager viewPager, EmotionData emotionData, EmotionView.EmotionClickListener emotionClickListener) {
        this.mContext = context;
        this.mEmotionViewPager = viewPager;
        this.mEmotionData = emotionData;
        this.mEmotionClickListener = emotionClickListener;
        init(emotionData);
    }

    private void init(EmotionData emotionData) {
        mRow = emotionData.getRow();
        mColumn = emotionData.getColumn();
        // 计算一页有几个元素
        mPageCount = calcPageCount(emotionData);
        // 计算总共有几页
        mCount = calcPageNumber(emotionData, mPageCount);
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        viewPageWidth = dm.widthPixels;
        mSize = calcItemSize(viewPageWidth);
    }

    /**
     * 计算每页有几个元素
     *
     * @param emotionData 用于提供行列和其他信息
     * @return 返回每页需要有几个元素
     */
    protected int calcPageCount(EmotionData emotionData) {
        return emotionData.getRow() * emotionData.getColumn();
    }

    /**
     * 计算总共会有几页
     *
     * @param emotionData 提供表情的list
     * @param pageCount   每页会出现的表情数
     * @return 返回总共的页数
     */
    protected int calcPageNumber(EmotionData emotionData, int pageCount) {
        int listSize = emotionData.getEmotionList().size();
        int pageNumber = 0;
        if (listSize % mPageCount > 0)
            pageNumber = listSize / mPageCount + 1;
        else {
            pageNumber = listSize / mPageCount;
        }
        return pageNumber;
    }

    /**
     * 计算每个元素在总宽上的均分大小(元素为正方形，高度不可知，故只能通过宽度)
     *
     * @param viewPageWidth 屏幕的宽度(屏幕宽即这个view的宽度，单位像素)
     * @return 返回每个元素在总宽的均分大小
     */
    protected int calcItemSize(int viewPageWidth) {
        return viewPageWidth / mColumn;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        ViewHolder holder = (ViewHolder) object;
        return view == holder.gridView;
    }

    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        GridView gridView;
        T adapter;
        ViewHolder holder;
        if (this.mViewHolder == null) {
            gridView = instantiateGridView();
            if (gridView == null)
                throw new NullPointerException("gridView 必须被实例化");
            gridView.setNumColumns(mColumn);

            // emotionViewPager在initData阶段是可能没有高度的(gone) 只有在emotionViewPager进行页面填充的时候才一定会有高度
            int viewPageHeight = getEmotionPageViewHeight();
            gridView = setGridViewMinimumHeight(gridView, viewPageHeight);
            setGridViewSpacing(gridView, viewPageHeight, viewPageWidth);

            adapter = createListAdapter(position);
            if (adapter == null)
                throw new NullPointerException("adapter 必须被实例化");

            holder = new ViewHolder();
            holder.gridView = gridView;
            holder.adapter = adapter;
        } else {
            gridView = mViewHolder.gridView;
            adapter = mViewHolder.adapter;
            holder = mViewHolder;
            mViewHolder = null;
        }
        bingData(adapter, position);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        container.addView(gridView);
        return holder;
    }

    @NonNull
    public abstract GridView instantiateGridView();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mEmotionClickListener == null)
            return;
        Emoticon emoticon = (Emoticon) parent.getItemAtPosition(position);
        if (emoticon.getEmoticonType() == Emoticon.EmoticonType.UNIQUE) {
            mEmotionClickListener.OnUniqueEmotionClick(mEmotionData.getUniqueItem(), view, mEmotionData.getCategory());
        } else {
            mEmotionClickListener.OnEmotionClick(emoticon, view, mEmotionData.getCategory());
        }
        OnEmoticonClick(emoticon, view);
    }

    protected void OnEmoticonClick(Emoticon emoticon, View view) {
    }

    @NonNull
    public abstract T createListAdapter(int currentPageNumber);

    public abstract T bingData(T listAdapter, int position);

    protected GridView setGridViewMinimumHeight(GridView gridView, int height) {
        gridView.setMinimumHeight(height);
        return gridView;
    }

    protected void setGridViewSpacing(GridView gridView, int viewPageHeight, int viewPageWeight) {
        int verticalSpacing = viewPageHeight / mRow - mSize;
        if (verticalSpacing < 0)
            verticalSpacing = 0;
        gridView.setVerticalSpacing(verticalSpacing);
    }

    private int getEmotionPageViewHeight() {
        return View.MeasureSpec.getSize(mEmotionViewPager.getMeasuredHeight());
    }


    public abstract EmotionData getEmotionData();

    public abstract void setEmotionData(EmotionData emotionData);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        this.mViewHolder = (ViewHolder) object;
        container.removeView(this.mViewHolder.gridView);
    }

    public abstract class BaseListAdapter extends BaseAdapter {
        @Override
        public final Object getItem(int position) {
            return getEmoticonItem(position);
        }

        public abstract Emoticon getEmoticonItem(int position);
    }

    protected class ViewHolder {
        GridView gridView;
        T adapter;
    }



}
