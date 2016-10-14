package com.reactiveapps.chatty.message;

/**
 * 聊天消息的协议格式
 * 
 */
public class ProtocolType {

    /**消息类型**/
    public static final String TEXT = "text";//文本消息
    public static final String IMAGE = "image";//图片消息
    public static final String FILE = "file";//文件消息
    public static final String VOICE = "voice";//语音消息
    public static final String VIDEO = "video";//视频消息
    public static final String LOCATION = "location";//位置消息
    public static final String LINK = "link ";//链接消息
    public static final String COMMON = "common";//系统消息（自定义，非服务器下发类型）

}
