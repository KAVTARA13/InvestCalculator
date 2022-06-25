package com.example.investcalculator
import androidx.room.ColumnInfo;
import androidx.room.Entity
import androidx.room.PrimaryKey;

@Entity(tableName = "NOTIFICATION")
data class NotificationTable (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Int,

    @ColumnInfo(name = "COIN_ID")
    val coinId: Int?,

    @ColumnInfo(name = "EXPECTED_PRICE")
    val expectedPrice: Double?,

    @ColumnInfo(name = "ADDITION")
    val addition: Int?,
        )