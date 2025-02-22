package com.example.testmvc.model.server


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient{
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val favServiceObj = retrofit.create(FavoriteService::class.java)

}


