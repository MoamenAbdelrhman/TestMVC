package com.example.testmvc.model.server


import com.example.testmvc.model.FavoriteResponse
import retrofit2.Response
import retrofit2.http.GET

interface FavoriteService {

    @GET("categories.php")

    suspend fun getCategories(): Response<FavoriteResponse>
}