package com.luthiers.udacitykotlincapstone.data.network

import android.location.Address
import com.luthiers.udacitykotlincapstone.data.models.ElectionsResponse
import com.luthiers.udacitykotlincapstone.data.models.Officials
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "AIzaSyDQMZ72jSz79AKqM7z-RQ9Peb2mNQ4nSFc"
val retrofit by lazy {
    Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/civicinfo/v2/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
}

interface INetworkDataSource {
    @GET("elections/?key=$API_KEY")
    suspend fun getElectionsAsync(): ElectionsResponse

    @GET("representatives/?key=$API_KEY")
    suspend fun getOfficialsAsync(@Query("address") address: String): Officials
}

class NetworkDataSource(private val retrofit: INetworkDataSource) {
    suspend fun getElections() =
        retrofit.getElectionsAsync()

    suspend fun getOfficialsAsync(location: String) =
        retrofit.getOfficialsAsync(location)
}