package com.jasonjohn.eattimer.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meals")
public class MealRecordEntity {
    @PrimaryKey
    public long time;

    @ColumnInfo(name = "counter")
    public int counter;
}