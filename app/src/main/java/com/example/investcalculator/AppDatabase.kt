package com.example.investcalculator

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Table::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getStepDao():StepDao
}