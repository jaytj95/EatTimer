package com.jasonjohn.eattimer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.jasonjohn.eattimer.db.AppDatabase;
import com.jasonjohn.eattimer.db.MealRecordEntity;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private AppDatabase mAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mRecyclerView = findViewById(R.id.recycler);
        mAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "eattimer-meals")
                .allowMainThreadQueries()
                .build();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MealStatsAdapter((ArrayList<MealRecordEntity>) mAppDatabase.mealRecordDao().getAll()));
    }
}