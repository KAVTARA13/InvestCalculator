package com.example.investcalculator

import android.app.Application
import androidx.room.Room
import com.example.investcalculator.api.RestClient

class App: Application() {
    lateinit var db: AppDatabase
    companion object{

        lateinit var instance:App
            private set

    }
    override fun onCreate() {
        super.onCreate()
        RestClient.initClients()
        instance = this

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "APP_DATABASE"
        ).allowMainThreadQueries().build()
    }
}