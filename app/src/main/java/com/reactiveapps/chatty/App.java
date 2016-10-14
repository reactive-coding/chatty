package com.reactiveapps.chatty;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Project name: chatty
 * Class description:
 * Auther: iamcxl369
 * Date: 16-9-12 下午12:51
 * Modify by: iamcxl369
 * Modify date: 16-9-12 下午12:51
 * Modify detail:
 */

public class App extends Application {

    private static App inst;
    public static App getInst(){
        if (inst == null){
            inst = new App();
        }
        return inst;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        inst = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
