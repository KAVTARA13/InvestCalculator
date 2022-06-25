package com.example.investcalculator

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg table: Table)

    @Query("SELECT * FROM INVEST")
    fun getInvest():List<Table>?

    @Query("DELETE FROM INVEST;")
    fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(vararg notification: NotificationTable)

    @Query("SELECT * FROM NOTIFICATION")
    fun getNotification():List<NotificationTable>?
}