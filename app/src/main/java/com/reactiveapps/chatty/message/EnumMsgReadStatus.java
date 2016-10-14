package com.reactiveapps.chatty.message;

/**
 * Created by on 2016/4/5.
 */
public enum EnumMsgReadStatus {
    UNREAD (0),
    READ(1);

    private int value = 0;

    EnumMsgReadStatus(int value) {    //    必须是private的，否则编译错误
        this.value = value;
    }

    public static EnumMsgReadStatus valueOf(int value) {
        switch (value) {
            case 0:
                return UNREAD;
            case 1:
                return READ;
            default:
                return UNREAD;
        }
    }

    public int value() {
        return this.value;
    }
}
