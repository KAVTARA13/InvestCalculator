package com.example.investcalculator

import android.app.Application
import com.example.investcalculator.api.RestClient

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        RestClient.initClients()
    }
}