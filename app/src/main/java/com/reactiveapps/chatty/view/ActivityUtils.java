package com.reactiveapps.chatty.view;

import android.app.Activity;
import android.content.Intent;

import com.reactiveapps.chatty.view.activity.ActivityChatting;
import com.reactiveapps.chatty.view.activity.ActivityGuide;
import com.reactiveapps.chatty.view.activity.ActivityLogin;
import com.reactiveapps.chatty.view.activity.ActivityMain;
import com.reactiveapps.chatty.view.activity.ActivitySplash;

/**
 * Project name: chatty
 * Class description:
 * Auther: iamcxl369
 * Date: 16-9-13 上午10:16
 * Modify by: iamcxl369
 * Modify date: 16-9-13 上午10:16
 * Modify detail:
 */

public class ActivityUtils {

    public static void showActivitySplash(Activity activity, Intent intent){

        if (null == activity){
            return;
        }
        if (null == intent){
            intent = new Intent(activity, ActivitySplash.class);
        }
        activity.startActivity(intent);
    }

    public static void showActivityGuide(Activity activity, Intent intent){

        if (null == activity){
            return;
        }
        if (null == intent){
            intent = new Intent(activity, ActivityGuide.class);
        }
        activity.startActivity(intent);
    }

    public static void showActivityLogin(Activity activity, Intent intent){

        if (null == activity){
            return;
        }
        if (null == intent){
            intent = new Intent(activity, ActivityLogin.class);
        }
        activity.startActivity(intent);
    }

    public static void showActivityMain(Activity activity, Intent intent){

        if (null == activity){
            return;
        }
        if (null == intent){
            intent = new Intent(activity, ActivityMain.class);
        }
        activity.startActivity(intent);
    }

    public static void showActivityChatting(Activity activity, Intent intent){

        if (null == activity){
            return;
        }
        if (null == intent){
            intent = new Intent(activity, ActivityChatting.class);
        }
        activity.startActivity(intent);
    }


//    public static void showActivityForgetPwd(Activity activity, Intent intent){
//
//        if (null == activity){
//            return;
//        }
//        if (null == intent){
//            intent = new Intent(activity, ActivityForgetPwd.class);
//        }
//        activity.startActivity(intent);
//    }
//
//    public static void showActivityAbout(Activity activity, Intent intent){
//
//        if (null == activity){
//            return;
//        }
//        if (null == intent){
//            intent = new Intent(activity, ActivityAbout.class);
//        }
//        activity.startActivity(intent);
//    }

}
