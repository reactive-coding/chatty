package com.reactiveapps.chatty.message;

import java.io.Serializable;

/**
 * Project name: chatty
 * Class description:
 * Auther: iamcxl369
 * Date: 16-9-19 下午3:56
 * Modify by: iamcxl369
 * Modify date: 16-9-19 下午3:56
 * Modify detail:
 */

public class MessageBase implements Serializable{
    public String id; // local create message unique id.
    public String from;
    public String to;
    public String mid;
}
