package com.reactiveapps.chatty.message;

/**
 * Created by on 2016/4/5.
 */
public enum EnumMsgSendStatus {
    MSG_DEFAULT(0),
    MSG_SUCCESS(1),
    MSG_FAILED(2),
    MSG__DRAFT(3),
    MSG_SENDING(4),
    MSG_SENT(5);

    private int value = 0;

    EnumMsgSendStatus(int value) {    //    必须是private的，否则编译错误
        this.value = value;
    }

    public static EnumMsgSendStatus valueOf(int value) {
        switch (value) {
            case 0:
                return MSG_DEFAULT;
            case 1:
                return MSG_SUCCESS;
            case 2:
                return MSG_FAILED;
            case 3:
                return MSG__DRAFT;
            case 4:
                return MSG_SENDING;
            case 5:
                return MSG_SENT;
            default:
                return MSG_DEFAULT;
        }
    }

    public int value() {
        return this.value;
    }
}
