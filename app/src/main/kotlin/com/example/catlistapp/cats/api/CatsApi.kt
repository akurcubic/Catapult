package com.example.catlistapp.cats.api

import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.api.model.CatApiGalleryModel
import com.example.catlistapp.cats.entities.CatGallery
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatsApi {


    @GET("breeds")
    suspend fun getAllCats(): List<CatApiModel>

    @GET("images/search?limit=20")
    suspend fun getAllCatPhotos(@Query("breed_ids") id: String): List<CatGallery>

    @GET("images/{photoId}")
    suspend fun fetchPhotoById(
        @Path("photoId") photoId: String,
    ): CatApiGalleryModel

}