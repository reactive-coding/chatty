package com.reactiveapps.chatty.utils;

import android.content.Intent;

import de.greenrobot.event.EventBus;

/**
 * Project name: chatty
 * Class description:
 * Auther: iamcxl369
 * Date: 15-12-30 下午7:06
 * Modify by: iamcxl369
 * Modify date: 15-12-30 下午7:06
 * Modify detail:
 *
 * onEvent:
 *      如果使用onEvent作为订阅函数，那么该事件在哪个线程发布出来的，onEvent就会在这个线程中运行，也就是说发布事件和接收事件线程在同一个线程。
 *      使用这个方法时，在onEvent方法中不能执行耗时操作，如果执行耗时操作容易导致事件分发延迟。
 * onEventMainThread:
 *      如果使用onEventMainThread作为订阅函数，那么不论事件是在哪个线程中发布出来的，onEventMainThread都会在UI线程中执行，接收事件就会在UI线程中运行，
 *      这个在Android中是非常有用的，因为在Android中只能在UI线程中跟新UI，所以在onEvnetMainThread方法中是不能执行耗时操作的。
 * onEventBackground:
 *      如果使用onEventBackgrond作为订阅函数，那么如果事件是在UI线程中发布出来的，那么onEventBackground就会在子线程中运行，
 *      如果事件本来就是子线程中发布出来的，那么onEventBackground函数直接在该子线程中执行。
 * onEventAsync：
 *      使用这个函数作为订阅函数，那么无论事件在哪个线程发布，都会创建新的子线程在执行onEventAsync.
 *
 * 用法:
 *      注册: EventBusUtils.register(activityObject);
 *      发布者: EventBusUtils.postEvent(new object);
 *      订阅者: 一般是Activity
 *             实现:public void onEvent(EntityEvent event) { }
 *                 public void onEventMainThread(EntityEvent event) { }
 *                 public void onEventBackground(EntityEvent event) { }
 *                 public void onEventAsync(EntityEvent event) { }
 */
public class EventBusUtils {

    public static void register(Object object){
        EventBus.getDefault().register(object);
    }

    public static void unRegister(Object object){
        EventBus.getDefault().unregister(object);
    }

    public static void registerStick(Object object){
        EventBus.getDefault().registerSticky(object);
    }

    public static void postEvent(Object object){
        EventBus.getDefault().post(object);
    }

    /**
     * 带TAG的通知,订阅者接收到这个订阅后，通过TAG检索这个事件是否是自己需要的，
     * 如果是自己需要的则处理，否则不处理.
     * @param intent
     */
    public static void postEvent(Intent intent){
        EventBus.getDefault().post(intent);
    }

    /**
     * Stick事件会把事件队列中最后一个Event Post出去.
     * Stick事件不需要解注册，但是有方法可以remove stickEvent.
     * eg. 从ActivityA将要跳转到ActivityB,在ActivityA中postStickEvent, ActivityB中订阅，那么ActivityB打开之后就会收到这个事件.
     *     ActivityB中的订阅方法要添加注解:@Subscriber
     * @param object
     */
    public static void postStickEvent(Object object){
        EventBus.getDefault().postSticky(object);
    }

    public static void postStickEvent(Intent intent){
        EventBus.getDefault().postSticky(intent);
    }

    public static void removeStickyEvent(Object Object){
        EventBus.getDefault().removeStickyEvent(Object);
    }

    public static void removeStickyEvent(Class classType){
        EventBus.getDefault().removeStickyEvent(classType);
    }

    public static void removeAllStickyEvents(){
        EventBus.getDefault().removeAllStickyEvents();
    }


}
