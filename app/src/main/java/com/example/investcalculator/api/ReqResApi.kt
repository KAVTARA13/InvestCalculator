package com.example.investcalculator.api

import com.example.investcalculator.api.dto.Coin
import com.example.investcalculator.api.dto.ReqResData
import retrofit2.Call
import retrofit2.http.*

interface ReqResApi {
    @Headers("X-CMC_PRO_API_KEY: 648810c2-13a2-4163-86c5-0e628f30678e")
    @GET("cryptocurrency/listings/latest")
    fun getCoins(@Query("limit") limit:Int): retrofit2.Call<ReqResData<Coin>>

    @Headers("X-CMC_PRO_API_KEY: 648810c2-13a2-4163-86c5-0e628f30678e")
    @GET("cryptocurrency/listings/latest")
    fun getCoinBySlug(@Query("slug") slug: String): retrofit2.Call<String>

    @Headers("X-CMC_PRO_API_KEY: 648810c2-13a2-4163-86c5-0e628f30678e")
    @GET("cryptocurrency/quotes/latest")
    fun getCoinBySymbol(@Query("symbol") symbol: String): retrofit2.Call<String>

    @Headers("X-CMC_PRO_API_KEY: 648810c2-13a2-4163-86c5-0e628f30678e")
    @GET("cryptocurrency/quotes/latest")
    fun getCoinById(@Query("id") id: Int): retrofit2.Call<String>
}