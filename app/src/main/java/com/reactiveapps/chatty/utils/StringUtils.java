package com.reactiveapps.chatty.utils;

import android.text.TextUtils;
import com.reactiveapps.chatty.App;
import com.reactiveapps.chatty.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {
    private final static String regEx = "[\\u4e00-\\u9fa5]";

    private static boolean isChinese(String str) {
        Pattern p = Pattern.compile(regEx);
        return p.matches(regEx, str);
    }

    public static int getLength(CharSequence lastContent) {
        int number = 0;
        for (int i = 0; i < lastContent.length(); i++) {
            if (isChinese(lastContent.charAt(i) + "")) {
                number += 2;
            } else {
                number += 1;
            }
        }
        return number;
    }

    /**
     *     <string name="html_url">http:([^>\\\"])+</string>
     * @param html
     * @return
     */
    public static String getUrlFromHtml(String html) {
        Pattern p = Pattern.compile(App.getInst().getApplicationContext().getResources().getString(R.string.html_url));
        if (TextUtils.isEmpty(html)) {
            return null;
        }
        Matcher m = p.matcher(html);
        if (m.find()) {
            return m.group();
        }
        return null;
    }
}
