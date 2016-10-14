package com.reactiveapps.chatty.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.reactiveapps.chatty.BuildConfig;
import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.message.ProtocolType;
import com.reactiveapps.chatty.message.EnumMsgDirection;
import com.reactiveapps.chatty.model.bean.ChatMessage;
import com.reactiveapps.chatty.utils.DensityUtil;
import com.reactiveapps.chatty.view.Base.ActivityBase;
import com.reactiveapps.chatty.view.adapter.AdapterChatMsgView;
import com.reactiveapps.chatty.view.widget.ChatToolBox;
import com.reactiveapps.chatty.view.widget.EndlessRecyclerOnScrollListener2;
import com.reactiveapps.chatty.view.widget.InputChat;
import com.reactiveapps.chatty.view.widget.KeyboardDetectorRelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class ActivityChatting extends ActivityBase {
    private static final String TAG = "input_chat";

    /**
     *  Title bar controls
     */

    //    private ScrollSwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mChatListView;//listview
    private AdapterChatMsgView mChatAdapter;// The message list view Adapter
    private List<Object> mChatDataArrays = new ArrayList<Object>();// the message object array
    private boolean isLoading;

    /**
     * Chat message list
     */

    private boolean isLoadMore = false;
    private int mLastOffset;
    private EndlessRecyclerOnScrollListener2 mEndlessRecyclerOnScrollListener2;


    private InputChat inputChat;

    /**
     * Handler msg id
     */
    private static final int MSG_SEND_TIMEOUT_WHAT = 0x1;
    private static final int MSG_NORESPONSE = 0x2;
    private static final int WAITER_STAUS_MESSAGE_WHAT = 0x3;
    public static int MSG_STOP_AUTH_PROGRESSBAR = 0x00000010;
    public static int MSG_LOADING_TIMEOUT = 0x00000011;

    public static int MSG_REQUEST_AUTH_STATUS = 0x00000012;
    public static int MSG_STOP_LOADING = 0x00000013;
    public static int MSG_AUTO_SCROLL_SCREEN = 0x00000014;
    public static int MSG_AUTO_DATA_CHANGE_SCROLL_SCREEN = 0x00000015;


    /**
     * Handler object
     */
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_STOP_AUTH_PROGRESSBAR) {
                Log.d(TAG, "------ msg.what == MSG_STOP_AUTH_PROGRESSBAR ------");
            } else if (msg.what == MSG_LOADING_TIMEOUT) {
                Log.d(TAG, "------ msg.what == MSG_LOADING_TIMEOUT ------");
                isLoading = false;
            } else if (msg.what == MSG_REQUEST_AUTH_STATUS) {
                Log.d(TAG, "------ msg.what == MSG_REQUEST_AUTH_STATUS ------");

            } else if (msg.what == MSG_STOP_LOADING) {
                Log.d(TAG, "------ msg.what == MSG_STOP_LOADING ------");
                isLoading = false;
//                mSwipeRefreshLayout.setRefreshing(false);
                removeLoadMoreProgressBar();
            } else if (MSG_AUTO_SCROLL_SCREEN == msg.what) {
                Log.d(TAG, "------ msg.what == MSG_AUTO_SCROLL_SCREEN ------");
                removeMessages(MSG_AUTO_SCROLL_SCREEN);
                scrollToEnd();
            } else if (MSG_AUTO_DATA_CHANGE_SCROLL_SCREEN == msg.what) {
                Log.d(TAG, "------ MSG_AUTO_DATA_CHANGE_SCROLL_SCREEN  ,notifyDataSetChanged() ------");
                mChatAdapter.notifyDataSetChanged(mChatDataArrays);
                scrollToEnd();
            } else if (msg.what == MSG_NORESPONSE) {
                Log.d(TAG, "------ msg.what == MSG_NORESPONSE ------");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        inputChat = new InputChat();

        inputChat.addToolBoxData(new ToolAd());

        getSupportFragmentManager().beginTransaction().replace(R.id.input_container, inputChat, TAG).commit();
        inputChat.setInputChatListener(new InputChat.InputChatListener() {
            @Override
            public void onSendMessage(String msg) {
                Log.d(TAG, "sendMessage:" + msg);
                addItemToChatList(createLeftTextMsg("Other","Me",msg));
            }

            @Override
            public void onSendVoiceMessage(String path, int duration, String description) {
                Log.d(TAG, "SendVoiceMessage:" + "path:" + path + " duration:" + duration + " description:" + description);
            }

            @Override
            public void onPickPhotoMessage(Intent data) {
                Log.d(TAG, "PickPhotoMessage:" + data.toString());
            }

            @Override
            public void onTakePhotoMessage(Intent data) {
                Log.d(TAG, "TakePhotoMessage:" + data.toString());
            }

            @Override
            public void onPickLocMessage(Intent data) {
                Log.d(TAG, "PickLocMessage:" + data.toString());
            }
        });

        KeyboardDetectorRelativeLayout root = (KeyboardDetectorRelativeLayout) findViewById(R.id.chat_root);
        root.setOnSoftKeyboardListener(new KeyboardDetectorRelativeLayout.OnSoftKeyboardListener() {
            @Override
            public void onShown(int keyboardHeight) {
                inputChat.onKeyboardShow(keyboardHeight);
            }

            @Override
            public void onHidden() {
                inputChat.onKeyboardDismiss();
            }

            @Override
            public void onMeasureFinished() {

            }
        });

        initView();

        initFirst();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //条用基类的方法，以便调出系统菜单（如果有的话）
        //super.onCreateOptionsMenu(menu);
        MenuItem item1 = menu.add(0, 1, 1, "Search");
        item1.setIcon(R.mipmap.icon_search);
        item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem item2 = menu.add(0, 2, 2, "Overflow");
        item2.setIcon(R.mipmap.icon_add);
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem item3 = menu.add(0, 2, 3, "Group Chat");
        item3.setIcon(R.mipmap.icon_menu_group_chat);
        item3.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem item4 = menu.add(0, 2, 4, "Add Contacts");
        item4.setIcon(R.mipmap.icon_menu_add_contacts);
        item4.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem item5 = menu.add(0, 2, 5, "Scan QR Code");
        item5.setIcon(R.mipmap.icon_menu_scan_qr);
        item5.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem item6 = menu.add(0, 2, 6, "Money");
        item6.setIcon(R.mipmap.icon_menu_scan_qr);
        item6.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem item7 = menu.add(0, 2, 7, "Help & Feedback");
        item7.setIcon(R.mipmap.icon_menu_scan_qr);
        item7.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        //返回值为“true”,表示菜单可见，即显示菜单
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == 2) {
            Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == 3) {
            Toast.makeText(this, "Overflow Menu", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == 4) {
            Toast.makeText(this, "Add Contacts", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == 5) {
            Toast.makeText(this, "Scan QR Code", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == 6) {
            Toast.makeText(this, "Money", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == 7) {
            Toast.makeText(this, "Help & Feedback", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView() {
        Log.d(TAG, "------ initView() ------>");
        initTitleBar();
        initChatListView();
        Log.d(TAG, "<------ initView() ------");
    }


    private void initTitleBar() {
        Log.d(TAG, "------ initTitleBar() ------>");
        setTitle("WeChat", TITLE_MODE_LEFT);
        mToolBar.setPadding(DensityUtil.dip2px(this, 14), 0, 0, 0);
        Log.d(TAG, "<------ initTitleBar() ------");
    }

    private void initChatListView() {
        Log.d(TAG, "------ initChatListView() ------>");
        mChatListView = (RecyclerView) findViewById(R.id.chatting_rv);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mChatListView.setHasFixedSize(true);
        mChatListView.setItemAnimator(new DefaultItemAnimator());
        Log.d(TAG, "<------ initChatListView() ------");
    }

    private void initFirst() {
        Log.d(TAG, "------ initFirst() ------>");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // 默认不弹出软键盘

        mChatAdapter = new AdapterChatMsgView(this, mChatListView, mChatDataArrays);
        mChatAdapter.enableFooter(true);
        mChatListView.setAdapter(mChatAdapter);
        mChatAdapter.setOnItemClickListener(new AdapterChatMsgView.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "------ onItemClick() ------>");

                Log.d(TAG, "<------ onItemClick() ------");
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.d(TAG, "------ onItemLongClick() ------>");
                Log.d(TAG, "<------ onItemLongClick() ------");

            }
        });


        /**
         * 这里标准做法:通过handler回调处理界面,否则unReadMsgNum不一定会同步
         */
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.scrollToPosition(mChatAdapter.getCount() - 1);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mChatListView.setLayoutManager(mLayoutManager);
        mEndlessRecyclerOnScrollListener2 = new EndlessRecyclerOnScrollListener2(mLayoutManager) {

            @Override
            public void onLoadMore() {
                Log.d(TAG, "------ onLoadMore() ------>");
                //add progress item
                addLoadMoreProgressBar();
                mHandler.sendEmptyMessageDelayed(MSG_STOP_LOADING, 500);
                Log.d(TAG, "<------ onLoadMore() ------");
            }

            @Override
            public void onLastOffset(int lastOffset) {
                mLastOffset = lastOffset;
            }
        };
        mChatListView.addOnScrollListener(mEndlessRecyclerOnScrollListener2);

        createMockData();
        Log.d(TAG, "<------ initFirst() ------");
    }

    private void addLoadMoreProgressBar() {
        Log.d(TAG, "------ addLoadMoreProgressBar() ------>");
        if (null != mChatDataArrays && mChatDataArrays.size() >= 10) {
            isLoadMore = true;
            mChatDataArrays.add(0, null);
            Log.d(TAG, "------ addLoadMoreProgressBar()  ,notifyItemInserted() ------");
            mChatAdapter.notifyItemInserted(0);
        }
        Log.d(TAG, "<------ addLoadMoreProgressBar() ------");
    }

    /**
     * remove the end progress bar when finish loadmore.
     */
    private void removeLoadMoreProgressBar() {
        Log.d(TAG, "------ removeLoadMoreProgressBar() ------>");
        if (isLoadMore) {
            mChatDataArrays.remove(0);
            Log.d(TAG, "------ removeLoadMoreProgressBar()  ,notifyItemRemoved() ------");
            mChatAdapter.notifyItemRemoved(0);
            isLoadMore = false;
        }
        ArrayList<Object> addDataList = null;

        Log.d(TAG, "------ removeLoadMoreProgressBar()  ,notifyDataSetChanged() ------");
        mChatAdapter.notifyDataSetChanged();
        Log.d(TAG, "------ removeLoadMoreProgressBar()  ,scrollToPositionWithOffset() ------");
//        mLayoutManager.scrollToPositionWithOffset(mChatAdapter.getCount() - mRefreshMsgNum - mSystemMsgNum, mLastOffset);
        Log.d(TAG, "<------ removeLoadMoreProgressBar() ------");
    }

    public void initData() {
        Log.d(TAG, "------ initData() ------>");

        if (null != mChatDataArrays) {
            mChatDataArrays.clear();

            if (null != mChatDataArrays) {
                if (mChatAdapter != null) {
                    Log.d(TAG, "------ initData()  ,notifyDataSetChanged() ------");
                    mChatAdapter.notifyDataSetChanged(mChatDataArrays);
                }
            }
        }
        Log.d(TAG, "<------ initData() ------");
    }

    private void scrollToEnd() {
        Log.d(TAG, "------ scrollToEnd() ------>");
        if (null != mChatAdapter && null != mChatListView) {
            int count = mChatAdapter.getCount();
            if (count > 1) {
                Log.d(TAG, "------ scrollToEnd()  ,smoothScrollToPosition() ------");
                mChatListView.smoothScrollToPosition(mChatAdapter.getCount() - 1);
            }
            Log.d(TAG, "------ scrollToEnd() ,notifyDataSetChanged() ------");
            mChatAdapter.notifyDataSetChanged();
        }
        Log.d(TAG, "<------ scrollToEnd() ------");
    }

    private void createMockData(){
        for (int i = 0; i < 15 ; i++) {
            addItemToChatList(createLeftTextMsg("Other","Me","Hi, R u ok"+i));
            addItemToChatList(createRightTextMsg("Me","Other","Who r u? "+i));
        }
    }

    private ChatMessage createRightTextMsg(String from, String to, String msg){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.from = from;
        chatMessage.to = to;
        chatMessage.content = msg;
        chatMessage.type = ProtocolType.TEXT;
        chatMessage.msg_direction = EnumMsgDirection.SEND;
        return chatMessage;
    }

    private ChatMessage createLeftTextMsg(String from, String to, String msg){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.from = from;
        chatMessage.to = to;
        chatMessage.content = msg;
        chatMessage.type = ProtocolType.TEXT;
        chatMessage.msg_direction = EnumMsgDirection.RECEIVER;
        return chatMessage;
    }

    private void addItemToChatList(ChatMessage item){
        mChatAdapter.addEntity(item);
        mChatAdapter.notifyDataSetChanged();
    }

    private class ToolAd implements ChatToolBox.ChatToolItem {
        @Override
        public int getIcon() {
            return R.drawable.app_panel_ads_selector;
        }

        @Override
        public String getName() {
            return "信息";
        }

        @Override
        public void onItemSelected() {
            if (BuildConfig.DEBUG){
               Log.d(TAG, "ToolAd.onItemSelected() ------> ");
            }
        }
    }

}
