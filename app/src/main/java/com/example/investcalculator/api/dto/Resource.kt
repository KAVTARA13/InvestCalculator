package com.example.investcalculator.api.dto

import com.google.gson.annotations.SerializedName

data class Coin(
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
    val price:Double?
)

data class ReqResData<T>(
    @SerializedName("data")
    val data: List<T>?
)