package com.reactiveapps.chatty.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.reactiveapps.chatty.model.bean.LastMessage;
import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.utils.DateUtils;
import com.reactiveapps.chatty.utils.ImageLoaderUtils;
import com.reactiveapps.chatty.view.fragment.FragmentChats;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdapterChats extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TAG = AdapterChats.class.getName();

    private final int RECYCLER_VIEW_ITEM = 1;
    private final int RECYCLER_VIEW_PROGRESSBAR = 0;

    private FragmentChats activityChatList;
    private ArrayList<Object> mItems;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public AdapterChats(FragmentChats context, ArrayList<Object> list) {
        super();
        this.activityChatList = new WeakReference<>(context).get();
        this.mItems = list;
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar loadMoreProgressBar;

        public ProgressViewHolder(View v) {
            super(v);
            Log.d(TAG, "ProgressViewHolder() ------>");
            loadMoreProgressBar = (ProgressBar) v.findViewById(R.id.chat_list_load_more_progressBar);
            if (null == loadMoreProgressBar) {
                Log.d(TAG, "------ ProgressBar == null ------");
            } else {
                Log.d(TAG, "------ ProgressBar != null ------");
            }
            Log.d(TAG, "ProgressViewHolder() <------");
        }
    }

    public class ChatItemViewHolder extends RecyclerView.ViewHolder {

        /**
         * user header
         */
        ImageView friendHeadIconView;
        TextView friendName;
        TextView content;
        TextView unReadMsgCount;
        ImageView divideLine;

        /**
         * message status
         */
        ImageView msgStatus;

        /**
         * message time
         */
        TextView msgTime;

        ChatItemViewHolder(View parent) {
            super(parent);
            Log.d(TAG, "ChatItemViewHolder() ------>");
            friendHeadIconView = (ImageView) parent.findViewById(R.id.chat_list_friend_head_iv);
            friendName = (TextView) parent.findViewById(R.id.chat_list_friend_name);
            content = (TextView) parent.findViewById(R.id.chat_list_msg);
            unReadMsgCount = (TextView) parent.findViewById(R.id.chat_list_unread_msg_count);
            msgStatus = (ImageView) parent.findViewById(R.id.chat_list_msg_status);
            msgTime = (TextView) parent.findViewById(R.id.chat_list_msg_time);
            divideLine = (ImageView) parent.findViewById(R.id.chat_list_divide_line);
            Log.d(TAG, "ChatItemViewHolder() <------");
        }
    }

    private boolean isFooterEnabled = true;

    /**
     * Enable or disable footer (Default is true)
     *
     * @param isEnabled boolean to turn on or off footer.
     */
    public void enableFooter(boolean isEnabled) {
        this.isFooterEnabled = isEnabled;
    }

    @Override
    public int getItemViewType(int position) {
        return (isFooterEnabled && position >= mItems.size()) ? RECYCLER_VIEW_PROGRESSBAR : RECYCLER_VIEW_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder() ------>");
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == RECYCLER_VIEW_ITEM) {
            Log.d(TAG, "------ viewType == RECYCLER_VIEW_ITEM  ------");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chats_msg_item, parent, false);
            viewHolder = new ChatItemViewHolder(view);
        } else if (viewType == RECYCLER_VIEW_PROGRESSBAR) {
            Log.d(TAG, "------ viewType == RECYCLER_VIEW_PROGRESSBAR  ------");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progressbar_item, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        Log.d(TAG, "onCreateViewHolder() <------");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() ------>");

        Log.d(TAG, "------ onBindViewHolder(), position:" + position + " ------");
        if (null == mItems || mItems.size() == 0 || mItems.size() <= position) {
            Log.d(TAG, "onBindViewHolder() null == mItems || mItems.size() == 0 || mItems.size() <= position <------");
            return;
        }
        if (null != mItems.get(position)) {
            Log.d(TAG,  mItems.get(position).toString() + " ------");
        } else {
            return;
        }
        if (holder instanceof ChatItemViewHolder) {
            if (mItems.size() > 0 && position < mItems.size()) {
                Log.d(TAG, "instance of ProductItemViewHolder  ------");
                ChatItemViewHolder chatItemViewHolder = (ChatItemViewHolder) holder;
                final LastMessage body = (LastMessage) mItems.get(position);
                if (null == body) {
                    return;
                }

                ((ChatItemViewHolder) holder).content.setText(body.content);
                ((ChatItemViewHolder) holder).friendName.setText(body.from);
                String time = body.time+"";
                ((ChatItemViewHolder) holder).msgTime.setText(DateUtils.convertMsec2DateTime(time));

                if (!TextUtils.isEmpty(body.friendHeadIconUrl)) {
                    ImageLoaderUtils.displayImage(activityChatList.getActivity(), body.friendHeadIconUrl, ((ChatItemViewHolder) holder).friendHeadIconView, R.mipmap.friend_head);
                } else {
                    ImageLoaderUtils.displayLocalImage(activityChatList.getActivity(), R.mipmap.friend_head, ((ChatItemViewHolder) holder).friendHeadIconView, R.mipmap.friend_head);
                }
                // The item click event
                if (mOnItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = holder.getLayoutPosition();
                            mOnItemClickListener.onItemClick(holder.itemView, pos);
                        }
                    });
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int pos = holder.getLayoutPosition();
                            mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                            return false;
                        }
                    });
                }
                /**
                 * invisible the last item
                 */
                if (position == (mItems.size() - 1)) {
                    chatItemViewHolder.divideLine.setVisibility(View.INVISIBLE);
                } else {
                    chatItemViewHolder.divideLine.setVisibility(View.VISIBLE);
                }
            }
        } else if (holder instanceof ProgressViewHolder) {
            Log.d(TAG, "instance of ProgressViewHolder  ------");
            ProgressViewHolder progressViewHolder = (ProgressViewHolder) holder;
            progressViewHolder.loadMoreProgressBar.setIndeterminate(true);
        }
        Log.d(TAG, "onBindViewHolder() <------");
    }

    @Override
    public int getItemCount() {
//        Log.i(TAG, "getItemCount() ------>");
        if (null != mItems) {
            int count = 0;
            if (isFooterEnabled) {
                count = mItems.size() + 1;
            } else {
                count = mItems.size();
            }
//            Log.i(TAG, "getItemCount():" + count + " ------");
//            Log.i(TAG, "getItemCount() <------");
            return count;
        }
//        Log.i(TAG, "getItemCount() <------");
        return 0;
    }

    public ArrayList<Object> getItems() {
        Log.d(TAG, "------ getItems() ------>");
        Log.d(TAG, "<------ getItems() ------");
        return mItems;
    }

    public Object getItem(int position) {
        Log.d(TAG, "getItem() ------>");
        if (null == mItems || mItems.isEmpty() || position >= mItems.size() || position < 0) {
            Log.d(TAG, "getItem() null <------");
            return null;
        }
        Log.d(TAG, "getItem() <------");
        return mItems.get(position);
    }

    public void notifyData() {
        Log.i(TAG, "notifyData(), resort the list by msg time ------>");
        Collections.sort(mItems, new TimeComparator());
        notifyDataSetChanged();
        Log.i(TAG, " notifyData() <------");
    }

    public void setItems(ArrayList<Object> list) {
        Log.d(TAG, "setItems() ------>");
        mItems = list;
        Log.d(TAG, "setItems() <------");
    }

    public class TimeComparator implements Comparator<Object> {
        @Override
        public int compare(Object arg0, Object arg1) {
            // TODO Auto-generated method stub
            LastMessage body1 = (LastMessage) arg0;
            LastMessage body2 = (LastMessage) arg1;
            int result = body1.time < body2.time ? 1 : (body1.time == body2.time ? 0 : -1);
            return result;
        }
    }
}