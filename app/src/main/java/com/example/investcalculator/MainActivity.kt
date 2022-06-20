package com.example.investcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.investcalculator.api.RestClient
import com.example.investcalculator.api.dto.Coin
import com.example.investcalculator.api.dto.ReqResData
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 60000

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNavigationView = findViewById(R.id.bottom_navigation)
        navController = findNavController(R.id.fragmentContainer)
        /*
        setupActionBarWithNavController(navController, AppBarConfiguration(
            setOf(
                R.id.apiFragment,
                R.id.buyFragment,
                R.id.dbFragment,
                R.id.settingsFragment
            )
        ))
        */
        bottomNavigationView.setupWithNavController(navController)


        RestClient.getReqResApi.getCoins(10)
            .enqueue(object : retrofit2.Callback<ReqResData<Coin>> {
                override fun onResponse(
                    call: Call<ReqResData<Coin>>,
                    response: Response<ReqResData<Coin>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let {
                            it.forEach {
                                Log.d(it.name, it.quote?.usd?.price.toString());
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<ReqResData<Coin>>, t: Throwable) {
                    t.getLocalizedMessage()?.let { Log.d("XXXX", it) };
                }
            })
    }
    override fun onResume() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            RestClient.getReqResApi.getCoins(10)
                .enqueue(object : retrofit2.Callback<ReqResData<Coin>> {
                    override fun onResponse(
                        call: Call<ReqResData<Coin>>,
                        response: Response<ReqResData<Coin>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.data?.let {
                                it.forEach {
                                    Log.d(it.name, it.quote?.usd?.price.toString());
                                }
                            }
                        }
                    }
                    override fun onFailure(call: Call<ReqResData<Coin>>, t: Throwable) {
                        t.getLocalizedMessage()?.let { Log.d("XXXX", it) };
                    }
                })
            Toast.makeText(this@MainActivity, "This method will run every 60 seconds", Toast.LENGTH_SHORT).show()
        }.also { runnable = it }, delay.toLong())
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable!!)
    }
}