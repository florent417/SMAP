package com.flow.lab42.backgroundservices;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class BoundService extends Service {
    private static final String TAG = "BoundService";

    private final IBinder binder = new LocalBinder();
    // Why this?
    public class LocalBinder extends Binder{
        BoundService getService() {
            return BoundService.this;
        }
    }

    private int  SPECIAL_NBR = 42;
    private final int  SPECIAL_NBR_1 = 1;
    private final String  SPECIAL_STRING = "HELLO";

    @Override
    public void onCreate() {
        super.onCreate();
        SPECIAL_NBR = 42;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // returns custom binder
        return binder;
    }

    public int getNumberSpeacial() {
        return SPECIAL_NBR;
    }

    public int getSPECIAL_NBR_1() {
        return SPECIAL_NBR_1;
    }

    public String getSPECIAL_STRING() {
        return SPECIAL_STRING;
    }

}
