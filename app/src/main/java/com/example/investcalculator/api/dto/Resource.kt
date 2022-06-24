package com.example.investcalculator.api.dto

import com.google.gson.annotations.SerializedName

data class Coin(
    @SerializedName("id")
    val coinID:Int?,
    @SerializedName("name")
    val name:String?,
    @SerializedName("symbol")
    val symbol:String?,
    @SerializedName("quote")
    val quote: Quote?
)


data class Quote(
    @SerializedName("USD")
    val usd:USD?
)

data class USD(
    @SerializedName("price")
    val price:Double?,
    @SerializedName("percent_change_24h")
    val percent_change_24h:Double?
)

data class ReqResData<T>(
    @SerializedName("data")
    val data: List<T>?
)