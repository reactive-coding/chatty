package com.reactiveapps.chatty.view.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reactiveapps.chatty.App;
import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.message.ProtocolType;
import com.reactiveapps.chatty.message.EnumMsgDirection;
import com.reactiveapps.chatty.message.EnumMsgDownloadStatus;
import com.reactiveapps.chatty.message.EnumMsgReadStatus;
import com.reactiveapps.chatty.message.EnumMsgSendStatus;
import com.reactiveapps.chatty.model.bean.ChatMessage;
import com.reactiveapps.chatty.utils.DateUtils;
import com.reactiveapps.chatty.utils.FileUtils;
import com.reactiveapps.chatty.utils.ImageLoaderUtils;
import com.reactiveapps.chatty.utils.PermissionsUtil;
import com.reactiveapps.chatty.utils.SharePreferenceUtil;
import com.reactiveapps.chatty.utils.StringUtils;
import com.reactiveapps.chatty.utils.ToastUtil;
import com.reactiveapps.chatty.view.activity.ActivityChatting;
import com.reactiveapps.chatty.view.activity.ActivityImagePreview;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdapterChatMsgView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = AdapterChatMsgView.class.getSimpleName();
    private RecyclerView mListView;
    public  ActivityChatting activityChatting;
    private AdapterChatMsgView mChatMsgViewAdapter;

    private List<Object> list;// The list of message object
    private LayoutInflater mInflater;
    private int mCurrentShowPicIndex = 0;
    private boolean mUpReturn = false;

    private Drawable mImageLoadingOverlay;
    private int mImageFixWidth;
    private int mAudioMinWidth;
    private int mAudioMaxWidth;

    private String mTmpVenderId;

    private static final String PATH_PREFIX = "file://";//fresco显示本地图片

    public static final int COMMON_MSG_TYPE = 0;
    private static final int LINK_LEFT_MSG_TYPE = 1;
    private static final int LINK_RIGHT_MSG_TYPE = 2;
    private static final int TEXT_LEFT_MSG_TYPE = 3;
    private static final int TEXT_RIGHT_MSG_TYPE = 4;
    private static final int FILE_LEFT_MSG_TYPE = 5;
    private static final int FILE_RIGHT_MSG_TYPE = 6;
    private static final int IMAGE_LEFT_MSG_TYPE = 7;
    private static final int IMAGE_RIGHT_MSG_TYPE = 8;
    private static final int VOICE_LEFT_MSG_TYPE = 9;
    private static final int VOICE_RIGHT_MSG_TYPE = 10;
    private static final int PROGRESSBAR_MSG_TYPE = 11;

    public AdapterChatMsgView(ActivityChatting activity, RecyclerView listView, List<Object> list) {
        this.activityChatting = new WeakReference<>(activity).get();
        this.mInflater = LayoutInflater.from(activityChatting);

        this.list = list;
        this.mListView = listView;
        this.mChatMsgViewAdapter = this;
        mImageLoadingOverlay = activityChatting.getResources().getDrawable(R.drawable.overflow);
        mImageFixWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, activityChatting.getResources().getDisplayMetrics());
        mAudioMinWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, activityChatting.getResources().getDisplayMetrics());
        mAudioMaxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, activityChatting.getResources().getDisplayMetrics());

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    RecyclerView.ViewHolder viewHolder = null;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "------ onCreateViewHolder() ------>");
        View convertView = null;
        if (viewType == COMMON_MSG_TYPE) {
            convertView = inflateView(R.layout.chat_system_layout, parent);
            viewHolder = new SystemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else if (viewType == LINK_LEFT_MSG_TYPE) {
            convertView = inflateView(R.layout.chat_link_left_layout, parent);
            viewHolder = new LinkLeftViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else if (viewType == LINK_RIGHT_MSG_TYPE) {
            convertView = inflateView(R.layout.chat_link_right_layout, parent);
            viewHolder = new LinkRightViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else if (viewType == TEXT_LEFT_MSG_TYPE) {
            convertView = inflateView(R.layout.chat_text_left_layout, parent);
            viewHolder = new TextLeftViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else if (viewType == TEXT_RIGHT_MSG_TYPE) {
            convertView = inflateView(R.layout.chat_text_right_layout, parent);
            viewHolder = new TextRightViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else if (viewType == FILE_LEFT_MSG_TYPE) {
            convertView = inflateView(R.layout.chat_file_left_layout, parent);
            viewHolder = new FileLeftViewHolder(convertView);
            convertView.setTag(viewHolder);
        }  else if (viewType == FILE_RIGHT_MSG_TYPE) {
            convertView = inflateView(R.layout.chat_file_right_layout, parent);
            viewHolder = new FileLeftViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else if (viewType == IMAGE_LEFT_MSG_TYPE) {
            convertView = inflateView(R.layout.chat_image_left_layout, parent);
            viewHolder = new ImageLeftViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else if (viewType == IMAGE_RIGHT_MSG_TYPE) {
            convertView = inflateView(R.layout.chat_image_right_layout, parent);
            viewHolder = new ImageRightViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else if (viewType == VOICE_LEFT_MSG_TYPE) {
            convertView = inflateView(R.layout.chat_voice_left_layout, parent);
            viewHolder = new VoiceLeftViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else if (viewType == VOICE_RIGHT_MSG_TYPE) {
            convertView = inflateView(R.layout.chat_voice_right_layout, parent);
            viewHolder = new VoiceRightViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else if (viewType == PROGRESSBAR_MSG_TYPE) {
            convertView = inflateView(R.layout.layout_progressbar_item, parent);
            viewHolder = new ProgressViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        Log.d(TAG, "<------ onCreateViewHolder() ------");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (null == list || list.size() == 0 || null == list.get(position)) {
            Log.d(TAG, "<------ onBindViewHolder() list size list.get(position) = null------");
            return;
        }
        final ChatMessage baseMessage = (ChatMessage) list.get(position);
        if (holder instanceof SystemViewHolder) {
            handleSystemMsg((SystemViewHolder) holder, position, baseMessage);
        } else if (holder instanceof LinkLeftViewHolder) {
            handleLeftLinkMsg((LinkLeftViewHolder) holder, baseMessage, position);
        } else if (holder instanceof LinkRightViewHolder) {
            handleRightLinkMsg((LinkRightViewHolder) holder, baseMessage, position);
        } else if (holder instanceof TextLeftViewHolder) {
            handleLeftTextMsg((TextLeftViewHolder) holder, baseMessage, position);
        } else if (holder instanceof TextRightViewHolder) {
            handleRightTextMsg((TextRightViewHolder) holder, baseMessage, position);
        } else if (holder instanceof FileLeftViewHolder) {
            handleLeftFileMsg((FileLeftViewHolder) holder, baseMessage, position);
        }  else if (holder instanceof FileRightViewHolder) {
            handleRightFileMsg((FileRightViewHolder) holder, baseMessage, position);
        } else if (holder instanceof ImageLeftViewHolder) {
            handleLeftImageMsg((ImageLeftViewHolder) holder, baseMessage, position);
        } else if (holder instanceof ImageRightViewHolder) {
            handleRightImageMsg((ImageRightViewHolder) holder, baseMessage, position);
        } else if (holder instanceof VoiceLeftViewHolder) {
            handleLeftVoiceMsg((VoiceLeftViewHolder) holder, baseMessage, position);
        } else if (holder instanceof VoiceRightViewHolder) {
            handleRightVoiceMsg((VoiceRightViewHolder) holder, baseMessage, position);
        }
    }

    @Override
    public int getItemCount() {
        if (null != list) {
            return list.size();
        }
        return 0;
    }

    public List<Object> getItems() {
        return list;
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

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void notifyDataSetChanged(List<Object> coll) {
        this.list.clear();
        this.list = coll;
        notifyDataSetChanged();
    }

    /**
     * 添加条目
     */
    public void addEntity(ChatMessage entity) {
        if (list != null && entity != null) {
            list.add(entity);
        }
    }

    public void update(ChatMessage from, ChatMessage to) {
        if (list != null) {
            int i = list.indexOf(from);
            if (i >= 0 && i < list.size()) {
                list.remove(from);
                list.add(i, to);
            }
            notifyDataSetChanged();
        }
    }

    private View inflateView(int layout, ViewGroup viewGroup) {
        return mInflater.inflate(layout, viewGroup, false);
    }

    public int getItemViewType(int position) {
        ChatMessage chatMessage = (ChatMessage) list.get(position);
        int type = 0;
        if (isFooterEnabled && (chatMessage == null)) {
            type = PROGRESSBAR_MSG_TYPE;
        } else {
            if (EnumMsgDirection.SEND == chatMessage.msg_direction) {
                if (ProtocolType.LINK.equals(chatMessage.type)) {
                    type = LINK_RIGHT_MSG_TYPE;
                } else if (ProtocolType.TEXT.equals(chatMessage.type)) {
                    type = TEXT_RIGHT_MSG_TYPE;
                } else if (ProtocolType.IMAGE.equals(chatMessage.type)) {
                    type = IMAGE_RIGHT_MSG_TYPE;
                } else if (ProtocolType.VOICE.equals(chatMessage.type)) {
                    type = VOICE_RIGHT_MSG_TYPE;
                }
            } else if (chatMessage.msg_direction.RECEIVER == chatMessage.msg_direction) {
                if (ProtocolType.COMMON.equals(chatMessage.type)) {
                    type = COMMON_MSG_TYPE;
                } else if (ProtocolType.LINK.equals(chatMessage.type)) {
                    type = LINK_LEFT_MSG_TYPE;
                } else if (ProtocolType.TEXT.equals(chatMessage.type)) {
                    type = TEXT_LEFT_MSG_TYPE;
                } else if (ProtocolType.FILE.equals(chatMessage.type)) {
                    type = FILE_LEFT_MSG_TYPE;
                } else if (ProtocolType.IMAGE.equals(chatMessage.type)) {
                    type = IMAGE_LEFT_MSG_TYPE;
                } else if (ProtocolType.VOICE.equals(chatMessage.type)) {
                    type = VOICE_LEFT_MSG_TYPE;
                }
            }
        }
        return type;
    }

    private void startInflateAnim(ImageView imageView) {
        Animation anim = AnimationUtils.loadAnimation(activityChatting.getApplicationContext(), R.anim.infinite_anim);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(1000);
        imageView.startAnimation(anim);
    }

    private void stopInflateAnim(final ImageView imageView) {
        activityChatting.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                imageView.clearAnimation();
                imageView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Friends head icon
     */
    void showFriendHeadIcon(final ChatMessage baseMessage, ImageView ImageView) {
        if (!TextUtils.isEmpty((baseMessage.from))) {
            ImageLoaderUtils.displayImage(activityChatting, R.mipmap.friend_head, ImageView);
            try {
                ImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            } catch (Exception e) {
                Log.d(TAG, "Exception : ",e);
            }
        }
    }

    /**
     * Show user head icon
     */
    void showUserHeadIcon(ImageView ImageView) {
        String headIconUrl = (String)SharePreferenceUtil.get(activityChatting, "from_head", "");
        if (!TextUtils.isEmpty(headIconUrl)) {
            ImageLoaderUtils.displayImage(activityChatting, headIconUrl, ImageView, R.mipmap.user_head);
        } else {
            ImageLoaderUtils.displayImage(activityChatting, R.mipmap.user_head, ImageView);
        }
    }

    /**
     * Show message time
     */
    boolean showMsgTime(ChatMessage baseMessage, int position) {
        boolean isDisplayTime = false;
        if (!TextUtils.isEmpty(baseMessage.datetime)) {
            String preDate = "";
            String nextDate = baseMessage.datetime;
            if (position != 0) {
                ChatMessage object2 = (ChatMessage) list.get(position - 1);
                if (null != object2) {
                    preDate = object2.datetime;
                }
                boolean between5Minutes = DateUtils.compareDatetimeBetween5Minutes(preDate, nextDate);
                if (!between5Minutes) {
                    isDisplayTime = true;
                }
            } else {
                isDisplayTime = true;
            }
        }
        return isDisplayTime;
    }

    /**
     * Handle the message status when user send out the message
     */
    void handleSendMsgState(final ChatMessage baseMessage, RecyclerView.ViewHolder convertView, int position, ImageView resendButton, ProgressBar sendProgressBar) {
        Log.d(TAG, "handleSendMsgState() ------>");
        Log.d(TAG, "------ handleSendMsgState(), position: " + position + " ------");
        Log.d(TAG, "------ handleSendMsgState()， ChatMessage: " + baseMessage + " ------");

        if (ProtocolType.IMAGE.equals(baseMessage.type)) {
            final ImageRightViewHolder imageRightViewHolder = (ImageRightViewHolder) convertView;
            if (EnumMsgSendStatus.MSG_SUCCESS == baseMessage.messageSendStatus) {
                Log.d(TAG, "------- MSG_SUCCESS:" + baseMessage.messageSendStatus);
                if (resendButton != null) {
                    resendButton.setVisibility(View.GONE);
                }
                if (sendProgressBar != null) {
                    sendProgressBar.setVisibility(View.GONE);
                }

                if (imageRightViewHolder.mImageRigtVHPictureLoading != null) {
                    imageRightViewHolder.mImageRigtVHPictureLoading.setVisibility(View.GONE);
                }

            } else if (EnumMsgSendStatus.MSG_SENDING == baseMessage.messageSendStatus) {
                Log.d(TAG, "------- MSG_SENDING:" + baseMessage.messageSendStatus);


            } else if (EnumMsgSendStatus.MSG_FAILED == baseMessage.messageSendStatus) {
                Log.d(TAG, "------- MSG_FAILED:" + baseMessage.messageSendStatus);

            }
        } else if (ProtocolType.VOICE.equals(baseMessage.type)) {
            if (EnumMsgSendStatus.MSG_SUCCESS == baseMessage.messageSendStatus) {
                Log.d(TAG, "------- MSG_SUCCESS:" + baseMessage.messageSendStatus);
                if (resendButton != null) {
                    resendButton.setVisibility(View.GONE);
                }
                if (sendProgressBar != null) {
                    sendProgressBar.setVisibility(View.GONE);
                }
            } else if (EnumMsgSendStatus.MSG_SENDING == baseMessage.messageSendStatus) {
                Log.d(TAG, "------- MSG_SENDING:" + baseMessage.messageSendStatus);
                if (resendButton != null) {
                    resendButton.setVisibility(View.GONE);
                }
                if (sendProgressBar != null) {
                    sendProgressBar.setVisibility(View.VISIBLE);
                }
                uploadVoiceMessage(baseMessage);
            } else if (EnumMsgSendStatus.MSG_FAILED == baseMessage.messageSendStatus) {
                Log.d(TAG, "------- MSG_FAILED:" + baseMessage.messageSendStatus);
                if (resendButton != null) {
                    resendButton.setVisibility(View.VISIBLE);
                }
                if (sendProgressBar != null) {
                    sendProgressBar.setVisibility(View.GONE);
                }
            }
        } else {
            if (EnumMsgSendStatus.MSG_SUCCESS == baseMessage.messageSendStatus) {
                Log.d(TAG, "------- MSG_SUCCESS:" + baseMessage.messageSendStatus);
                if (resendButton != null) {
                    resendButton.setVisibility(View.GONE);
                }
                if (sendProgressBar != null) {
                    sendProgressBar.setVisibility(View.GONE);
                }

            } else if (EnumMsgSendStatus.MSG_SENDING == baseMessage.messageSendStatus) {
                Log.d(TAG, "------- MSG_SENDING:" + baseMessage.messageSendStatus);
                if (resendButton != null) {
                    resendButton.setVisibility(View.GONE);
                }
                if (sendProgressBar != null) {
                    sendProgressBar.setVisibility(View.VISIBLE);
                }
            } else if (EnumMsgSendStatus.MSG_FAILED == baseMessage.messageSendStatus) {
                Log.d(TAG, "------- MSG_FAILED:" + baseMessage.messageSendStatus);
                if (resendButton != null) {
                    resendButton.setVisibility(View.VISIBLE);
                }
                if (sendProgressBar != null) {
                    sendProgressBar.setVisibility(View.GONE);
                }
            }
        }
        Log.d(TAG, "handleSendMsgState() <------");
    }

    /**
     * Handle the system message which show in chat screen center as tips.
     */
    void handleSystemMsg(SystemViewHolder systemViewHolder, int position, ChatMessage baseMessage) {
        Log.d(TAG, "handleSystemMsg() ------>");

            systemViewHolder.mSystemMsgLl.setVisibility(View.VISIBLE);
            systemViewHolder.mSystemMsgLl.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            systemViewHolder.mSystemMsgTime.setText("(" + baseMessage.datetime + ")");
            systemViewHolder.mSystemMsgTv.setText(baseMessage.content);
        Log.d(TAG, "handleSystemMsg() <------");
    }

    /**
     * Handle the link msg for left, which include the link information of link picture, link description,
     * link title and so on.
     */
    void handleLeftLinkMsg(LinkLeftViewHolder viewHolder, final ChatMessage baseMessage, int position) {
        Log.d(TAG, "handleLeftLinkMsg() ------>");
        boolean mDisPlay = showMsgTime(baseMessage, position);
        if (mDisPlay) {
            viewHolder.mReceiveTime.setVisibility(View.VISIBLE);
            viewHolder.mReceiveTime.setText(DateUtils.getDisplayDateTime(baseMessage.datetime, true));
        } else {
            viewHolder.mReceiveTime.setVisibility(View.GONE);
        }
        showFriendHeadIcon(baseMessage, viewHolder.mVednerHead);
        viewHolder.mLinkRl.setVisibility(View.VISIBLE);

        viewHolder.mLinkTitle.setText(baseMessage.link_title);
        viewHolder.mLinkDesc.setText(baseMessage.link_description);
        if (!TextUtils.isEmpty(baseMessage.link_image_url)) {
            ImageLoaderUtils.displayImage(activityChatting, baseMessage.link_image_url, viewHolder.mLinkImage, R.mipmap.friend_head);
        } else {
            ImageLoaderUtils.displayLocalImage(activityChatting, R.mipmap.friend_head, viewHolder.mLinkImage, R.mipmap.friend_head);
        }
        Log.d(TAG, "handleLeftLinkMsg() <------");
    }

    /**
     * Handle the link msg for right, which include the link information of link picture, link description,
     * link title and so on.
     */
    void handleRightLinkMsg(LinkRightViewHolder viewHolder, final ChatMessage baseMessage, int position) {
        Log.d(TAG, "handleRightLinkMsg() ------>");
        boolean mDisPlay = showMsgTime(baseMessage, position);
        if (mDisPlay) {
            viewHolder.mSendTime.setVisibility(View.VISIBLE);
            viewHolder.mSendTime.setText(DateUtils.getDisplayDateTime(baseMessage.datetime, true));
        } else {
            viewHolder.mSendTime.setVisibility(View.GONE);
        }
        showUserHeadIcon(viewHolder.mUserHead);
        viewHolder.mLinkRl.setVisibility(View.VISIBLE);

        viewHolder.mLinkTitle.setText(baseMessage.link_title);
        viewHolder.mLinkDesc.setText(baseMessage.link_description);
        if (!TextUtils.isEmpty(baseMessage.link_image_url)) {
            ImageLoaderUtils.displayImage(activityChatting, baseMessage.link_image_url, viewHolder.mLinkImage, R.mipmap.friend_head);
        } else {
            ImageLoaderUtils.displayLocalImage(activityChatting, R.mipmap.friend_head, viewHolder.mLinkImage, R.mipmap.friend_head);
        }

        handleSendMsgState(baseMessage, viewHolder, position, viewHolder.mBtnMsgSendState, viewHolder.mPbMsgSendState);
        Log.d(TAG, "handleRightLinkMsg() <------");
    }

    /**
     * 处理左边文本消息
     */
    void handleLeftTextMsg(final TextLeftViewHolder viewHolder, final ChatMessage baseMessage, final int position) {
        Log.d(TAG, "handleLeftTextMsg() ------>");
        if (TextUtils.isEmpty(baseMessage.content)) {
            return;
        }
        boolean mDisPlay = showMsgTime(baseMessage, position);
        if (mDisPlay) {
            viewHolder.mReceiveTime.setVisibility(View.VISIBLE);
            viewHolder.mReceiveTime.setText(DateUtils.getDisplayDateTime(baseMessage.datetime, true));
        } else {
            viewHolder.mReceiveTime.setVisibility(View.GONE);
        }
        showFriendHeadIcon(baseMessage, viewHolder.mFriendHead);
        viewHolder.mContentAndSmiley.setVisibility(View.VISIBLE);
        viewHolder.mImageGif.setVisibility(View.GONE);
        viewHolder.mContentAndSmiley.setText(baseMessage.content);
        Log.d(TAG, "handleLeftTextMsg() <------");
    }

    /**
     * 处理右边文本消息
     */
    void handleRightTextMsg(TextRightViewHolder viewHolder, final ChatMessage baseMessage, final int position) {
        Log.d(TAG, "handleRightTextMsg() ------>");
        if (TextUtils.isEmpty(baseMessage.content)) {
            return;
        }
        boolean mDisPlay = showMsgTime(baseMessage, position);
        if (mDisPlay) {
            viewHolder.mSendTime.setVisibility(View.VISIBLE);
            viewHolder.mSendTime.setText(DateUtils.getDisplayDateTime(baseMessage.datetime, true));
        } else {
            viewHolder.mSendTime.setVisibility(View.GONE);
        }
        showUserHeadIcon(viewHolder.mUserHead);
        viewHolder.mContentAndSmiley.setVisibility(View.VISIBLE);
        viewHolder.mImageGif.setVisibility(View.GONE);
        viewHolder.mContentAndSmiley.setText(baseMessage.content);
        handleSendMsgState(baseMessage, viewHolder, position, viewHolder.mBtnMsgSendState, viewHolder.mPbMsgSendState);
        Log.d(TAG, "handleRightTextMsg() <------");
    }

    /**
     * 处理左边文件消息
     */
    void handleLeftFileMsg(FileLeftViewHolder viewHolder, ChatMessage baseMessage, int position) {
        Log.d(TAG, "handleLeftFileMsg() ------>");
        boolean mDisPlay = showMsgTime(baseMessage, position);
        if (mDisPlay) {
            viewHolder.mReceiveTime.setVisibility(View.VISIBLE);
            viewHolder.mReceiveTime.setText(DateUtils.getDisplayDateTime(baseMessage.datetime, true));
        } else {
            viewHolder.mReceiveTime.setVisibility(View.GONE);
        }
        showFriendHeadIcon(baseMessage, viewHolder.mVednerHead);
        final String fileUrl = baseMessage.file_url;
        int fileType = FileUtils.getFileType(baseMessage.file_name);
        viewHolder.mFileImage.setImageResource(getFileResourceId(fileType));
        viewHolder.mFileName.setText(baseMessage.file_name);
        if (baseMessage.file_size > 0) {
            viewHolder.mFileSize.setVisibility(View.VISIBLE);
            viewHolder.mFileSize.setText(FileUtils.formatSizeShow(baseMessage.file_size));
        }
        viewHolder.mFileLin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(fileUrl));
                    activityChatting.startActivity(intent);
                } catch (Exception e) {
                    ToastUtil.showShortToast("Download File fails!");
                }
            }
        });
        Log.d(TAG, "handleLeftFileMsg() <------");
    }

    /**
     * 处理右边文件消息
     */
    void handleRightFileMsg(FileRightViewHolder viewHolder, ChatMessage baseMessage, int position) {
        Log.d(TAG, "handleLeftFileMsg() ------>");
        boolean mDisPlay = showMsgTime(baseMessage, position);
        if (mDisPlay) {
            viewHolder.mReceiveTime.setVisibility(View.VISIBLE);
            viewHolder.mReceiveTime.setText(DateUtils.getDisplayDateTime(baseMessage.datetime, true));
        } else {
            viewHolder.mReceiveTime.setVisibility(View.GONE);
        }
        showFriendHeadIcon(baseMessage, viewHolder.mVednerHead);
        final String fileUrl = baseMessage.file_url;
        int fileType = FileUtils.getFileType(baseMessage.file_name);
        viewHolder.mFileImage.setImageResource(getFileResourceId(fileType));
        viewHolder.mFileName.setText(baseMessage.file_name);
        if (baseMessage.file_size > 0) {
            viewHolder.mFileSize.setVisibility(View.VISIBLE);
            viewHolder.mFileSize.setText(FileUtils.formatSizeShow(baseMessage.file_size));
        }
        viewHolder.mFileLin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(fileUrl));
                    activityChatting.startActivity(intent);
                } catch (Exception e) {
//                    ToastUtil.showShortToast(R.string.load_file_error);
                }
            }
        });
        Log.d(TAG, "handleLeftFileMsg() <------");
    }

    /**
     * 处理左边图片消息
     */
    void handleLeftImageMsg(final ImageLeftViewHolder viewHolder, final ChatMessage baseMessage, final int position) {
        Log.d(TAG, "handleLeftImageMsg() ------>");
        if (TextUtils.isEmpty(baseMessage.picture_url)) {
            return;
        }
        viewHolder.mIvContentImage.clearAnimation();
        boolean mDisPlay = showMsgTime(baseMessage, position);
        if (mDisPlay) {
            viewHolder.mReceiveTime.setVisibility(View.VISIBLE);
            viewHolder.mReceiveTime.setText(DateUtils.getDisplayDateTime(baseMessage.datetime, true));
        } else {
            viewHolder.mReceiveTime.setVisibility(View.GONE);
        }
        showFriendHeadIcon(baseMessage, viewHolder.mVednerHead);
        viewHolder.mImageWrapper.setVisibility(View.VISIBLE);
        viewHolder.mIvContentImage.setVisibility(View.VISIBLE);
        viewHolder.mImageWrapper.setBackgroundResource(R.drawable.chat_from_background);
        viewHolder.mIvContentImage.setScaleType(ScaleType.FIT_CENTER);
        viewHolder.mIvContentImage.setAdjustViewBounds(true);
        LayoutParams imageLayoutParams = viewHolder.mIvContentImage.getLayoutParams();
        imageLayoutParams.width = mImageFixWidth;
        viewHolder.mIvContentImage.setLayoutParams(imageLayoutParams);

//        viewHolder.mIvContentImage.setImageURI(Uri.parse(newUrl));
        ImageLoaderUtils.displayImage(activityChatting, baseMessage.picture_url, viewHolder.mIvContentImage, R.mipmap.chatting_default_download_icon);
        bindClickListenerReceive(viewHolder.mIvContentImage, baseMessage, baseMessage.messageDownloadStatus.value());
        viewHolder.mIvContentImage.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                // TODO Auto-generated method stub
                handleLongClickMsg(baseMessage, position);
                return false;
            }
        });
        Log.d(TAG, "handleLeftImageMsg() <------");
    }

    /**
     * 处理右边图片消息
     */
    void handleRightImageMsg(final ImageRightViewHolder viewHolder, final ChatMessage baseMessage, final int position) {
        Log.d(TAG, "handleRightImageMsg() ------>");
        Log.d(TAG, "------ handleRightImageMsg(), position: " + position + " ------");
        boolean mDisPlay = showMsgTime(baseMessage, position);
        if (mDisPlay) {
            viewHolder.mImageRightVHSendTime.setVisibility(View.VISIBLE);
            viewHolder.mImageRightVHSendTime.setText(DateUtils.getDisplayDateTime(baseMessage.datetime, true));
        } else {
            viewHolder.mImageRightVHSendTime.setVisibility(View.GONE);
        }
        showUserHeadIcon(viewHolder.mImageRightVHUserIcon);
        viewHolder.mImageRigtVHPicture.clearAnimation();
        viewHolder.mImageRigtVHPicture.setImageBitmap(null);
        viewHolder.mImageRightVHBubble.setVisibility(View.VISIBLE);
        viewHolder.mImageRigtVHPicture.setVisibility(View.VISIBLE);
        viewHolder.mImageRightVHBubble.setBackgroundResource(R.drawable.chat_to_background);
        viewHolder.mImageRigtVHPicture.setScaleType(ScaleType.FIT_CENTER);
        viewHolder.mImageRigtVHPicture.setAdjustViewBounds(true);
        LayoutParams imageLayoutParams = viewHolder.mImageRigtVHPicture.getLayoutParams();
        imageLayoutParams.width = mImageFixWidth;
        viewHolder.mImageRigtVHPicture.setLayoutParams(imageLayoutParams);
        viewHolder.mImageRigtVHPicture.setVisibility(View.VISIBLE);
        viewHolder.mImageRightVHPictureOverlay.setForeground(null);
        if (baseMessage.messageSendStatus == EnumMsgSendStatus.MSG_SENDING) {// 正在上传
            viewHolder.mImageRigtVHPictureLoading.setVisibility(View.VISIBLE);
            startInflateAnim(viewHolder.mImageRigtVHPictureLoading);
            viewHolder.mImageRightVHPictureOverlay.setForeground(mImageLoadingOverlay);
        }// 处理又新发一条消息的时候UI问题
        if (!TextUtils.isEmpty(baseMessage.file_local_path)) {
            File file = new File(baseMessage.file_local_path);
            Log.d(TAG, "------ handleRightImageMsg(), file_path: " + baseMessage.file_local_path + " ------");
            ImageLoaderUtils.displayLocalImage(activityChatting, baseMessage.file_local_path, viewHolder.mImageRigtVHPicture, R.mipmap.chatting_default_download_icon);
            bindClickListenerSend(viewHolder.mImageRigtVHPicture, baseMessage, file.getAbsolutePath(), file.getAbsolutePath(), baseMessage.messageDownloadStatus.value(), file.getAbsolutePath(), baseMessage.id);
        }else {
            Log.d(TAG, "------ handleRightImageMsg(), default:  ------");
            viewHolder.mImageRigtVHPicture.setImageResource(R.mipmap.chatting_default_download_icon);
        }
        viewHolder.mImageRigtVHPicture.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                // TODO Auto-generated method stub
                handleLongClickMsg(baseMessage, position);
                return false;
            }
        });
        handleSendMsgState(baseMessage, viewHolder, position, viewHolder.mImageRightVHResendBtn, viewHolder.mImageRightVHSendPB);
        Log.d(TAG, "handleRightImageMsg() <------");
    }

    /**
     * 处理左边语音消息
     */
    void handleLeftVoiceMsg(final VoiceLeftViewHolder viewHolder, final ChatMessage baseMessage, final int position) {
        Log.d(TAG, "handleLeftVoiceMsg() ------>");
        boolean mDisPlay = showMsgTime(baseMessage, position);
        if (mDisPlay) {
            viewHolder.mReceiveTime.setVisibility(View.VISIBLE);
            viewHolder.mReceiveTime.setText(DateUtils.getDisplayDateTime(baseMessage.datetime, true));
        } else {
            viewHolder.mReceiveTime.setVisibility(View.GONE);
        }
        showFriendHeadIcon(baseMessage, viewHolder.mVedorHead);
        viewHolder.mImageLoadingOverlay.setForeground(null);
        viewHolder.mAudioPlayTime.setVisibility(View.VISIBLE);
        viewHolder.mAudioPlayTime.setText("" + baseMessage.voice_duration + "\"");
        viewHolder.mImageWrapper.setVisibility(View.VISIBLE);
        viewHolder.mIvContentImage.setMinimumHeight(50);
        final int id = R.mipmap.audio_play_left_3;
        viewHolder.mIvContentImage.setScaleType(ScaleType.FIT_START);
        viewHolder.mIvContentImage.setAdjustViewBounds(false);
        LayoutParams audioLayoutParams = viewHolder.mIvContentImage.getLayoutParams();
        audioLayoutParams.width = (int) Math.min(mAudioMaxWidth, mAudioMinWidth + (baseMessage.voice_duration * 1.0f / 10) * (mAudioMaxWidth - mAudioMinWidth));
        viewHolder.mIvContentImage.setLayoutParams(audioLayoutParams);
        viewHolder.mIvContentImage.setImageResource(id);
        viewHolder.mIvContentImage.setVisibility(View.VISIBLE);
        viewHolder.mImageWrapper.setBackgroundResource(R.drawable.chat_from_background);
        if (baseMessage.voice_playing) {
            startFrameAnim(viewHolder.mIvContentImage, true);
        } else {
            stopFrameAnim(viewHolder.mIvContentImage, true);
        }
        viewHolder.mIvContentImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (PermissionsUtil.getInstance().isMNC()) {
                    boolean hasPermission = PermissionsUtil.getInstance().hasPermission(App.getInst(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (!hasPermission) {
                        PermissionsUtil.getInstance().requestPermission(activityChatting, Manifest.permission.WRITE_EXTERNAL_STORAGE, 0, activityChatting);
                    } else {
                        onAudioClick(v, baseMessage.voice_url, true, id, baseMessage);
                    }
                } else {
                    onAudioClick(v, baseMessage.voice_url, true, id, baseMessage);
                }
            }
        });
        viewHolder.mIvContentImage.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                // TODO Auto-generated method stub
                handleLongClickMsg(baseMessage, position);
                return false;
            }
        });
        Log.d(TAG, "handleLeftVoiceMsg() <------");
    }

    /**
     * 处理右边语音消息
     */
    void handleRightVoiceMsg(final VoiceRightViewHolder viewHolder, final ChatMessage baseMessage, final int position) {
        Log.d(TAG, "handleRightVoiceMsg() ------>");
        boolean mDisPlay = showMsgTime(baseMessage, position);
        if (mDisPlay) {
            viewHolder.mSendTime.setVisibility(View.VISIBLE);
            viewHolder.mSendTime.setText(DateUtils.getDisplayDateTime(baseMessage.datetime, true));
        } else {
            viewHolder.mSendTime.setVisibility(View.GONE);
        }
        showUserHeadIcon(viewHolder.mUserHead);
        final String audioUrl = baseMessage.voice_url;
        viewHolder.mImageLoadingOverlay.setForeground(null);
        viewHolder.mAudioPlayTime.setVisibility(View.VISIBLE);
        viewHolder.mAudioPlayTime.setText("" + baseMessage.voice_duration + "\"");
        viewHolder.mImageWrapper.setVisibility(View.VISIBLE);
        viewHolder.mIvContentImage.setMinimumHeight(50);
        final int id = R.mipmap.audio_play_right_3;
        viewHolder.mIvContentImage.setScaleType(ScaleType.FIT_END);
        viewHolder.mIvContentImage.setAdjustViewBounds(false);
        LayoutParams audioLayoutParams = viewHolder.mIvContentImage.getLayoutParams();
        audioLayoutParams.width = (int) Math.min(mAudioMaxWidth, mAudioMinWidth + (baseMessage.voice_duration * 1.0f / 10) * (mAudioMaxWidth - mAudioMinWidth));
        viewHolder.mIvContentImage.setLayoutParams(audioLayoutParams);
        viewHolder.mIvContentImage.setImageResource(id);
        viewHolder.mIvContentImage.setVisibility(View.VISIBLE);
        viewHolder.mImageWrapper.setBackgroundResource(R.drawable.chat_to_background);
        if (baseMessage.voice_playing) {
            startFrameAnim(viewHolder.mIvContentImage, false);
        } else {
            stopFrameAnim(viewHolder.mIvContentImage, false);
        }
        viewHolder.mIvContentImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                if (PermissionsUtil.getInstance().isMNC()) {
                    boolean hasPermission = PermissionsUtil.getInstance().hasPermission(App.getInst(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (!hasPermission) {
                        PermissionsUtil.getInstance().requestPermission(activityChatting, Manifest.permission.WRITE_EXTERNAL_STORAGE, 0, activityChatting);
                    } else {
                        onAudioClick(v, audioUrl, false, id, baseMessage);
                    }
                } else {
                    onAudioClick(v, audioUrl, false, id, baseMessage);
                }
            }
        });
        viewHolder.mIvContentImage.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                // TODO Auto-generated method stub
                handleLongClickMsg(baseMessage, position);
                return false;
            }
        });
        handleSendMsgState(baseMessage, viewHolder, position, viewHolder.mBtnMsgSendState, viewHolder.mPbMsgSendState);

        Log.d(TAG, "handleRightVoiceMsg() <------");
    }

    public void handleLongClickMsg(ChatMessage chatMessage, int position) {
//        try {
//            FragmentChatMsgLongClick dialog = new FragmentChatMsgLongClick(chatMessage, position, activityChatting, mChatMsgViewAdapter, mListView);
//            dialog.show(activityChatting.getSupportFragmentManager(), "msg_long_click");
//        } catch (Exception e) {
//            Log.e(TAG, "长按弹出框失败");
//            e.printStackTrace();
//        }
    }

    /***
     * 系统消息viewHolder
     */
    private class SystemViewHolder extends RecyclerView.ViewHolder {
        /**
         * 普通系统消息
         **/
        private LinearLayout mSystemMsgLl;
        private TextView mSystemMsgTime;
        private TextView mSystemMsgTv;


        public SystemViewHolder(View convertView) {
            super(convertView);
            //普通系统消息
            mSystemMsgLl = (LinearLayout) convertView.findViewById(R.id.chatting_system_msg_ll);
            mSystemMsgTime = (TextView) convertView.findViewById(R.id.chatting_system_msg_time);
            mSystemMsgTv = (TextView) convertView.findViewById(R.id.chatting_system_msg_tv);
        }
    }

    /**
     * 左边link消息
     **/
    private class LinkLeftViewHolder extends RecyclerView.ViewHolder {
        private TextView mReceiveTime;
        private ImageView mVednerHead;
        private RelativeLayout mLinkRl;
        private ImageView mLinkImage;
        private TextView mLinkTitle;
        private TextView mLinkDesc;

        public LinkLeftViewHolder(View convertView) {
            super(convertView);
            mReceiveTime = (TextView) convertView.findViewById(R.id.chatting_left_link_msg_time);
            mVednerHead = (ImageView) convertView.findViewById(R.id.chatting_left_link_friend_head);
            mLinkRl = (RelativeLayout) convertView.findViewById(R.id.chatting_left_link_rl);
            mLinkImage = (ImageView) convertView.findViewById(R.id.chatting_left_link_image);
            mLinkTitle = (TextView) convertView.findViewById(R.id.chatting_left_link_title);
            mLinkDesc = (TextView) convertView.findViewById(R.id.chatting_left_link_desc);
        }
    }

    /**
     * 右边link消息
     **/
    private class LinkRightViewHolder extends RecyclerView.ViewHolder {
        private TextView mSendTime;
        private ImageView mUserHead;
        private ImageButton mBtnMsgSendState;
        private ProgressBar mPbMsgSendState;
        private RelativeLayout mLinkRl;
        private ImageView mLinkImage;
        private TextView mLinkTitle;
        private TextView mLinkDesc;

        public LinkRightViewHolder(View convertView) {
            super(convertView);
            mSendTime = (TextView) convertView.findViewById(R.id.chatting_right_link_msg_time);
            mUserHead = (ImageView) convertView.findViewById(R.id.chatting_right_link_user_head);
            mBtnMsgSendState = (ImageButton) convertView.findViewById(R.id.chatting_right_link_msg_send_state);
            mPbMsgSendState = (ProgressBar) convertView.findViewById(R.id.chatting_right_link_msg_sending_progressbar);
            mLinkRl = (RelativeLayout) convertView.findViewById(R.id.chatting_right_link_rl);
            mLinkImage = (ImageView) convertView.findViewById(R.id.chatting_right_link_image);
            mLinkTitle = (TextView) convertView.findViewById(R.id.chatting_right_link_title);
            mLinkDesc = (TextView) convertView.findViewById(R.id.chatting_right_link_desc);
        }
    }

    /**
     * 左边文本消息
     **/
    private class TextLeftViewHolder extends RecyclerView.ViewHolder {
        private TextView mReceiveTime;
        private ImageView mFriendHead;
        private TextView mContentAndSmiley;//显示文本，静态表情
        private ImageView mImageGif;// 显示gif表情

        public TextLeftViewHolder(View convertView) {
            super(convertView);
            mReceiveTime = (TextView) convertView.findViewById(R.id.chatting_left_text_msg_time);
            mFriendHead = (ImageView) convertView.findViewById(R.id.chatting_left_text_friend_head);
            mContentAndSmiley = (TextView) convertView.findViewById(R.id.chatting_left_text_common_text);
            mImageGif = (ImageView) convertView.findViewById(R.id.chatting_left_text_smiley_text);
        }
    }

    /**
     * 右边文本消息
     **/
    private class TextRightViewHolder extends RecyclerView.ViewHolder {
        private TextView mSendTime;
        private ImageView mUserHead;
        private ImageButton mBtnMsgSendState;
        private ProgressBar mPbMsgSendState;
        private TextView mContentAndSmiley;//显示文本，静态表情
        private ImageView mImageGif;// 显示gif表情

        public TextRightViewHolder(View convertView) {
            super(convertView);
            mSendTime = (TextView) convertView.findViewById(R.id.chatting_right_text_msg_time);
            mUserHead = (ImageView) convertView.findViewById(R.id.chatting_right_text_user_head);
            mBtnMsgSendState = (ImageButton) convertView.findViewById(R.id.chatting_right_text_msg_send_state);
            mPbMsgSendState = (ProgressBar) convertView.findViewById(R.id.chatting_right_text_msg_sending_prograessbar);
            mContentAndSmiley = (TextView) convertView.findViewById(R.id.chatting_right_text_common_text);
            mImageGif = (ImageView) convertView.findViewById(R.id.chatting_right_text_smiley_text);

        }
    }

    /**
     * 左边文件消息
     **/
    private class FileLeftViewHolder extends RecyclerView.ViewHolder {
        private TextView mReceiveTime;
        private ImageView mVednerHead;
        private LinearLayout mFileLin;
        private ImageView mFileImage;
        private TextView mFileName;
        private TextView mFileSize;

        public FileLeftViewHolder(View convertView) {
            super(convertView);
            mReceiveTime = (TextView) convertView.findViewById(R.id.chatting_left_file_msg_time);
            mVednerHead = (ImageView) convertView.findViewById(R.id.chatting_left_file_friend_head);
            mFileLin = (LinearLayout) convertView.findViewById(R.id.chatting_left_file_lin);
            mFileImage = (ImageView) convertView.findViewById(R.id.chatting_left_file_image);
            mFileName = (TextView) convertView.findViewById(R.id.chatting_left_file_name);
            mFileSize = (TextView) convertView.findViewById(R.id.chatting_left_file_size);

        }
    }

    /**
     * 左边图片消息
     **/
    private class ImageLeftViewHolder extends RecyclerView.ViewHolder {
        private TextView mReceiveTime;
        private ImageView mVednerHead;
        private View mImageWrapper;
        private FrameLayout mImageLoadingOverlay;
        private ImageView mIvContentImage;//图片显示view
        private ImageView mIvContentImageTwo;//下载转圈view
//        private TextView mIvContentText;//下载失败提示

        public ImageLeftViewHolder(View convertView) {
            super(convertView);
            mReceiveTime = (TextView) convertView.findViewById(R.id.chatting_left_image_msg_time);
            mVednerHead = (ImageView) convertView.findViewById(R.id.chatting_left_image_friend_head);
            mImageWrapper = convertView.findViewById(R.id.chatting_left_image_rl_image_wrapper);
            mImageLoadingOverlay = (FrameLayout) convertView.findViewById(R.id.chatting_left_image_rl_image_overlay);
            mIvContentImage = (ImageView) convertView.findViewById(R.id.chatting_left_image_image);
            mIvContentImageTwo = (ImageView) convertView.findViewById(R.id.chatting_left_image_image_two);
        }
    }

    /**
     * 右边图片消息
     **/
    private class ImageRightViewHolder extends RecyclerView.ViewHolder {
        private TextView mImageRightVHSendTime;
        private ImageView mImageRightVHUserIcon;
        private ImageButton mImageRightVHResendBtn;
        private ProgressBar mImageRightVHSendPB;
        private View mImageRightVHBubble;
        private FrameLayout mImageRightVHPictureOverlay;
        //        private ImageView mImageRigtVHPicture;//图片显示view
        private ImageView mImageRigtVHPicture;//图片显示view
        private ImageView mImageRigtVHPictureLoading;//上传转圈view

//        public boolean isImageShow = false;
//        public String ImagefilePath;

        public ImageRightViewHolder(View convertView) {
            super(convertView);
            mImageRightVHSendTime = (TextView) convertView.findViewById(R.id.chatting_right_image_msg_time);
            mImageRightVHUserIcon = (ImageView) convertView.findViewById(R.id.chatting_right_image_user_head);
            mImageRightVHResendBtn = (ImageButton) convertView.findViewById(R.id.chatting_right_image_msg_send_state);
            mImageRightVHSendPB = (ProgressBar) convertView.findViewById(R.id.chatting_right_image_msg_sending_progressbar);
            mImageRightVHBubble = convertView.findViewById(R.id.chatting_right_image_rl_image_wrapper);
            mImageRightVHPictureOverlay = (FrameLayout) convertView.findViewById(R.id.chatting_right_image_rl_image_overlay);
            mImageRigtVHPicture = (ImageView) convertView.findViewById(R.id.chatting_right_image_image);
            mImageRigtVHPictureLoading = (ImageView) convertView.findViewById(R.id.chatting_right_image_image_two_right);
        }
    }

    /**
     * 左边语音消息
     **/
    private class VoiceLeftViewHolder extends RecyclerView.ViewHolder {
        private TextView mReceiveTime;
        private ImageView mVedorHead;
        private View mImageWrapper;
        private FrameLayout mImageLoadingOverlay;
        private ImageView mIvContentImage;//语音显示view
        private TextView mAudioPlayTime;//语音时长

        public VoiceLeftViewHolder(View convertView) {
            super(convertView);
            mReceiveTime = (TextView) convertView.findViewById(R.id.chatting_left_voice_msg_time);
            mVedorHead = (ImageView) convertView.findViewById(R.id.chatting_left_voice_friend_head);
            mImageWrapper = convertView.findViewById(R.id.chatting_left_voice_rl_image_wrapper);
            mImageLoadingOverlay = (FrameLayout) convertView.findViewById(R.id.chatting_left_voice_rl_image_overlay);
            mIvContentImage = (ImageView) convertView.findViewById(R.id.chatting_left_voice_iv_image);
            mAudioPlayTime = (TextView) convertView.findViewById(R.id.chatting_left_voice_audio_play_time);
        }
    }

    /**
     * 右边语音消息
     **/
    private class VoiceRightViewHolder extends RecyclerView.ViewHolder {
        private TextView mSendTime;
        private ImageView mUserHead;
        private ImageButton mBtnMsgSendState;
        private ProgressBar mPbMsgSendState;
        private View mImageWrapper;
        private FrameLayout mImageLoadingOverlay;
        private ImageView mIvContentImage;//语音显示view
        private TextView mAudioPlayTime;//语音时长

        public VoiceRightViewHolder(View convertView) {
            super(convertView);
            mSendTime = (TextView) convertView.findViewById(R.id.chatting_right_voice_msg_time);
            mUserHead = (ImageView) convertView.findViewById(R.id.chatting_right_voice_user_head);
            mBtnMsgSendState = (ImageButton) convertView.findViewById(R.id.chatting_right_voice_msg_send_state);
            mPbMsgSendState = (ProgressBar) convertView.findViewById(R.id.chatting_right_voice_msg_sending_progressbar);
            mImageWrapper = convertView.findViewById(R.id.chatting_right_voice_rl_image_wrapper);
            mImageLoadingOverlay = (FrameLayout) convertView.findViewById(R.id.chatting_right_voice_rl_image_overlay);
            mIvContentImage = (ImageView) convertView.findViewById(R.id.chatting_right_voice_iv_image);
            mAudioPlayTime = (TextView) convertView.findViewById(R.id.chatting_right_voice_audio_play_time);
        }
    }

    /**
     * 右边文件消息
     **/
    private class FileRightViewHolder extends RecyclerView.ViewHolder {
        private TextView mReceiveTime;
        private ImageView mVednerHead;
        private LinearLayout mFileLin;
        private ImageView mFileImage;
        private TextView mFileName;
        private TextView mFileSize;

        public FileRightViewHolder(View convertView) {
            super(convertView);
            mReceiveTime = (TextView) convertView.findViewById(R.id.chatting_right_file_msg_time);
            mVednerHead = (ImageView) convertView.findViewById(R.id.chatting_right_file_user_head);
//            mFileLin = (LinearLayout) convertView.findViewById(R.id.chatting_right_file_lin);
//            mFileImage = (ImageView) convertView.findViewById(R.id.chatting_right_file_image);
            mFileName = (TextView) convertView.findViewById(R.id.chatting_right_file_name);
            mFileSize = (TextView) convertView.findViewById(R.id.chatting_right_file_size);

        }
    }


    /**
     * 下来刷新控件
     */
    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar loadMoreProgressBar;

        public ProgressViewHolder(View v) {
            super(v);
            Log.d(TAG, "------ ProgressViewHolder() ------>");
            loadMoreProgressBar = (ProgressBar) v.findViewById(R.id.chat_list_load_more_progressBar);
            if (null == loadMoreProgressBar) {
                Log.d(TAG, "------ ProgressBar == null ------");
            } else {
                Log.d(TAG, "------ ProgressBar != null ------");
            }
            Log.d(TAG, "<------ ProgressViewHolder() ------");
        }
    }

    private void onAudioClick(View v, String content, final boolean isComeFinal, final int id, final ChatMessage verForDBorUiFianal) {
        // TODO 自动生成的方法存根
        Log.d(TAG, "onAudioClick() ------>");

    }

    /**
     * Upload the voice file to service.
     *
     * @param chatMessage
     */

    public void uploadVoiceMessage(final ChatMessage chatMessage) {

    }


    /**
     * Title: setMsgState
     * Description: set the msg state
     */
    public void setMsgState(String msg_id, int resultCode) {
        Log.d(TAG, "setMsgState() ---->, msg_Id: " + msg_id + ", resultCode:" + resultCode);
        for (Object msg : list) {
            if (msg instanceof ChatMessage) {// 上行
                ChatMessage tcpUpAsk = (ChatMessage) msg;
                if (msg_id.equals(tcpUpAsk.id)) {
                    tcpUpAsk.messageSendStatus = EnumMsgSendStatus.valueOf(resultCode);
                    tcpUpAsk.messageReadStatus = EnumMsgReadStatus.valueOf(resultCode);
                    notifyDataSetChanged();
                    return;
                }
            } else if (msg instanceof ChatMessage) {
                ChatMessage tcpDownAnswer = (ChatMessage) msg;
                if (msg_id.equals(tcpDownAnswer.id)) {
                    tcpDownAnswer.messageSendStatus = EnumMsgSendStatus.valueOf(resultCode);
                    tcpDownAnswer.messageReadStatus = EnumMsgReadStatus.valueOf(resultCode);
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    public void setMsgReadSendStatus(String msg_id, int readStatus, int sendStatus) {
        Log.d(TAG, "setMsgReadSendStatus() ---->, msg_Id: " + msg_id + ", readStatus:" + readStatus);
        Log.d(TAG, "setMsgReadSendStatus() ---->, msg_Id: " + msg_id + ", sendStatus:" + sendStatus);
        for (Object msg : list) {
            if (msg instanceof ChatMessage) {// 上行
                ChatMessage tcpUpAsk = (ChatMessage) msg;
                if (msg_id.equals(tcpUpAsk.id)) {
                    tcpUpAsk.messageSendStatus = EnumMsgSendStatus.valueOf(sendStatus);
                    tcpUpAsk.messageReadStatus = EnumMsgReadStatus.valueOf(readStatus);
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    public void setMsgSendStatus(String msg_id, int sendStatus) {
        Log.d(TAG, "setMsgSendStatus() ---->, msg_Id: " + msg_id + ", sendStatus:" + sendStatus);
        for (Object msg : list) {
            if (msg instanceof ChatMessage) {// 上行
                ChatMessage tcpUpAsk = (ChatMessage) msg;
                if (msg_id.equals(tcpUpAsk.id)) {

                    tcpUpAsk.messageSendStatus = EnumMsgSendStatus.valueOf(sendStatus);
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    public void setMsgReadStatus(String msg_id, int readStatus) {
        Log.d(TAG, "setMsgReadStatus() ---->, msg_Id: " + msg_id + ", readStatus:" + readStatus);
        for (Object msg : list) {
            if (msg instanceof ChatMessage) {// 上行
                ChatMessage tcpUpAsk = (ChatMessage) msg;
                if (msg_id.equals(tcpUpAsk.id)) {
                    tcpUpAsk.messageReadStatus = EnumMsgReadStatus.valueOf(readStatus);
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    public int getFileResourceId(int filetype) {
        int resourceId = R.mipmap.file_undefine;
        switch (filetype) {
            case 0:
                resourceId = R.mipmap.file_undefine;
                break;
            case 1:
                break;
            case 2:
                resourceId = R.mipmap.file_excel;
                break;
            case 3:
                resourceId = R.mipmap.file_exe;
                break;
            case 4:
                resourceId = R.mipmap.file_image;
                break;
            case 5:
                resourceId = R.mipmap.file_music;
                break;
            case 6:
                resourceId = R.mipmap.file_ppt;
                break;
            case 7:
                resourceId = R.mipmap.file_txt;
                break;
            case 8:
                resourceId = R.mipmap.file_word;
                break;
            case 9:
                resourceId = R.mipmap.file_zip;
                break;
            default:
                resourceId = R.mipmap.file_undefine;
                break;
        }
        return resourceId;
    }


    private void bindClickListenerReceive(final ImageView imageView, final ChatMessage basemessage, final int downLoadState) {
        if (imageView != null && basemessage != null) {
            if (downLoadState != EnumMsgDownloadStatus.ERROR.value()) {
                imageView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        /** 进图图片预览页 */
                        ActivityImagePreview.ImageInfo images = getImagesByUid(basemessage, null, null, null, null);
                        showActivityImagePreview(activityChatting, images, mCurrentShowPicIndex, "rec");
                    }
                });
            }
        }
    }

    private void bindClickListenerSend(final ImageView imageView, final ChatMessage basemessage, final String path, final String thumbPath, final int downLoadState, final String bigFilePath, final String msgId) {
        if (imageView != null && basemessage != null) {
            if (downLoadState != EnumMsgDownloadStatus.ERROR.value()) {
                imageView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        /** 进图图片预览页 */
                        ActivityImagePreview.ImageInfo images = getImagesByUid(basemessage, path, thumbPath, bigFilePath, msgId);
                        showActivityImagePreview(activityChatting, images, mCurrentShowPicIndex, "send");
                    }
                });
            }
        }
    }

    private void startFrameAnim(ImageView imageView, boolean isComMsg) {
        int resourceId = isComMsg ? R.drawable.frame_audio_left_play_anim : R.drawable.frame_audio_right_play_anim;
        imageView.setImageResource(resourceId);
        ((AnimationDrawable) imageView.getDrawable()).start();
    }

    private void stopFrameAnim(ImageView imageView, boolean isComMsg) {
        if (imageView.getDrawable() instanceof AnimationDrawable) {
            int id = isComMsg ? R.mipmap.audio_play_left_3 : R.mipmap.audio_play_right_3;
            ((AnimationDrawable) imageView.getDrawable()).stop();
            imageView.clearAnimation();
            imageView.setImageResource(id);
        }
    }

    public void clear() {
        if (null != list) {
            list.clear();
        }
        notifyDataSetChanged();
    }

    public static void showActivityImagePreview(Context context, ActivityImagePreview.ImageInfo images, int currentIndex, String sendOrRec) {
        Intent intent = new Intent(context, ActivityImagePreview.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("images", images);
        bundle.putInt("index", currentIndex);
        bundle.putString("sendOrRec", sendOrRec);
        bundle.putString("from", "chat");
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public ActivityImagePreview.ImageInfo getImagesByUid(ChatMessage chatMessage, String path, String thumbPath, String bigPath, String msgId) {
        ActivityImagePreview.ImageInfo info;
        if (chatMessage.msg_direction == EnumMsgDirection.RECEIVER) {
            ChatMessage upAsk = chatMessage;
            if (TextUtils.isEmpty(path)) {
                path = TextUtils.isEmpty(bigPath) ? thumbPath : bigPath;// 处理发送图片首次进入预览并保存时报空指针
            }
            info = new ActivityImagePreview.ImageInfo(StringUtils.getUrlFromHtml(upAsk.picture_url), path, upAsk.id, upAsk.datetime, thumbPath, bigPath);
            // 缩略图暂时未存库
        } else {
            ChatMessage downAnswer = chatMessage;
            info = new ActivityImagePreview.ImageInfo(StringUtils.getUrlFromHtml(downAnswer.picture_url), path, downAnswer.id, downAnswer.datetime, thumbPath, bigPath);
            // 缩略图暂时未存库
        }
        return info;
    }

}