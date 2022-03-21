package com.jasonjohn.eattimer;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.brkckr.circularprogressbar.CircularProgressBar;
import com.jasonjohn.eattimer.db.AppDatabase;
import com.jasonjohn.eattimer.db.MealRecordDao;
import com.jasonjohn.eattimer.db.MealRecordEntity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EAT.MAIN";
    private CircularProgressBar mCircularProgressbar;
    private TextView mTimeText;
    private SeekBar mSeekbar;
    private TextView mSeekbarValueText;

    private BroadcastReceiver mBroadcastReceiver;
    private AppDatabase mAppDatabase;

    private int mTimerValue = 90;
    private int mCounter = 0;
    private boolean mServiceRunning = false;
    private long mStartTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "eattimer-meals")
                .allowMainThreadQueries()
                .build();

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
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        findViewById(R.id.finish).setOnClickListener(v -> {
            Log.d(TAG, "Counter is " + mCounter);
            Log.d(TAG, "Start time is " + mStartTime);

            MealRecordDao dao = mAppDatabase.mealRecordDao();
            MealRecordEntity entity = new MealRecordEntity();
            entity.counter = mCounter;
            entity.time = mStartTime;

            dao.insertAll(entity);

            startActivity(new Intent(this, ResultsActivity.class));
        });
    }

    private void startTimer() {
        mServiceRunning = true;
        mStartTime = System.currentTimeMillis();
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