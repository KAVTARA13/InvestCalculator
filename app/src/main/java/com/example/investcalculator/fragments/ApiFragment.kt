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
import com.example.investcalculator.R
import com.example.investcalculator.api.RestClient
import com.example.investcalculator.api.dto.Coin
import com.example.investcalculator.api.dto.ReqResData
import retrofit2.Call
import retrofit2.Response


class ApiFragment : Fragment(R.layout.fragment_api) {

    var delay: Long = 10000
    private var apiLimit = 32
    var apiData = mutableListOf<List<String>>()
    lateinit var handler: Handler

    private val apiUpdate = object : Runnable {
        override fun run() {
            apiCall()
            handler.postDelayed(this, delay)
        }
    }

    fun displayApi() {
        try {
            val apiTable = view?.findViewById<LinearLayout>(R.id.apiDisplayContainer)
            apiTable?.removeAllViews()
            for (i in 1..apiLimit) {
                val textName = TextView(ContextThemeWrapper(context, R.style.CryptoName))
                textName.text = apiData[i - 1][0]
                val textSymbol = TextView(ContextThemeWrapper(context, R.style.CryptoSymbol))
                textSymbol.text = apiData[i - 1][1]
                val nameContainer =
                    LinearLayout(ContextThemeWrapper(context, R.style.NameContainer))

                nameContainer.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    5.0f
                )
                nameContainer.layoutParams.width = 0

                nameContainer.addView(textName)
                nameContainer.addView(textSymbol)
                val textPrice = TextView(ContextThemeWrapper(context, R.style.CryptoLastPrice))

                textPrice.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    2.5f
                )
                textPrice.layoutParams.width = 0

                textPrice.text = "$" + apiData[i - 1][2]
                val textChange = TextView(ContextThemeWrapper(context, R.style.Crypto24hChange))

                textChange.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    2.5f
                )
                textChange.layoutParams.width = 0
                if (apiData[i - 1][3].contains("-")) {
                    textChange.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                    textChange.text = apiData[i - 1][3] + "%"
                } else {
                    textChange.setTextColor(ContextCompat.getColor(context!!, R.color.green))
                    textChange.text = "+" + apiData[i - 1][3] + "%"
                }
                val rowContainer =
                    LinearLayout(ContextThemeWrapper(context, R.style.RowContainer))
                rowContainer.addView(nameContainer)
                rowContainer.addView(textPrice)
                rowContainer.addView(textChange)
                apiTable?.addView(rowContainer)
            }
        } catch (e: Exception) {
            Log.e("Error", e.toString())
        }
    }

    fun apiCall() {
        RestClient.getReqResApi.getCoins(apiLimit)
            .enqueue(object : retrofit2.Callback<ReqResData<Coin>> {
                override fun onResponse(
                    call: Call<ReqResData<Coin>>,
                    response: Response<ReqResData<Coin>>
                ) {
                    if (response.isSuccessful) {
                        apiData.clear()
                        response.body()?.data?.let { it ->
                            it.forEach {
                                val price = if (it.quote?.usd?.price?.toFloat()!! > 1.0f) {
                                    String.format("%.0f", it.quote.usd.price.toFloat())
                                } else if (it.quote.usd.price.toFloat() > 0.1f) {
                                    String.format("%.3f", it.quote.usd.price.toFloat())
                                } else {
                                    String.format("%.6f", it.quote.usd.price.toFloat())
                                }
                                val row = listOf(
                                    it.name.toString(),
                                    it.symbol.toString(),
                                    price,
                                    String.format(
                                        "%.2f",
                                        it.quote.usd.percent_change_24h?.toFloat()
                                    )
                                )
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
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_api, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler = Handler(Looper.getMainLooper())

    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(apiUpdate)
    }

    override fun onResume() {
        super.onResume()
        handler.post(apiUpdate)
    }

}
