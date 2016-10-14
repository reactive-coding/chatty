package com.reactiveapps.chatty.background.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ServiceRoot extends Service {
    public ServiceRoot() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
