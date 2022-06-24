package com.example.investcalculator

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import com.example.investcalculator.api.RestClient
import com.example.investcalculator.api.dto.Coin
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNavigationView = findViewById(R.id.bottom_navigation)
        navController = findNavController(R.id.fragmentContainer)
        bottomNavigationView.setupWithNavController(navController)


//        val filterCoin = "Bitcoin"
//        RestClient.getReqResApi2.getCoinBySymbol(filterCoin)
//            .enqueue(object : retrofit2.Callback<String> {
//                override fun onResponse(call: Call<String>, response: Response<String>) {
//                    if (response.isSuccessful) {
//                        val text = response.body().toString().substring(
//                            response.body().toString().indexOf(filterCoin.uppercase() + "\":{") + 5,
//                            response.body().toString().length - 2
//                        )
//                        val coin = Gson().fromJson(text, Coin::class.java)
//                        Log.d("aCoin", coin.name.toString())
//                        Log.d("aCoin", coin.symbol.toString())
//                        Log.d("aCoin", coin.quote?.usd?.price.toString())
//                    }
//                }
//
//                override fun onFailure(call: Call<String>, t: Throwable) {
//                    Log.d("aCoin", "error")
//                }
//            })
    }
}