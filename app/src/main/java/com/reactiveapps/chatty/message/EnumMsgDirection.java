package com.reactiveapps.chatty.message;

/**
 * Created by .
 */
public enum EnumMsgDirection {
    SEND(0),
    RECEIVER(1);

    private int value = 0;

    EnumMsgDirection(int value) {
        this.value = value;
    }

    public static EnumMsgDirection valueOf(int value) {
        switch (value) {
            case 0:
                return SEND;
            case 1:
                return RECEIVER;
            default:
                return SEND;
        }
    }

    public int value() {
        return this.value;
    }
}
