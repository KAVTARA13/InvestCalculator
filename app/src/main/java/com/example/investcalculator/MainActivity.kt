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


//        RestClient.getReqResApi.getCoins(1)
//            .enqueue(object : retrofit2.Callback<ReqResData<Coin>> {
//                override fun onResponse(
//                    call: Call<ReqResData<Coin>>,
//                    response: Response<ReqResData<Coin>>
//                ) {
//                    if (response.isSuccessful) {
//                        response.body()?.data?.let {
//                            it.forEach {
//                                Log.d(it.name, it.quote?.usd?.price.toString());
//                            }
//                        }
//                    }
//                }
//                override fun onFailure(call: Call<ReqResData<Coin>>, t: Throwable) {
//                    t.getLocalizedMessage()?.let { Log.d("XXXX", it) };
//                }
//            })
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