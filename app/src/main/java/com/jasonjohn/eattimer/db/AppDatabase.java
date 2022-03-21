package com.jasonjohn.eattimer.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MealRecordEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MealRecordDao mealRecordDao();
}
