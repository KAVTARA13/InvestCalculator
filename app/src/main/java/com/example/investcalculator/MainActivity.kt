package com.example.investcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.investcalculator.api.RestClient
import com.google.gson.Gson
import com.example.investcalculator.api.dto.Coin
import com.example.investcalculator.api.dto.ReqResData
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 60000
    var apiLimit = 2
    var apiData = mutableListOf<List<String>>()

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNavigationView = findViewById(R.id.bottom_navigation)
        navController = findNavController(R.id.fragmentContainer)
        bottomNavigationView.setupWithNavController(navController)

        fun displayApi(){
            for (i in 1..apiLimit) {
                for (j in 1..3){
                    Log.d("apiData", apiData[i - 1][j - 1])
                }
            }
        }

        RestClient.getReqResApi.getCoins(apiLimit)
            .enqueue(object : retrofit2.Callback<ReqResData<Coin>> {
                override fun onResponse(
                    call: Call<ReqResData<Coin>>,
                    response: Response<ReqResData<Coin>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let { it ->
                            it.forEach {
                                val row = listOf(
                                    it.name.toString(),
                                    it.symbol.toString(),
                                    String.format("%.3f", it.quote?.usd?.price?.toFloat()).toDouble().toString())
                                apiData.add(row)
                            }

                        }
                        displayApi()
                    }
                }
                override fun onFailure(call: Call<ReqResData<Coin>>, t: Throwable) {
                    t.localizedMessage?.let { Log.d("error", it) }
                }
            })



        /*
        val filterCoin = "SOL"
        RestClient.getReqResApi2.getCoinBySlug( filterCoin)
            .enqueue(object : retrofit2.Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val text = response.body().toString().substring(response.body().toString().indexOf(filterCoin+"\":{")+5,response.body().toString().length-2 )
                        val coin = Gson().fromJson(text , Coin::class.java)
                        Log.d("Coin", coin.name.toString())
                        Log.d("Coin", coin.symbol.toString())
                        Log.d("Coin", coin.quote?.usd?.price.toString())
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
         */
    }
//    override fun onResume() {
//        handler.postDelayed(Runnable {
//            handler.postDelayed(runnable!!, delay.toLong())
//            RestClient.getReqResApi.getCoins(10)
//                .enqueue(object : retrofit2.Callback<ReqResData<Coin>> {
//                    override fun onResponse(
//                        call: Call<ReqResData<Coin>>,
//                        response: Response<ReqResData<Coin>>
//                    ) {
//                        if (response.isSuccessful) {
//                            response.body()?.data?.let {
//                                it.forEach {
//                                    Log.d(it.name, it.quote?.usd?.price.toString());
//                                }
//                            }
//                        }
//                    }
//                    override fun onFailure(call: Call<ReqResData<Coin>>, t: Throwable) {
//                        t.getLocalizedMessage()?.let { Log.d("XXXX", it) };
//                    }
//                })
//            Toast.makeText(this@MainActivity, "This method will run every 60 seconds", Toast.LENGTH_SHORT).show()
//        }.also { runnable = it }, delay.toLong())
//        super.onResume()
//    }
//    override fun onPause() {
//        super.onPause()
//        handler.removeCallbacks(runnable!!)
//    }
}