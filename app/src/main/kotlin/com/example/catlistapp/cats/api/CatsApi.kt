package com.example.catlistapp.cats.api

import com.example.catlistapp.cats.api.model.CatApiModel
import retrofit2.http.GET
import retrofit2.http.Path

interface CatsApi {


    @GET("breeds")
    suspend fun getAllCats(): List<CatApiModel>

    @GET("breeds/{breed_id}")
    suspend fun getCat(
        @Path("breed_id") id: String,
    ): CatApiModel
}