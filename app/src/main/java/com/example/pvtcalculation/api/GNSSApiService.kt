package com.example.pvtcalculation.api

import com.example.pvtcalculation.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = Constants.BASE_URL

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface GNSSApiService {
//    @GET("above/41.702/-76.014/0/70/18/&apiKey=AWZCKM-QARK7C-HS2A2A-4SUO")
//    fun getProperties():
//            Call<String>
    @GET("above/{observerLat}/{observerLng}/{observerAlt}/{searchRadius}/{categoryId}")
    fun getAbove(@Path("observerLat") observerLat: String,
                         @Path("observerLng") observerLng: String,
                         @Path("observerAlt") observerAlt: String,
                         @Path("searchRadius") searchRadius: String,
                         @Path("categoryId") categoryId: String,
                         @Query("apiKey") APIKey: String
    ): Call<String>
}


// Create the MarsApi object using Retrofit ro implement the MarsApiService
object GNSSApi {
    val retrofitService : GNSSApiService by lazy {
        retrofit.create(GNSSApiService::class.java)
    }
}
