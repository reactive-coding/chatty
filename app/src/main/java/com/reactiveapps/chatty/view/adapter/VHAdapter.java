package com.reactiveapps.chatty.view.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class VHAdapter extends BaseAdapter {

    public abstract class VH {
        public abstract void setupViewItem(View v, int position);

        public abstract void fillViewItem(Object obj, int position);
    }

    protected Activity mContext;

    protected ArrayList<Object> mItems;

    protected LayoutInflater mInflater;

    protected int mResource = 0;

    public VHAdapter(Activity context) throws IllegalStateException {
        if (null == context) {
            throw new IllegalStateException("adapter context is null.");
        }
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setItems(ArrayList<Object> list) {
        if (mItems != null) {
            mItems.clear();
            mItems = null;
        }
        mItems = list;
        notifyDataSetChanged();
    }

    public void setItemsNotNotifaData(ArrayList<Object> list) {
        if (mItems != null) {
            mItems.clear();
            mItems = null;
        }
        mItems = list;
    }

    @Override
    public int getCount() {
        if (mItems != null)
            return mItems.size();
        return 0;
    }

    @Override
    public Object getItem(int paramInt) {
        if ((mItems != null) && (mItems.size() > 0) && (paramInt >= 0)
                && (paramInt < mItems.size()))
            return mItems.get(paramInt);
        else
            return null;
    }

    @Override
    public long getItemId(int paramInt) {
        return paramInt;
    }

    public void removeAll() {
        if (mItems != null) {
            mItems.clear();
            notifyDataSetChanged();
        }
    }

    public void clearAll() {
        if (mItems != null) {
            mItems.clear();
            notifyDataSetChanged();
        }
    }

    public void add(Object i) {
        if (mItems != null) {
            mItems.add(i);
        } else {
            mItems = new ArrayList<>();
            mItems.add(i);
        }
        notifyDataSetChanged();
    }

    public void insert(int index, Object i) {
        if (index < 0)
            index = 0;
        if (mItems != null) {
            mItems.add(index, i);
        } else {
            mItems = new ArrayList<>();
            mItems.add(i);
        }
        notifyDataSetChanged();
    }

    public void update(Object from, Object to) {
        if (mItems != null) {
            int i = mItems.indexOf(from);
            if (i >= 0 && i < mItems.size()) {
                mItems.remove(from);
                mItems.add(i, to);
            }
            notifyDataSetChanged();
        }
    }

    public void remove(Object o) {
        if (mItems != null && o != null) {
            mItems.remove(o);
            notifyDataSetChanged();
        }
    }

    public void remove(int paramInt) {
        if (mItems != null && mItems.size() > 0 && paramInt >= 0
                && paramInt < mItems.size()) {
            mItems.remove(paramInt);
            notifyDataSetChanged();
        }
    }

    protected View createItemView(int position, ViewGroup viewGroup) throws IllegalStateException {
        if (0 == mResource) {
            throw new IllegalStateException(
                    "you must set a resource id to this adapter.");
        }
        return mInflater.inflate(mResource, null);
    }

    protected abstract VH createViewHolder(int position);

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        VH vh = null;
        if (convertView == null) {
            vh = createViewHolder(position);
            convertView = createItemView(position, viewGroup);
            if (null != convertView) {
                vh.setupViewItem(convertView, position);
                convertView.setTag(vh);
            }
        } else {
            vh = (VH) convertView.getTag();
        }
        if (vh != null) {
            vh.fillViewItem(getItem(position), position);
        }
        return convertView;
    }

}