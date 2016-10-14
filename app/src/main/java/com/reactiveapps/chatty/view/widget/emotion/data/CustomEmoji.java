package com.reactiveapps.chatty.view.widget.emotion.data;

import android.net.Uri;

/**
 * Created by Administrator on 2015/11/16.
 */
public class CustomEmoji implements Emoticon {
    private String path;

    public CustomEmoji(String path) {
        this.path = path;
    }

    @Override
    public int getResourceId() {
        return 0;
    }

    @Override
    public String getImagePath() {
        return path;
    }

    @Override
    public Uri getUri() {
        return null;
    }

    @Override
    public String getDesc() {
        return path;
    }

    @Override
    public EmoticonType getEmoticonType() {
        return EmoticonType.NORMAL;
    }
}
