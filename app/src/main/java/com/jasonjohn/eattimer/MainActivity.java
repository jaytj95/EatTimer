package com.jasonjohn.eattimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.brkckr.circularprogressbar.CircularProgressBar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EAT.MAIN";
    private CircularProgressBar mCircularProgressbar;
    private TextView mTimeText;
    private SeekBar mSeekbar;
    private TextView mSeekbarValueText;

    private int mTimerValue = 90;
    private int mCounter = 0;
    private BroadcastReceiver mBroadcastReceiver;
    boolean mServiceRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mCircularProgressbar = findViewById(R.id.progressbar);
        mTimeText = findViewById(R.id.time);

        mTimeText.setOnClickListener(v -> {
            if (mServiceRunning) {
                stopTimer();
            } else {
                startTimer();
            }
        });

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                float progressValue = intent.getFloatExtra("value", 0);

                mCircularProgressbar.setProgressValue(intent.getFloatExtra("value", 0));
                mTimeText.setText(progressValue + "");

                if (progressValue == -1F) {
                    Log.d(TAG, "Timer done");

                    if (((CheckBox)findViewById(R.id.repeatcheckbox)).isChecked()) {
                        startTimer();
                    } else {
                        stopTimer();
                    }
                    mCounter++;
                    ((TextView)findViewById(R.id.counter)).setText("Counter: " + mCounter);
                }
            }
        };
        registerReceiver(mBroadcastReceiver, new IntentFilter("bg_timer_broadcast"));

        mSeekbar = findViewById(R.id.seekbar);
        mSeekbarValueText = findViewById(R.id.seekbarvalue);
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "Seekbar changed == " + progress);
                mTimerValue = progress;
                long minutes = (progress / 1000) / 60;
                long seconds = (progress / 1000) % 60;
                mSeekbarValueText.setText(String.format("%d:%02d",minutes, seconds));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // no-op
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // no-op
            }
        });
    }

    private void startTimer() {
        mServiceRunning = true;
        Intent intent = new Intent(getApplicationContext(), TimerService.class);
        intent.putExtra("time", mTimerValue);
        startService(intent);
        toggleControls();
    }
    private void stopTimer() {
        mServiceRunning = false;
        Intent intent = new Intent(getApplicationContext(), TimerService.class);
        stopService(intent);
        toggleControls();
    }

    private void toggleControls() {
        findViewById(R.id.configmenu).setVisibility(mServiceRunning ? View.GONE : View.VISIBLE);
    }
}