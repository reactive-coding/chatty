package com.reactiveapps.chatty.model.bean;


import com.reactiveapps.chatty.message.EnumMsgDirection;
import com.reactiveapps.chatty.message.EnumMsgDownloadStatus;
import com.reactiveapps.chatty.message.EnumMsgReadStatus;
import com.reactiveapps.chatty.message.EnumMsgSendStatus;
import com.reactiveapps.chatty.message.MessageBase;

import java.io.Serializable;

public class ChatMessage extends MessageBase implements Serializable {

    /**
     * local
     */
    /**
     * msg_direction: 1 receive, 0 send
     */
    public EnumMsgDirection msg_direction = EnumMsgDirection.SEND;

    /**
     * IS_SENDED = 5;
     * IS_SENDING = 4;
     * SEND_SUCCESS = 1;
     * SEND_FAILED = 2;
     * IS_DRAFT = 3;
     * DEFUALT_SEND_STATE = 0;
     */
    public EnumMsgSendStatus messageSendStatus = EnumMsgSendStatus.MSG_DEFAULT;

    /**
     * 1:read, 0 unread
     */
    public EnumMsgReadStatus messageReadStatus = EnumMsgReadStatus.UNREAD;

    /**
     *
     */
    public EnumMsgDownloadStatus messageDownloadStatus = EnumMsgDownloadStatus.DEFAULT;

    /**
     * Message type
     */
    public String type;

    public String datetime;

    public String content;

    public String picture_url;

    public boolean voice_playing;

    public String voice_url;

    public String voice_local_path;

    public int voice_duration; // seconds



    /**
     * Link msg
     */
    public String link_url;

    public String link_image_url;

    public String link_title;

    public String link_description;

    /**
     * File msg
     */
    public String file_name;

    public String link_type;

    public Long file_size;

    public String file_description;

    public String file_url;

    public String file_local_path;

}
