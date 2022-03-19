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
    private BroadcastReceiver mBroadcastReceiver;
    boolean serviceRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mCircularProgressbar = findViewById(R.id.progressbar);
        mTimeText = findViewById(R.id.time);

        mTimeText.setOnClickListener(v -> {
            if (serviceRunning) {
                stopService(new Intent(getApplicationContext(), TimerService.class));
            } else {
                Intent intent = new Intent(getApplicationContext(), TimerService.class);
                intent.putExtra("time", mTimerValue);
                startService(intent);
            }
            serviceRunning = !serviceRunning;
            toggleControls(serviceRunning);
        });

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mTimeText.setText(""+intent.getFloatExtra("value", 0));
                mCircularProgressbar.setProgressValue(intent.getFloatExtra("value", 0));
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

    private void toggleControls(boolean serviceRunning) {
        findViewById(R.id.configmenu).setVisibility(serviceRunning ? View.GONE : View.VISIBLE);
    }
}