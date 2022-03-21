package com.jasonjohn.eattimer.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MealRecordDao {
    @Query("SELECT * FROM meals")
    List<MealRecordEntity> getAll();

    @Insert
    void insertAll(MealRecordEntity... users);

    @Delete
    void delete(MealRecordEntity user);
}
