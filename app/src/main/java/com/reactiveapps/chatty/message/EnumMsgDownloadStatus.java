package com.reactiveapps.chatty.message;

/**
 * Created by  on 2016/4/5.
 */
public enum EnumMsgDownloadStatus {
    DEFAULT(0),
    DOWNLOADING(1),
    COMPLETE (2),
    ERROR(3);

    private int value = 0;

    EnumMsgDownloadStatus(int value) {    //    必须是private的，否则编译错误
        this.value = value;
    }

    public static EnumMsgDownloadStatus valueOf(int value) {
        switch (value) {
            case 0:
                return DEFAULT;
            case 1:
                return DOWNLOADING;
            case 2:
                return COMPLETE;
            case 3:
                return ERROR;
            default:
                return DEFAULT;
        }
    }

    public int value() {
        return this.value;
    }
}
