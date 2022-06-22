package com.example.investcalculator.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RestClient {
    private lateinit var rwtrofit:Retrofit
    private lateinit var rwtrofit2:Retrofit
    private lateinit var okHttpClient:OkHttpClient

    fun initClients(){
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        rwtrofit = Retrofit.Builder()
            .baseUrl("https://pro-api.coinmarketcap.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        rwtrofit2 = Retrofit.Builder()
            .baseUrl("https://pro-api.coinmarketcap.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun <S> getService(serviceClass:Class<S>): S {
        return rwtrofit.create(serviceClass)
    }
    private fun <S> getService2(serviceClass:Class<S>): S {
        return rwtrofit2.create(serviceClass)
    }

    val getReqResApi:ReqResApi
        get() = getService(ReqResApi::class.java)
    val getReqResApi2:ReqResApi
        get() = getService2(ReqResApi::class.java)
}