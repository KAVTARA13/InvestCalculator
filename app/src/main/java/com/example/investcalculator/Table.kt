package com.example.investcalculator

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "INVEST")
data class Table (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Int,

    @ColumnInfo(name = "COIN_ID")
    val coinId: Int?,

    @ColumnInfo(name = "BOUGHT_PRICE")
    val boughtPrice: Double?,

    @ColumnInfo(name = "AMOUNT")
    val amount: Double?,
)