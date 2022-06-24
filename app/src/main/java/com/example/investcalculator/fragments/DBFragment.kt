package com.example.investcalculator.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.investcalculator.App
import com.example.investcalculator.R
import com.example.investcalculator.api.RestClient
import com.example.investcalculator.api.dto.Coin
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import java.text.DecimalFormat

class DBFragment : Fragment(R.layout.fragment_db) {

    lateinit var handler: Handler
    var delay: Long = 10000
    private var dbData = mutableListOf<List<String>>()

    private val dbUpdate = object : Runnable {
        override fun run() {
            dbCall()
            handler.postDelayed(this, delay)
        }
    }

    private fun displayDB() {
        try {
            val dbTable = view?.findViewById<LinearLayout>(R.id.dbDisplayContainer)
            dbTable?.removeAllViews()
            for (i in 1..dbData.size) {
                val textName = TextView(ContextThemeWrapper(context, R.style.CryptoName))
                textName.text = dbData[i - 1][0]
                textName.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    2f
                )
                textName.layoutParams.width = 0

                val amount = TextView(ContextThemeWrapper(context, R.style.CryptoLastPrice))
                amount.text = dbData[i - 1][1]
                amount.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    2f
                )
                amount.layoutParams.width = 0

                val textBoughtPrice =
                    TextView(ContextThemeWrapper(context, R.style.CryptoLastPrice))
                textBoughtPrice.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    2f
                )
                textBoughtPrice.layoutParams.width = 0
                textBoughtPrice.text = "$" + dbData[i - 1][2]

                val textCurrentPrice =
                    TextView(ContextThemeWrapper(context, R.style.CryptoLastPrice))
                textCurrentPrice.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    2f
                )
                textCurrentPrice.layoutParams.width = 0
                textCurrentPrice.text = "$" + dbData[i - 1][3]

                val profit = TextView(ContextThemeWrapper(context, R.style.Crypto24hChange))
                profit.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    2f
                )
                profit.layoutParams.width = 0
                if (dbData[i - 1][4].contains("-")) {
                    profit.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                    profit.text = dbData[i - 1][4] + "%"
                } else {
                    profit.setTextColor(ContextCompat.getColor(context!!, R.color.green))
                    profit.text = "+" + dbData[i - 1][4] + "%"
                }
                val rowContainer =
                    LinearLayout(ContextThemeWrapper(context, R.style.RowContainer))
                rowContainer.addView(textName)
                rowContainer.addView(amount)
                rowContainer.addView(textBoughtPrice)
                rowContainer.addView(textCurrentPrice)
                rowContainer.addView(profit)
                dbTable?.addView(rowContainer)
            }
        } catch (e: Exception) {
            Log.e("Error", e.toString())
        }
    }

    fun dbCall() {
        dbData.clear()
        App.instance.db.getStepDao().getInvest()?.let { it ->
            it.forEach {
                val coinData = getCoinByID(it.coinId!!)
                val currentPriceX = coinData[1].toFloat()
                val boughtPrice = if (it.boughtPrice?.toFloat()!! > 1.0f) {
                    String.format("%.0f", it.boughtPrice.toFloat())
                } else if (it.boughtPrice.toFloat() > 0.1f) {
                    String.format("%.2f", it.boughtPrice.toFloat())
                } else {
                    String.format("%.4f", it.boughtPrice.toFloat())
                }
                val currentPrice = if (currentPriceX > 1.0f) {
                    String.format("%.0f", currentPriceX)
                } else if (currentPriceX > 0.1f) {
                    String.format("%.2f", currentPriceX)
                } else {
                    String.format("%.4f", currentPriceX)
                }

                val row = listOf(
                    coinData[0],
                    DecimalFormat("0.#").format(it.amount),
                    DecimalFormat("0.#").format(boughtPrice.toFloat()),
                    DecimalFormat("0.#").format(currentPrice.toFloat()),
                    DecimalFormat("0.#").format(String.format(
                        "%.2f",
                        (currentPrice.toFloat() - boughtPrice.toFloat()) * 100 / boughtPrice.toFloat()
                    ).toFloat())
                )
                dbData.add(row)
            }
        }
        displayDB()
    }

    private fun getCoinByID(id: Int): List<String> {
        var coinData = listOf("name", "0")
        RestClient.getReqResApi2.getCoinById(id)
            .enqueue(object : retrofit2.Callback<String> {
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    if (response.isSuccessful) {
                        val text = response.body().toString().substring(
                            response.body().toString().indexOf(
                                "\"data\":{"
                            ) + id.toString().length + 11, response.body().toString().length - 2
                        )
                        val coin = Gson().fromJson(text, Coin::class.java)
                        coinData = mutableListOf(
                            coin.name.toString(),
                            String.format("%.2f", coin.quote?.usd?.price?.toFloat())
                        )
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("Error", t.toString())
                }
            })
        return coinData
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_db, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler = Handler(Looper.getMainLooper())


    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(dbUpdate)
    }

    override fun onResume() {
        super.onResume()
        handler.post(dbUpdate)
    }
}