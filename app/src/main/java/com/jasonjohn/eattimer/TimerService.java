package com.jasonjohn.eattimer;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

public class TimerService extends Service {
    private static final String TAG = "EAT.SERVICE";
    private CountDownTimer countDownTimer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Timer Service created");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Timer Service started");
        long totalTime = intent.getIntExtra("time", 90000);
        countDownTimer = new CountDownTimer(totalTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "Timer Service ticked - " + millisUntilFinished);
                Intent intent = new Intent("bg_timer_broadcast");
                intent.putExtra("value", ((float) millisUntilFinished/totalTime)*100);
                sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "Timer Service ticking finished");
                Intent intent = new Intent("bg_timer_broadcast");
                intent.putExtra("value", -1F);
                sendBroadcast(intent);
            }
        };
        countDownTimer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
}
