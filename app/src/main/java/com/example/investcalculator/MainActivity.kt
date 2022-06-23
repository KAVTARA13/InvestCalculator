package com.example.investcalculator

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.investcalculator.api.RestClient
import com.example.investcalculator.api.dto.Coin
import com.example.investcalculator.api.dto.ReqResData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var handler: Handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private var delay = 10000
    private var apiLimit = 10
    var apiData = mutableListOf<List<String>>()

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNavigationView = findViewById(R.id.bottom_navigation)
        navController = findNavController(R.id.fragmentContainer)
        bottomNavigationView.setupWithNavController(navController)


        //App.instance.db.getStepDao().delete()

//
//        RestClient.getReqResApi.getCoins(apiLimit)
//            .enqueue(object : retrofit2.Callback<ReqResData<Coin>> {
//                override fun onResponse(
//                    call: Call<ReqResData<Coin>>,
//                    response: Response<ReqResData<Coin>>
//                ) {
//                    if (response.isSuccessful) {
//                        response.body()?.data?.let { it ->
//                            it.forEach {
//                                Log.d(it.name, it.quote?.usd?.price.toString())
//                            }
//                        }
//                    }
//                }
//                override fun onFailure(call: Call<ReqResData<Coin>>, t: Throwable) {
//                    t.localizedMessage?.let { Log.d("error", it) }
//                }
//            })
//        val filterCoin = "Sol"
//        RestClient.getReqResApi2.getCoinBySymbol(filterCoin)
//            .enqueue(object : retrofit2.Callback<String> {
//                override fun onResponse(call: Call<String>, response: Response<String>) {
//                    if (response.isSuccessful) {
//                        val text = response.body().toString().substring(response.body().toString().indexOf(filterCoin.uppercase()+"\":{")+5,response.body().toString().length-2 )
//                        val coin = Gson().fromJson(text , Coin::class.java)
//                        Log.d("Coin", coin.name.toString())
//                        Log.d("Coin", coin.symbol.toString())
//                        Log.d("Coin", coin.quote?.usd?.price.toString())
//                    }
//                }
//                override fun onFailure(call: Call<String>, t: Throwable) {
//                    TODO("Not yet implemented")
//                }
//            })
    }
//    override fun onResume() {
//        handler.postDelayed(Runnable {
//            handler.postDelayed(runnable!!, delay.toLong())
//            RestClient.getReqResApi.getCoins(apiLimit)
//                .enqueue(object : retrofit2.Callback<ReqResData<Coin>> {
//                    override fun onResponse(
//                        call: Call<ReqResData<Coin>>,
//                        response: Response<ReqResData<Coin>>
//                    ) {
//                        if (response.isSuccessful) {
//                            apiData.clear()
//                            response.body()?.data?.let { it ->
//                                it.forEach {
//                                    val price = if(String.format("%.0f", it.quote?.usd?.price?.toFloat()).toFloat() > 1.0f){
//                                        String.format("%.0f", it.quote?.usd?.price?.toFloat())
//                                    }else{
//                                        String.format("%.3f", it.quote?.usd?.price?.toFloat())
//                                    }
//                                    val row = listOf(
//                                        it.name.toString(),
//                                        it.symbol.toString(),
//                                        price,
//                                        String.format("%.2f", it.quote?.usd?.percent_change_24h?.toFloat())
//                                    )
//                                    apiData.add(row)
//                                }
//                            }
//                            displayApi()
//                        }
//                    }
//                    override fun onFailure(call: Call<ReqResData<Coin>>, t: Throwable) {
//                        t.localizedMessage?.let { Log.d("error", it) }
//                    }
//                })
//            Toast.makeText(this@MainActivity, "This method will run every 10 seconds", Toast.LENGTH_SHORT).show()
//        }.also { runnable = it }, delay.toLong())
//        super.onResume()
//    }
//    override fun onPause() {
//        super.onPause()
//        handler.removeCallbacks(runnable!!)
//    }
}