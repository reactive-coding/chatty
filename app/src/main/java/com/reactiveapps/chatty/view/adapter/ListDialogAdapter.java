package com.reactiveapps.chatty.view.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reactiveapps.chatty.R;

public class ListDialogAdapter extends VHAdapter {

    public ListDialogAdapter(Activity c) {
        super(c);
    }

    @Override
    protected View createItemView(int position, ViewGroup viewGroup) {
        View view = ((LayoutInflater) mContext
                .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.dialog_list_item, viewGroup, false);
        return view;
    }

    @Override
    protected VH createViewHolder(int position) {
        VH vh;
        vh = new VHMore();
        return vh;
    }

    private class VHMore extends VH {
        TextView mItemText;
        ImageView mItemLine;

        public void setupViewItem(View v, int position) {
            if (v != null) {
                mItemText = ((TextView) v.findViewById(R.id.item_list_dialog_text));
                mItemLine = (ImageView) v.findViewById(R.id.item_list_dialog_line);
            }
        }

        public void fillViewItem(Object obj, int position) {
            if (obj != null) {
                String item = (String) obj;
                mItemText.setText(item);
            }
        }
    }
}